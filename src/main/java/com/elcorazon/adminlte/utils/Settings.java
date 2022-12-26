package com.elcorazon.adminlte.utils;

import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.main.Layer;
import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.model.settings.save.SettingsSave;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
/**********************************************************************************************************************/
public class Settings {
    /******************************************************************************************************************/
    private com.elcorazon.adminlte.model.Settings settings;
    /******************************************************************************************************************/
    private Environment environment;

    /******************************************************************************************************************/
    public Settings(Environment environment) throws IOException {
        this.environment = environment;

        ObjectMapper objectMapper = new ObjectMapper();

        this.settings = objectMapper.readValue(new String(Files.readAllBytes(Paths.get(Objects.requireNonNull(this.environment.getProperty("settings.file.name")))), StandardCharsets.UTF_8), objectMapper.getTypeFactory().constructType(com.elcorazon.adminlte.model.Settings.class));
    }

    /******************************************************************************************************************/
    public String getPath() {
        return this.settings.path;
    }

    /******************************************************************************************************************/
    public String getWatermark() {
        return this.settings.watermark;
    }

    /******************************************************************************************************************/
    public String getVersion() {
        return environment.getProperty("settings.version");
    }

    /******************************************************************************************************************/
    public com.elcorazon.adminlte.model.settings.main.Settings load(com.elcorazon.adminlte.model.settings.main.Settings settings, String i, List<Watermark> watermarks_top, List<Watermark> watermarks_bottom, boolean exception) throws Exception {
        try {
            SettingsSave settingsSave = (new ObjectMapper()).readValue(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "images\\" + settings.uuid + "\\settings.json").toFile(), SettingsSave.class);

            settings = new com.elcorazon.adminlte.model.settings.main.Settings();

            Layer bottom = new Layer();

            bottom.uuid = settingsSave.bottom.uuid;
            bottom.alpha = settingsSave.bottom.alpha;
            bottom.scale = settingsSave.bottom.scale;
            bottom.width = settingsSave.bottom.width;
            bottom.height = settingsSave.bottom.height;

            bottom.image = Images.getWatermark(bottom);

            settings.bottom = bottom;

            Layer top = new Layer();

            top.uuid = settingsSave.top.uuid;
            top.alpha = settingsSave.top.alpha;
            top.scale = settingsSave.top.scale;
            top.width = settingsSave.top.width;
            top.height = settingsSave.top.height;

            top.image = Images.getWatermark(top);

            settings.top = top;

            settings.uuid = settingsSave.uuid;
            settings.name = settingsSave.name;
            settings.width = settingsSave.width;
            settings.height = settingsSave.height;

            settings.image = Images.loadImage(i, settings.uuid, false);

            for (Watermark watermark : watermarks_top) {
                watermark.checked = watermark.uuid.equals(top.uuid);
            }

            for (Watermark watermark : watermarks_bottom) {
                watermark.checked = watermark.uuid.equals(bottom.uuid);
            }
        } catch (Exception e) {
            if (exception) {
                throw new Exception("Not found file");
            }
        }
        return Images.appendSettings(settings, i);
    }

    /******************************************************************************************************************/
    public void save(com.elcorazon.adminlte.model.settings.main.Settings settings) throws IOException {
        SettingsSave settingsSave = new SettingsSave();

        settingsSave.uuid = settings.uuid;
        settingsSave.name = settings.name;
        settingsSave.height = settings.height;
        settingsSave.width = settings.width;

        LayerSave bottom = new LayerSave();

        bottom.alpha = settings.bottom.alpha;
        bottom.height = settings.bottom.height;
        bottom.scale = settings.bottom.scale;
        bottom.uuid = settings.bottom.uuid;
        bottom.width = settings.bottom.width;

        LayerSave top = new LayerSave();

        top.alpha = settings.top.alpha;
        top.height = settings.top.height;
        top.scale = settings.top.scale;
        top.uuid = settings.top.uuid;
        top.width = settings.top.width;

        settingsSave.top = top;
        settingsSave.bottom = bottom;

        (new ObjectMapper()).writeValue(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "images\\" + settings.uuid + "\\settings.json").toFile(), settingsSave);
    }
}