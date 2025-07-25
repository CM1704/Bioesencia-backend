package com.bioesencia.backend.dto;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

import com.bioesencia.backend.model.Cita;
import com.bioesencia.backend.model.EstadoCita;
import com.bioesencia.backend.model.Usuario;
import com.bioesencia.backend.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


public class CitaDTO {
    
    private int duracion;
    private EstadoCita estado;
    private LocalDateTime fechaHora;
    private String notas;
    private String servicio;
    private Long usuarioId;
    
    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

}
