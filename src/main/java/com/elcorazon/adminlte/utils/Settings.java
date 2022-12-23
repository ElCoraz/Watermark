package com.elcorazon.adminlte.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.env.Environment;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
}