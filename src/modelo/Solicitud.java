/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.LocalDate;

/**
 *
 * @author MI PC
 */
public class Solicitud {
    
    private static int idCounter = 1; // Generador de IDs únicos
    private int id;
    private Cotizante cotizante;
    private String estado; // Estados: "Pendiente", "Procesada", "Rechazada"
    private LocalDate fechaCreacion;
    private String comentarios;

    public Solicitud(Cotizante cotizante, String comentarios) {
        this.id = idCounter++;
        this.cotizante = cotizante;
        this.estado = "Pendiente"; // Estado inicial
        this.fechaCreacion = LocalDate.now();
        this.comentarios = comentarios;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public Cotizante getCotizante() {
        return cotizante;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "Solicitud{" +
                "id=" + id +
                ", cotizante=" + cotizante +
                ", estado='" + estado + '\'' +
                ", fechaCreacion=" + fechaCreacion +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
