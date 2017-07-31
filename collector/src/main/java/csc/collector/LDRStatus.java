package csc.collector;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class LDRStatus extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum LDRData {
		Sunny,
		Mood,
		Night
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		StationData sData = (StationData) input.getValueByField("data");
		
		if(sData != null) {
			if(sData.getSensors().getLDR() > 90.0)
				collector.emit(new Values(SenderBolt.LDRStatusBolt,
						sData.getDatetime().getValue(),
						LDRData.Sunny));
			else if (sData.getSensors().getLDR() > 60.0)
				collector.emit(new Values(SenderBolt.LDRStatusBolt,
						sData.getDatetime().getValue(),
						LDRData.Mood));
			else
				collector.emit(new Values(SenderBolt.LDRStatusBolt,
						sData.getDatetime().getValue(),
						LDRData.Night));
				
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("worker", "time", "data"));

	}

}
