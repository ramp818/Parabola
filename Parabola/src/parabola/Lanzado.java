package parabola;

//Clase para el objeto lanzado
public class Lanzado extends Base{
    private int velocidadX;  //variable de velocidad
    private int velocidadY;
    
        /**
	 * Metodo constructor que hereda los atributos de la clase <code>Universo</code>.
	 * @param posX es la <code>posiscion en x</code> del objeto Planeta.
	 * @param posY es el <code>posiscion en y</code> del objeto Planeta.
	 * @param image es la <code>imagen</code> del objeto Planeta.
         * @param velX
         * @param velY
	 */
    
    public Lanzado(int posX,int posY,Animacion image, int velX, int velY){
		super(posX,posY,image);	
                velocidadX = velX;  //se define la velocidad del objeto
                velocidadY = velY;
	}
    /**
     * Método <I>setVelocidad</I> que define la velocidad del objeto
     * 
     * @param vel es el valor de la velocidad del tipo <code>int</code>
     */
    public void setVelocidadX(int vel) {
        velocidadX = vel;  //define el valor de la velocidad
    }
    
    /**
     * Método <I>getVelocidad</I> que regresa el valor de la velocidad del objeto
     * 
     * @return valor de la velocidad del objeto de tipo <code>int</code>
     */
    public int getVelocidadX() {
        return velocidadX;  //regresa el valor de la velocidad
    }
    
    public void setVelocidadY(int vel) {
        velocidadY = vel;  //define el valor de la velocidad
    }
    
    public int getVelocidadY() {
        return velocidadY;  //regresa el valor de la velocidad
    }
}
