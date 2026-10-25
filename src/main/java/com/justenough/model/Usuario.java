package com.justenough.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.justenough.model.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)   // no puede haber dos usuarios con el mismo email
    private String email;

    @JsonIgnore   // nunca se envía al frontend, aunque el usuario venga anidado en otra entidad
    @Column(nullable = false)
    private String passwordHash;   // NUNCA la contraseña real: se guarda encriptada

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @ManyToOne   // muchos usuarios pertenecen al UN comercio
    private Comercio comercio;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    // El default 1 en la base hace que las cuentas creadas antes de existir la verificación queden verificadas.
    // Las cuentas nuevas se crean con false desde UsuarioService.
    @Builder.Default
    @Column(nullable = false, columnDefinition = "bit(1) not null default 1")
    private boolean emailVerificado = true;

    @JsonIgnore
    private String tokenVerificacion;

    @JsonIgnore
    private LocalDateTime tokenVerificacionExpira;
}
