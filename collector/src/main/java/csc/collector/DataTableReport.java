package csc.collector;

import com.microsoft.azure.storage.table.TableServiceEntity;

import csc.collector.ATMLevel.ATMLevelData;
import csc.collector.ComputeHeatIndex.HeatIndexStatus;
import csc.collector.LDRStatus.LDRData;
import csc.collector.RHStatus.RHStateData;

public class DataTableReport extends TableServiceEntity {
	String atmStatus;
	String atmValue;
	String ldrStatus;
	String ldrValue;
	String rhStatus;
	String rhValue;
	String heatStatus;
	String heatValue;
	String pressure;
	String temperature;
	
	Integer stationID;
	String timeStamp;
	Boolean done;
	
	
	
	public DataTableReport(Integer sID, String date) {
		this.partitionKey = sID.toString();
		this.rowKey = date;
		
		this.stationID = sID;
		this.timeStamp = date;
		
		this.atmStatus = null;
		this.atmValue = null;
		this.ldrStatus = null;
		this.ldrValue = null;
		this.rhStatus = null;
		this.rhValue = null;
		this.heatStatus = null;
		this.heatValue = null;
		this.temperature = null;
	}
	
	public DataTableReport() { 
		this.atmStatus = null;
		this.atmValue = null;
		this.ldrStatus = null;
		this.ldrValue = null;
		this.rhStatus = null;
		this.rhValue = null;
		this.heatStatus = null;
		this.heatValue = null;
		this.temperature = null;
		
		this.stationID = null;
		this.timeStamp = null;
	}
	
	
	
	public String getAtmStatus() {
		return atmStatus;
	}

	public void setAtmStatus(String atmStatus) {
		this.atmStatus = atmStatus;
	}

	public String getAtmValue() {
		return atmValue;
	}

	public void setAtmValue(String atmValue) {
		this.atmValue = atmValue;
	}

	public String getLdrStatus() {
		return ldrStatus;
	}

	public void setLdrStatus(String ldrStatus) {
		this.ldrStatus = ldrStatus;
	}

	public String getLdrValue() {
		return ldrValue;
	}

	public void setLdrValue(String ldrValue) {
		this.ldrValue = ldrValue;
	}

	public String getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(String rhStatus) {
		this.rhStatus = rhStatus;
	}

	public String getRhValue() {
		return rhValue;
	}

	public void setRhValue(String rhValue) {
		this.rhValue = rhValue;
	}

	public String getHeatStatus() {
		return heatStatus;
	}

	public void setHeatStatus(String heatStatus) {
		this.heatStatus = heatStatus;
	}

	public String getHeatValue() {
		return heatValue;
	}

	public void setHeatValue(String heatValue) {
		this.heatValue = heatValue;
	}

	public String getPressure() {
		return pressure;
	}

	public void setPressure(String pressure) {
		this.pressure = pressure;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public Integer getStationID() {
		return stationID;
	}

	public void setStationID(Integer stationID) {
		this.stationID = stationID;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Boolean getDone() {
		return done;
	}

	public void setDone(Boolean done) {
		this.done = done;
	}

	public boolean isDone() {
		
		if ( this.atmStatus == null )
			return false;
		else if (this.atmValue == null)
			return false;
		else if (this.ldrStatus == null)
			return false;
		else if (this.ldrValue == null)
			return false;
		else if (this.rhStatus == null)
			return false;
		else if (this.rhValue == null)
			return false;
		else if (this.heatStatus == null)
			return false;
		else if (this.heatValue == null)
			return false;
		else if (this.temperature== null)
			return false;
		else if (this.stationID == null)
			return false;
		else if (this.timeStamp == null)
			return false;
		
		return true;
	}
	
}
