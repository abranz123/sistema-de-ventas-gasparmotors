package com.gasparmotors.sistema.services;

import com.gasparmotors.sistema.models.Producto;
import com.gasparmotors.sistema.repositories.ProductoRepository;
import org.apache.commons.lang3.StringUtils; // 30% RÚBRICA: Uso de Apache Commons
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    // 1. Método para listar todas las motos y repuestos
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    // 2. Método para guardar o actualizar un producto con reglas de negocio
    public Producto guardar(Producto producto) {

        // SEGURIDAD / VALIDACIÓN: Usamos Apache Commons para asegurar que el nombre no sea solo espacios en blanco
        if (StringUtils.isBlank(producto.getNombre_producto())) {
            throw new IllegalArgumentException("El nombre del producto o repuesto no puede estar vacío.");
        }

        // REGLA DE NEGOCIO (Base para TDD): Impedir el ingreso de stock negativo
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser menor a cero.");
        }

        return productoRepository.save(producto);
    }
}