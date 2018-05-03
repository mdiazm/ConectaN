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
 * Código original: * Lenin Palafox * http://www.leninpalafox.com
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ConectaN extends JFrame implements ActionListener {

    // Número de turnos/movimientos
    private int turno = 0;
    // Jugador 2, CPU por defecto
    private boolean jugadorcpu = true;
    // Jugador 2, CPU aleatorio por defecto
    private boolean alfabeta = false;
    // Marca si el jugador pulsa sobre el tablero
    private boolean pulsado;

    // Parámetros
    // Número de filas
    private final int alto;
    // Número de columnas
    private final int ancho;
    // Número de fichas que han de colocarse en secuencia para ganar
    private final int conecta;

    // Tablero de juego
    private Tablero juego;

    //menus y elementos de la GUI
    private final JMenuBar barra = new JMenuBar();
    private final JMenu archivo = new JMenu("Archivo");
    private final JMenu opciones = new JMenu("Opciones");
    private final JMenuItem nuevo = new JMenuItem("Nueva partida");
    private final JMenuItem salir = new JMenuItem("Salir");
    private final JRadioButton p1h = new JRadioButton("Humano", true);
    private final JRadioButton p2h = new JRadioButton("Humano", false);
    private final JRadioButton p2c = new JRadioButton("CPU (Aleatorio)", true);
    private final JRadioButton p2c2 = new JRadioButton("CPU (AlfaBeta)", false);
    private final JLabel nombre = new JLabel("Pr\u00e1cticas de Inteligencia Artificial (Curso 2016-17) - Dpto. Inform\u00e1tica - UJA", JLabel.CENTER);

    //jugadores
    private Jugador player2;

    /**
     * Constructor
     *
     * @param alto Número de filas
     * @param ancho Número de columnas
     * @param conecta Número de fichas consecutivas para ganar
     */
    public ConectaN(int alto, int ancho, int conecta) {
        this.alto = alto;
        this.ancho = ancho;
        this.conecta = conecta;
    }

    /**
     * Gestión de eventos
     *
     * @param ae
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // Eventos del menú Opciones
        if (ae.getSource() == p2h) {
            jugadorcpu = false; // Humano
            reset();
        }
        if (ae.getSource() == p2c) {
            jugadorcpu = true; // CPU Random
            alfabeta = false;
            reset();
        }
        if (ae.getSource() == p2c2) {
            jugadorcpu = true; // CPU Alfa Beta;
            alfabeta = true;
            reset();
        }
        if (ae.getSource() == salir) {
            dispose();
            System.exit(0);
        }
        if (ae.getSource() == nuevo) {
            reset();
        }

        // Control del juego por el usuario
        int x = 0;
        int y = 0;

        // Siempre empieza el jugador 1
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                if (ae.getSource() == juego.getButton(i, j)) {
                    //Ir hasta la ultima posicion
                    int k = alto;
                    do {
                        k--;
                    } while (juego.getButton(k, j).getIcon() != null & k != 0);
                    //Pintar Ficha
                    if (juego.getButton(k, j).getIcon() == null) {
                        juego.setButton(k, j, (turno % 2 == 0));
                        if (jugadorcpu) //Si es modo un jugador o dos
                        {
                            pulsado = true;
                        }
                        turno++;
                        x = k;
                        y = j;
                        // Comprobar si acabó el juego
                        finJuego(juego.checkWin(x, y, conecta));
                    }
                }
            }
        }
        //Empate!!!
        if (turno >= ancho * alto) {
            JOptionPane.showMessageDialog(this, "¡Empate!", "Conecta-N", JOptionPane.INFORMATION_MESSAGE);
            reset();
        }
        // Mostrar tablero tras cada movimiento
        juego.print();

        // Pasa el turno al jugador 2
        if (pulsado) {
            if (jugadorcpu) {
                pulsado = false;
                turno++;
                // Comprobar si acabó el juego
                finJuego(player2.jugada(x, y, conecta, juego));

                //Empate!!!
                if (turno >= ancho * alto) {
                    JOptionPane.showMessageDialog(this, "¡Empate!", "Conecta-N", JOptionPane.INFORMATION_MESSAGE);
                    reset();
                }
                // Mostrar tablero tras cada movimiento
                juego.print();           
            }
        }

        

    } // actionPerformed         

    public void finJuego(int ganador) {
        // Comprobamos si llegamos al final del juego        
        switch (ganador) {
            case 1:
                JOptionPane.showMessageDialog(this, "¡Ganador, Jugador 1\nen " + turno + " movimientos!", "Conecta-N", JOptionPane.INFORMATION_MESSAGE, juego.getFicha1());
                System.out.println("Ganador: Jugador 1, en " + turno + " movimientos.");
                reset();
                break;
            case -1:
                JOptionPane.showMessageDialog(this, "¡Ganador, Jugador 2\nen " + turno + " movimientos!", "Conecta-N", JOptionPane.INFORMATION_MESSAGE, juego.getFicha2());
                System.out.println("Ganador: Jugador 2, en " + turno + " movimientos.");
                reset();
                break;
        }

    } // terminarJuego

    /**
     * Método principal
     *
     * Lectura de parámetros desde línea de comandos e inicio del programa
     *
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("ConectaN.java - N en Raya");
        System.out.println("-----------------------------------------");
        System.out.println("Inteligencia Artificial - Curso 2016-17");
        System.out.println("Dpto. Inform\u00e1tica - Universidad de Ja\u00e9n");
        try {
            int alto = Integer.parseInt(args[0]);
            int ancho = Integer.parseInt(args[1]);
            int conecta = Integer.parseInt(args[2]);

            // Los parámetros mínimos del juego son: 
            // Mínimo conecta: 4
            // Mínimo alto: 7
            // Mínimo ancho: 7
            if (alto < 7 || ancho < 7 || conecta < 4 || conecta > alto || conecta > ancho) {
                ayuda();
                System.exit(0);
            }

            System.out.println();
            System.out.println("Nueva partida.");
            System.out.println("Tama\u00f1o del tablero: " + alto + " filas x " + ancho + " columnas.");
            System.out.println("Conecta " + conecta + " fichas en raya para ganar.");
            System.out.println();
            System.out.println("Estado inicial:");
            System.out.println("Jugador 1: Humano");
            System.out.println("Jugador 2: CPU (Modo aleatorio)");

            ConectaN juego = new ConectaN(alto, ancho, conecta);
            juego.run();

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException ex) {
            System.out.println(ex.getMessage());
            ayuda();
        }
    } // main

    /**
     * Mostrar un mensaje del uso correcto del programa
     *
     * Indica qué parámetros debemos pasarle obligatoriamente al programa y cómo
     */
    private static void ayuda() {
        System.out.println();
        System.out.println();
        System.out.println("Error en los par\u00e1metros de llamada");
        System.out.println();
        System.out.println("Uso: java [classpath] conectaN.ConectaN <alto> <ancho> <fichas>");
        System.out.println();
        System.out.println("[classpath]: Si es necesario, incluir el classpath, p.e.: -cp classes");
        System.out.println("<alto>     : N\u00faúmero de filas del tablero. Entero. OBLIGATORIO. M\u00edn: 7");
        System.out.println("<ancho>    : N\u00famero de columnas del tablero. Entero. OBLIGATORIO. M\u00edn: 7");
        System.out.println("<fichas>   : N\u00famero de fichas seguidas para ganar la partida. Entero. OBLIGATORIO. M\u00edn: 4");
        System.out.println();
    }

    /**
     * Configuración inicial
     *
     * Creación de la interfaz gráfica del juego
     */
    private void run() {

        juego = new Tablero(alto, ancho, "assets/player1.png", "assets/player2.png");
        int altoVentana = (alto + 1) * juego.getFicha1().getIconWidth();
        int anchoVentana = ancho * juego.getFicha1().getIconWidth();

        if (alfabeta) {
            player2 = new JugadorCPUAlfaBeta();
        } else {
            player2 = new JugadorCPURandom();
        }

        //menu GUI
        nuevo.addActionListener(this);
        archivo.add(nuevo);
        archivo.addSeparator();
        salir.addActionListener(this);
        archivo.add(salir);
        // Player 1
        opciones.add(new JLabel("Jugador 1:"));
        ButtonGroup m1Jugador = new ButtonGroup();
        m1Jugador.add(p1h);
        // Player 2
        opciones.add(p1h);
        opciones.add(new JLabel("Jugador 2:"));
        p2h.addActionListener(this);
        p2c.addActionListener(this);
        p2c2.addActionListener(this);
        ButtonGroup m2Jugador = new ButtonGroup();
        m2Jugador.add(p2h);
        m2Jugador.add(p2c);
        m2Jugador.add(p2c2);
        opciones.add(p2h);
        opciones.add(p2c);
        opciones.add(p2c2);

        barra.add(archivo);
        barra.add(opciones);
        setJMenuBar(barra);

        //Panel Principal 
        JPanel principal = new JPanel();
        principal.setLayout(new GridLayout(alto, ancho));

        //Colocar Botones
        for (int i = 0; i < alto; i++) {
            for (int j = 0; j < ancho; j++) {
                juego.initialize(i, j, this, Color.BLACK);
                principal.add(juego.getButton(i, j));
            }
            nombre.setForeground(Color.BLUE);
            add(nombre, "North");
            add(principal, "Center");
        }

        //Para cerrar la Ventana
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        //tamaño frame
        setLocation(170, 25);
        setSize(anchoVentana, altoVentana);
        setResizable(false);
        setTitle("Conecta-N 1.2 - Dpto. Inform\u00e1tica - UJA 2016-17");
        setVisible(true);
    } // config

    /**
     * Reinicia una partida
     */
    private void reset() {
        // Volver el programa al estado inicial	
        juego.reset();
        turno = 0;
        pulsado = false;

        System.out.println();
        System.out.println("Nueva partida:");
        System.out.println("Jugador 1: Humano");
        System.out.print("Jugador 2: ");
        if (jugadorcpu) {
            if (alfabeta) {
                player2 = new JugadorCPUAlfaBeta();
                System.out.println("CPU (AlfaBeta)");
            } else {
                player2 = new JugadorCPURandom();
                System.out.println("CPU (Modo aleatorio)");
            }
        } else {
            player2 = new JugadorHumano();
            System.out.println("Humano");
        }
    } // reset

} // ConectaN
