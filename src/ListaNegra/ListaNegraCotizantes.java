/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ListaNegra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import modelo.Cotizante;

/**
 *
 * @author MI PC
 */
public class ListaNegraCotizantes implements Iterable<Cotizante> {

    private LinkedList<Cotizante> cotizantesEmbargables;
    private static Set<Integer> listaNegra = new HashSet<>(); // Almacén de IDs de cotizantes en lista negra

    public ListaNegraCotizantes() {
        this.cotizantesEmbargables = new LinkedList<>();
    }

    public void addCotizante(Cotizante cotizante) {
        if (cotizante.isEmbargable() && !cotizantesEmbargables.contains(cotizante)) {
            cotizantesEmbargables.add(cotizante);
        }
    }

    public static boolean estaEnListaNegra(int idCotizante) {
        return listaNegra.contains(idCotizante);
    }

    // Método para agregar un cotizante a la lista negra
    public static void agregarACListaNegra(int idCotizante) {
        listaNegra.add(idCotizante);
    }

    // Método para eliminar un cotizante de la lista negra
    public static void eliminarDeListaNegra(int idCotizante) {
        listaNegra.remove(idCotizante);
    }

    // Método para obtener todos los IDs en la lista negra
    public static Set<Integer> obtenerListaNegra() {
        return listaNegra;
    }

    @Override
    public Iterator<Cotizante> iterator() {
        return new Iterator<Cotizante>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < cotizantesEmbargables.size();
            }

            @Override
            public Cotizante next() {
                return cotizantesEmbargables.get(currentIndex++);
            }
        };
    }
}
