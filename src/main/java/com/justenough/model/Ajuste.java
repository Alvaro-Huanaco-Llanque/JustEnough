package com.justenough.model;

import java.time.LocalDateTime;

import com.justenough.model.enums.MotivoAjuste;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

// Clase que se guarda en MySQL. 
// Entity = tabla, cada instancia = fila
// Cuando el encargado corrige una sugerencia (queda como historial del ajuste).
@Entity
public class Ajuste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne   // cada sugerencia tiene como máximo UN ajuste
    private Sugerencia sugerencia;

    private double cantidadOriginal;
    private double cantidadAjustada;

    @Enumerated(EnumType.STRING)
    private MotivoAjuste motivo;

    private String comentario;

    @ManyToOne
    private Usuario usuario;

    private LocalDateTime fecha;

    public Ajuste() {
    }

    public Ajuste(Sugerencia sugerencia, double cantidadAjustada, MotivoAjuste motivo, String comentario, Usuario usuario) {
        this.sugerencia = sugerencia;
        this.cantidadOriginal = sugerencia.getCantidadSugerida();
        this.cantidadAjustada = cantidadAjustada;
        this.motivo = motivo;
        this.comentario = comentario;
        this.usuario = usuario;
        this.fecha = LocalDateTime.now();
    }

    // ===== Getters y Setters =====

    public Long getId() {
        return id;
    }

    public Sugerencia getSugerencia() {
        return sugerencia;
    }

    public void setSugerencia(Sugerencia sugerencia) {
        this.sugerencia = sugerencia;
    }

    public double getCantidadOriginal() {
        return cantidadOriginal;
    }

    public void setCantidadOriginal(double cantidadOriginal) {
        this.cantidadOriginal = cantidadOriginal;
    }

    public double getCantidadAjustada() {
        return cantidadAjustada;
    }

    public void setCantidadAjustada(double cantidadAjustada) {
        this.cantidadAjustada = cantidadAjustada;
    }

    public MotivoAjuste getMotivo() {
        return motivo;
    }

    public void setMotivo(MotivoAjuste motivo) {
        this.motivo = motivo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
