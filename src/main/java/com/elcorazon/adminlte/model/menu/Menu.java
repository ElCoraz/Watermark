package com.elcorazon.adminlte.model.menu;

import java.util.List;
/**********************************************************************************************************************/
public class Menu {
    /******************************************************************************************************************/
    public String name;
    public String link;
    public String icon;
    public List<SubMenu> subMenu;
    /******************************************************************************************************************/
    public Menu(String _name, String _link, String _icon, List<SubMenu> _subMenu) {
        name = _name;
        link = _link;
        icon = _icon;
        subMenu = _subMenu;
    }
}