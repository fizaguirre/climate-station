package csc.collector;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;


public class ComputeHeatIndex extends BaseBasicBolt {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Double ic;
	private final Double c1 = -42.379;
	private final Double c2 = 2.04901523;
	private final Double c3 = 10.14333127;
	private final Double c4 = -0.22475541;
	private final Double c5 = -0.00683783;
	private final Double c6 = -0.05481717;
	private final Double c7 = 0.00122874;
	private final Double c8 = 0.00085282;
	private final Double c9 = -0.00000199;
	
	public enum HeatIndexStatus {
		OK,
		CAUTION,
		EXTREMELY_CAUTION,
		DANGER,
		EXTREMELY_DANGER
	}

	public void execute(Tuple input, BasicOutputCollector collector) {
		StationData sData = (StationData) input.getValueByField("data");
		
		HeatIndexStatus hs = HeatIndexStatus.OK;
		
		if (sData != null) {
			Double T = 1.8 * sData.getSensors().getDHT22AH() + 32.0; //Temperature in F
			Double R = sData.getSensors().getDHT22AH(); //Relative Humidity
			
			Double T2 = T*T;
			Double R2 = R*R;
			
			ic = (5.0/9.0)*((c1 + c2*T + c3*R + c4*T*R + c5*T2
					+ c6*R2 + c7*T2*R + c8*T*R2 + c9*T2*R2) - 32);
			
			ic = ic/1.8 - 32;
			
			
			if ( ic < 27 )
				hs = HeatIndexStatus.OK;
			else if ( ic >= 27 && ic < 32)
				hs = HeatIndexStatus.CAUTION;
			else if (ic >= 32 && ic < 41)
				hs = HeatIndexStatus.EXTREMELY_CAUTION;
			else if (ic >= 41 && ic < 54)
				hs = HeatIndexStatus.DANGER;
			else if (ic >= 54)
				hs = HeatIndexStatus.EXTREMELY_DANGER;
			
			
			collector.emit(new Values(SenderBolt.ComputeHeatIndexBolt,
					sData.getDatetime().getValue(),
					ic, hs, sData));
		}

	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		arg0.declare(new Fields("worker", "time", "ic", "status", "sData"));

	}

}
