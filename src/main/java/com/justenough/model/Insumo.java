package com.justenough.model;

import java.math.BigDecimal;

import com.justenough.model.enums.UnidadMedida;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private UnidadMedida unidadMedida;

    private double stockActual;
    private BigDecimal costoUnitario;   // BigDecimal para plata: double redondea mal (0.1 + 0.2 != 0.3)
    private int vidaUtilDias;

    public Insumo() {
    }

    public Insumo(Comercio comercio, String nombre, UnidadMedida unidadMedida, BigDecimal costoUnitario, int vidaUtilDias) {
        this.comercio = comercio;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.costoUnitario = costoUnitario;
        this.vidaUtilDias = vidaUtilDias;
        this.stockActual = 0;
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

    public double getStockActual() {
        return stockActual;
    }

    public void setStockActual(double stockActual) {
        this.stockActual = stockActual;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public int getVidaUtilDias() {
        return vidaUtilDias;
    }

    public void setVidaUtilDias(int vidaUtilDias) {
        this.vidaUtilDias = vidaUtilDias;
    }
}
