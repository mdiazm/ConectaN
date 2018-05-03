/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conectan;

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author José María Serrano
 * @version 1.1 Departamento de Informática Universidad de Jáen
 *
 * Inteligencia Artificial 2º Curso Grado en Ingeniería Informática
 *
 */
public class Tablero {

    // Representación del tablero
    // array de botones para la GUI
    private JButton boton[][];
    // array de enteros
    private int boton_int[][];

    //imagenes
    private ImageIcon foto1;
    private ImageIcon foto2;

    // Número de columnas
    private int ancho;
    // Número de filas
    private int alto;

    // Constructor
    public Tablero(int alto, int ancho, String pic1, String pic2) {

        //Cargar imagenes
        foto1 = new ImageIcon(pic1);
        foto2 = new ImageIcon(pic2);

        this.ancho = ancho;
        this.alto = alto;

        boton = new JButton[alto][ancho];
        boton_int = new int[alto][ancho];
    } // Tablero

    // Devuelve la casilla en la posición <x,y>
    public JButton getButton(int x, int y) {
        return boton[x][y];
    }

    // Coloca una ficha en la casilla <x,y>
    public void setButton(int x, int y, boolean jugador1) {
        if (jugador1) {
            boton[x][y].setIcon(foto1); // Jugador 1
            boton_int[x][y] = 1;
        } else {
            boton[x][y].setIcon(foto2); // Jugador 2
            boton_int[x][y] = -1;
        }
    }

    // Ficha del jugador 1
    public ImageIcon getFicha1() {
        return foto1;
    }

    // Ficha del jugador 2
    public ImageIcon getFicha2() {
        return foto2;
    }

    // Devuelve el número de columnas del tablero de juego
    public int getAncho() {
        return ancho;
    }

    // Devuelve el número de filas del tablero de juego
    public int getAlto() {
        return alto;
    }

    // Inicialización de un tablero vacío
    public void initialize(int i, int j, java.awt.event.ActionListener principal, Color col) {
        boton[i][j] = new JButton();
        boton[i][j].addActionListener(principal);
        boton[i][j].setBackground(col);
        boton_int[i][j] = 0;
    }

    // Reiniciar el tablero a vacío.
    public void reset() {
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                boton[i][j].setIcon(null);
                boton_int[i][j] = 0;
            }
        }
    }

    // Devuelve el array de enteros con el tablero actual
    public int[][] toArray() {
        return boton_int;
    }

    // Comprobar si el tablero se halla en un estado de fin de partida
    public int checkWin(int x, int y, int conecta) {
        /*
		 *	x fila
		 *	y columna
         */

        //Comprobar vertical
        int ganar1 = 0;
        int ganar2 = 0;
        int ganador = 0;
        boolean salir = false;
        for (int j = 0; (j < alto) && !salir; j++) {
            if (boton_int[j][y] != 0) {
                if (boton_int[j][y] == 1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (!salir) {
                    if (boton_int[j][y] == -1) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = -1;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
        }
        // Comprobar horizontal
        ganar1 = 0;
        ganar2 = 0;
        for (int i = 0; (i < ancho) && !salir; i++) {
            if (boton_int[x][i] != 0) {
                if (boton_int[x][i] == 1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (boton_int[x][i] == -1) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = -1;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
        }
        // Comprobar oblicuo. De izquierda a derecha
        ganar1 = 0;
        ganar2 = 0;
        int a = x;
        int b = y;
        while (b > 0 && a > 0) {
            a--;
            b--;
        }
        while (b < ancho && a < alto && !salir) {
            if (boton_int[a][b] != 0) {
                if (boton_int[a][b] == 1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (boton_int[a][b] == -1) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = -1;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
            a++;
            b++;
        }
        // Comprobar oblicuo de derecha a izquierda 
        ganar1 = 0;
        ganar2 = 0;
        a = x;
        b = y;
        //buscar posición de la esquina
        while (b < ancho - 1 && a > 0) {
            a--;
            b++;
        }
        while (b > -1 && a < alto && !salir) {
            if (boton_int[a][b] != 0) {
                if (boton_int[a][b] == 1) {
                    ganar1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (boton_int[a][b] == -1) {
                        ganar2++;
                    } else {
                        ganar2 = 0;
                    }
                    // Gana el jugador 2
                    if (ganar2 == conecta) {
                        ganador = -1;
                        salir = true;
                    }
                }
            } else {
                ganar1 = 0;
                ganar2 = 0;
            }
            a++;
            b--;
        }

        return ganador;
    } // checkWin

    // Método para mostrar el estado actual del tablero por la salida estándar
    public void print() {
        System.out.println();
        System.out.println("Conecta-N:");
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                System.out.print(boton_int[i][j] + " ");
            }
            System.out.println();
        }
    }
} // Tablero
