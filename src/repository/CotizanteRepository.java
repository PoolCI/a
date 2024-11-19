/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.List;
import modelo.Cotizante;

/**
 *
 * @author MI PC
 */
public interface CotizanteRepository {

    Cotizante findById(String id);

    List<Cotizante> findAll();

    void save(Cotizante cotizante);

    void delete(Cotizante cotizante);
}
