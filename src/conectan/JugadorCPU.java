/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conectan;

/**
 *
 * @author José María Serrano
 * @version 1.1 Departamento de Informática Universidad de Jáen
 *
 * Inteligencia Artificial 2º Curso Grado en Ingeniería Informática
 *
 */
public abstract class JugadorCPU extends Jugador {

    protected int genRandom(int posicion, Tablero grid) {
        //Buscar columna en la que se puede poner
        double aleatorio = 0;
        do {
            aleatorio = Math.random() * grid.getAncho();
            posicion = (int) aleatorio;
        } while (grid.getButton(0, posicion).getIcon() != null); //posicion 0: para q busque las columnas q no esten llenas
        return posicion;
    }

} // JugadorCPU
