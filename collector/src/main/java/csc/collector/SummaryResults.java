package csc.collector;

import java.util.HashMap;
import java.util.Map;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;

import csc.collector.ATMLevel.ATMLevelData;
import csc.collector.ComputeHeatIndex.HeatIndexStatus;
import csc.collector.LDRStatus.LDRData;
import csc.collector.RHStatus.RHStateData;

public class SummaryResults extends BaseRichBolt {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OutputCollector collector;
	HashMap<Integer, HashMap<String, DataTableReport>> tableData;
	CloudTable entryTable;
	
	public static final String storageConnectionString =
		    "DefaultEndpointsProtocol=http;" +
		    "AccountName=izastorm;" +
		    "AccountKey=isUic1EEbXg8l53zUrl+o1Jmf8JPze/E8S5XQ3ActlrmpEmGqMSKdkSP/RTF4aFAdQmLeVy6DWT3pGJ1k/I2HA==";
	

	public void execute(Tuple input) {
		SenderBolt sender = (SenderBolt) input.getValueByField("worker");
		
		StationData sData;
		switch ( sender ) {
		case ATMLevelBolt:
			ATMLevelData atm = (ATMLevelData) input.getValueByField("data");
			System.out.println("ATM: " + atm);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case ComputeHeatIndexBolt:
			Double chi = (Double) input.getValueByField("ic");
			HeatIndexStatus hs = (HeatIndexStatus) input.getValueByField("status");
			System.out.println("Heat Index: " + chi +" Status: "+ hs);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case LDRStatusBolt:
			LDRData ldr = (LDRData) input.getValueByField("data");
			System.out.println("LDR: " + ldr);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case RHStatusBolt:
			RHStateData rh = (RHStateData) input.getValueByField("data");
			System.out.println("RH: " + rh);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		default:
			break;
		}
	}

	public void prepare(Map map, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
		this.tableData = new HashMap<Integer, HashMap<String, DataTableReport>>();
		
		try
		{
		    // Retrieve storage account from connection-string.
		    CloudStorageAccount storageAccount =
		        CloudStorageAccount.parse(storageConnectionString);

		    // Create the table client.
		    CloudTableClient tableClient = storageAccount.createCloudTableClient();

		    // Create the table if it doesn't exist.
		    entryTable = tableClient.getTableReference("cstations");
		    entryTable.createIfNotExists();
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer arg0) {
		// TODO Auto-generated method stub

	}
	
	private void insertTableEntry(StationData sData, Tuple entry, SenderBolt sender) {
		try {
			DataTableReport table;
			HashMap<String, DataTableReport> report;
			if (this.tableData.containsKey(sData.getDatetime().getStationID())) {
				report = this.tableData.get(sData.getDatetime().getStationID());
			}
			else {
				report = new HashMap<String, DataTableReport>();
			}
				
				
			if (report.containsKey(sData.getDatetime().getValue())) {
				table = report.get(sData.getDatetime().getValue());
			}
			else {
				table = new DataTableReport(sData.getDatetime().getStationID(),
						sData.getDatetime().getValue());
				table.setTemperature(sData.getSensors().getDHT22TEMP());
			}
				
			switch ( sender ) {
			case ATMLevelBolt:
				ATMLevelData atm = (ATMLevelData) entry.getValueByField("data");
				table.setAtmStatus(atm);
				table.setAtmValue(sData.getSensors().getDHT22AH());
				break;
			case ComputeHeatIndexBolt:
				Double chi = (Double) entry.getValueByField("ic");
				HeatIndexStatus hs = (HeatIndexStatus) entry.getValueByField("status");
				table.setHeatStatus(hs);
				table.setHeatValue(chi);
				table.setPressure(sData.getSensors().getBMP085PRESSURE());
				break;
			case LDRStatusBolt:
				LDRData ldr = (LDRData) entry.getValueByField("data");
				table.setLdrStatus(ldr);
				table.setLdrValue(sData.getSensors().getLDR());
				break;
			case RHStatusBolt:
				RHStateData rh = (RHStateData) entry.getValueByField("data");
				table.setRhStatus(rh);
				table.setRhValue(sData.getSensors().getDHT22AH());
				break;
			default:
				break;
			}
				
			report.put(sData.getDatetime().getValue(), table);
			this.tableData.put(sData.getDatetime().getStationID(), report);
			
			if (table.isDone()) {
				System.out.println("Table " + table.toString() + " done.");
				TableOperation toperation = TableOperation.insert(table);
				entryTable.execute(toperation);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
