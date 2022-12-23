package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.model.database.Template;
import com.elcorazon.adminlte.model.database.Watermark;
import com.elcorazon.adminlte.repository.TemplateRepository;
import com.elcorazon.adminlte.repository.WatermarkRepository;
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Optional;

@ApiIgnore
@Controller
public class TemplateController {
    /******************************************************************************************************************/
    @Autowired
    private TemplateRepository templateRepository;
    /******************************************************************************************************************/
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        model.addAttribute("list", templateRepository.findAll());

        return "template/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/template/edit/{id}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable String id) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        Optional<Template> template = templateRepository.findById(id);

        template.ifPresent(value -> model.addAttribute("data", value));

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
