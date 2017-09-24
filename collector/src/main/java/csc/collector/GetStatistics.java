package csc.collector;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.queue.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

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
	
	public static final String storageConnectionString = ""; //String to connect to the Azure Queue
	
	CloudStorageAccount csa;
	CloudQueueClient cqc;
	CloudQueue cq;
	
	public void ack(Object msgId) {
		//System.out.println("OK: "+msgId);
	}
	
	public void fail(Object msgId) {
		System.out.println("FAIL: "+msgId);
	}
	
	public void close() {}

	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.collector = collector;
		this.mapper = new ObjectMapper();
		this.mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
		/*
		 * Build the classes needed to retrieve data from the queue
		 */
		
		try{
			csa = CloudStorageAccount.parse(storageConnectionString);
			cqc = csa.createCloudQueueClient();
			cq = cqc.getQueueReference("stormtest");
			cq.setShouldEncodeMessage(false);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void nextTuple() {
		/*
		 * Gets a message from the queue, deserialize and emit to the next Bolt.
		 * The deserialization is made using jackson to parse the json.
		 */
		
		try {
			CloudQueueMessage message = cq.retrieveMessage();
			//System.out.println("Checking message");
			if (message == null)
				Thread.sleep(1000);
			
			if (message != null)
			{
				sData = this.mapper.readValue(message.getMessageContentAsString(), StationData.class);
				this.collector.emit(new Values(sData.getDatetime().getStationID(),
						sData));
				cq.deleteMessage(message);
			}
		}
		catch (StorageException e) {
			System.out.println(e.getExtendedErrorInformation().toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("source", "data"));

	}

}
