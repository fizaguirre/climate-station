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
import com.microsoft.azure.storage.table.TableBatchOperation;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableServiceException;

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
	//HashMap<Integer, TableBatchOperation> batchOperations;
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
			//System.out.println("ATM: " + atm);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case ComputeHeatIndexBolt:
			Double chi = (Double) input.getValueByField("ic");
			HeatIndexStatus hs = (HeatIndexStatus) input.getValueByField("status");
			//System.out.println("Heat Index: " + chi +" Status: "+ hs);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case LDRStatusBolt:
			LDRData ldr = (LDRData) input.getValueByField("data");
			//System.out.println("LDR: " + ldr);
			sData = (StationData) input.getValueByField("sData");
			this.insertTableEntry(sData, input, sender);
			break;
		case RHStatusBolt:
			RHStateData rh = (RHStateData) input.getValueByField("data");
			//System.out.println("RH: " + rh);
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
		//this.batchOperations = new HashMap<Integer, TableBatchOperation>();
		
		try
		{
		    CloudStorageAccount storageAccount =
		        CloudStorageAccount.parse(storageConnectionString);

		    CloudTableClient tableClient = storageAccount.createCloudTableClient();
		    
		    //tableBatch = new TableBatchOperation();

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
				table.setTemperature(sData.getSensors().getDHT22TEMP().toString());
			}
				
			switch ( sender ) {
			case ATMLevelBolt:
				ATMLevelData atm = (ATMLevelData) entry.getValueByField("data");
				table.setAtmStatus(atm.toString());
				table.setAtmValue(sData.getSensors().getDHT22AH().toString());
				break;
			case ComputeHeatIndexBolt:
				Double chi = (Double) entry.getValueByField("ic");
				HeatIndexStatus hs = (HeatIndexStatus) entry.getValueByField("status");
				table.setHeatStatus(hs.toString());
				table.setHeatValue(chi.toString());
				table.setPressure(sData.getSensors().getBMP085PRESSURE().toString());
				break;
			case LDRStatusBolt:
				LDRData ldr = (LDRData) entry.getValueByField("data");
				table.setLdrStatus(ldr.toString());
				table.setLdrValue(sData.getSensors().getLDR().toString());
				break;
			case RHStatusBolt:
				RHStateData rh = (RHStateData) entry.getValueByField("data");
				table.setRhStatus(rh.toString());
				table.setRhValue(sData.getSensors().getDHT22AH().toString());
				break;
			default:
				break;
			}
				
			report.put(sData.getDatetime().getValue(), table);
			this.tableData.put(sData.getDatetime().getStationID(), report);
			
			if (table.isDone()) {
				try {
					//System.out.println("Table " + table.toString() + " done.");
					TableOperation op = TableOperation.insertOrReplace(table);
					entryTable.execute(op);
				}
				catch (TableServiceException e) {
					e.printStackTrace();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
}

}
