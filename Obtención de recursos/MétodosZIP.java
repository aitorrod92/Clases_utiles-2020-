import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Proporciona diferentes métodos para trabajar con archivoz ZIP.
 */
public class MétodosZIP {

    public MétodosZIP() {

    }

    /**
     * Obtiene el listado de InputStreams y el listado de formatos
     * correspondientes a los archivos de cierto tipo de un ZIP.
     *
     * @param archivo El archivo comprimido.
     * @param formatosIncluidos String [] con los tipos de archivo que se quiere
     * obtener (no debe incluir el "."). Si se quiere utilizar todos, pasar
     * "null".
     * @return Mapa unielemento que contiene el ArrayList de InputStreams y el
     * ArrayList de nombres. El orden es coherente, por lo que se puede
     * reconstruir los archivos completamente a partir de ellos.
     * @throws java.io.IOException
     */
    public HashMap<ArrayList<InputStream>, ArrayList<String>> ObtenerRecursosDirectorio(File archivo, String[] formatosIncluidos) throws IOException {
        ZipFile zipfile = new ZipFile(archivo);
        HashMap<ArrayList<InputStream>, ArrayList<String>> MapaInputs = new HashMap<>();
        ArrayList<InputStream> arrayStreams = new ArrayList();
        ArrayList<String> arrayNombres = new ArrayList();
        ArrayList<String> formatos = null;
        if (formatosIncluidos != null) {
             formatos = new ArrayList(Arrays.asList(formatosIncluidos));
        }
        Enumeration<? extends ZipEntry> entradas = zipfile.entries();
        try {
            while (true) {
                try {
                    ZipEntry entrada = entradas.nextElement();
                    String nombreEntrada = entrada.getName();
                    InputStream is = zipfile.getInputStream(entrada);
                    if (formatosIncluidos != null) {
                        String formatoEntrada = nombreEntrada.substring(nombreEntrada.indexOf(".") + 1, nombreEntrada.length());
                        if (formatos.contains(formatoEntrada)) {
                            IncluirInputYNombre(arrayStreams, is, arrayNombres, nombreEntrada);
                        }
                    } else {
                        IncluirInputYNombre(arrayStreams, is, arrayNombres, nombreEntrada);
                    }
                } catch (NoSuchElementException e) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("No se puede leer el archivo comprimido");
        }
        MapaInputs.put(arrayStreams, arrayNombres);
        return MapaInputs;
    }

    private void IncluirInputYNombre(ArrayList<InputStream> arrayStreams, InputStream is, ArrayList<String> arrayNombres, String nombreEntrada) {
        arrayStreams.add(is);
        arrayNombres.add(nombreEntrada);
    }

    /**
     * Permite extraer los archivos comprimidos de ciertos tipos de un ZIP.El
     * HashMap que requiere como parámetro puede ser obtenido mediante
     * "ObtenerRecursosDirectorio".
     *
     * @param inputs Hashmap de ArrayList de InputStream y de ArrayList de
     * String que contiene los archivos y sus nombres, respectivamente.
     * @throws IOException
     */
    public void ExtraerArchivos(HashMap<ArrayList<InputStream>, ArrayList<String>> inputs) throws IOException {
        ArrayList<InputStream> arrayInputs = null;
        ArrayList<String> arrayStrings = null;
        for (ArrayList<InputStream> arrayList : inputs.keySet()) {
            arrayInputs = arrayList;
        }
        for (ArrayList<String> arrayString : inputs.values()) {
            arrayStrings = arrayString;
        }

        for (int i = 0; i < arrayInputs.size(); i++) {
            try {
                URI uri = new File(System.getProperty("user.dir").toString() + "\\" + arrayStrings.get(i)).toURI();
                Files.copy((InputStream) arrayInputs.get(i), Paths.get(uri), REPLACE_EXISTING);
            } catch (AccessDeniedException ex) {
                System.out.println("Acceso denegado para la extracción de los archivos");
            }

        }
    }

}
