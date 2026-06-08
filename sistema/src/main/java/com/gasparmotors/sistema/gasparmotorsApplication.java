package com.gasparmotors.sistema;

import com.gasparmotors.sistema.models.Categoria;
import com.gasparmotors.sistema.models.Producto;
import com.gasparmotors.sistema.repositories.CategoriaRepository;
import com.gasparmotors.sistema.repositories.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;

@SpringBootApplication
class gasparmotorsApplication {

	public static void main(String[] args) {
		SpringApplication.run(gasparmotorsApplication.class, args);
	}

	// Este Bean se ejecuta automáticamente al iniciar el servidor
	@Bean
	public CommandLineRunner poblarDatosPrueba(CategoriaRepository categoriaRepository, ProductoRepository productoRepository) {
		return args -> {
			// Solo insertamos datos si la tabla de categorías está vacía
			if (categoriaRepository.count() == 0) {

				// 1. Creamos Categorías
				Categoria catRepuestos = new Categoria();
				catRepuestos.setNombre_categoria("Repuestos");
				categoriaRepository.save(catRepuestos);

				Categoria catMotos = new Categoria();
				catMotos.setNombre_categoria("Motocicletas");
				categoriaRepository.save(catMotos);

				// 2. Creamos Productos de Prueba
				Producto prod1 = new Producto();
				prod1.setNombre_producto("Bujía NGK Iridium");
				prod1.setPrecio(new BigDecimal("45.50"));
				prod1.setStock(150);
				prod1.setCategoria(catRepuestos);
				productoRepository.save(prod1);

				Producto prod2 = new Producto();
				prod2.setNombre_producto("Yamaha MT-03 2024");
				prod2.setPrecio(new BigDecimal("21500.00"));
				prod2.setStock(3);
				prod2.setCategoria(catMotos);
				productoRepository.save(prod2);

				System.out.println("✅ DATOS DE PRUEBA CARGADOS CON ÉXITO");
			}
		};
	}
}