package com.elcorazon.adminlte.utils;

import org.springframework.security.core.Authentication;
/**********************************************************************************************************************/
public class User {
    /******************************************************************************************************************/
    public String id;
    public String name;
    /******************************************************************************************************************/
    public User(Authentication authentication) {
        id = authentication.getName();
        name = authentication.getName();
    }
}