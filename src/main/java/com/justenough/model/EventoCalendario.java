package com.justenough.model;

import java.time.LocalDate;

import com.justenough.model.enums.TipoEvento;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;


// Clase que se guarda en MySQL. 
// Entity = tabla, cada instancia = fila
// Feriados y eventos que afectan la demanda
@Entity 
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;   // null = aplica a todos (ej: feriado nacional)

    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;

    private String descripcion;

    public EventoCalendario() {
    }
    // Constructor: 
    public EventoCalendario(Comercio comercio, LocalDate fecha, TipoEvento tipo, String descripcion) {
        this.comercio = comercio;
        this.fecha = fecha;
        this.tipo = tipo;
        this.descripcion = descripcion;
    }

    // Getters y Setters:

    public Long getId() {
        return id;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
