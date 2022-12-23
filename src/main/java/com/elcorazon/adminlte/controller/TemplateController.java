package com.elcorazon.adminlte.controller;

import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.annotations.ApiIgnore;

@ApiIgnore
@Controller
public class TemplateController {
    /******************************************************************************************************************/
    @RequestMapping(value = "/template", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        return "template/index";
    }

    /******************************************************************************************************************/
    @RequestMapping(value = "/template/{id}", method = RequestMethod.GET)
    public String template(Model model, @PathVariable String id) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        return "template/template";
    }
}
