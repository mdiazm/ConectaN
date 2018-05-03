/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conectan;

import static java.lang.Integer.max;

import java.util.ArrayList;

/**
 *
 * @author Miguel Díaz
 * @brief Práctica 3 Inteligencia Artificial: Algoritmo de poda Alfa-Beta
 *
 * Inteligencia Artificial 2º Curso Grado en Ingeniería Informática
 *
 */
public class JugadorCPUAlfaBeta extends JugadorCPU {

    /**
     *
     * @param x valor x donde se colocará la ficha del jugador.
     * @param y valor y donde se colocará la ficha del jugador.
     * @param conecta número de fichas que que hay que colocar para obtener una
     * victoria.
     * @param grid Tablero sobre el que se desarrolla el juego.
     * @return valor entero informando sobre qué ha sucedido tras un movimiento.
     * 1: gana jugador 1. -1: gana jugador 2: 0 empate.
     *
     */
    @Override
    public int jugada(int x, int y, int conecta, Tablero grid) {

        int posicion;

        int[][] array = grid.toArray();

        int[][] copia = copiarMatriz(array);

        Par<Integer, Integer> retorno = new Par(0, 0);

        int aux = podaAlfaBeta(grid, grid.getAlto(), Integer.MIN_VALUE, Integer.MAX_VALUE, 2, conecta, 0, 0, copia, retorno);

        posicion = retorno.x;

        // ...
        // Almacenar en posicion la mejor columna posible donde hacer nuestra jugada
        int k = grid.getAlto();
        //Ir a la última posición de la columna	
        do {
            k--;
        } while (grid.getButton(k, posicion).getIcon() != null & k != 0);

        System.out.println("Poda Alfa-Beta: " + aux);

        //Pintar Ficha
        grid.setButton(k, posicion, false);
        return grid.checkWin(k, posicion, conecta);
    }

    /**
     *
     * Algoritmo de la poda alfa-beta.
     *
     * @param tablero tablero sobre el que se desarrolla el juego. Es útil para
     * la función, para conocer el tamaño del tablero, pues será variable.
     * @param profundidad Profundidad que alcanza el árbol de estados del juego.
     * @param alfa valor alfa del algoritmo de poda alfa-beta.
     * @param beta valor beta del algoritmo de poda alfa-beta.
     * @param jugador jugador al que le toca poner en cada turno
     * @param conecta número de fichas que hay que conectar para obtener una
     * victoria.
     * @param x posición x donde se colocará una pieza
     * @param y posición y donde se colocará una pieza
     * @param matriz matriz en memoria sobre la que se construye el tablero,
     * útil para probar las posibles victorias o derrotas.
     * @param par par x,y para devolver la posición donde deberá colocar el
     * jugador CPU.
     * @return valor entero informando sobre qué ha sucedido tras un movimiento.
     * 1: gana jugador 1. -1: gana jugador 2: 0 empate.
     */
    private int podaAlfaBeta(Tablero tablero, int profundidad, int alfa, int beta, int jugador, int conecta, int x, int y, int[][] matriz, Par<Integer, Integer> par) {
        MiValorInteger heuristico = new MiValorInteger(0);
        if (comprobarVictoria(y, x, conecta, matriz, tablero.getAlto(), tablero.getAncho(), heuristico) == -1 || profundidad == 0 || comprobarVictoria(y, x, conecta, matriz, tablero.getAlto(), tablero.getAncho(), heuristico) == 1) {

            return heuristico.valor;
        }

        ArrayList<Par<Integer, Integer>> posibles = getMovimientosPosibles(matriz); // Los posibles deberán de calcularse a partir de la matriz, también
        if (jugador == 1) {
            for (int i = 0; i < posibles.size(); i++) {
                x = (int) posibles.get(i).x;
                y = (int) posibles.get(i).y;
                matriz = escribirMatriz(1, matriz, y, x);
                int a = podaAlfaBeta(tablero, profundidad - 1, alfa, beta, 2, conecta, x, y, matriz, par);

                matriz = escribirMatriz(0, matriz, y, x);

                if (a > alfa) {
                    alfa = a;
                    // par.x = x; // Dudas con esta línea de código.
                }
                if (alfa >= beta) {
                    return a;
                }

            }

            return alfa;
        } else {
            for (int i = 0; i < posibles.size(); i++) {
                x = (int) posibles.get(i).x;
                y = (int) posibles.get(i).y;
                matriz = escribirMatriz(-1, matriz, y, x);

                int a = podaAlfaBeta(tablero, profundidad - 1, alfa, beta, 1, conecta, x, y, matriz, par);

                if (a < beta) {
                    beta = a;
                    par.x = x;
                }
                if (beta <= alfa) {
                    return a;
                }

                matriz = escribirMatriz(0, matriz, y, x);
            }

            return beta;
        }

    }

    /**
     *
     * Clase privada que nos será útil durante la ejecución del algoritmo.
     *
     * @param <A>
     * @param <B>
     */
    private class Par<A, B> {

        public A x;
        public B y;

        public Par(A first, B second) {
            this.x = first;
            this.y = second;
        }
    }

