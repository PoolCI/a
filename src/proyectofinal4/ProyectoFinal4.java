/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package proyectofinal4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import lector.LectorCotizante;
import lector.SuperCache;
import modelo.Cotizante;
import repository.CotizanteRepository;
import repository.InMemoryCotizanteRepository;
import servicio.ServicioTraslado;
import servicio.TrasladoArchivos;
import static servicio.TrasladoArchivos.trasladarArchivos;

/**
 *
 * @author MI PC
 */
public class ProyectoFinal4 {

    public static void main(String[] args) {
        // Ruta al archivo CSV de cotizantes

        iniciarScheduler();
        String carpetaOrigen = "CaracterizacionesEntrantes"; //hola diego zzzz
        String carpetaDestino = "CaracterizacionesEnProcesamiento";

        try {
            // Crear las carpetas si no existen
            Files.createDirectories(Paths.get(carpetaOrigen));
            Files.createDirectories(Paths.get(carpetaDestino));

            // Llamar al método de traslado
            trasladarArchivos(carpetaOrigen, carpetaDestino);
            System.out.println("Archivos trasladados con éxito.");
        } catch (IOException e) {
            System.err.println("Error al crear directorios o trasladar archivos: " + e.getMessage());
            return; // Detener ejecución si hay problemas con el traslado
        }

        String filePath = "C:\\Users\\MI PC\\Downloads\\cotizantes.csv";

        // Leer los cotizantes desde el archivo CSV
        LectorCotizante csvReader = new LectorCotizante(filePath);
        LinkedList<Cotizante> cotizantes = csvReader.leerTodasLasFilas();

        // Crear el repositorio y el servicio de traslado
        CotizanteRepository cotizanteRepository = new InMemoryCotizanteRepository();
        ServicioTraslado servicioTraslado = new ServicioTraslado(cotizanteRepository);

        String reportePath = "reporte_solicitudes.txt";
        servicioTraslado.generarReporteTexto(reportePath);

        // Crear solicitudes y procesarlas
        int idSolicitud = 1; // ID inicial de solicitudes
        for (Cotizante cotizante : cotizantes) {
            // Ajustar atributos del cotizante
            cotizante.setEmbargable((Boolean) cotizante.getAttribute("embargable"));
            cotizante.setPrioridad(Integer.parseInt(cotizante.getAttribute("prioridad").toString()));

            // Registrar la solicitud para este cotizante
            servicioTraslado.registrarSolicitud(cotizante, "Traslado de fondo");

            // Agregar a la cache
            SuperCache cache = SuperCache.getInstance();
            cache.addToCache(cotizante.getAttribute("id").toString(), cotizante.attributes);

            // Procesar la solicitud registrada
            servicioTraslado.procesarSolicitud(idSolicitud); // Procesa según el ID
            idSolicitud++;
        }

        // Procesar la cola de traslados
        servicioTraslado.procesarColaTraslados();

        // Mostrar cotizantes embargables en la lista negra
        System.out.println("Cotizantes embargables en lista negra:");
        for (Cotizante cotizante : servicioTraslado.listaNegra) {
            System.out.println("ID: " + cotizante.getAttribute("id") + ", Nombre: " + cotizante.getAttribute("nombre"));
        }

        // Generar los archivos CSV
        servicioTraslado.generarArchivosCSV();
    }


    public static void iniciarScheduler() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        Runnable tarea = () -> {
            try {
                TrasladoArchivos.trasladarArchivos("CaracterizacionesEntrantes", "CaracterizacionesEnProcesamiento");
            } catch (Exception e) {
                System.err.println("Error en la tarea programada: " + e.getMessage());
            }
        };

        long initialDelay = calcularDelayInicial();
        long period = 24 * 60 * 60; // 24 horas en segundos

        scheduler.scheduleAtFixedRate(tarea, initialDelay, period, TimeUnit.SECONDS);
    }
    
    public static long calcularDelayInicial() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime siguienteEjecucion = ahora.toLocalDate().atTime(0, 30);
        if (ahora.isAfter(siguienteEjecucion)) {
            siguienteEjecucion = siguienteEjecucion.plusDays(1);
        }
        return Duration.between(ahora, siguienteEjecucion).getSeconds();
    }
}
