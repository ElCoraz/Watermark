package com.elcorazon.adminlte.model.settings.prototype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**********************************************************************************************************************/
@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
    /******************************************************************************************************************/
    public Settings() {
        this.uuid = "";
        this.name = "";
        this.width = 100;
        this.height = 100;
    }
    /******************************************************************************************************************/
    @JsonProperty("name")
    public String name;
    /******************************************************************************************************************/
    @JsonProperty("uuid")
    public String uuid;
    /******************************************************************************************************************/
    @JsonProperty("width")
    public Integer width;
    /******************************************************************************************************************/
    @JsonProperty("height")
    public Integer height;
}
