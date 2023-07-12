package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.Image;
import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.repository.TemplateRepository;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.Query;
import com.elcorazon.adminlte.utils.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
/**********************************************************************************************************************/

@ApiIgnore
@Controller
public class ProductController {
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
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root() {
        return "redirect:/product";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product", method = RequestMethod.GET)
    public String index(Model model) throws JsonProcessingException {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("data", Query.getStringQuery("product", HttpMethod.GET).replaceAll("\uFEFF", ""));

        return "product/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product/{id}/{index}", method = RequestMethod.GET)
    public String position(Model model, @PathVariable String id, @PathVariable String index) throws Exception {
        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings(index, id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom), true);

        Template template = templateRepository.findAllById(settings.template);

        if (template != null) {
            settings.top = (new ObjectMapper().readValue(template.top, LayerSave.class)).getLoad();
            settings.bottom = (new ObjectMapper().readValue(template.bottom, LayerSave.class)).getLoad();
        }

        settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, index, watermarks_top, watermarks_bottom, false, true);

        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("image", Images.getImage(Images.mergeImage(settings)));

        settings.name = getName(settings.uuid);

        model.addAttribute("index", index);
        model.addAttribute("settings", settings);

        model.addAttribute("templates", templateRepository.findAll());

        model.addAttribute("watermarks_top", watermarks_top);
        model.addAttribute("watermarks_bottom", watermarks_bottom);
        model.addAttribute("show_watermarks_top", Images.haveWatermark(watermarks_top));
        model.addAttribute("show_watermarks_bottom", Images.haveWatermark(watermarks_bottom));

        ArrayList images = new ArrayList();

        if (!Files.isDirectory(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id))) {
            Files.createDirectories(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id));
        } else {
            File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id)).listFiles();

            Integer i = 1;

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile() && (file.getName().indexOf(".png") > 0)) {
                        images.add(new Image(file.getName(), i));

                        i++;
                    }
                }
            }
        }

        model.addAttribute("images", images);
        model.addAttribute("countImages", images.size());

        return "product/position";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product/upload/{id}", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String id) throws IOException {
        if (!Files.isDirectory(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id))) {
            Files.createDirectories(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id));
        }

        Files.copy(file.getInputStream(), (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id + "/" + (getCount(id) + 1) + ".png")).toPath());

        File localFile = new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id + "/" + (getCount(id) + 1) + ".png");

        if (localFile.length() == 0) {
            localFile.delete();
        }

        return "redirect:/product/" + id + "/1";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable String id) throws IOException {
        File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id)).listFiles();

        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                file.delete();
            }
        }

        Files.delete(Paths.get(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id));

        return "redirect:/product/" + id + "/1";
    }

    /******************************************************************************************************************/
    private String getName(String id) throws JsonProcessingException {
        return Query.getStringQuery("name/" + id, HttpMethod.POST);
    }

    /******************************************************************************************************************/
    private Integer getCount(String id) throws IOException {
        File[] listOfFiles = (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "/images/" + id)).listFiles();

        if (listOfFiles != null) {
            return listOfFiles.length;
        }

        return 0;
    }
}
