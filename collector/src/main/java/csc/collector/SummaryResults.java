package csc.collector;

import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import csc.collector.ATMLevel.ATMLevelData;
import csc.collector.LDRStatus.LDRData;
import csc.collector.RHStatus.RHStateData;

public class SummaryResults extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	OutputCollector collector;

	public void execute(Tuple input) {
		SenderBolt sender = (SenderBolt) input.getValueByField("worker");
		
		switch ( sender ) {
		case ATMLevelBolt:
			ATMLevelData atm = (ATMLevelData) input.getValueByField("data");
			System.out.println("ATM: " + atm);
			break;
		case ComputeHeatIndexBolt:
			Double chi = (Double) input.getValueByField("data");
			System.out.println("Heat Index: " + chi);
			break;
		case LDRStatusBolt:
			LDRData ldr = (LDRData) input.getValueByField("data");
			System.out.println("ATM: " + ldr);
			break;
		case RHStatusBolt:
			RHStateData rh = (RHStateData) input.getValueByField("data");
			System.out.println("ATM: " + rh);
			break;
		default:
			break;
		}
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}

}
