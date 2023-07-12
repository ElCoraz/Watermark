package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.Search;
import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.repository.TemplateRepository;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.Query;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
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

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", true, true);

        new com.elcorazon.adminlte.utils.Settings(environment).save(settings);

        return ResponseEntity.status(HttpStatus.OK).body(Images.getImage(Images.mergeImage(settings)));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/table/{id}")
    public ResponseEntity<String> table(@PathVariable String id) throws JsonProcessingException {
        return Query.getResponseEntityQuery("list/" + id, HttpMethod.GET, null);
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/search")
    public ResponseEntity<String> search(@RequestBody String body) throws JsonProcessingException {
        return Query.getResponseEntityQuery("search/", HttpMethod.POST, new ObjectMapper().readValue(body, Search.class));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/template/{template}")
    public ResponseEntity<String> template(@RequestBody String body, @PathVariable String template) throws IOException {
        Template Template = templateRepository.findAllById(template);

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", true, true);

        settings.top = (new ObjectMapper().readValue(Template.top, LayerSave.class)).getLoad();
        settings.bottom = (new ObjectMapper().readValue(Template.bottom, LayerSave.class)).getLoad();

        new com.elcorazon.adminlte.utils.Settings(environment).save(settings);

        return ResponseEntity.status(HttpStatus.OK).body(Images.getImage(Images.mergeImage(settings)));
    }

    /******************************************************************************************************************/
    @PostMapping(path = "/template/create/{name}/{id}")
    public ResponseEntity<String> create(@RequestBody String body, @PathVariable String name, @PathVariable String id) throws IOException {
        Template template = templateRepository.findAllById(id);

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1", false, false);

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
    public ResponseEntity<String> count(@PathVariable String id) throws JsonProcessingException {
        return Query.getResponseEntityQuery("count/" + id, HttpMethod.GET, null);
    }
    
    /******************************************************************************************************************/
    @GetMapping(path = "/info")
    public ResponseEntity<String> info() throws IOException {
        JSONArray jsonArray = new JSONArray();

        File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/")).listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("id", file.getName());

                int count = 0;

                File[] listOfFiles1 = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + file.getName())).listFiles();

                if (listOfFiles1 != null) {
                        for (File file1 : listOfFiles1) {
                                if (file1.isFile() && (file1.getName().indexOf(".png") > 0)) {
                                        count++;
                                }
                        }
                }

                jsonObject.put("count", count);

                jsonArray.put(jsonObject);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(jsonArray.toString());
    }

    /******************************************************************************************************************/
    @GetMapping(path = "/image/{id}")
    public ResponseEntity<String> propertyImage(@PathVariable String id) throws IOException {
        int count = 0;

        File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id)).listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile() && (file.getName().indexOf(".png") > 0) && (file.length() > 0)) {
                    count++;
                }

                if (file.length() == 0) {
                    file.delete();
                }
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("{\"count\": " + count + " }");
    }

    /******************************************************************************************************************/
    private ResponseEntity<byte[]> image(ByteArrayOutputStream byteArrayOutputStream, String type) {
        MediaType mediaType = null;

        switch (type) {
            case "png":
                mediaType = MediaType.IMAGE_PNG;
                break;
            case "gif":
                mediaType = MediaType.IMAGE_GIF;
                break;
            case "jpg":
                mediaType = MediaType.IMAGE_JPEG;
                break;
        }

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image." + type + "\"")
                .contentType(mediaType)
                .body(byteArrayOutputStream.toByteArray());
    }

    /******************************************************************************************************************/
    @GetMapping(path = "/image/{id}/{type}/{index}")
    public ResponseEntity<byte[]> dataImage(@PathVariable String id, @PathVariable String type, @PathVariable String index) throws Exception {
        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings(index, id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom), true);

        try {
            settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, index, watermarks_top, watermarks_bottom, true, true);
        } catch (Exception e) {
            com.elcorazon.adminlte.utils.Settings newSettings = new com.elcorazon.adminlte.utils.Settings();

            newSettings.setEnvironment(environment);

            newSettings.save(id);

            settings = newSettings.load(settings, index, watermarks_top, watermarks_bottom, true, true);
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        BufferedImage image = Images.mergeImage(settings);

        if (type.equals("jpg") || type.equals("gif")) {
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

            newImage.createGraphics().drawImage(image, 0, 0, Color.white, null);

            image = newImage;
        }

        ImageIO.write(image, type, byteArrayOutputStream);

        return image(byteArrayOutputStream, type);
    }
}
