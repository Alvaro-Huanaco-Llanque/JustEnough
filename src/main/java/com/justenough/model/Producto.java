package com.justenough.model;

import com.justenough.model.enums.UnidadMedida;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne   // muchos productos pertenecen a UN comercio
    private Comercio comercio;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    private int vidaUtilDias;
    private boolean priorizado;    // true = producto de alta rotación, el sistema lo trabaja
    private double stockMinimo;    // lo calcula el sistema según el historial
    private boolean activo;

    public Producto() {
    }

    public Producto(Comercio comercio, String nombre, UnidadMedida unidadMedida, int vidaUtilDias) {
        this.comercio = comercio;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.vidaUtilDias = vidaUtilDias;
        this.priorizado = false;
        this.stockMinimo = 0;
        this.activo = true;
    }

    // ===== Getters y Setters =====

    public Long getId() {
        return id;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public int getVidaUtilDias() {
        return vidaUtilDias;
    }

    public void setVidaUtilDias(int vidaUtilDias) {
        this.vidaUtilDias = vidaUtilDias;
    }

    public boolean isPriorizado() {
        return priorizado;
    }

    public void setPriorizado(boolean priorizado) {
        this.priorizado = priorizado;
    }

    public double getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(double stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
