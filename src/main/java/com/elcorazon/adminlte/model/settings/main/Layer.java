package com.elcorazon.adminlte.model.settings.main;

import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.image.BufferedImage;
/**********************************************************************************************************************/
public class Layer extends com.elcorazon.adminlte.model.settings.prototype.Layer {
    /******************************************************************************************************************/
    public Layer () {
        super();

        this.image = null;
    }
    /******************************************************************************************************************/
    @JsonIgnore
    public BufferedImage image;
    /******************************************************************************************************************/
    @JsonIgnore
    public LayerSave getSave() {
        LayerSave layer = new LayerSave();

        layer.uuid = this.uuid;
        layer.width = this.width;
        layer.scale = this.scale;
        layer.alpha = this.alpha;
        layer.height = this.height;

        return layer;
    }
}