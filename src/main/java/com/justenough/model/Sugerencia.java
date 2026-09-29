package com.justenough.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.justenough.model.enums.EstadoSugerencia;
import com.justenough.model.enums.NivelConfianza;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// Lo que devuelve el motor de predicción: "el <fecha> producí <cantidadSugerida> de <producto>"
@Entity
public class Sugerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    private LocalDate fecha;             // el día para el que se sugiere
    private double cantidadSugerida;     // lo que dijo el sistema (no cambia nunca)

    @Enumerated(EnumType.STRING)
    private NivelConfianza nivelConfianza;

    private double cantidadFinal;        // lo que se decidió producir (igual a la sugerida, salvo ajuste)

    @Enumerated(EnumType.STRING)
    private EstadoSugerencia estado;

    private LocalDateTime fechaGeneracion;

    public Sugerencia() {
    }

    public Sugerencia(Producto producto, LocalDate fecha, double cantidadSugerida, NivelConfianza nivelConfianza) {
        this.producto = producto;
        this.fecha = fecha;
        this.cantidadSugerida = cantidadSugerida;
        this.nivelConfianza = nivelConfianza;
        this.cantidadFinal = cantidadSugerida;
        this.estado = EstadoSugerencia.PENDIENTE;
        this.fechaGeneracion = LocalDateTime.now();
    }

    // ===== Getters y Setters =====

    public Long getId() {
        return id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public double getCantidadSugerida() {
        return cantidadSugerida;
    }

    public void setCantidadSugerida(double cantidadSugerida) {
        this.cantidadSugerida = cantidadSugerida;
    }

    public NivelConfianza getNivelConfianza() {
        return nivelConfianza;
    }

    public void setNivelConfianza(NivelConfianza nivelConfianza) {
        this.nivelConfianza = nivelConfianza;
    }

    public double getCantidadFinal() {
        return cantidadFinal;
    }

    public void setCantidadFinal(double cantidadFinal) {
        this.cantidadFinal = cantidadFinal;
    }

    public EstadoSugerencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoSugerencia estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
