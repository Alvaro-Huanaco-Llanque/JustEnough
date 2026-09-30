package com.justenough.model;

import java.time.LocalDateTime;

import com.justenough.model.enums.TipoAlerta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;

    @ManyToOne
    private Producto producto;   // puede ser null (ej: alerta de inactividad es de todo el comercio)

    @Enumerated(EnumType.STRING)
    private TipoAlerta tipo;

    private String mensaje;
    private LocalDateTime fechaCreacion;
    private boolean leida;

    public Alerta() {
    }

    public Alerta(Comercio comercio, Producto producto, TipoAlerta tipo, String mensaje) {
        this.comercio = comercio;
        this.producto = producto;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false;
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

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlerta tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isLeida() {
        return leida;
    }

    public void setLeida(boolean leida) {
        this.leida = leida;
    }
}
