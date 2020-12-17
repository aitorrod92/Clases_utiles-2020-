import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.application.Platform;

/**
 * Crea un temporizador que, al finalizar, invoca un método en un objeto.
 *
 * @param duracion Duración en milisegundos.
 * @param bucle -1 si es un bucle infinito, X si es un bucle finito.
 * @param instancia Objeto que posee el método que se quiere invocar.
 * @param metodo Nombre del método a invocar.
 * @param argumentos Vararg de argumentos a utilizar por el método.
 *
 */
public class Temporizador2 extends Thread {

    private Boolean terminado = false;

    private int duracion;
    private Object instancia;
    private String metodo;
    private String[] argumentos;
    private int bucle;

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public void setInstancia(Object instancia) {
        this.instancia = instancia;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public void setArgumentos(String[] argumentos) {
        this.argumentos = argumentos;
    }

    public void setBucle(int bucle) {
        this.bucle = bucle;
    }

    public Temporizador2(int duracion, int bucle, Object instancia, String metodo, String... argumentos) {
        this.duracion = duracion;
        this.instancia = instancia;
        this.metodo = metodo;
        this.argumentos = argumentos;
        this.bucle = bucle;
    }

    public void run() {
        while (bucle != 0) {
            bucle--;
            esperar();
            obtenerMétodo();
        }
    }

    /**
     * Detiene la espera, forzando que el hilo finalice sin llegar a invocar el
     * método.
     */
    public void interrumpirEspera() {
        this.interrupt();
    }

    /**
     * Detiene el hilo durante el tiempo especificado. Si la espera es
     * interrumpida, se propaga la excepción para que el hilo finalice.
     *
     */
    private void esperar() {
        try {
            Thread.sleep(duracion);
        } catch (InterruptedException ex) {
            throw new RuntimeException("Espera interrumpida");
        }
    }

    /**
     * Llama al método al finalizar la espera.
     */
    private void obtenerMétodo() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Method método;
                try {
                    if (Temporizador2.this.argumentos.length != 0) {
                        System.out.println("Por allá");
                        método = instancia.getClass().getDeclaredMethod(metodo, String[].class);
                        Temporizador2.this.invocarMétodo(método, true);
                    } else {
                        System.out.println("Por acá");
                        método = instancia.getClass().getDeclaredMethod(metodo);

                        Temporizador2.this.invocarMétodo(método, false);
                    }
                } catch (NoSuchMethodException ex) {
                    System.out.println("No existe el método");
                } catch (SecurityException ex) {
                    System.out.println("El método no es accesible");
                }
            }
        });
    }

    private void invocarMétodo(Method método, Boolean argumentos) {
        método.setAccessible(true);
        try {
            if (argumentos) {
                método.invoke(instancia, new Object[]{argumentos});
            } else {
                método.invoke(instancia);
            }
        } catch (IllegalAccessException ex) {
            System.out.println("Acceso al método es ilegal");
        } catch (IllegalArgumentException ex) {
            System.out.println("Argumento(s) inválido(s)");
        } catch (InvocationTargetException ex) {
            System.out.println("Error en la invocación");
        }
    }
}
