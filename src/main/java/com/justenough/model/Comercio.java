package com.justenough.model;

import java.time.LocalDate;

import com.justenough.model.enums.Rubro;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

///EL @ Indica que es una anotación de java (@Entity, @Id, etc), Es lo que le dice a Spring "esta clase se guarda en MySQL". 
// Sin eso, la alternativa es escribir a mano todo el SQL (INSERT, SELECT, abrir y cerrar conexiones) para cada clase.


@Entity   // "esta clase es una tabla en MySQL"
public class Comercio {

    @Id                                                   // clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // MySQL genera el id solo (1, 2, 3...)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)   // guarda "PANADERIA" como texto, no como número
    private Rubro rubro;

    private String localidad;
    private String provincia;
    private LocalDate fechaAlta;
    private boolean activo;

    // Constructor vacío: JPA lo necesita para leer los datos de la base
    public Comercio() {
    }

    // Constructor normal para crear un comercio nuevo
    public Comercio(String nombre, Rubro rubro, String localidad, String provincia) {
        this.nombre = nombre;
        this.rubro = rubro;
        this.localidad = localidad;
        this.provincia = provincia;
        this.fechaAlta = LocalDate.now();
        this.activo = true;
    }

    // ===== Getters y Setters =====

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public void setRubro(Rubro rubro) {
        this.rubro = rubro;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
