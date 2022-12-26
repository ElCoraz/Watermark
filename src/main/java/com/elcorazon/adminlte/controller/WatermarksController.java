package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.database.Watermark;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.Images;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

/**********************************************************************************************************************/
@ApiIgnore
@Controller
public class WatermarksController {
    /******************************************************************************************************************/
    @Autowired
    private WatermarkRepository watermarkRepository;
    /******************************************************************************************************************/
    @Autowired
    private Environment environment;

    /******************************************************************************************************************/
    @RequestMapping(value = "/watermark", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("list", watermarkRepository.findAll());

        return "watermark/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/watermark/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String id) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        Optional<Watermark> watermark = watermarkRepository.findById(id);

        watermark.ifPresent(value -> model.addAttribute("data", value));

        return "watermark/form";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/watermark/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable String id) {
        Optional<Watermark> watermark = watermarkRepository.findById(id);

        watermark.ifPresent(value -> watermarkRepository.delete(value));

        return "redirect:/watermark";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/watermark/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam("name") String name) throws IOException {
        String id = UUID.randomUUID().toString();

        Files.copy(file.getInputStream(), (new File(Images.getPath() + (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\watermarks\\" + id + ".png")).toPath());

        Watermark watermark = new Watermark();

        watermark.id = id;
        watermark.url = (new com.elcorazon.adminlte.utils.Settings(environment).getPath()) + "\\watermarks\\" + id + ".png";
        watermark.name = name;
        watermark.uuid = id;
        watermark.value = "";
        watermark.template = "";

        watermarkRepository.save(watermark);

        return "redirect:/watermark";
    }
}