import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

/**
 * Barra de progreso animada con la posibilidad de asignar colores.
 *
 */
public class BarraDeProgreso extends ProgressBar {

    private double progreso;
    private double aumentoPorTarea;

    private ArrayList<String> colores;
    private int contadorColores;

    /**
     * Constructor sin parámetros, sólo utilizado para consultar fácilmente los
     * métodos.
     */
    public BarraDeProgreso() {

    }

    /**
     * Constructor sin asignación de colores.
     *
     * @param progreso Punto de inicio del progreso.
     * @param NúmeroDeTareas Número de subtareas que deben ser completadas por
     * la barra.
     * @param AnimaciónInicial Se muestra animación de inicio o no.
     *
     */
    public BarraDeProgreso(double progreso, int NúmeroDeTareas, Boolean AnimaciónInicial) {
        DefinirParámetrosComunesInicio(progreso, AnimaciónInicial, NúmeroDeTareas);
    }

    /**
     * Constructor con asignación de colores. Si el número de colores es
     * inferior al de tareas, el color intermedio se utilizará varias veces (y,
     * si hay más de 3 colores, todos los que no sean primero, intermedio y
     * final se ignorarán).
     *
     * @param progreso Punto de inicio del progreso.
     * @param NúmeroDeTareas Número de subtareas que deben ser completadas por
     * la barra.
     * @param colores Colores a mostrar en cada punto del progreso.
     * @param AnimaciónInicial Se muestra animación de inicio o no.
     *
     */
    public BarraDeProgreso(double progreso, int NúmeroDeTareas, Boolean AnimaciónInicial, String[] colores) {
        DefinirParámetrosComunesInicio(progreso, AnimaciónInicial, NúmeroDeTareas);
        this.colores = new ArrayList();
        contadorColores = 0;
        if (NúmeroDeTareas == colores.length) {
            for (String color : colores) {
                this.colores.add(color);
            }
        } else {
            for (int i = 0; i < NúmeroDeTareas; i++) {
                if (i == 0) {
                    this.colores.add(colores[i]);
                } else if (i == NúmeroDeTareas - 1) {
                    this.colores.add(colores[colores.length - 1]);
                } else {
                    this.colores.add(colores[1]);
                }
            }
        }
    }

    private void DefinirParámetrosComunesInicio(double progreso1, Boolean AnimaciónInicial, int NúmeroDeTareas) {
        this.progreso = progreso1;
        if (!AnimaciónInicial) {
            this.setProgress(progreso1);
        }
        aumentoPorTarea = 1 / (Double.valueOf(NúmeroDeTareas));
    }

    /**
     * Método para ser llamado cada vez que se quiere aumentar la barra.
     */
    public void AumentarProgreso() {
        progreso += aumentoPorTarea;
        Timeline timeline = new Timeline();
        KeyValue keyValue = new KeyValue(this.progressProperty(), progreso);
        KeyFrame keyFrame = new KeyFrame(new Duration(1000), keyValue);
        timeline.getKeyFrames().add(keyFrame);
        if (colores != null) {
            CambiarColor();
        }
        timeline.play();
    }

    private void CambiarColor() {
        this.setStyle("-fx-accent: " + colores.get(contadorColores));
        contadorColores++;
    }

    public double getProgreso() {
        return progreso;
    }
}
