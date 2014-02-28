package parabola;

import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParabolaJFrame extends JFrame implements Runnable, KeyListener, MouseListener, MouseMotionListener
{
    
        private static final long serialVersionUID = 1L;
	// Se declaran las variables.
	private int direccion;    // Direccion
        private int posX;
        private int posY;
        private int puntos;
	private final int MIN = 3;    //Minimo al generar un numero al azar.
	private final int MAX = 6;    //Maximo al generar un numero al azar.
	private Image dbImage;	// Imagen a proyectar
        private Image gameover;
	private Graphics dbg;	// Objeto grafico
	private SoundClip explosion;    // Objeto AudioClip
        private SoundClip beep;
	private Lanzado pelota;    // Objeto de la clase Lanzado
	private Atrapador mario;    //Objeto de la clase Atrapador
	private boolean choco;
        private boolean colision;
        private boolean pausa;
        private boolean instrucciones;
        private boolean click;
        private Animacion animPelota;
        private Animacion animMario;
        private long tiempoActual;
	private long tiempoInicial;
        private int ultDireccion;
        private int vidas;
        private int bolaPerdida;
        private String nombreArchivo;
        
        public ParabolaJFrame(){
            
                direccion=0;
                vidas=5;
                bolaPerdida=0;
                pausa=false;
                instrucciones=false;
                nombreArchivo="Juego.txt";
                
                Image pelota0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola1.png"));
                Image pelota1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola2.png"));
                Image pelota2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola3.png"));
                Image pelota3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Bola4.png"));
            
                animPelota= new Animacion();
                animPelota.sumaCuadro(pelota0,100);
                animPelota.sumaCuadro(pelota1,100);
                animPelota.sumaCuadro(pelota2,100);
                animPelota.sumaCuadro(pelota3,100);
            
                Image mario0 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario0.png"));
                Image mario1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario1.png"));
                Image mario2 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario2.png"));
                Image mario3 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario3.png"));
                Image mario4 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario4.png"));
                Image mario5 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario5.png"));
                Image mario6 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("imagenes/Mario6.png"));
            
                animMario= new Animacion();
                animMario.sumaCuadro(mario0,100);
                animMario.sumaCuadro(mario1,100);
                animMario.sumaCuadro(mario2,100);
                animMario.sumaCuadro(mario3,100);
                animMario.sumaCuadro(mario4,100);
                animMario.sumaCuadro(mario5,100);
                animMario.sumaCuadro(mario6,100);
                
                mario=new Atrapador(400,400,animMario);
                pelota= new Lanzado(100,100,animPelota);
                
                beep = new SoundClip("sonidos/beep.wav");
                explosion = new SoundClip("sonidos/explosion.wav");
            
                setBackground(Color.white);
                setSize(800,500);
                addKeyListener(this);
                addMouseMotionListener(this);
                addMouseListener(this);
            
                Thread th = new Thread (this);
		// Empieza el hilo
		th.start ();
        }
        
        
        /** 
	 * Metodo <I>run</I> sobrescrito de la clase <code>Thread</code>.<P>
     * En este metodo se ejecuta el hilo, es un ciclo indefinido donde se incrementa
     * la posicion en x o y dependiendo de la direccion, finalmente 
     * se repinta el <code>Applet</code> y luego manda a dormir el hilo.
     * 
     */
        public void run () {
		while (true) {
			actualiza();
			checaColision();
			repaint();    // Se actualiza el <code>Applet</code> repintando el contenido.
			try	{
				// El thread se duerme.
				Thread.sleep (20);
			}
			catch (InterruptedException ex)	{
				System.out.println("Error en " + ex.toString());
			}
		}
	}
        
        /**
	 * Metodo usado para actualizar la posicion de objetos planeta y meteorito.
	 * 
	 */
        
        public void actualiza() {
            
            if(!pausa){
            
                long tiempoTranscurrido= System.currentTimeMillis() - tiempoActual;
                tiempoActual += tiempoTranscurrido;
                animMario.actualiza(tiempoTranscurrido);
                animPelota.actualiza(tiempoTranscurrido);
                //Dependiendo de la direccion del bueno es hacia donde se mueve.
                switch(direccion){
                    case 1: {
                        mario.setPosX(mario.getPosX() + 2);
                        break;
                    }
                    case 2: {
                        mario.setPosX(mario.getPosX() - 2);
                        break;
                    }
                }
                
            }
        }
        
        /**
	 * Metodo usado para checar las colisiones del objeto planeta y meteorito
	 * con las orillas del <code>JFrame</code>.
	 */
        public void checaColision(){
            
            if (mario.getPosX() + mario.getAncho() > getWidth()) {
                     mario.setPosX(getWidth()-mario.getAncho());
                }
                 
                if (mario.getPosX() < 0) {
			mario.setPosX(0);
		}
                
		if (mario.getPosY() + mario.getAlto() > getHeight()) {
			mario.setPosY(getHeight()-mario.getAlto());
		}
                
                if (mario.getPosY() < 0) {
                     mario.setPosY(0);  
                }
                
            if(pelota.getPosY() + pelota.getAlto() > getHeight()){
                
                pelota.setPosX(100);
                pelota.setPosY(100);
                explosion.play();
                bolaPerdida+=1;
                if(bolaPerdida==3){
                    vidas-=1;
                    bolaPerdida=0;
                }
                
                //Colision entre objetos
                if(mario.intersecta(pelota)){
                    
                    pelota.setPosX(100);
                    pelota.setPosY(100);
                    beep.play();
                    puntos+=2;
                }
            
        }
        }
      
        /**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>JFrame</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo lo que hace es actualizar el contenedor
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint(Graphics g){
		// Inicializan el DoubleBuffer
		if (dbImage == null){
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}

		// Actualiza la imagen de fondo.
		dbg.setColor(getBackground ());
		dbg.fillRect(0, 0, this.getSize().width, this.getSize().height);

		// Actualiza el Foreground.
		dbg.setColor(getForeground());
		paint1(dbg);

		// Dibuja la imagen actualizada
		g.drawImage (dbImage, 0, 0, this);
	}
        
        /**
	 * Metodo <I>paint</I> sobrescrito de la clase <code>Applet</code>,
	 * heredado de la clase Container.<P>
	 * En este metodo se dibuja la imagen con la posicion actualizada,
	 * ademas que cuando la imagen es cargada te despliega una advertencia.
	 * @param g es el <code>objeto grafico</code> usado para dibujar.
	 */
	public void paint1(Graphics g) {
                    
                    if(!pausa){
                            if(mario != null && pelota != null){
                                     //Dibuja la imagen en la posicion actualizada, dibuja el puntaje 
                                     //y despliega la imagen al terminar el juego
                                     g.drawImage(mario.getAnimacion().getImagen(), mario.getPosX(),mario.getPosY(), this);
                                     g.drawImage(pelota.getAnimacion().getImagen(), pelota.getPosX(), pelota.getPosY(), this);
                                     g.setColor(Color.black);
                                     g.drawString("Puntos: " + puntos, 30, 50);
                                     g.drawString("Vidas: " + vidas, 30,65);
                            }
                            else {

                            //Da un mensaje mientras se carga el dibujo
                            g.drawString("No se cargo la imagen..",20,20);
                        }
                    }
                    else{
                        
                       if(mario != null && pelota != null){
                                     //Dibuja la imagen en la posicion actualizada, dibuja el puntaje 
                                     //y despliega la imagen al terminar el juego
                                     g.drawImage(mario.getAnimacion().getImagen(), mario.getPosX(),mario.getPosY(), this);
                                     g.drawImage(pelota.getAnimacion().getImagen(), pelota.getPosX(), pelota.getPosY(), this);
                                     g.setColor(Color.black);
                                     g.drawString("Puntos: " + puntos, 30, 50);
                                     g.drawString("Vidas: " + vidas, 30,65);
                                     g.drawString("PAUSA", 100, 100);
                                    
                    }
                }
                
                if(instrucciones){
                            if(mario != null && pelota != null){
                                     //Dibuja la imagen en la posicion actualizada, dibuja el puntaje 
                                     //y despliega la imagen al terminar el juego
                                     g.drawImage(mario.getAnimacion().getImagen(), mario.getPosX(),mario.getPosY(), this);
                                     g.drawImage(pelota.getAnimacion().getImagen(), pelota.getPosX(), pelota.getPosY(), this);
                                     g.setColor(Color.black);
                                     g.drawString("Puntos: " + puntos, 30, 50);
                                     g.drawString("Vidas: " + vidas, 30,65);
                                     g.drawString("ATRAPA LA PELOTA",100,100);
                            }
                            else {

                            //Da un mensaje mientras se carga el dibujo
                            g.drawString("No se cargo la imagen..",20,20);
                        }
               }
}
       
      /**
     * Metodo <I>keyPressed</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar cualquier la
     * tecla.
     *
     * @param e es el <code>evento</code> generado al presionar las teclas.
     */
    public void keyPressed(KeyEvent e) {
        
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {    //Presiono flecha derecha
                
                direccion = 1;
            } 
            else if (e.getKeyCode() == KeyEvent.VK_LEFT) {    //Presiono tecla A izquierda
                
                direccion = 2;
            }
            else if (e.getKeyCode() == KeyEvent.VK_P){
                
                pausa=!pausa;
            }
            else if(e.getKeyCode() == KeyEvent.VK_I){
                
                instrucciones=!instrucciones;
            }
            else if(e.getKeyCode() == KeyEvent.VK_S){
                
                beep.stop();
                explosion.stop();
            }
            else if(e.getKeyCode() == KeyEvent.VK_G){
                try {
                    grabaArchivo();
                } catch (IOException ex) {
                    Logger.getLogger(ParabolaJFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
    }

    /**
     * Metodo <I>keyTyped</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al presionar una tecla que
     * no es de accion.
     *
     * @param e es el <code>evento</code> que se genera en al presionar las
     * teclas.
     */
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Metodo <I>keyReleased</I> sobrescrito de la interface
     * <code>KeyListener</code>.<P>
     * En este metodo maneja el evento que se genera al soltar la tecla
     * presionada.
     *
     * @param e es el <code>evento</code> que se genera en al soltar las teclas.
     */
    
    public void keyReleased(KeyEvent e) {
        
    }
    
    /**
	 * Metodo <I>mouseClicked</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar el mouse.
	 * @param e es el <code>evento</code> generado al presionar el mouse.
	 */
	public void mouseClicked(MouseEvent e) {
    }
    
        /**
	 * Metodo <I>mouseEntered</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera cuando el cursor entra en un componente
	 * @param e es el <code>evento</code> que se genera al entrar al area del objeto.
	 */
    public void mouseEntered(MouseEvent e){
    	
    }
    
    /**
	 * Metodo <I>mouseExited</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al abandonar el area del objeto seleccionado con el cursor;
	 * @param e es el <code>evento</code> que se genera en al soltar las teclas.
	 */
    public void mouseExited(MouseEvent e){
    	
    }
    
    /**
	 * Metodo <I>mousePressed</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al presionar con el cursor el componente
	 * @param e es el <code>evento</code> que se genera en al soltar las teclas.
	 */
    public void mousePressed(MouseEvent e){
         if (pelota.contiene(e.getX(), e.getY()) && !click) {
            click = true;
        }
    }
    
    /**
	 * Metodo <I>mouseReleased</I> sobrescrito de la interface <code>MouseListener</code>.<P>
	 * En este metodo maneja el evento que se genera al soltar el boton del mouse.
	 * @param e es el <code>evento</code> que se genera al soltar el boton del mouse
	 */
    public void mouseReleased(MouseEvent e){
        click=false;
    	
    }
    
    public void mouseMoved(MouseEvent e){
        
    }
    
    public void mouseDragged(MouseEvent e){
       
    }
    
     public void grabaArchivo() throws IOException {
                                                          
                PrintWriter fileOut = new PrintWriter(new FileWriter(nombreArchivo));  
                fileOut.println(mario.getPosX() + " " +mario.getPosY());
                fileOut.close();
        }

}
