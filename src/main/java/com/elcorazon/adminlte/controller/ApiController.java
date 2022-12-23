package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.SettingsSave;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Paths;

/**********************************************************************************************************************/
@RestController
@RequestMapping(path = "/api")
public class ApiController {
    /******************************************************************************************************************/
    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "";
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/merge")
    public ResponseEntity<String> merge(@RequestBody String body) throws IOException {

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class));

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

        (new ObjectMapper()).writeValue(Paths.get(Images.getPath() + settings.uuid + ".json").toFile(), settingsSave);

        return ResponseEntity.status(HttpStatus.OK).body(Images.getImage(Images.mergeImage(settings)));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/table/{id}")
    public ResponseEntity<String> table(@PathVariable String id) {
        return Query.getResponseEntityQuery("list/" + id, HttpMethod.GET);
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/search/{text}")
    public ResponseEntity<String> search(@PathVariable String text) {
        return Query.getResponseEntityQuery("search/" + text, HttpMethod.GET);
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/count/{id}")
    public ResponseEntity<String> count(@PathVariable String id) {
        return Query.getResponseEntityQuery("count/" + id, HttpMethod.GET);
    }
}
