package csc.collector;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class ProcessReport extends BaseBasicBolt {
	
	private HashMap<String, Double> averages;

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		averages = new HashMap<String, Double>();
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		String worker = (String) input.getValueByField("worker");
		Double average = (Double) input.getValueByField("average");
		this.averages.put(worker,  average);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void cleanup() {
		System.out.println("===== Averages =====");
		for (String worker : averages.keySet())
			System.out.println("Worker "+worker+" Average: "+ averages.get(worker));
		System.out.println("=====================");
	}

}
