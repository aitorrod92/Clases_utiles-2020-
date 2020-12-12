import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * Clase para obtener recursos de fuera o dentro del JAR, accediendo desde
 * distintos puntos.
 */
public class ObtenerRecursos {

    /**
     * Constructor sin parámetros, sólo utilizado para consultar fácilmente los
     * métodos.
     */
    public ObtenerRecursos() {

    }

    /**
     * Obtiene los archivos contenidos en un directorio externo.
     *
     * @param rutaDirectorio Ruta al directorio desde el root del classpath.
     * @param formatosAdmitidos Formatos de archivo admitidos.
     * @return Listado de File.
     */
    public ArrayList<File> ObtenerRecursosDirectorioFuera(String rutaDirectorio, String[] formatosAdmitidos) {
        ArrayList<File> archivosDirectorio = new ArrayList();
        File directorio = new File(rutaDirectorio);
        for (File archivo : directorio.listFiles()) {
            for (String formato : formatosAdmitidos) {
                if (archivo.getName().toLowerCase().endsWith(formato)) {
                    archivosDirectorio.add(archivo);
                }
            }
        }
        return archivosDirectorio;
    }

    /**
     * Obtiene un string con el contenido de un archivo de texto que está fuera
     * del JAR.
     *
     * @param archivo Archivo de texto a leer
     * @return String con el contenido del archivo.
     * @throws FileNotFoundException Si el archivo no existe.
     * @throws IOException Si el archivo no se puede leer.
     */
    public String ObtenerTextoFuera(File archivo) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        String string = "";
        String line = null;
        while ((line = br.readLine()) != null) {
            string = string.concat(line + "\n");
        }
        br.close();
        fr.close();
        return string;
    }

    /**
     * Obtiene el listado de inputstreams de un directorio dentro del JAR, los
     * cuales pueden ser utilizados para generar archivos de distinto tipo.
     *
     * @return Listado de InputStream.
     */
    public ArrayList<InputStream> ObtenerRecursosDirectorioDentro(String rutaDirectorio, Class clase, String[] formatosAdmitidos) {
        CodeSource src = clase.getProtectionDomain().getCodeSource();
        ArrayList<String> ArrayNombres = new ArrayList<>();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip;
            try {
                zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null) {
                        break;
                    }
                    if (e.getName().contains(rutaDirectorio) && !e.getName().endsWith(rutaDirectorio + "/")) {
                        for (String formato : formatosAdmitidos) {
                            if (e.getName().endsWith(formato)) {
                                ArrayNombres.add(e.getName().substring(e.getName().indexOf(rutaDirectorio.substring(rutaDirectorio.indexOf("/") + 1))));
                            }
                        }
                    }
                }
            } catch (IOException ex) {
                System.out.println("No se puede leer el JAR");
            }
        } else {
            System.out.println("No se ha podido acceder al directorio");
        }

        ArrayList<InputStream> ArrayInputs = new ArrayList<>();

        /**
         * Rellenamos ArraInputs
         */
        for (int i = 0; i < ArrayNombres.size(); i++) {
            InputStream input = this.getClass().getResourceAsStream(ArrayNombres.get(i));
            if (input != null) {
                ArrayInputs.add(input);
            }
        }
        return ArrayInputs;
    }

    /**
     * Obtiene un string con el contenido de un archivo de texto que está dentro
     * del JAR.
     *
     * @param rutaDirectorio Ruta desde la clase en la que se encuentra el
     * recurso.
     * @param clase Clase desde la que parte el @param rutaDirectorio.
     * @return
     */
    public String ObtenerTextoDentro(String rutaDirectorio, Class clase) {
        String string = "";
        InputStreamReader isr = new InputStreamReader(clase.getResourceAsStream(rutaDirectorio));
        BufferedReader br = new BufferedReader(isr);
        String line = null;
        try {
            while ((line = br.readLine()) != null) {
                string = string.concat(line + "\n");
            }
        } catch (IOException ex) {
            System.out.println("Error al leer el archivo");
        }
        return string;
    }

}
