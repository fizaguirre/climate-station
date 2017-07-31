package csc.collector;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class ATMLevel extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum ATMLevelData {
		High,
		Low
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		StationData sData = (StationData) input.getValueByField("data");
		
		if (sData != null) {
			if ( sData.getSensors().getBMP085PRESSURE() > 1013.0)
				collector.emit(new Values(SenderBolt.ATMLevelBolt,
						sData.getDatetime().getValue(),
						ATMLevelData.High));
			else if ( sData.getSensors().getBMP085PRESSURE() <= 1013.0)
				collector.emit(new Values(SenderBolt.ATMLevelBolt,
						sData.getDatetime().getValue(),
						ATMLevelData.Low));
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("worker", "time", "data"));

	}

}
