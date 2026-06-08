package com.gasparmotors.sistema.controllers;

import com.gasparmotors.sistema.models.Producto;
import com.gasparmotors.sistema.repositories.CategoriaRepository;
import com.gasparmotors.sistema.services.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    // Inyectamos el DAO de categorías para el formulario
    @Autowired
    private CategoriaRepository categoriaRepository;

    // 1. Mostrar el inventario (El que ya teníamos)
    @GetMapping("/inventario")
    public String listarInventario(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "inventario";
    }

    // 2. NUEVO: Mostrar el formulario para agregar motos/repuestos
    @GetMapping("/inventario/nuevo")
    public String mostrarFormulario(Model model) {
        Producto producto = new Producto(); // Creamos un producto vacío
        model.addAttribute("producto", producto);
        // Mandamos la lista de categorías a la web para el <select>
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "nuevo-producto";
    }

    // 3. NUEVO: Recibir los datos del formulario y guardarlos en MySQL
    @PostMapping("/inventario/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        // Llamamos a tu servicio (que tiene la validación de Apache Commons)
        productoService.guardar(producto);
        // Redirigimos a la tabla para ver el nuevo producto guardado
        return "redirect:/inventario";
    }
}