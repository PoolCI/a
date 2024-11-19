/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import ListaNegra.ListaNegraCotizantes;
import enumeradores.NombresApellidos;
import java.io.BufferedWriter;
import java.util.PriorityQueue;
import lector.SuperCache;
import modelo.Cotizante;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import modelo.Solicitud;
import repository.CotizanteRepository;

/**
 *
 * @author MI PC
 */


public class ServicioTraslado {

    private PriorityQueue<Cotizante> colaCotizantes;
    public ListaNegraCotizantes listaNegra;
    private SuperCache cache;
    private CotizanteRepository cotizanteRepository;
    private Map<Integer, Solicitud> solicitudes; // Almacén de solicitudes
    private List<Solicitud> solicitudesProcesadas;
    private List<Solicitud> solicitudesRechazadas;
    private static final int TOTAL_ARCHIVOS = 10000;
    private static final int TOTAL_REGISTROS = 100;
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy_MM_dd");

    public ServicioTraslado(CotizanteRepository cotizanteRepository) {
        this.colaCotizantes = new PriorityQueue<>((c1, c2) -> c2.getPrioridad() - c1.getPrioridad());
        this.listaNegra = new ListaNegraCotizantes();
        this.cache = SuperCache.getInstance();
        this.cotizanteRepository = cotizanteRepository;
        this.solicitudes = new HashMap<>();
        solicitudesProcesadas = new ArrayList<>();
        solicitudesRechazadas = new ArrayList<>();
    }

    // Método para generar archivos CSV con los datos simulados
    public void generarArchivosCSV() {
        Random random = new Random();
        int archivosGenerados = 0;

        for (int i = 0; i < TOTAL_ARCHIVOS; i++) {
            // Generar fecha aleatoria para el nombre del archivo
            LocalDate fechaAleatoria = generarFechaAleatoria(random);
            String nombreArchivo = "SolicitudesProcesadas_" + fechaAleatoria.format(FORMATO_FECHA) + "_" + i + ".csv";

            try (FileWriter writer = new FileWriter(nombreArchivo)) {
                // Escribir encabezado del archivo
                writer.write("ID,Nombre,Apellido,FechaNacimiento,Ingreso,Estado\n");

                for (int j = 0; j < TOTAL_REGISTROS; j++) {
                    // Generar datos aleatorios para cada registro
                    String registro = generarRegistroAleatorio(j + 1, random);
                    writer.write(registro + "\n");
                }

                archivosGenerados++;
                System.out.println("Archivo creado: " + nombreArchivo);

            } catch (IOException e) {
                System.out.println("Error al escribir el archivo: " + nombreArchivo);
                e.printStackTrace();
            }
        }

        System.out.println("Total de archivos generados exitosamente: " + archivosGenerados);
    }

    private LocalDate generarFechaAleatoria(Random random) {
        int anio = 2022 + random.nextInt(3); // Años entre 2022 y 2024
        int mes = 1 + random.nextInt(12);
        int dia = 1 + random.nextInt(28);
        return LocalDate.of(anio, mes, dia);
    }

    private String generarRegistroAleatorio(int id, Random random) {
        String nombre = NombresApellidos.INSTANCE.obtenerNombreAleatorio();
        String apellido = NombresApellidos.INSTANCE.obtenerApellidoAleatorio();
        String fechaNacimiento = "19" + (50 + random.nextInt(50)) + "-" + (1 + random.nextInt(12)) + "-" + (1 + random.nextInt(28));
        double ingreso = 1000 + (random.nextDouble() * 9000);
        String[] estados = {"Aprobado", "Pendiente", "Rechazada"};
        String estado = estados[random.nextInt(estados.length)];

        return id + "," + nombre + "," + apellido + "," + fechaNacimiento + "," + String.format("%.2f", ingreso) + "," + estado;
    }

    public void procesarSolicitudTraslado(Cotizante cotizante) {
        if (validarRequisitos(cotizante)) {
            if (cotizante.isEmbargable()) {
                listaNegra.addCotizante(cotizante);
            }
            colaCotizantes.add(cotizante);

            // Guardar en el repositorio después de procesar
            cotizanteRepository.save(cotizante);
        }
    }

