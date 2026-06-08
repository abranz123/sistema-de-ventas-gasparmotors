package com.gasparmotors.sistema.controllers;

import com.gasparmotors.sistema.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VentaController {

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/ventas/nueva")
    public String mostrarNuevaVenta(Model model) {
        // Mandamos todos los productos (motos y repuestos) para llenar el buscador del carrito
        model.addAttribute("productos", productoRepository.findAll());
        return "nueva-venta";
    }
}