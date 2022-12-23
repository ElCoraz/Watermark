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
public class Settings {
    /******************************************************************************************************************/
    public Settings () {
        this.uuid = "";
        this.name = "";
        this.top = new Layer();
        this.bottom = new Layer();
        this.width = 100;
        this.height = 100;
        this.image = null;
    }
    /******************************************************************************************************************/
    @JsonProperty("name")
    public String name;
    /******************************************************************************************************************/
    @JsonProperty("uuid")
    public String uuid;
    /******************************************************************************************************************/
    @JsonProperty("top")
    public Layer top;
    /******************************************************************************************************************/
    @JsonProperty("bottom")
    public Layer bottom;
    /******************************************************************************************************************/
    @JsonProperty("width")
    public Integer width;
    /******************************************************************************************************************/
    @JsonProperty("height")
    public Integer height;
    /******************************************************************************************************************/
    public BufferedImage image;
}
