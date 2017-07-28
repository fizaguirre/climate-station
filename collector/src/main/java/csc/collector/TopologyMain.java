package csc.collector;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

public class TopologyMain {

	public static void main(String[] args) throws InterruptedException {
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("get-statistics", new GetStatistics(), 2);
		builder.setBolt("process-data",  new ProcessClimateData(), 4).
		fieldsGrouping("get-statistics", new Fields("source"));
		builder.setBolt("report-average",  new ProcessReport()).globalGrouping("process-data");
		
		Config conf = new Config();
		conf.put("jsonFile",  args[0]);
		conf.setDebug(false);
		
		if( args != null && args.length == 2)
		{
			try {
				StormSubmitter.submitTopology("climate-station-processor", conf,
						builder.createTopology());
			} catch (InvalidTopologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AuthorizationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AlreadyAliveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("Collecting data",  conf,  builder.createTopology());
			Thread.sleep(10000);
			cluster.shutdown();
		}
		
	}

}
