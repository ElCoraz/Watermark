package com.elcorazon.adminlte.model.settings.save;

import com.elcorazon.adminlte.model.settings.main.Layer;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**********************************************************************************************************************/
public class LayerSave extends com.elcorazon.adminlte.model.settings.prototype.Layer {
    /******************************************************************************************************************/
    public LayerSave () {
        super();
    }
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