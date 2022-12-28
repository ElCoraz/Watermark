package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.settings.Watermark;
import com.elcorazon.adminlte.model.settings.main.Settings;
import com.elcorazon.adminlte.model.settings.save.LayerSave;
import com.elcorazon.adminlte.repository.TemplateRepository;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApiIgnore
@Controller
public class TemplateController {
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
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("list", templateRepository.findAll());

        return "template/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/template/template", method = RequestMethod.GET)
    public String template(Model model) throws Exception {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings("", "", Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom), false);

        settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, "", watermarks_top, watermarks_bottom, false, false);

        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("image", Images.getImage(Images.mergeImage(settings)));

        model.addAttribute("id", UUID.randomUUID().toString());
        model.addAttribute("name", "");
        model.addAttribute("settings", settings);
        model.addAttribute("templates", templateRepository.findAll());

        model.addAttribute("watermarks_top", watermarks_top);
        model.addAttribute("watermarks_bottom", watermarks_bottom);
        model.addAttribute("show_watermarks_top", Images.haveWatermark(watermarks_top));
        model.addAttribute("show_watermarks_bottom", Images.haveWatermark(watermarks_bottom));

        return "template/template";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/template/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String id) throws Exception {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        Optional<Template> template = templateRepository.findById(id);

        Images.setEnvironment(environment);

        List<Watermark> watermarks_top = Images.getWatermarks(watermarkRepository);
        List<Watermark> watermarks_bottom = Images.getWatermarks(watermarkRepository);

        Settings settings = Images.getSettings("", "", Images.getCurrentWatermarks(watermarks_top), Images.getCurrentWatermarks(watermarks_bottom), false);

        if (template.isPresent()) {
            settings.top = (new ObjectMapper().readValue(template.get().top, LayerSave.class)).getLoad();
            settings.bottom = (new ObjectMapper().readValue(template.get().bottom, LayerSave.class)).getLoad();
        }

        settings = new com.elcorazon.adminlte.utils.Settings(environment).load(settings, "", watermarks_top, watermarks_bottom, false, false);

        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("image", Images.getImage(Images.mergeImage(settings)));

        model.addAttribute("id", template.get().id);
        model.addAttribute("name", template.get().name);
        model.addAttribute("settings", settings);
        model.addAttribute("templates", template.get());

        for (Watermark watermark:watermarks_top) {
                watermark.checked = watermark.uuid.equals(settings.top.uuid);
        }

        for (Watermark watermark:watermarks_bottom) {
            watermark.checked = watermark.uuid.equals(settings.bottom.uuid);
        }

        model.addAttribute("watermarks_top", watermarks_top);
        model.addAttribute("watermarks_bottom", watermarks_bottom);
        model.addAttribute("show_watermarks_top", Images.haveWatermark(watermarks_top));
        model.addAttribute("show_watermarks_bottom", Images.haveWatermark(watermarks_bottom));

        return "template/template";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/template/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable String id) {
        Optional<Template> template = templateRepository.findById(id);

        template.ifPresent(value -> templateRepository.delete(value));

        return "redirect:/template";
    }
}