    private boolean validarRequisitos(Cotizante cotizante) {
        String estado = (String) cotizante.getAttribute("estado");
        Double ingreso = (Double) cotizante.getAttribute("ingreso");
        LocalDate fechaNacimiento;

        // Validar que los atributos necesarios no sean nulos
        if (estado == null || ingreso == null || cotizante.getAttribute("fechaNacimiento") == null) {
            return false;
        }

        // Validar formato de fecha de nacimiento
        try {
            String fechaNacimientoStr = (String) cotizante.getAttribute("fechaNacimiento");
            fechaNacimiento = LocalDate.parse(fechaNacimientoStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            System.out.println("Error en formato de fecha de nacimiento para el cotizante: "
                    + cotizante.getAttribute("id"));
            return false;
        }

        // Verificar requisitos específicos
        boolean esEstadoAprobado = estado.equalsIgnoreCase("Aprobado");
        boolean ingresoMinimo = ingreso >= 1500; // Requisito de ingreso mínimo
        boolean edadValida = LocalDate.now().getYear() - fechaNacimiento.getYear() >= 18; // Mayor de edad

        // El cotizante es válido si cumple todos los requisitos
        return esEstadoAprobado && ingresoMinimo && edadValida;
    }

    public void procesarColaTraslados() {
        while (!colaCotizantes.isEmpty()) {
            Cotizante cotizante = colaCotizantes.poll();
            realizarTraslado(cotizante);
        }
    }

    private void registrarLogTraslado(Cotizante cotizante) {
        String logFilePath = "traslados_log.csv";

        try (FileWriter logWriter = new FileWriter(logFilePath, true)) {
            String registro = cotizante.getAttribute("id") + ","
                    + cotizante.getAttribute("nombre") + ","
                    + cotizante.getAttribute("apellido") + ","
                    + cotizante.getAttribute("estado") + "\n";
            logWriter.write(registro);
        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de log para el cotizante: "
                    + cotizante.getAttribute("id"));
            e.printStackTrace();
        }
    }

    private void procesarEmbargo(Cotizante cotizante) {
        System.out.println("Procesando embargo para el cotizante ID: " + cotizante.getAttribute("id"));

        // Validar si el cotizante es embargable
        if (cotizante.isEmbargable()) {
            // Obtener el ingreso del cotizante para decidir si corresponde embargo
            Double ingreso = (Double) cotizante.getAttribute("ingreso");

            // Condiciones para embargo (ejemplo)
            if (ingreso < 3000) {
                // Si el ingreso es menor que 3000, consideramos que puede ser embargado
                System.out.println("El cotizante con ID: " + cotizante.getAttribute("id") + " tiene un ingreso bajo y es susceptible a embargo.");

                // Aquí puedes agregar el proceso de embargo, como notificar o registrar
                // Esto es un ejemplo, puedes personalizar la lógica según tu sistema
                cotizante.setAttribute("estado", "Embargado");
                System.out.println("Cotizante con ID: " + cotizante.getAttribute("id") + " ha sido embargado debido a ingresos bajos.");
            } else {
                // Si no cumple con las condiciones de embargo, indicar que no es necesario el embargo
                System.out.println("El cotizante con ID: " + cotizante.getAttribute("id") + " no es susceptible a embargo.");
            }
        } else {
            // Si el cotizante no es embargable
            System.out.println("El cotizante con ID: " + cotizante.getAttribute("id") + " no es embargable.");
        }
    }

