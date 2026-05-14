package com.formacionbdi.springboot.app.productos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.formacionbdi.springboot.app.productos.models.dao.ProductoDao;
import com.formacionbdi.springboot.app.productos.models.entity.Producto;
import com.formacionbdi.springboot.app.productos.models.service.ProductoServiceImpl;

public class ProductoServiceImplTest {

    @Mock
    private ProductoDao productoDao;

    @InjectMocks
    private ProductoServiceImpl productoService;

    private Producto producto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        producto = new Producto();
        producto.setId(1L);
        producto.setMarca("Toyota");
        producto.setModelo("Corolla");
        producto.setAnio(2024);
        producto.setPrecio(625900.00);
    }

    @Test
    void testFindAll() {
        when(productoDao.findAll()).thenReturn(Arrays.asList(producto));
        List<Producto> productos = productoService.findAll();
        assertNotNull(productos);
        assertEquals(1, productos.size());
        assertEquals("Toyota", productos.get(0).getMarca());
        verify(productoDao, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(productoDao.findById(1L)).thenReturn(Optional.of(producto));
        Producto result = productoService.findById(1L);
        assertNotNull(result);
        assertEquals("Corolla", result.getModelo());
        verify(productoDao, times(1)).findById(1L);
    }

    @Test
    void testSave() {
        when(productoDao.save(any(Producto.class))).thenReturn(producto);
        Producto result = productoService.save(producto);
        assertNotNull(result);
        assertEquals(2024, result.getAnio());
        verify(productoDao, times(1)).save(producto);
    }

    @Test
    void testEliminar() {
        productoService.eliminar(1L);
        verify(productoDao, times(1)).deleteById(1L);
    }
}
