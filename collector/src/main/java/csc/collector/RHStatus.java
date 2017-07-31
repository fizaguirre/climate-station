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

	public enum RHStateData {
		Atention,
		Alert,
		Emergency
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		StationData sData = (StationData) input.getValueByField("data");
		
		if(sData != null) {
			if(sData.getSensors().getBMP085PRESSUqRE().intValue() >= 20 )
				collector.emit(new Values(Long.toString(Thread.currentThread().getId()),
						sData.getDatetime().getValue(),
						RHStateData.Atention));
			else if (sData.getSensors().getBMP085PRESSUqRE().intValue() >= 12)
				collector.emit(new Values(Long.toString(Thread.currentThread().getId()),
						sData.getDatetime().getValue(),
						RHStateData.Alert));
			else if (sData.getSensors().getBMP085PRESSUqRE().intValue() < 12)
				collector.emit(new Values(Long.toString(Thread.currentThread().getId()),
						sData.getDatetime().getValue(),
						RHStateData.Emergency));
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("worker", "time", "data"));

	}

}
