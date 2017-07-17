package csc.collector;


import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;


public class ProcessClimateData extends BaseBasicBolt {
	private int count;

	public void execute(Tuple input, BasicOutputCollector collector) {
		//String str = input.getString(0);
		//StationData sData = (StationData) input.getValue(0);
		String date, temperature;
		date = input.getString(0);
		temperature = input.getString(1);
		
		
		//System.out.println("String received fron spout: "+ sData.getDatetime().getValue().toString());
		System.out.println("String received fron spout: Date "+ date 
				+ " Temperature: "+temperature);
		count++;

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
	}
	
	@Override
	public void cleanup() {
		System.out.println("Number of messages: " + Integer.toString(count));
	}

}
