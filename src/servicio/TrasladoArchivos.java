/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

/**
 *
 * @author MI PC
 */
public class TrasladoArchivos {
    
    public static void trasladarArchivos(String origen, String destino) throws IOException {
        Path dirOrigen = Paths.get(origen);
        Path dirDestino = Paths.get(destino);

        try (Stream<Path> archivos = Files.list(dirOrigen)) {
            archivos.forEach(archivo -> {
                try {
                    if (Files.isRegularFile(archivo)) {
                        Path destinoArchivo = dirDestino.resolve(archivo.getFileName());
                        Files.move(archivo, destinoArchivo, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Archivo trasladado: " + archivo.getFileName());
                    }
                } catch (IOException e) {
                    System.err.println("Error al trasladar archivo: " + archivo.getFileName() + " - " + e.getMessage());
                }
            });
        }
    }
}
