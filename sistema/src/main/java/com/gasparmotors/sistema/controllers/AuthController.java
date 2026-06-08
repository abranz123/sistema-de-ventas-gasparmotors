package com.gasparmotors.sistema.controllers;

import com.gasparmotors.sistema.models.Cliente;
import com.gasparmotors.sistema.models.Empleado;
import com.gasparmotors.sistema.models.Producto;
import com.gasparmotors.sistema.repositories.CategoriaRepository;
import com.gasparmotors.sistema.repositories.ClienteRepository;
import com.gasparmotors.sistema.repositories.EmpleadoRepository;
import com.gasparmotors.sistema.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login-post")
    public String procesarLogin(@RequestParam("username") String username,
                                @RequestParam("password") String password,
                                Model model) {
        boolean esAdmin = username.equals("12345678") && password.equals("admin123");
        boolean esVendedor = username.equals("87654321") && password.equals("carlos123");

        if (esAdmin || esVendedor) {
            return "redirect:/inicio";
        } else {
            model.addAttribute("error", true);
            return "login";
        }
    }

    @GetMapping("/inicio")
    public String mostrarInicio(Model model) {
        model.addAttribute("totalProductos", productoRepository.count());
        model.addAttribute("totalClientes", clienteRepository.count());
        return "inicio";
    }

    // ==========================================
    // MÓDULO CLIENTES (COMPLETO)
    // ==========================================
    @GetMapping("/clientes")
    public String mostrarClientes(Model model) {
        model.addAttribute("clientes", clienteRepository.findAll());
        return "clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String mostrarNuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "nuevo-cliente";
    }

    @PostMapping("/clientes/guardar")
    public String guardarCliente(@ModelAttribute("cliente") Cliente cliente) {
        clienteRepository.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editarCliente(@PathVariable("id") Long id, Model model) {
        Cliente cliente = clienteRepository.findById(id).orElse(new Cliente());
        model.addAttribute("cliente", cliente);
        return "nuevo-cliente";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminarCliente(@PathVariable("id") Long id) {
        clienteRepository.deleteById(id);
        return "redirect:/clientes";
    }

    // ==========================================
    // MÓDULO PERSONAL (COMPLETO)
    // ==========================================
    @GetMapping("/personal")
    public String mostrarPersonal(Model model) {
        model.addAttribute("empleados", empleadoRepository.findAll());

        long total = empleadoRepository.count();
        long admins = empleadoRepository.findAll().stream().filter(e -> e.getRol().equalsIgnoreCase("Administrador")).count();
        long vendedores = empleadoRepository.findAll().stream().filter(e -> e.getRol().equalsIgnoreCase("Vendedor")).count();

        model.addAttribute("totalEmpleados", total);
        model.addAttribute("totalAdmins", admins);
        model.addAttribute("totalVendedores", vendedores);

        return "personal";
    }

    @GetMapping("/personal/nuevo")
    public String mostrarNuevoEmpleado(Model model) {
        model.addAttribute("empleado", new Empleado());
        return "nuevo-empleado";
    }

    @PostMapping("/personal/guardar")
    public String guardarEmpleado(@ModelAttribute("empleado") Empleado empleado) {
        empleadoRepository.save(empleado);
        return "redirect:/personal";
    }

    @GetMapping("/personal/editar/{id}")
    public String editarEmpleado(@PathVariable("id") Long id, Model model) {
        // Cambiado de null a new Empleado() para evitar el Error 500 en Thymeleaf
        Empleado empleado = empleadoRepository.findById(id).orElse(new Empleado());
        model.addAttribute("empleado", empleado);
        return "nuevo-empleado";
    }

    @GetMapping("/personal/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable("id") Long id) {
        empleadoRepository.deleteById(id);
        return "redirect:/personal";
    }
    // ==========================================
    // MÉTODOS PARA EDITAR Y ELIMINAR INVENTARIO
    // ==========================================
    @GetMapping("/inventario/editar/{id}")
    public String editarProducto(@PathVariable("id") Integer id, Model model) {
        Producto producto = productoRepository.findById(id).orElse(new Producto());

        // 1. Enviamos el producto a editar
        model.addAttribute("producto", producto);

        // 2. ¡ESTA ES LA LÍNEA QUE FALTABA! Enviamos las categorías para llenar el Select
        model.addAttribute("categorias", categoriaRepository.findAll());

        return "nuevo-producto";
    }

    @GetMapping("/inventario/eliminar/{id}")
    public String eliminarProducto(@PathVariable("id") Integer id) {
        productoRepository.deleteById(id);
        return "redirect:/inventario";
    }
    // ==========================================
    // MÉTODOS PARA RECURSOS JAVA: APACHE POI
    // ==========================================
    @GetMapping("/inventario/exportar")
    public void exportarAExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Inventario_GasparMotors.xlsx";
        response.setHeader(headerKey, headerValue);

        // Obtenemos todas las motos y repuestos
        List<Producto> productos = (List<Producto>) productoRepository.findAll();

        // Creamos el archivo Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Inventario Actual");

        // Fila 0: Encabezados
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("ID");
        headerRow.createCell(1).setCellValue("Nombre del Producto");
        headerRow.createCell(2).setCellValue("Precio (S/)");
        headerRow.createCell(3).setCellValue("Stock");

        // Filas siguientes: Los datos de tu MySQL
        int rowCount = 1;
        for (Producto p : productos) {
            Row row = sheet.createRow(rowCount++);

            // ID y Nombre
            row.createCell(0).setCellValue(p.getId_producto());
            row.createCell(1).setCellValue(p.getNombre_producto());

            // Convertimos el BigDecimal a Double
            if (p.getPrecio() != null) {
                row.createCell(2).setCellValue(p.getPrecio().doubleValue());
            } else {
                row.createCell(2).setCellValue(0.0);
            }

            // Aseguramos el Stock
            if (p.getStock() != null) {
                row.createCell(3).setCellValue(p.getStock().doubleValue());
            } else {
                row.createCell(3).setCellValue(0);
            }
        }

        // Escribimos el archivo y cerramos
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    // ==========================================
    // BÚSQUEDA AUTOMÁTICA DE CLIENTE (AJAX/FETCH)
    // ==========================================
    @GetMapping("/api/clientes/dni/{dni}")
    @ResponseBody
    public Cliente buscarClienteDni(@PathVariable("dni") String dni) {
        // Buscamos en la base de datos local
        return clienteRepository.findByDni(dni);
    }

}
