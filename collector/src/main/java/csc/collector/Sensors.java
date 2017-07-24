
package csc.collector;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "DHT22_AH",
    "LDR",
    "BMP085_PRESSURE",
    "DHT22_TEMP"
})
public class Sensors implements Serializable {

    @JsonProperty("DHT22_AH")
    private Double dHT22AH;
    @JsonProperty("LDR")
    private Double lDR;
    @JsonProperty("BMP085_PRESSURE")
    private Double bMP085PRESSURE;
    @JsonProperty("DHT22_TEMP")
    private Double dHT22TEMP;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("DHT22_AH")
    public Double getDHT22AH() {
        return dHT22AH;
    }

    @JsonProperty("DHT22_AH")
    public void setDHT22AH(Double dHT22AH) {
        this.dHT22AH = dHT22AH;
    }

    @JsonProperty("LDR")
    public Double getLDR() {
        return lDR;
    }

    @JsonProperty("LDR")
    public void setLDR(Double lDR) {
        this.lDR = lDR;
    }

    @JsonProperty("BMP085_PRESSURE")
    public Double getBMP085PRESSURE() {
        return bMP085PRESSURE;
    }

    @JsonProperty("BMP085_PRESSURE")
    public void setBMP085PRESSURE(Double bMP085PRESSURE) {
        this.bMP085PRESSURE = bMP085PRESSURE;
    }

    @JsonProperty("DHT22_TEMP")
    public Double getDHT22TEMP() {
        return dHT22TEMP;
    }

    @JsonProperty("DHT22_TEMP")
    public void setDHT22TEMP(Double dHT22TEMP) {
        this.dHT22TEMP = dHT22TEMP;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
