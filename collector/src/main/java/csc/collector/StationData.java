
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
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sensors",
    "datetime"
})
public class StationData implements Serializable
{

    @JsonProperty("sensors")
    private Sensors sensors;
    @JsonProperty("datetime")
    private Datetime datetime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3380959894865867235L;

    @JsonProperty("sensors")
    public Sensors getSensors() {
        return sensors;
    }

    @JsonProperty("sensors")
    public void setSensors(Sensors sensors) {
        this.sensors = sensors;
    }

    @JsonProperty("datetime")
    public Datetime getDatetime() {
        return datetime;
    }

    @JsonProperty("datetime")
    public void setDatetime(Datetime datetime) {
        this.datetime = datetime;
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
