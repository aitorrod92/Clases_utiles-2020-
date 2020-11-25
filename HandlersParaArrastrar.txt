import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class AsignarHandlersParaArrastrar {

    /**
     * Clase auxiliar para guardar las distancias iniciales en permitirArrastrar
     */
    private class Delta {

        double x, y;
    }

    /**
     * Hace que uno o varios nodos se puedan mover arrastrándolo con el cursor
     *
     * @param cursorCambia Booleano indicando si se debe cambiar el aspecto
     * del cursor
     * @param nodos Nodos que se quiere poder arrastrar
     */
    AsignarHandlersParaArrastrar(Boolean cursorCambia, final Node... nodos) {
        final Delta dragDelta = new Delta();
        for (Node nodo : nodos) {
            nodo.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    // Guarda la distancia entre el nodo y el ratón
                    for (Node n : nodos) {
                        dragDelta.x = n.getTranslateX() - mouseEvent.getX();
                        dragDelta.y = n.getTranslateY() - mouseEvent.getY();
                        if (cursorCambia) {
                            nodo.getScene().setCursor(Cursor.MOVE);
                        }
                    }
                }
            });
            nodo.setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    for (Node n : nodos) {
                        n.getScene().setCursor(Cursor.DEFAULT);
                    }
                }
            });
            nodo.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    /* Mueve el nodo en función de la localización del ratón y de
                    la distancia con el nodo que había inicialmente */
                    for (Node n : nodos) {
                        n.setTranslateX(mouseEvent.getX() + dragDelta.x);
                        n.setTranslateY(mouseEvent.getY() + dragDelta.y);
                    }
                }
            });
        }
    }
}