package com.formacionbdi.springboot.app.item.clientes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.formacionbdi.springboot.app.item.models.Producto;

@Component
public class ProductoClienteRestFallback implements ProductoClienteRest {

	@Override
	public List<Producto> listar() {
		// Método fallback: retorna una lista vacía
		System.out.println("Fallback ejecutado: listar()");
		return new ArrayList<>();
	}

	@Override
	public Producto detalle(Long id) {
		// Método fallback: retorna un producto con datos predeterminados
		System.out.println("Fallback ejecutado: detalle(" + id + ")");
		Producto producto = new Producto();
		producto.setId(id);
		producto.setMarca("No disponible");
		producto.setModelo("Serv. no disponible");
		producto.setAnio(0);
		producto.setPrecio(0.0);
		return producto;
	}

	@Override
	public Producto detalleConRetardo(Long id, Integer tiempo) {
		System.out.println("Fallback ejecutado: detalleConRetardo(" + id + ", " + tiempo + ")");
		return detalle(id);
	}

	@Override
	public void eliminarProducto(Long id) {
		// Método fallback: simplemente registra el intento
		System.out.println("Fallback ejecutado: eliminarProducto(" + id + ")");
	}

}
