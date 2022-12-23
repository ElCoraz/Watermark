package com.elcorazon.adminlte.model.settings;

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
public class Watermark {
    /******************************************************************************************************************/
    public Watermark (String uuid, String name, Boolean checked) {
        this.uuid = uuid;
        this.name = name;
        this.checked = checked;
    }
    /******************************************************************************************************************/
    @JsonProperty("uuid")
    public String uuid;
    /******************************************************************************************************************/
    @JsonProperty("name")
    public String name;
    /******************************************************************************************************************/
    @JsonProperty("checked")
    public Boolean checked;
}
