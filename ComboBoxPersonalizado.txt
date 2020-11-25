import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

/**
 * Crea un combobox personalizado.
 *
 * @param Opciones Array de strings que se mostrarán como opciones.
 * @param seleccionado Índice de la opción seleccionada por defecto.
 * @param ListaImagenes HashMap <nombreImagen,Imagen>, donde "nombreImagen" ha
 * de coincidir con alguna de las opciones para que su imagen asociada aparezca.
 */
public class ComboBoxPersonalizado extends ComboBox {

    public static HashMap<String, Image> ListaImagenes;
    public static String[] Opciones;

    public ComboBoxPersonalizado(String[] Opciones, int seleccionado, HashMap<String, Image> ListaImagenes) {
        new ComboBox(FXCollections.observableArrayList(Opciones));

        //if (Opciones.length == ListaImagenes.size()) {
        setItems(FXCollections.observableArrayList(Opciones));
        this.setCellFactory(new IconosFactory());
        ComboBoxPersonalizado.ListaImagenes = ListaImagenes;
        ComboBoxPersonalizado.Opciones = Opciones;
        if (seleccionado >= 0) {
            this.getSelectionModel().select(seleccionado);
        }
    }

    public class IconosFactory implements Callback<ListView<String>, ListCell<String>> {
        @Override
        public ListCell<String> call(ListView<String> arg0) {
            return new IconoCell();
        }
    }

    public class IconoCell extends ListCell<String> {
        // "item" es cada una de las opciones que metimos en el combobox.
        @Override
        public void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setText(null);
                setGraphic(null);
            } else {
                setText(item);
                setGraphic(new ImageView(obtenerImagen(item)));
            }
        }

        /* Chequea si la imagen está incluida en el HashMap en base a 
        su nombre y, en tal caso, la devuelve */
        public Image obtenerImagen(String nombreImagen) {
            if (ListaImagenes.containsKey(nombreImagen)) {
                Image im = ListaImagenes.get(nombreImagen);
                return im;
            } else {
                return null;
            }
        }
    }
}
