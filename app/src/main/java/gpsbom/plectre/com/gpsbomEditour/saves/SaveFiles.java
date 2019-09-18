package gpsbom.plectre.com.gpsbomEditour.saves;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import gpsbom.plectre.com.gpsbomEditour.KmlFactory;
import gpsbom.plectre.com.gpsbomEditour.LauncherActivity;
import gpsbom.plectre.com.gpsbomEditour.R;


/**
 * Created by plectre on 09/03/17
 * Classe de Gestion de dossier et
 * du fichier Appelé par SaveBox
 * E//storage/emulated/0/Gps Bom
 */

public class SaveFiles  {

    private String path;
    public static Boolean isCreate = false;
    public final String DIR = "/Service Bom";
    private File fFilePath;
    private File fichier;
    private File fichierPoints;
    private static String sFilePath;
    private static String fName;
    private String pNoirs;
    private static String nomFichierPoints;
    private Context context;
    private Activity activity;

    public String getNomFichierPoints() {
        return nomFichierPoints;
    }

    public String getfName() {
        return fName;
    }

    public String getFilePath() {
        return sFilePath;
    }

    public Boolean getIsCreate() {
        return isCreate;
    }


    public SaveFiles() {
    }
    public SaveFiles(Context c, Activity activity){
        this.context = c;
        this.activity = activity;
    }

    public void testCarteSd(String pName) {
        // Test Si la carte est presente
        String directory = Environment.getExternalStorageState();
        if (directory.equals(Environment.MEDIA_MOUNTED)) {
            //Log.i("la carte", "presente !");
            // On recupére le chemin du Dossier
            path = Environment.getExternalStorageDirectory().getPath();
            // Appel method de création du dossier
            createDir(pName);
            return;
        } else {

            Toast.makeText(context, "Pas de carte", Toast.LENGTH_SHORT).show();
        }
    }

    // Création du Dossier Service Bom
    public void createDir(String pName) {
        this.fName = pName;
        this.pNoirs = pName;
        this.fFilePath = new File(path + DIR);
        sFilePath = String.valueOf(fFilePath);

        if (!fFilePath.exists()) {
            fFilePath.mkdir();
            //Log.i("Dossier créer", "");
        }

        // Creation du fichier .kml
        fichier = new File(fFilePath + "/" + pName + ".kml");
        if (fichier.exists()) {
            //Dialog dialog = new Dialog(context);

            // Ouverture d'une alertDialog si le fichier existe déjà
            AlertBox alertBox = new AlertBox(context);
            alertBox.setTitle(" !! Attention !! ");
            alertBox.setMessage("Le "+pName+" existe déjà !. Il ne peut pas y avoir de doublons !");
            alertBox.setButton(AlertDialog.BUTTON_NEUTRAL, String.valueOf(context.getResources().getString(R.string.annuler)),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(activity, LauncherActivity.class);
                            dialog.dismiss();
                            activity.startActivity(i);
                            return;
                        }
                    });
            alertBox.show();
            return;

        } else {
            try {
                fichier.createNewFile();

                fName = pName + ".kml";
                Log.e("isCreate SaveFile", String.valueOf(isCreate));
                isCreate = true;

                Log.e("isCreate SaveFile", String.valueOf(isCreate));
                filePoints(pName);

                // instanciation de la classe kmlFactory et appel de la methode header
                // qui sauvegarde l'entête du kml
                //KmlFactory kmlFactory = new KmlFactory();
                //kmlFactory.headerKml();

            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        Log.e(sFilePath, "Existe déjà");

        sFilePath = String.valueOf(fFilePath);
        //Log.e(DIR, "Existe");
        //Log.e(String.valueOf(fFilePath), "fFilePath");
    }

    // Creation du fichier points noir
    public void filePoints(String pNoirs) {
        nomFichierPoints = pNoirs + "_Poi.kml";
        fichierPoints = new File(fFilePath + "/" + nomFichierPoints);
        try {
            fichierPoints.createNewFile();
            KmlFactory kmlFactory = new KmlFactory();
            kmlFactory.headerPoints(pNoirs);
        } catch (IOException e) {
            Log.e("APP", "Erreur ecriture filePoints " + e);
            e.printStackTrace();
        }
    }

    protected class AlertBox extends android.support.v7.app.AlertDialog {

        protected AlertBox(Context context) {
            super(context);

        }
    }

}
