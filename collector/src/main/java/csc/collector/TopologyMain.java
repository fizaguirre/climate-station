package csc.collector;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import csc.collector.*;

public class TopologyMain {

	public static void main(String[] args) throws InterruptedException {
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("get-statistics", new GetStatistics(), 2);
		//RH
		builder.setBolt("rh-status", new RHStatus(), 4).
		fieldsGrouping("get-statistics", new Fields("source"));
		//LDR
		builder.setBolt("ldr-status", new LDRStatus(), 4).
		fieldsGrouping("get-statistics", new Fields("source"));
		//ATM
		builder.setBolt("atm-status", new ATMLevel(), 4).
		fieldsGrouping("get-statistics", new Fields("source"));
		//HI
		builder.setBolt("compute-heat-index", new ComputeHeatIndex(), 4).
		fieldsGrouping("get-statistics", new Fields("source"));
		//Summary
		builder.setBolt("summary", new SummaryResults(), 4)
			.shuffleGrouping("ldr-status")
			.shuffleGrouping("atm-status")
			.shuffleGrouping("rh-status")
			.shuffleGrouping("compute-heat-index");
		
		Config conf = new Config();
		//conf.put("jsonFile",  args[0]);
		conf.setDebug(false);

		if( args != null && args.length == 1) {
			if (args[0].equals("local")) {
				System.out.println("===== > Starting Local Cluster");
				conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);
				LocalCluster cluster = new LocalCluster();
				cluster.submitTopology("Collecting data",  conf,  builder.createTopology());
				Thread.sleep(10000);
				cluster.shutdown();
				System.out.println("===== > Shutting down Local Cluster");
			}
			else {
				System.out.println("Incorrect argument");
			}
		}
		else {
			System.out.println("===== > Starting Remote Cluster");
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
			System.out.println("===== > Shutting down Remote Cluster");
		}
		
	}

}
