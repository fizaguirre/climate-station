package csc.collector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ProcessReport extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HashMap<Integer, Long > workersCounter;
	HashMap<Integer, Double > workersData;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.workersCounter = new HashMap<Integer, Long>();
		this.workersData = new HashMap<Integer, Double>();
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String worker = (String) input.getValueByField("worker");
		this.workersCounter.putAll((HashMap<Integer, Long>)input.getValueByField("counter"));
		this.workersData.putAll((HashMap<Integer, Double>)input.getValueByField("data"));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void cleanup() {
		System.out.println("===== Averages =====");
		
		Double average;
		for (Map.Entry<Integer, Double> d : this.workersData.entrySet()) {
			average = d.getValue() / this.workersCounter.get(d.getKey());
			System.out.println("Station ID: "+ d.getKey() + " Average: "+ average
					+ " Couter: " + this.workersCounter.get(d.getKey()) + 
					" Data: " + d.getValue());
			//System.out.println("Counter: "+ this.workersCounter.get(d.getKey()));
			//System.out.print("Data: "+ d.getValue());
		}
		
		//for (String worker : averages.keySet())
		//	System.out.println("Worker "+worker+" Average: "+ averages.get(worker));
		System.out.println("=====================");
	}

}
