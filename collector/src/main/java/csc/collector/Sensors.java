
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
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "DHT22_AH",
    "LDR",
    "BMP085_PRESSUqRE",
    "DHT22_TEMP"
})
public class Sensors implements Serializable
{

    @JsonProperty("DHT22_AH")
    private Double dHT22AH;
    @JsonProperty("LDR")
    private Double lDR;
    @JsonProperty("BMP085_PRESSUqRE")
    private Double bMP085PRESSUqRE;
    @JsonProperty("DHT22_TEMP")
    private Double dHT22TEMP;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 5202241079807197312L;

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

    @JsonProperty("BMP085_PRESSUqRE")
    public Double getBMP085PRESSUqRE() {
        return bMP085PRESSUqRE;
    }

    @JsonProperty("BMP085_PRESSUqRE")
    public void setBMP085PRESSUqRE(Double bMP085PRESSUqRE) {
        this.bMP085PRESSUqRE = bMP085PRESSUqRE;
    }

    @JsonProperty("DHT22_TEMP")
    public Double getDHT22TEMP() {
        return dHT22TEMP;
    }

    @JsonProperty("DHT22_TEMP")
    public void setDHT22TEMP(Double dHT22TEMP) {
        this.dHT22TEMP = dHT22TEMP;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
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
