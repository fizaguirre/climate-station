package csc.collector;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

public class RHStatus extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	// Relative humidity index
	public enum RHStateData {
		OK,
		Atention,
		Alert,
		Emergency
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		StationData sData = (StationData) input.getValueByField("data");
		
		if(sData != null) {
			if ( sData.getSensors().getDHT22AH().intValue() > 30 )
				collector.emit(new Values(SenderBolt.RHStatusBolt,
						sData.getDatetime().getStationID(),
						RHStateData.OK, sData));
			else if(sData.getSensors().getDHT22AH().intValue() >= 20 
					&& sData.getSensors().getDHT22AH().intValue() <= 30)
				collector.emit(new Values(SenderBolt.RHStatusBolt,
						sData.getDatetime().getStationID(),
						RHStateData.Atention, sData));
			else if (sData.getSensors().getDHT22AH().intValue() >= 12
					&& sData.getSensors().getDHT22AH().intValue() < 20)
				collector.emit(new Values(SenderBolt.RHStatusBolt,
						sData.getDatetime().getStationID(),
						RHStateData.Alert, sData));
			else if (sData.getSensors().getDHT22AH().intValue() < 12)
				collector.emit(new Values(SenderBolt.RHStatusBolt,
						sData.getDatetime().getStationID(),
						RHStateData.Emergency, sData));
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("worker", "sID", "data", "sData"));

	}

}
