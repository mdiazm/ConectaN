/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conectan;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.ArrayList;

/**
 *
 * @author José María Serrano
 * @version 1.1 Departamento de Informática Universidad de Jáen
 *
 * Inteligencia Artificial 2º Curso Grado en Ingeniería Informática
 *
 */
public class JugadorCPURandom extends JugadorCPU {

    // Método para determinar, de forma muy básica, donde colocar la siguiente ficha
    // No es especialmente brillante
    public int jugada(int x, int y, int conecta, Tablero grid) {
        boolean cubrir_izquierda = false;
        int ganarVert = 0;
        int ganarHorz;
        int posicion; //Generar Aleatorio por defecto
        posicion = genRandom(x, grid);

        //Ataque en horizontal
        ganarHorz = 0;
        for (int i = 0; i < grid.getAlto(); i++) //buscamos en todo el tablero
        {
            for (int j = 0; j < grid.getAncho(); j++) {
                if (grid.getButton(i, j).getIcon() != null) {
                    if (grid.getButton(i, j).getIcon().equals(grid.getFicha2())) {
                        ganarHorz++;
                    } else {
                        ganarHorz = 0;
                    }
                    if (ganarHorz == conecta - 1) {
                        posicion = j;
                        if (posicion != grid.getAncho() - 1) {
                            if (grid.getButton(x, j + 1).getIcon() == null) {
                                posicion++;
                            } else if (j >= conecta - 1 && grid.getButton(x, j - (conecta - 1)).getIcon() == null) {
                                posicion = posicion - (conecta - 1);
                            }
                        }
                    }
                }
            }
            ganarHorz = 0;
        }
        // Defenderse en Horizontal hacia la izquierda
        ganarHorz = 0;
        for (int j = grid.getAncho() - 1; j >= 0; j--) {
            if (grid.getButton(x, j).getIcon() != null) {
                if (grid.getButton(x, j).getIcon().equals(grid.getFicha1())) {
                    ganarHorz++;
                } else {
                    ganarHorz = 0;
                }
                if (ganarHorz == conecta - 1 && j != 0) {
                    posicion = j;
                    if (grid.getButton(x, j - 1).getIcon() == null) {
                        posicion--;
                        cubrir_izquierda = true;
                    }
                }
            }
        }
        // Defenderse en Horizontal hacia la derecha
        ganarHorz = 0;
        if (!cubrir_izquierda) {
            for (int j = 0; j < grid.getAncho(); j++) {
                if (grid.getButton(x, j).getIcon() != null) {
                    if (grid.getButton(x, j).getIcon().equals(grid.getFicha1())) {
                        ganarHorz++;
                    } else {
                        ganarHorz = 0;
                    }
                    if (ganarHorz == conecta - 1) {
                        posicion = j;
                        if (posicion != grid.getAncho() - 1) {
                            if (grid.getButton(x, j + 1).getIcon() == null) {
                                posicion++;
                            }
                        }
                    }
                }
            }
        }
        // Defenderse en Vertical
        for (int i = 0; i < grid.getAlto(); i++) {
            if (grid.getButton(i, y).getIcon() != null) {
                if (grid.getButton(i, y).getIcon().equals(grid.getFicha1())) {
                    ganarVert++;
                } else {
                    ganarVert = 0;
                }
                if (ganarVert == conecta - 1) {
                    posicion = y; //obtiene la columna en la que se puede ganar;
                }
            }
        }
        // Ataque en Vertical
        ganarVert = 0;
        for (int i = 0; i < grid.getAncho(); i++) //buscamos en todo el tablero
        {
            for (int j = 0; j < grid.getAlto(); j++) {
                if (grid.getButton(j, i).getIcon() != null) {
                    if (grid.getButton(j, i).getIcon().equals(grid.getFicha2())) {
                        ganarVert++;
                    } else {
                        ganarVert = 0;
                    }
                    if (ganarVert == conecta - 1 && j != 0) //si en alguna columna hay n-1 fichas seguidas de la mAquina
                    {
                        posicion = i; //obtiene la columna en la que se puede ganar;
                    }
                }
            }
            ganarVert = 0;
        }

        if (grid.getButton(0, posicion).getIcon() != null) //si no se pude poner ficha en la columna (columna llena)
        {
            posicion = genRandom(posicion, grid); //Genera posición aleatoria
        }
        int k = grid.getAlto();
        //Ir a la última posición de la columna	
        do {
            k--;
        } while (grid.getButton(k, posicion).getIcon() != null & k != 0);
        //Pintar Ficha
        grid.setButton(k, posicion, false);
        cubrir_izquierda = false;
        return grid.checkWin(k, posicion, conecta);
    }

    
// jugada          
} // JugadorCPURandom
