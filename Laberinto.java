import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Laberinto extends JFrame {

    // Tamaño de cada bloque en la pantalla (píxeles)
    private static final int TAMANO_BLOQUE = 40;
    
    // Posición actual del jugador (en la cuadrícula del mapa)
    private int jugadorX = 1;
    private int jugadorY = 1;
    
    // Nivel actual (empieza en 0, que es el Nivel 1)
    private int nivelActual = 0;

    // Significado de los números en los mapas:
    // 0 = Camino libre
    // 1 = Pared del laberinto
    // 2 = Meta para pasar de nivel
    private final int[][][] MAPAS = {
        // NIVEL 1
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 1, 0, 0, 0, 0, 0, 1},
            {1, 1, 0, 1, 0, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 2, 1}, // El 2 es la meta
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        // NIVEL 2
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 1, 0, 0, 0, 1, 0, 0, 1},
            {1, 0, 1, 0, 1, 0, 1, 1, 0, 1},
            {1, 0, 0, 0, 1, 0, 0, 1, 0, 1},
            {1, 1, 1, 0, 1, 1, 0, 1, 0, 1},
            {1, 2, 0, 0, 0, 1, 0, 0, 0, 1}, // Meta en un lugar diferente
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        },
        // NIVEL 3
        {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 0, 1},
            {1, 0, 0, 0, 0, 0, 0, 1, 0, 1},
            {1, 0, 1, 1, 1, 1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0, 1, 0, 0, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        }
        // ¡Aquí puedes seguir pegando matrices hasta llegar a 25!
    };

    public Laberinto() {
        // Configuración básica de la ventana
        setTitle("Laberinto - Nivel " + (nivelActual + 1));
        setSize(420, 320); // Ajustado al tamaño de la matriz (10x7 bloques)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel personalizado donde dibujamos el juego
        PanelJuego panel = new PanelJuego();
        add(panel);

        // Escuchar las teclas para mover al cubito verde
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int nuevoX = jugadorX;
                int nuevoY = jugadorY;

                // Detectar qué flecha se presionó
                if (e.getKeyCode() == KeyEvent.VK_UP)    nuevoY--;
                if (e.getKeyCode() == KeyEvent.VK_DOWN)  nuevoY++;
                if (e.getKeyCode() == KeyEvent.VK_LEFT)  nuevoX--;
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) nuevoX++;

                // Lógica de colisión y movimiento
                int[][] mapaActual = MAPAS[nivelActual];
                
                // Si la posición a la que queremos ir no es una pared (1)
                if (mapaActual[nuevoY][nuevoX] != 1) {
                    jugadorX = nuevoX;
                    jugadorY = nuevoY;

                    // Si tocamos la meta (2)
                    if (mapaActual[jugadorY][jugadorX] == 2) {
                        pasarDeNivel();
                    }
                }

                // Redibujar la pantalla con los cambios
                panel.repaint();
            }
        });
    }

    private void pasarDeNivel() {
        if (nivelActual < MAPAS.length - 1) {
            nivelActual++;
            // Reiniciar la posición del cubito al inicio del nuevo nivel
            jugadorX = 1;
            jugadorY = 1;
            setTitle("Laberinto - Nivel " + (nivelActual + 1));
            JOptionPane.showMessageDialog(this, "¡Nivel Completado! Cargando Nivel " + (nivelActual + 1));
        } else {
            JOptionPane.showMessageDialog(this, "¡Felicidades! Completaste todos los niveles.");
            System.exit(0);
        }
    }

    // Clase interna que maneja los gráficos (el lienzo)
    private class PanelJuego extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            int[][] mapaActual = MAPAS[nivelActual];

            // Dibujar el laberinto bloque por bloque
            for (int fila = 0; fila < mapaActual.length; fila++) {
                for (int col = 0; col < mapaActual[fila].length; col++) {
                    if (mapaActual[fila][col] == 1) {
                        g.setColor(Color.DARK_GRAY); // Paredes grises
                        g.fillRect(col * TAMANO_BLOQUE, fila * TAMANO_BLOQUE, TAMANO_BLOQUE, TAMANO_BLOQUE);
                        g.setColor(Color.BLACK); // Bordes de los bloques
                        g.drawRect(col * TAMANO_BLOQUE, fila * TAMANO_BLOQUE, TAMANO_BLOQUE, TAMANO_BLOQUE);
                    } else if (mapaActual[fila][col] == 2) {
                        g.setColor(Color.RED); // Meta roja
                        g.fillRect(col * TAMANO_BLOQUE, fila * TAMANO_BLOQUE, TAMANO_BLOQUE, TAMANO_BLOQUE);
                    }
                }
            }

            // DIBUJAR AL PERSONAJE (Tu cubito verde)
            g.setColor(Color.GREEN);
            g.fillRect(jugadorX * TAMANO_BLOQUE + 5, jugadorY * TAMANO_BLOQUE + 5, TAMANO_BLOQUE - 10, TAMANO_BLOQUE - 10);
            
            // Ojo o detalle para que se vea más como un personaje
            g.setColor(Color.BLACK);
            g.fillRect(jugadorX * TAMANO_BLOQUE + 12, jugadorY * TAMANO_BLOQUE + 12, 5, 5);
            g.fillRect(jugadorX * TAMANO_BLOQUE + 22, jugadorY * TAMANO_BLOQUE + 12, 5, 5);
        }
    }

    public static void main(String[] args) {
        // Ejecutar la ventana en el hilo de interfaz gráfica de Java
        SwingUtilities.invokeLater(() -> {
            new Laberinto().setVisible(true);
        });
    }
}
