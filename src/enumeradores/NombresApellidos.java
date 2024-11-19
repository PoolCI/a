/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package enumeradores;

import java.util.Random;

/**
 *
 * @author MI PC
 */

//enumerador que permite generar nombres aleatorios a la hora de crear los archivos csv
public enum NombresApellidos {
    INSTANCE; // Singleton para poder usar una única instancia

    private final String[] nombres = {
        "Carlos", "Maria", "Juan", "Ana", "Luis", "Lucia", "Jorge", "Elena", "Roberto", "Isabel",
        "Daniel", "Sofia", "Miguel", "Laura", "Pedro", "Carmen", "Raul", "Julia", "Jose", "Marta",
        "Alberto", "Silvia", "Manuel", "Teresa", "Ricardo", "Cristina", "Antonio", "Beatriz", "Victor", "Rosa",
        "Francisco", "Paula", "David", "Patricia", "Adrian", "Sandra", "Andres", "Ines", "Fernando", "Sara",
        "Alvaro", "Clara", "Hugo", "Nuria", "Enrique", "Alicia", "Angel", "Gloria", "Rafael", "Monica",
        "Gabriel", "Irene", "Oscar", "Eva", "Pablo", "Carla", "Eduardo", "Raquel", "Javier", "Elisa",
        "Ramiro", "Susana", "Tomas", "Lorena", "Vicente", "Angela", "Ramon", "Lola", "Mario", "Esther",
        "Felipe", "Aitana", "Jaime", "Marina", "Ruben", "Celia", "Nicolas", "Emma", "Sergio", "Iris",
        "Bruno", "Olga", "Marcos", "Blanca", "Ivan", "Daniela", "Alex", "Claudia", "Joel", "Paloma"
    };

    private final String[] apellidos = {
        "Garcia", "Martinez", "Rodriguez", "Lopez", "Hernandez", "Gonzalez", "Perez", "Sanchez", "Ramirez", "Torres",
        "Diaz", "Alvarez", "Moreno", "Munoz", "Castro", "Suarez", "Ortega", "Vega", "Molina", "Jimenez",
        "Navarro", "Ruiz", "Serrano", "Cruz", "Reyes", "Gutierrez", "Ramos", "Prieto", "Aguilar", "Marquez",
        "Castillo", "Peña", "Ortiz", "Soto", "Medina", "Rivas", "Vargas", "Herrera", "Morales", "Flores",
        "Rojas", "Acosta", "Castellanos", "Barrera", "Villanueva", "Blanco", "Escobar", "Pardo", "Montes", "Nunez",
        "Iglesias", "Carrillo", "Campos", "Maldonado", "Bautista", "Lozano", "Padilla", "Cortes", "Ferrer", "Robles",
        "Rivera", "Santos", "Lara", "Fuentes", "Vera", "Bravo", "Lorenzo", "Carrasco", "Moya", "Vidal",
        "Leal", "Aranda", "Miranda", "Gimenez", "Paredes", "Salas", "Mendez", "Parra", "Alonso", "Cabrera",
        "Cuesta", "Fernandez", "Gil", "Figueroa", "Delgado", "Velasco", "Ponce", "Bernal", "Benitez", "Esquivel"
    };

    public String obtenerNombreAleatorio() {
        return nombres[new Random().nextInt(nombres.length)];
    }

    public String obtenerApellidoAleatorio() {
        return apellidos[new Random().nextInt(apellidos.length)];
    }
}
