package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.repository.TemplateRepository;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**********************************************************************************************************************/
@RestController
@RequestMapping(path = "/api")
public class ApiController {
    /******************************************************************************************************************/
    @Autowired
    private WatermarkRepository watermarkRepository;
    /******************************************************************************************************************/
    @Autowired
    private TemplateRepository templateRepository;
    /******************************************************************************************************************/
    @Autowired
    private Environment environment;
    /******************************************************************************************************************/
    @GetMapping(path = "/", produces = "application/json")
    public String index() {
        return "";
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/merge")
    public ResponseEntity<String> merge(@RequestBody String body) throws IOException {

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", true);

        new com.elcorazon.adminlte.utils.Settings(environment).save(settings);

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
    @PostMapping(path = "/template/{template}")
    public ResponseEntity<String> template(@RequestBody String body, @PathVariable String template) throws IOException  {
        Template Template = templateRepository.findAllById(template);

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", true);

        settings.top = (new ObjectMapper().readValue(Template.top, LayerSave.class)).getLoad();
        settings.bottom = (new ObjectMapper().readValue(Template.bottom, LayerSave.class)).getLoad();

        new com.elcorazon.adminlte.utils.Settings(environment).save(settings);

        return ResponseEntity.status(HttpStatus.OK).body(Images.getImage(Images.mergeImage(settings)));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/template/create/{name}/{id}")
    public ResponseEntity<String> create(@RequestBody String body, @PathVariable String name, @PathVariable String id) throws IOException  {
        Template template = templateRepository.findAllById(id);

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", false);

        if (template == null) {
            template = new Template();

            template.id = id;
        }

        template.name = name;

        template.top = (new ObjectMapper()).writeValueAsString(settings.top.getSave());
        template.bottom = (new ObjectMapper()).writeValueAsString(settings.bottom.getSave());

        templateRepository.save(template);

        return ResponseEntity.status(HttpStatus.OK).body(Images.getImage(Images.mergeImage(settings)));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/count/{id}")
    public ResponseEntity<String> count(@PathVariable String id) {
        return Query.getResponseEntityQuery("count/" + id, HttpMethod.GET);
    }

    /******************************************************************************************************************/
    @GetMapping(path = "/image/{id}")
    public ResponseEntity<String> propertyImage(@PathVariable String id) throws IOException {
        int count = 0;

        File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id)).listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && (file.getName().indexOf(".png") > 0)) {
                    count++;
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"count\": " + count + " }");
    }

    /******************************************************************************************************************/
    @GetMapping(path = "/image/{id}/{index}")
    public ResponseEntity<byte[]> dataImage(@PathVariable String id, @PathVariable String index) throws Exception {
        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings(index, id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom));

        try {
            settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, index, watermarks_top, watermarks_bottom, true);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image.png\"")
                    .contentType(MediaType.IMAGE_PNG)
                    .body((new ByteArrayOutputStream()).toByteArray());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        ImageIO.write(Images.mergeImage(settings) , "png", byteArrayOutputStream);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image.png\"")
                        .contentType(MediaType.IMAGE_PNG)
                        .body(byteArrayOutputStream.toByteArray());
    }
}
