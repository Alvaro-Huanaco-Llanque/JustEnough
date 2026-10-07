package com.justenough.service;

import com.justenough.model.Comercio;
import com.justenough.repository.ComercioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ComercioService {

    @Autowired
    private ComercioRepository comercioRepository;

    // Crear un nuevo comercio
    @Transactional
    public Comercio crearComercio(Comercio comercio) {
        return comercioRepository.save(comercio);
    }

    // Listar todos los comercios
    public List<Comercio> listarComercios() {
        return comercioRepository.findAll();
    }

    // Buscar comercio por ID
    public Optional<Comercio> buscarPorId(Long id) {
        return comercioRepository.findById(id);
    }

    // Actualizar datos del comercio
    @Transactional
    public Comercio actualizarComercio(Long id, Comercio detalles) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con ID: " + id));
        
        comercio.setNombre(detalles.getNombre());
        comercio.setRubro(detalles.getRubro());
        comercio.setLocalidad(detalles.getLocalidad());
        comercio.setProvincia(detalles.getProvincia());
        
        return comercioRepository.save(comercio);
    }

    // Baja lógica del comercio
    @Transactional
    public void desactivarComercio(Long id) {
        Comercio comercio = comercioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comercio no encontrado con ID: " + id));
        
        comercio.setActivo(false);
        comercioRepository.save(comercio);
    }
}
