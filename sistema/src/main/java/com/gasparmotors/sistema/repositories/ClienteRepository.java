package com.gasparmotors.sistema.repositories;

import com.gasparmotors.sistema.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Método mágico para buscar exactamente por DNI
    Cliente findByDni(String dni);
}