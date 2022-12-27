package com.elcorazon.adminlte.model.settings.save;

import com.fasterxml.jackson.annotation.JsonProperty;

/**********************************************************************************************************************/
public class SettingsSave extends com.elcorazon.adminlte.model.settings.prototype.Settings {
    /******************************************************************************************************************/
    public SettingsSave () {
        super();

        this.top = new LayerSave();
        this.bottom = new LayerSave();
    }

    /******************************************************************************************************************/
    @JsonProperty("top")
    public LayerSave top;
    /******************************************************************************************************************/
    @JsonProperty("bottom")
    public LayerSave bottom;
}
