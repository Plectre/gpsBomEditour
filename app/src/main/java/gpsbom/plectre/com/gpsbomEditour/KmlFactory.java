package gpsbom.plectre.com.gpsbomEditour;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import gpsbom.plectre.com.gpsbomEditour.saves.SaveFiles;

/**
 * Created by plectre on 04/04/17.
 * Classe se chargeant de construire la syntaxe Kml
 */

@TargetApi(Build.VERSION_CODES.KITKAT)
public class KmlFactory extends Activity {
    public Context context;
    private String fName;
    private String path;
    private String DEBUT = "Debut";
    private String FIN = "Fin";
    private String NEW_LINE = System.lineSeparator();
    //private String kml;
    private String pointFichierName;


    // Header kml
    public void headerKml(String lat, String lon) {
        getDirPath();

        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE
                + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" + NEW_LINE
                + "<Document>" + NEW_LINE
                + "<name>" + fName + "</name>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                + "<name>" +DEBUT+"</name>" + NEW_LINE
                + "<Point>" + NEW_LINE
                + "<coordinates>" + NEW_LINE
                + lon + "," + lat + NEW_LINE
                + "</coordinates>" + NEW_LINE
                + "</Point>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "<altitudeMode>clampToGround</altitudeMode>" + NEW_LINE
                + "<styleUrl></styleUrl>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                + "<LineString>" + NEW_LINE
                + "<coordinates>";
        saveKml(header);
    }


    // Body kml
    public void setKml(String typeCollecte) {
        // Recuperation des coordonnées afin d'eviter les
        // /coupures du circuit lors du changement de type de collecte


       String kml =   "</coordinates>"
                + NEW_LINE
                + "</LineString>"
                + NEW_LINE
                + "</Placemark>"
                + NEW_LINE
                + "<Placemark>"
                + NEW_LINE
                + "<name>" + typeCollecte + "</name>"
                + NEW_LINE +
                "<styleUrl>" + "</styleUrl>"
                + NEW_LINE +
                "<LineString>"
                + NEW_LINE +
                "<tesselate>1</tesselate>"
                + NEW_LINE
                + "<coordinates>";
                //+ NEW_LINE;
                //+ lon + "," + lat;
        saveKml(kml);
        Log.i("KML_Factory", kml);
    }

    // Footer
    public void footerKml(String lat, String lon) {
        String footer = "</coordinates>"+ NEW_LINE
                + "</LineString>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "<Placemark>" + NEW_LINE
                + "<name>" + FIN + "</name>" + NEW_LINE
                + "<Point>" + NEW_LINE
                + "<coordinates>" + NEW_LINE
                + lon +","+ lat + NEW_LINE
                + "</coordinates>" + NEW_LINE
                + "</Point>" + NEW_LINE
                + "</Placemark>" + NEW_LINE
                + "</Document>" + NEW_LINE
                + "</kml>";
        saveKml(footer);
        Log.e("FOOTEERRR" , "appel footer");
    }

    // Header Points
    public void headerPoints(String nomDuFichier) {
        String headerPoints = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + NEW_LINE
                + "<kml xmlns=\"http://www.opengis.net/kml/2.2\">" + NEW_LINE
                + "<Document>" + NEW_LINE
                + "<name>" + nomDuFichier + "</name>";
        saveKmlPoints(headerPoints);
    }
    // Coordonnées Point noir
    public void blackPoint(String lat, String lon) {

        //SavePoiBox savePoiBox = new SavePoiBox(this);
        //savePoiBox.show();

        String blackPoint = "<Placemark>" + NEW_LINE
                + "<name>poi</name>" + NEW_LINE
                + "<Point>" + NEW_LINE
                + "<coordinates>" + NEW_LINE
                + lon + "," +lat + NEW_LINE
                + "</coordinates>" + NEW_LINE
                + "</Point>" + NEW_LINE
                + "</Placemark>";
        saveKmlPoints(blackPoint);
    }
    public void footerPoint() {

        String footer = "</Document>" + NEW_LINE
                        + "</kml>";
        saveKmlPoints(footer);
    }

    // On recupere nom du dossier créer dans la classe SaveFile()
    public void getDirPath() {
        SaveFiles sf = new SaveFiles();
        //this.fDossier = sf.getFilePath();
        this.fName = sf.getfName();
        this.path = sf.getFilePath();
    }
    public void getFichierPoints(){
        SaveFiles sf = new SaveFiles();
        this.path = sf.getFilePath();
        this.pointFichierName = sf.getNomFichierPoints();
    }

    public void saveKml(String kmlpart) {
        // Appel de la fonction qui récupére le chemin et le nom du fichier
        getDirPath();
        // Ecriture des coordonnées sur le fichier
        File file = new File(path, fName);
        try {
            FileWriter output = new FileWriter(file, true);
            output.append(kmlpart);
            output.write(NEW_LINE);

            //Log.i("Enregistrement Ok",String.valueOf(fName));
            output.close();
        } catch (IOException ex) {
            Log.e("Enregistrement fail", String.valueOf(ex));
        }
    }

    public void saveKmlPoints(String kmlPart) {
        getFichierPoints();
        SaveFiles sf = new SaveFiles();
        pointFichierName = sf.getNomFichierPoints();
        File file = new File(path, pointFichierName);
        try{
            FileWriter output = new FileWriter (file, true);
            output.append(kmlPart);
            output.write(NEW_LINE);
            output.close();
            Log.i("APP", "ecriture header point" + path + "/" +pointFichierName + " _ " + kmlPart);
        } catch (IOException ex){
            Log.e("APP", "erreur ecriture header points");
        }
    }
}