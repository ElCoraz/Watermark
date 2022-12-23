package com.elcorazon.adminlte.utils;

import com.elcorazon.adminlte.model.menu.Menu;

import java.util.List;
import java.util.ArrayList;
/**********************************************************************************************************************/
public class MenuCreate {
    /******************************************************************************************************************/
    public static List<Menu> getMenu() {
        List<Menu> menu = new ArrayList<Menu>();

        menu.add(new Menu("Продукция", "/product", "far fa-circle nav-icon", null));
        menu.add(new Menu("Водяные знаки", "/watermark", "far fa-circle nav-icon", null));
        menu.add(new Menu("Шаблоны", "/template", "far fa-circle nav-icon", null));

        return menu;
    }
}