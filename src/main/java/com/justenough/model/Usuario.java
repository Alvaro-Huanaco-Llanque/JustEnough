package com.justenough.model;

import com.justenough.model.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true, nullable = false)   // no puede haber dos usuarios con el mismo email
    private String email;

    private String passwordHash;   // NUNCA la contraseña real: se guarda encriptada

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @ManyToOne   // muchos usuarios pertenecen a UN comercio
    private Comercio comercio;

    private boolean activo;

    public Usuario() {
    }

    public Usuario(String nombre, String email, String passwordHash, Rol rol, Comercio comercio) {
        this.nombre = nombre;
        this.email = email;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.comercio = comercio;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Comercio getComercio() {
        return comercio;
    }

    public void setComercio(Comercio comercio) {
        this.comercio = comercio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
