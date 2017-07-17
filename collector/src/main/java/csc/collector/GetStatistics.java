package csc.collector;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;


import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetStatistics extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean complete = false;
	ObjectMapper mapper;
	StationData sData;
	
	
	public void ack(Object msgId) {
		System.out.println("OK:"+msgId);
	}
	
	public void fail(Object msgId) {
		System.out.println("FAIL:"+msgId);
	}
	
	public void close() {}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		try {
			this.fileReader = new FileReader(conf.get("jsonFile").toString());
		}
		catch (FileNotFoundException e) {
			throw new RuntimeException("Error reading file ["+conf.get("jsonFile")+"]");
		}
		this.collector = collector;
		this.mapper = new ObjectMapper();

	}

	public void nextTuple() {
		
		if (complete) {
			try {
				Thread.sleep(1000);
			}
			catch (InterruptedException e) {
				// TODO: handle exception
			}
			return;
		}
		
		String str;
		BufferedReader reader = new BufferedReader(fileReader);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		try {
			while ((str = reader.readLine()) != null) {
				
				
				try
				{
					sData = mapper.readValue(str,  StationData.class);
				}
				catch (JsonGenerationException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				catch (JsonMappingException e){
					e.printStackTrace();
				} catch (JsonParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				this.collector.emit(new Values(sData.getDatetime().getValue(),
									sData.getSensors().getDHT22TEMP().toString()), sData.toString());
			}
		}
		catch(Exception e) {
			throw new RuntimeException("Error reading tunple", e);
		}
		finally {
			complete = true;
		}
		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("date", "temp"));

	}

}
