package com.formacionbdi.springboot.app.item.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VistaController {

    @GetMapping("/productos")
    public String productosVista() {
        return "forward:/productos.html";
    }
}
