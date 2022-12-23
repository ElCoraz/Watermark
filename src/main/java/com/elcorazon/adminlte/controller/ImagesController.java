package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.*;
import com.elcorazon.adminlte.model.settings.main.Layer;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.SettingsSave;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**********************************************************************************************************************/
@ApiIgnore
@Controller
public class ImagesController {
    /******************************************************************************************************************/
    @RequestMapping(value = "/images", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        return "images/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public String image(Model model, @PathVariable String id) throws IOException {
        List<Watermark> watermarks_top = Images.getWatermarks();
        List<Watermark> watermarks_bottom = Images.getWatermarks();

        Settings settings = Images.getSettings(id, Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom));

        try {
            SettingsSave settingsSave = (new ObjectMapper()).readValue(Paths.get(Images.getPath() + id + ".json").toFile(), SettingsSave.class);

            settings = new Settings();

            Layer bottom = new Layer();

            bottom.alpha = settingsSave.bottom.alpha;
            bottom.height = settingsSave.bottom.height;
            bottom.scale = settingsSave.bottom.scale;
            bottom.uuid = settingsSave.bottom.uuid;
            bottom.width = settingsSave.bottom.width;
            bottom.image = Images.getWatermark(bottom);

            settings.bottom = bottom;

            Layer top = new Layer();

            top.alpha = settingsSave.top.alpha;
            top.height = settingsSave.top.height;
            top.scale = settingsSave.top.scale;
            top.uuid = settingsSave.top.uuid;
            top.width = settingsSave.top.width;
            top.image = Images.getWatermark(top);

            settings.top = top;

            settings.height = settingsSave.height;
            settings.uuid = settingsSave.uuid;
            settings.width = settingsSave.width;
            settings.image = Images.loadImage(settings.uuid, false);
            ;

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

        model.addAttribute("settings", settings);

        model.addAttribute("watermarks_top", watermarks_top);
        model.addAttribute("watermarks_bottom", watermarks_bottom);
        model.addAttribute("show_watermarks_top", Images.haveWatemark(watermarks_top));
        model.addAttribute("show_watermarks_bottom", Images.haveWatemark(watermarks_bottom));

        return "images/image";
    }
}