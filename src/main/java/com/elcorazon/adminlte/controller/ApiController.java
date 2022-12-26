package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.Image;
import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.main.Layer;
import com.elcorazon.adminlte.model.settings.main.Settings;
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
import java.awt.image.BufferedImage;
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

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1");

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
    public ResponseEntity<String> search(@RequestBody String body, @PathVariable String template) throws IOException  {

        Template Template = templateRepository.findAllById(template);

        Settings settings = Images.appendSettings(new ObjectMapper().readValue(body, Settings.class), "1");

        settings.top = new ObjectMapper().readValue(Template.top, Layer.class);
        settings.bottom = new ObjectMapper().readValue(Template.bottom, Layer.class);

        new com.elcorazon.adminlte.utils.Settings(environment).save(settings);

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

        Integer count = 0;

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
    public ResponseEntity<byte[]> dataImage(@PathVariable String id, @PathVariable String index) throws IOException {

        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings(index, id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom));

        settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, index, watermarks_top, watermarks_bottom);

        BufferedImage bufferedImage = Images.mergeImage(settings);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage , "png", byteArrayOutputStream);

        byte[] imageInByte = byteArrayOutputStream.toByteArray();
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"image.png\"")
                        .contentType(MediaType.IMAGE_PNG)
                        .body(imageInByte);
    }
}
