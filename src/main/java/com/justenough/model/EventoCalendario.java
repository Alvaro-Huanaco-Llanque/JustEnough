package com.justenough.model;

import java.time.LocalDate;

import com.justenough.model.enums.TipoEvento;
// ... resto de los imports

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
@Table(name = "eventos_calendario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Comercio comercio;   // null = aplica a todos (ej: feriado nacional)

    @Column(nullable = false)
    private LocalDate fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoEvento tipo;

    @Column(nullable = false)
    private String descripcion;
}