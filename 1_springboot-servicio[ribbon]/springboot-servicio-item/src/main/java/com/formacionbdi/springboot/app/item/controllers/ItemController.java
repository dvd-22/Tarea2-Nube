package com.formacionbdi.springboot.app.item.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.item.models.Item;
import com.formacionbdi.springboot.app.item.models.Producto;
import com.formacionbdi.springboot.app.item.models.service.ItemService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class ItemController {

    @Autowired
    @Qualifier("serviceFeign")
    private ItemService itemService;

    @GetMapping("/listar")
    public List<Item> listar() {
        return itemService.findAll();
    }

    @HystrixCommand(fallbackMethod = "metodoAlternativo")
    @GetMapping("/ver/{id}/cantidad/{cantidad}")
    public Item detalle(@PathVariable Long id, @PathVariable Integer cantidad) {
        return itemService.findById(id, cantidad);
    }

    @HystrixCommand(fallbackMethod = "metodoAlternativoLatencia")
    @GetMapping("/ver/{id}/cantidad/{cantidad}/retardo/{retardo}")
    public Item detalleConRetardo(@PathVariable Long id, @PathVariable Integer cantidad, @PathVariable Integer retardo) {
        return itemService.findByIdConRetardo(id, cantidad, retardo);
    }

    // Fallback para /ver/{id}/cantidad/{cantidad}
    public Item metodoAlternativo(Long id, Integer cantidad, Throwable e) {
        Item item = new Item();
        Producto producto = new Producto();

        item.setCantidad(cantidad);

        producto.setId(id);
        producto.setMarca("No disponible");
        producto.setModelo("Servicio no disponible");
        producto.setAnio(0);
        producto.setPrecio(0.0);

        item.setProducto(producto);

        if (e != null) {
            System.out.println("Fallback activado: " + e.getMessage());
        }

        return item;
    }

    // Fallback para /ver/{id}/cantidad/{cantidad}/retardo/{retardo}
    public Item metodoAlternativoLatencia(Long id, Integer cantidad, Integer retardo, Throwable e) {
        Item item = new Item();
        Producto producto = new Producto();

        item.setCantidad(cantidad);

        producto.setId(id);
        producto.setMarca("No disponible");
        producto.setModelo("Servicio no disponible");
        producto.setAnio(0);
        producto.setPrecio(0.0);

        item.setProducto(producto);

        if (e != null) {
            System.out.println("Fallback activado (latencia): " + e.getMessage());
        }

        return item;
    }
}