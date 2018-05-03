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
public class JugadorHumano extends Jugador {

    // En realidad, este método no lo usaremos nunca
    // porque el jugador humano usa la GUI para marcar su jugada
    @Override
    public int jugada(int x, int y, int conecta, Tablero grid) {
        return 0;
    }

} // JugadorHumano