    private void realizarTraslado(Cotizante cotizante) {
        // 1. Registrar el traslado en un archivo de log
        registrarLogTraslado(cotizante);
        cotizanteRepository.save(cotizante); // Guardar estado actualizado
        System.out.println("Traslado completado para el cotizante ID: " + cotizante.getAttribute("id"));

        // 2. Notificar al sistema de embargo si el cotizante es embargable
        if (cotizante.isEmbargable()) {
            procesarEmbargo(cotizante);
        }

        // 3. Actualizar la memoria caché con el estado del traslado
        SuperCache cache = SuperCache.getInstance();
        cotizante.setAttribute("estado", "Trasladado");
        cache.addToCache(cotizante.getAttribute("id").toString(), cotizante.attributes);

        System.out.println("Traslado completado para el cotizante ID: " + cotizante.getAttribute("id"));
    }

    public Solicitud registrarSolicitud(Cotizante cotizante, String comentarios) {
        Solicitud nuevaSolicitud = new Solicitud(cotizante, comentarios);
        solicitudes.put(nuevaSolicitud.getId(), nuevaSolicitud);
        System.out.println("Solicitud registrada: " + nuevaSolicitud);
        return nuevaSolicitud;
    }

    // Obtener todas las solicitudes
    public Map<Integer, Solicitud> obtenerSolicitudes() {
        return solicitudes;
    }

    public void procesarSolicitud(int idSolicitud) {
        Solicitud solicitud = solicitudes.get(idSolicitud);
        if (solicitud == null) {
            System.out.println("No se encontró la solicitud con ID: " + idSolicitud);
            return;
        }

        // Verificar el estado de la solicitud (esto depende del valor generado aleatoriamente)
        String estado = solicitud.getEstado();

        // Si el cotizante está en la lista negra, se rechaza la solicitud
        Cotizante cotizante = solicitud.getCotizante();
        if (ListaNegraCotizantes.estaEnListaNegra(solicitud.getId())) {
            solicitud.setEstado("Rechazada");
            solicitud.setComentarios("Cotizante está en la lista negra");
            solicitudesRechazadas.add(solicitud); // Se agrega a la lista de rechazadas
        } else if ("Pendiente".equals(estado)) {
            // Si el estado es "Pendiente", dejamos que se procese
            solicitud.setEstado("Procesada");
            solicitud.setComentarios("Solicitud aprobada y procesada exitosamente.");
            solicitudesProcesadas.add(solicitud); // Se agrega a la lista de procesadas
        } else if ("Rechazada".equals(estado)) {
            // Si el estado es "Rechazada", dejamos que se maneje el rechazo
            solicitud.setEstado("Rechazada");
            solicitud.setComentarios("Solicitud rechazada por estado inicial.");
            solicitudesRechazadas.add(solicitud); // Se agrega a la lista de rechazadas
        } else if ("Aprobado".equals(estado)) {
            // Si el estado es "Aprobado", podemos decidir que se procese directamente
            solicitud.setEstado("Procesada");
            solicitud.setComentarios("Solicitud aprobada y procesada.");
            solicitudesProcesadas.add(solicitud); // Se agrega a la lista de procesadas
        }
    }

    public void generarReporteTexto(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Reporte de Solicitudes Procesadas\n");
            writer.write("==================================\n\n");
            writer.write("Total de solicitudes procesadas: " + solicitudesProcesadas.size() + "\n");
            writer.write("Total de solicitudes rechazadas: " + solicitudesRechazadas.size() + "\n\n");

            writer.write("Detalle de solicitudes procesadas:\n");
            for (Solicitud solicitud : solicitudesProcesadas) {
                writer.write("ID Solicitud: " + solicitud.getId() + "\n");
                writer.write("Estado: " + solicitud.getEstado() + "\n");
                writer.write("Comentarios: " + solicitud.getComentarios() + "\n");
                writer.write("-------------------------------\n");
            }

            writer.write("\nDetalle de solicitudes rechazadas:\n");
            for (Solicitud solicitud : solicitudesRechazadas) {
                writer.write("ID Solicitud: " + solicitud.getId() + "\n");
                writer.write("Estado: " + solicitud.getEstado() + "\n");
                writer.write("Comentarios: " + solicitud.getComentarios() + "\n");
                writer.write("-------------------------------\n");
            }

            System.out.println("Reporte generado correctamente en: " + filePath);
        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }
}
