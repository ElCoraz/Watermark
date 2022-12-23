package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.settings.main.Layer;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.SettingsSave;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.Query;
import com.elcorazon.adminlte.utils.User;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@ApiIgnore
@Controller
public class ProductController {
    /******************************************************************************************************************/
    @Autowired
    private WatermarkRepository watermarkRepository;
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
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("data", Query.getStringQuery("product", HttpMethod.GET).replaceAll("\uFEFF", ""));

        return "product/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product/{id}", method = RequestMethod.GET)
    public String position(Model model, @PathVariable String id) throws IOException {

        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings(id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom));

        try {
            SettingsSave settingsSave = (new ObjectMapper()).readValue(Paths.get(Images.getPath() + id + ".json").toFile(), SettingsSave.class);

            settings = new Settings();

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

            settings.image = Images.loadImage(settings.uuid, false);

            for (Watermark watermark : watermarks_top) {
                watermark.checked = watermark.uuid.equals(top.uuid);
            }

            for (Watermark watermark : watermarks_bottom) {
                watermark.checked = watermark.uuid.equals(bottom.uuid);
            }

            settings = Images.appendSettings(settings);

        } catch (IOException ignored) {
        }

        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("image", Images.getImage(Images.mergeImage(settings)));

        settings.name = getName(settings.uuid);

        model.addAttribute("settings", settings);

        model.addAttribute("watermarks_top", watermarks_top);
        model.addAttribute("watermarks_bottom", watermarks_bottom);
        model.addAttribute("show_watermarks_top", Images.haveWatemark(watermarks_top));
        model.addAttribute("show_watermarks_bottom", Images.haveWatemark(watermarks_bottom));

        ArrayList images = new ArrayList();

        if (!Files.isDirectory(Paths.get(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id))) {
            Files.createDirectories(Paths.get(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id));
        } else {
            File[] listOfFiles = (new File(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id)).listFiles();

            if (listOfFiles != null) {
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        images.add(file.getName());
                    }
                }
            }
        }

        model.addAttribute("images", images);

        return "product/position";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/product/upload/{id}", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, @PathVariable String id) throws IOException {

        if (!Files.isDirectory(Paths.get(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id))) {
            Files.createDirectories(Paths.get(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id));
        }

        Files.copy(file.getInputStream(), (new File(Images.path + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\images\\" + id + "\\" + id + ".png")).toPath());

        return "redirect:/product/" + id;
    }

    /******************************************************************************************************************/
    private String getName(String id) {
        return Query.getStringQuery("name/" + id, HttpMethod.POST);
    }
}
