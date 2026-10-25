package com.justenough.service;

import com.justenough.exception.NegocioException;
import com.justenough.model.Comercio;
import com.justenough.model.Insumo;
import com.justenough.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

// Todas las operaciones trabajan solo sobre los insumos del comercio del usuario logueado
@Service
public class InsumoService {

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private SesionService sesionService;

    @Transactional
    public Insumo guardarInsumo(Insumo insumo) {
        Comercio comercio = sesionService.getComercioActual();
        String nombreBase = insumo.getNombre().trim();
        String nombreFinal = nombreBase;
        int contador = 2;

        // Si existe, probamos con 2, 3, 4... hasta encontrar un nombre libre
        while (insumoRepository.findByNombreAndComercioId(nombreFinal, comercio.getId()).isPresent()) {
            nombreFinal = nombreBase + " " + contador;
            contador++;
        }

        insumo.setId(null);
        insumo.setComercio(comercio);
        insumo.setNombre(nombreFinal);
        return insumoRepository.save(insumo);
    }

    public List<Insumo> listarInsumos() {
        return insumoRepository.findByComercioId(sesionService.getComercioActual().getId());
    }

    public Optional<Insumo> buscarPorId(Long id) {
        return insumoRepository.findByIdAndComercioId(id, sesionService.getComercioActual().getId());
    }

    @Transactional
    public Insumo actualizarStock(Long id, double nuevoStock) {
        Insumo insumo = buscarPorId(id)
                .orElseThrow(() -> NegocioException.noEncontrado("Insumo no encontrado con ID: " + id));

        insumo.setStockActual(nuevoStock);
        return insumoRepository.save(insumo);
    }

    @Transactional
    public void eliminarInsumo(Long id) {
        Insumo insumo = buscarPorId(id)
                .orElseThrow(() -> NegocioException.noEncontrado("Insumo no encontrado con ID: " + id));
        insumoRepository.delete(insumo);
    }
}
