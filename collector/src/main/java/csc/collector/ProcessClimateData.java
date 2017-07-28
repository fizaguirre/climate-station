package csc.collector;


import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;


public class ProcessClimateData extends BaseBasicBolt {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Long > counter;
	private HashMap<Integer, Double > accumulator;

	public void execute(Tuple input, BasicOutputCollector collector) {

		
		StationData sData = (StationData) input.getValueByField("data");
		
		/*String source = (String) 
				input.getValueByField("source");
		  System.out.println("String received fron spout: Source "+ source 
				+ " Temperature: "+ sData.getSensors().getDHT22TEMP().toString() + " tid: " +
				Long.toString(Thread.currentThread().getId()));
		*/
		
		if (this.accumulator.containsKey(sData.getDatetime().getStationID())) {
			this.counter.put(sData.getDatetime().getStationID(),
					this.counter.get(sData.getDatetime().getStationID()) + 1);
			
			this.accumulator.put(sData.getDatetime().getStationID(),
					this.accumulator.get(sData.getDatetime().getStationID()) + sData.getSensors().getDHT22TEMP());
		}
		else
		{
			this.counter.put(sData.getDatetime().getStationID(), new Long(1));
			
			this.accumulator.put(sData.getDatetime().getStationID(),
					sData.getSensors().getDHT22TEMP());
		}
		
		collector.emit(new Values(Long.toString(Thread.currentThread().getId()), counter, accumulator));
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("worker", "counter", "data"));

	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		this.accumulator = new HashMap<Integer, Double>();
		this.counter = new HashMap<Integer, Long>();
	}
	
	@Override
	public void cleanup() {
		System.out.println("=== Worker : " + Thread.currentThread().getId() + "====");
		
		for ( Map.Entry<Integer, Long> wCount : this.counter.entrySet()) {
			System.out.println("Station ID: " + wCount.getKey()
				+ " Counter: " + wCount.getValue() + " Data: " + this.accumulator.get(wCount.getKey()));
		}
	}

}
