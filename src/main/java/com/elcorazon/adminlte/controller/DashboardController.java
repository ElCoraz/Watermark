package com.elcorazon.adminlte.controller;
//**********************************************************************************************************************
import com.elcorazon.adminlte.utils.MenuCreate;
import com.elcorazon.adminlte.utils.User;
//**********************************************************************************************************************
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
//**********************************************************************************************************************
@Controller
public class DashboardController {
    //******************************************************************************************************************
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User(SecurityContextHolder.getContext().getAuthentication()));
        model.addAttribute("menu", MenuCreate.getMenu());

        return "dashboard/index";
    }
    //******************************************************************************************************************
}
//**********************************************************************************************************************