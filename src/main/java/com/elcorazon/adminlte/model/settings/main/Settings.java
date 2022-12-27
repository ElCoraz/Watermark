package com.elcorazon.adminlte.model.settings.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.BufferedImage;

/**********************************************************************************************************************/
public class Settings extends com.elcorazon.adminlte.model.settings.prototype.Settings {
    /******************************************************************************************************************/
    public Settings () {
        super();

        this.top = new Layer();
        this.bottom = new Layer();

        this.image = null;
    }
    /******************************************************************************************************************/
    @JsonProperty("top")
    public Layer top;
    /******************************************************************************************************************/
    @JsonProperty("bottom")
    public Layer bottom;
    /******************************************************************************************************************/
    @JsonIgnore
    public BufferedImage image;
}