    /**
     *
     * Esta función privada se llama desde la poda alfabeta en cada ejecución
     * del algoritmo.
     *
     * @param matriz matriz sobre la que se construye el juego
     * @return ArrayList que contiene los pares de casillas en los que es
     * posible colocar una ficha en cada momento.
     */
    private ArrayList getMovimientosPosibles(int[][] matriz) {
        ArrayList<Par<Integer, Integer>> posibles = new ArrayList<>();

        int posY = matriz.length - 1;

        int[][] aux = matriz;

        for (int i = 0; i < aux[0].length; i++) {
            while (aux[posY][i] != 0 && posY > 0) {
                posY--;
            }
            if (posY != 0 || aux[posY][i] == 0) {
                Par<Integer, Integer> par = new Par(i, posY);
                posibles.add(par);

            }
            posY = matriz.length - 1;
        }

        return posibles;
    }

    /**
     *
     * La función devuelve si habrá algún jugador ganador o habrá empate. A la
     * vez, y mientras calcula un ganador, se inicializan ocho variables
     * enteras, cuatro por ficha y dos por comprobación, de manera que: cuando
     * se hace la comprobación vertical, nos quedamos con la subsecuencia máxima
     * de fichas de cada tipo, y así con las cuatro comprobaciones. Una vez
     * hecho esto, nos quedamos el mayor valor de cada cuádrupla de variables
     * para cada ficha. Los valores máximos obtenidos para el jugador 1 y para
     * el jugador 2, se relacionan tal que: maxjug1/maxjug2, y el resultado de
     * esta comprobación se multiplica por 100 para obtener un valor entero.
     * Gracias a esto obtendremos valores grandes para las victorias del jugador
     * 1 (max), y valores pequeños para las victorias del jugador 2 (min) dando
     * de esta manera un valor heurístico asociado a un nodo, útil en el
     * algoritmo de poda alfa-beta.
     *
     * @param x fila del tablero
     * @param y columna del tablero (las coordenadas están cambiadas).
     * @param conecta fichas que hay que conectar para ganar
     * @param matriz matriz sobre la que se construye el tablero
     * @param alto alto del tablero
     * @param ancho ancho del tablero
     * @param valor valor heurístico asociado a un nodo que se devolverá por
     * referencia en función de las características de la partida actual.
     * @return 1, 0, ó -1 dependiendo de si gana el jugador 1, hay empate o gana
     * el jugador 2.
     *
     */
    public int comprobarVictoria(int x, int y, int conecta, int[][] matriz, int alto, int ancho, MiValorInteger valor) {
        /*
		 *	x fila
		 *	y columna
         */

        int num1 = 0, num2 = 0, num3 = 0, num4 = 0, num5 = 0, num6 = 0, num7 = 0, num8 = 0;

        //Comprobar vertical
        int ganar1 = 0;
        int ganar2 = 0;
        int ganador = 0;
        boolean salir = false;
        for (int j = 0; (j < alto) && !salir; j++) {
            if (matriz[j][y] != 0) {
                if (matriz[j][y] == 1) {
                    ganar1++;
                    num1++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (!salir) {
                    if (matriz[j][y] == -1) {
                        ganar2++;
                        num2++;
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
            if (matriz[x][i] != 0) {
                if (matriz[x][i] == 1) {
                    ganar1++;
                    num3++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (matriz[x][i] == -1) {
                        ganar2++;
                        num4++;
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
            if (matriz[a][b] != 0) {
                if (matriz[a][b] == 1) {
                    ganar1++;
                    num5++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (matriz[a][b] == -1) {
                        ganar2++;
                        num6++;
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
            if (matriz[a][b] != 0) {
                if (matriz[a][b] == 1) {
                    ganar1++;
                    num7++;
                } else {
                    ganar1 = 0;
                }
                // Gana el jugador 1
                if (ganar1 == conecta) {
                    ganador = 1;
                    salir = true;
                }
                if (ganador != 1) {
                    if (matriz[a][b] == -1) {
                        ganar2++;
                        num8++;
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

        int def1 = max(max(num1, num3), max(num5, num7)); // Subsecuencia de fichas máxima de tipo 1.
        int def2 = max(max(num2, num4), max(num6, num8)); // Subsecuencia de fichas máxima de tipo 2.

        float valorHeuristico;
        if (def2 > 0) {
            valorHeuristico = (float) def1 / def2;
        } else {
            valorHeuristico = def1 * 100;

        }

        if (valorHeuristico == 0) {
            valorHeuristico = def1;
        }

        valorHeuristico *= 100;
        int valorRetorno = (int) valorHeuristico;
        System.out.println("Valor retorno: " + valorRetorno);
        valor.valor = valorRetorno;

        return ganador;
    } // comprobarVictoria

    public int[][] escribirMatriz(int jugador, int[][] matriz, int i, int j) {
        matriz[i][j] = jugador;
        return matriz;
    }

    public int[][] copiarMatriz(int[][] matriz) {
        int[][] copia = new int[matriz.length][];
        for (int m = 0; m < matriz.length; m++) {
            copia[m] = new int[matriz[m].length];
            for (int j = 0; j < matriz[m].length; j++) {
                copia[m][j] = matriz[m][j];
            }
        }
        return copia;
    }

    public void imprimirMatriz(int[][] matriz) {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%d ", matriz[i][j]);

            }
            System.out.println();
        }
    }

    private class MiValorInteger {

        public int valor;

        public MiValorInteger(int valor) {
            this.valor = valor;
        }
    }
} // JugadorCPUAlfaBeta;
