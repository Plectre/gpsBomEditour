package gpsbom.plectre.com.gpsbomEditour.saves;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gpsbom.plectre.com.gpsbomEditour.MainActivity;

/**
 * Created by plectre on 31/03/17.
 * Classe servant à enregistrer sur le fichier les coordonnées
 * et appellé par MyReciever
 **/

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class SaveCoordinates {

    private String fName;
    private String path;
    private String NEW_LINE = System.lineSeparator();
    public String saveLat;
    private String saveLon;

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();
    }

    public void saveCoor(String pLat, String pLon) {
        Log.i("saveCoordinates", pLon + ":" + pLat);
        this.saveLat = pLat;
        this.saveLon = pLon;

        // Appel de la fonction qui récupére le chemin et le nom du fichier
        getDirPath();

        // Ecriture des coordonnées sur le fichier
        File file = new File(path, fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(pLat);
            output.write(",");
            output.append(pLon);
            output.write(NEW_LINE);

            output.close();
        } catch (IOException ex) {
        }
    }
}