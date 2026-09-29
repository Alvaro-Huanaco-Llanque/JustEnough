package com.justenough.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

// Una fila = "1 unidad de <producto> lleva <cantidadPorUnidad> de <insumo>"
// Ej: 1 medialuna lleva 0.04 kg de harina
@Entity
public class RecetaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Producto producto;

    @ManyToOne
    private Insumo insumo;

    private double cantidadPorUnidad;

    public RecetaItem() {
    }

    public RecetaItem(Producto producto, Insumo insumo, double cantidadPorUnidad) {
        this.producto = producto;
        this.insumo = insumo;
        this.cantidadPorUnidad = cantidadPorUnidad;
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

    public Insumo getInsumo() {
        return insumo;
    }

    public void setInsumo(Insumo insumo) {
        this.insumo = insumo;
    }

    public double getCantidadPorUnidad() {
        return cantidadPorUnidad;
    }

    public void setCantidadPorUnidad(double cantidadPorUnidad) {
        this.cantidadPorUnidad = cantidadPorUnidad;
    }
}
