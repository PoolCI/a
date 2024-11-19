/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 *
 * @author MI PC
 */

//Clase abstracta que define un lector genérico para la gestión de archivos.
//Proporciona funcionalidades básicas para leer y escribir elementos de tipo genérico T.
public abstract class Lector<T> {

    protected String filePath;
    protected Map<String, String> attributes;

    public Lector(String filePath) {
        this.filePath = filePath;
        this.attributes = new HashMap<>();
    }

    public abstract LinkedList<T> leerTodasLasFilas();

    public abstract void escribirFila(T elemento);
}
