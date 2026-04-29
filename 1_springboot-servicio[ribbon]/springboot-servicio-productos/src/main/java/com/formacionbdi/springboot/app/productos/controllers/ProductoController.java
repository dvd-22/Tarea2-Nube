package com.formacionbdi.springboot.app.productos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.IProductoService;

@RestController
public class ProductoController {
	
	@Autowired
	private IProductoService productoService;

	@Value("${server.port}")
	private Integer port;
	
	@GetMapping("/listar")
	public List<Producto> listar(){
		List<Producto> productos = productoService.findAll();
		productos.forEach(producto -> {
			if (producto != null) {
				producto.setPort(port);
			}
		});
		return productos;
	}
	
	@GetMapping("/ver/{id}")
	public Producto detalle(@PathVariable Long id) {
    	if (id.equals(10L)) {
        	throw new RuntimeException("Fallo simulado en servicio-productos");
    	}
    	Producto producto = productoService.findById(id);
    	if (producto != null) {
    		producto.setPort(port);
    	}
    	return producto;
	}

	@GetMapping("/ver/{id}/retardo/{tiempo}")
	public Producto detalleConRetardo(@PathVariable Long id, @PathVariable Integer tiempo) {
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		Producto producto = productoService.findById(id);
		if (producto != null) {
			producto.setPort(port);
		}
		return producto;
	}

	@DeleteMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id) {
		productoService.eliminar(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/eliminar/{id}")
	public ResponseEntity<?> eliminarPorNavegador(@PathVariable Long id) {
		productoService.eliminar(id);
		return ResponseEntity.ok("Producto eliminado");
	}

}
