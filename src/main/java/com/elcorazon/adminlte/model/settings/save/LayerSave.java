package com.elcorazon.adminlte.model.settings.save;

import com.elcorazon.adminlte.model.settings.main.Layer;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class LayerSave {
    /******************************************************************************************************************/
    public LayerSave () {
        this.uuid = "";
        this.scale = 10;
        this.alpha = 0.5f;
        this.width = 100;
        this.height = 100;
    }
    /******************************************************************************************************************/
    @JsonProperty("uuid")
    public String uuid;
    /******************************************************************************************************************/
    @JsonProperty("scale")
    public Integer scale;
    /******************************************************************************************************************/
    @JsonProperty("alpha")
    public float alpha;
    /******************************************************************************************************************/
    @JsonProperty("width")
    public Integer width;
    /******************************************************************************************************************/
    @JsonProperty("height")
    public Integer height;
    /******************************************************************************************************************/
    @JsonIgnore
    public Layer getLoad() {
        Layer layer = new Layer();

        layer.uuid = this.uuid;
        layer.width = this.width;
        layer.scale = this.scale;
        layer.alpha = this.alpha;
        layer.height = this.height;

        return layer;
    }
}