package csc.collector;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;

public class TopologyMain {

	public static void main(String[] args) throws InterruptedException {
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("get-statistcs", new GetStatistics());
		builder.setBolt("process-climate",  new ProcessClimateData())
		.shuffleGrouping("get-statistcs");
		
		Config conf = new Config();
		conf.put("jsonFile",  args[0]);
		conf.setDebug(false);
		
		if( args != null && args.length == 2)
		{
			try {
				StormSubmitter.submitTopology("climate-station-processor", conf,
						builder.createTopology());
			} catch (AlreadyAliveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Collecting data",  conf,  builder.createTopology());
			Thread.sleep(1000);
			cluster.shutdown();
		}
		
	}

}
