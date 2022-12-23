package com.elcorazon.adminlte.model.settings.main;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**********************************************************************************************************************/
@Data
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Layer {
    /******************************************************************************************************************/
    public Layer () {
        this.uuid = "";
        this.scale = 50;
        this.alpha = 0.5f;
        this.width = 100;
        this.height = 100;
        this.image = null;
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
    public BufferedImage image;
}