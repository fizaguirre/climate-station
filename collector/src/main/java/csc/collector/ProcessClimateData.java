package csc.collector;


import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import java.io.IOException;
import java.util.Map;


public class ProcessClimateData extends BaseBasicBolt {
	private long count = 0;
	private double temperatureAccumulator = 0.0;
	private double average = 0.0;
	private OutputCollector collector;

	public void execute(Tuple input, BasicOutputCollector collector) {
		String source = (String) 
				input.getValueByField("source");
		StationData sData = (StationData) input.getValueByField("data");
		
		System.out.println("String received fron spout: Source "+ source 
				+ " Temperature: "+ sData.getSensors().getDHT22TEMP().toString());
		
		temperatureAccumulator += sData.getSensors().getDHT22TEMP();
		count++;
		
		average = temperatureAccumulator / count;
		
		collector.emit(new Values(Long.toString(Thread.currentThread().getId()), average));

	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("worker", "average"));

	}
	
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}
	
	@Override
	public void cleanup() {
		//System.out.println("Number of messages: " + Long.toString(count));
	}

}
