package csc.collector;

import com.microsoft.azure.storage.table.TableServiceEntity;

import csc.collector.ATMLevel.ATMLevelData;
import csc.collector.ComputeHeatIndex.HeatIndexStatus;
import csc.collector.LDRStatus.LDRData;
import csc.collector.RHStatus.RHStateData;

public class DataTableReport extends TableServiceEntity {
	ATMLevelData atmStatus;
	Double atmValue;
	LDRData ldrStatus;
	Double ldrValue;
	RHStateData rhStatus;
	Double rhValue;
	HeatIndexStatus heatStatus;
	Double heatValue;
	Double pressure;
	Double temperature;
	
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
	
	

	public Double getPressure() {
		return pressure;
	}

	public void setPressure(Double pressure) {
		this.pressure = pressure;
	}

	public ATMLevelData getAtmStatus() {
		return atmStatus;
	}

	public void setAtmStatus(ATMLevelData atmStatus) {
		this.atmStatus = atmStatus;
	}

	public Double getAtmValue() {
		return atmValue;
	}

	public void setAtmValue(Double atmValue) {
		this.atmValue = atmValue;
	}

	public LDRData getLdrStatus() {
		return ldrStatus;
	}

	public void setLdrStatus(LDRData ldrStatus) {
		this.ldrStatus = ldrStatus;
	}

	public Double getLdrValue() {
		return ldrValue;
	}

	public void setLdrValue(Double ldrValue) {
		this.ldrValue = ldrValue;
	}

	public RHStateData getRhStatus() {
		return rhStatus;
	}

	public void setRhStatus(RHStateData rhStatus) {
		this.rhStatus = rhStatus;
	}

	public Double getRhValue() {
		return rhValue;
	}

	public void setRhValue(Double rhValue) {
		this.rhValue = rhValue;
	}

	public HeatIndexStatus getHeatStatus() {
		return heatStatus;
	}

	public void setHeatStatus(HeatIndexStatus heatStatus) {
		this.heatStatus = heatStatus;
	}

	public Double getHeatValue() {
		return heatValue;
	}

	public void setHeatValue(Double heatValue) {
		this.heatValue = heatValue;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
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
