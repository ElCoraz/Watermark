package com.elcorazon.adminlte.model.settings.save;

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
public class SettingsSave {
    /******************************************************************************************************************/
    public SettingsSave () {
        this.uuid = "";
        this.name = "";
        this.top = new LayerSave();
        this.bottom = new LayerSave();
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
    /******************************************************************************************************************/
    @JsonProperty("top")
    public LayerSave top;
    /******************************************************************************************************************/
    @JsonProperty("bottom")
    public LayerSave bottom;
}
