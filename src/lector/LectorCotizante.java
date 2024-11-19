/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import modelo.Cotizante;

/**
 *
 * @author MI PC
 */
public class LectorCotizante extends Lector<Cotizante>  {
    
     public LectorCotizante(String filePath) {
        super(filePath);
    }

    @Override
    public LinkedList<Cotizante> leerTodasLasFilas() {
        LinkedList<Cotizante> cotizantes = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String[] headers = br.readLine().split(","); // Primera línea son los headers
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Cotizante cotizante = new Cotizante();
                
                for (int i = 0; i < headers.length; i++) {
                    cotizante.setAttribute(headers[i], values[i]);
                }
                
                cotizantes.add(cotizante);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cotizantes;
    }

    @Override
    public void escribirFila(Cotizante cotizante) {
        try (FileWriter fw = new FileWriter(filePath, true)) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> entry : cotizante.attributes.entrySet()) {
                sb.append(entry.getValue()).append(",");
            }
            fw.write(sb.substring(0, sb.length() - 1) + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
