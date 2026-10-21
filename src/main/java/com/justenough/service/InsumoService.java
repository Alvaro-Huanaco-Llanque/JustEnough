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

    @Transactional
    public Insumo guardarInsumo(Insumo insumo) {
        // Lógica de renombramiento automático para insumos duplicados
        String nombreBase = insumo.getNombre().trim();
        String nombreFinal = nombreBase;
        int contador = 2;

        while (insumoRepository.findByNombre(nombreFinal).isPresent()) {
            nombreFinal = nombreBase + " " + contador;
            contador++;
        }
        
        insumo.setNombre(nombreFinal);
        return insumoRepository.save(insumo);
    }

    public List<Insumo> listarInsumos() {
        return insumoRepository.findAll();
    }

    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findById(id);
    }

    @Transactional
    public Insumo actualizarStock(Long id, double nuevoStock) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Insumo no encontrado con ID: " + id));
        
        insumo.setStockActual(nuevoStock);
        return insumoRepository.save(insumo);
    }

    @Transactional
    public void eliminarInsumo(Long id) {
        if (!insumoRepository.existsById(id)) {
            throw new RuntimeException("Insumo no encontrado con ID: " + id);
        }
        insumoRepository.deleteById(id);
    }
}