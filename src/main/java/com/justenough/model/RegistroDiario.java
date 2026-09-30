package com.justenough.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

// La base de datos NO permite dos registros del mismo producto en la misma fecha
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"producto_id", "fecha"}))
public class RegistroDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    private LocalDate fecha;              // el día al que corresponden los datos
    private double cantidadProducida;
    private double cantidadVendida;
    private Double cantidadDesperdiciada; // Double (con mayúscula) puede ser null = "no lo cargó"

    @ManyToOne
    private Usuario cargadoPor;

    private LocalDateTime fechaCarga;     // cuándo se cargó realmente (puede ser otro día)

    public RegistroDiario() {
    }

    public RegistroDiario(Producto producto, LocalDate fecha, double cantidadProducida, 
        double cantidadVendida, Double cantidadDesperdiciada, Usuario cargadoPor) {
        this.producto = producto;
        this.fecha = fecha;
        this.cantidadProducida = cantidadProducida;
        this.cantidadVendida = cantidadVendida;
        this.cantidadDesperdiciada = cantidadDesperdiciada;
        this.cargadoPor = cargadoPor;
        this.fechaCarga = LocalDateTime.now();
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

    public double getCantidadProducida() {
        return cantidadProducida;
    }

    public void setCantidadProducida(double cantidadProducida) {
        this.cantidadProducida = cantidadProducida;
    }

    public double getCantidadVendida() {
        return cantidadVendida;
    }

    public void setCantidadVendida(double cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    public Double getCantidadDesperdiciada() {
        return cantidadDesperdiciada;
    }

    public void setCantidadDesperdiciada(Double cantidadDesperdiciada) {
        this.cantidadDesperdiciada = cantidadDesperdiciada;
    }

    public Usuario getCargadoPor() {
        return cargadoPor;
    }

    public void setCargadoPor(Usuario cargadoPor) {
        this.cargadoPor = cargadoPor;
    }

    public LocalDateTime getFechaCarga() {
        return fechaCarga;
    }

    public void setFechaCarga(LocalDateTime fechaCarga) {
        this.fechaCarga = fechaCarga;
    }
}
