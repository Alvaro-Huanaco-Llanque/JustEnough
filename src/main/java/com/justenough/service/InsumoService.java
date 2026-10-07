package com.justenough.service;

import com.justenough.model.Insumo;
import com.justenough.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    // Registrar o actualizar un insumo
    @Transactional
    public Insumo guardarInsumo(Insumo insumo) {
        return insumoRepository.save(insumo);
    }

    // Listar todos los insumos
    public List<Insumo> listarInsumos() {
        return insumoRepository.findAll();
    }

    // Buscar insumo por ID
    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findById(id);
    }

    // Método específico para actualizar el stock actual de un insumo
    @Transactional
    public Insumo actualizarStock(Long id, double nuevoStock) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));
        
        insumo.setStockActual(nuevoStock);
        return insumoRepository.save(insumo);
    }

    // Eliminar un insumo
    @Transactional
    public void eliminarInsumo(Long id) {
        if (!insumoRepository.existsById(id)) {
            throw new RuntimeException("Insumo no encontrado con ID: " + id);
        }
        insumoRepository.deleteById(id);
    }
}