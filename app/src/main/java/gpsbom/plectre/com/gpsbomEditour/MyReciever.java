package gpsbom.plectre.com.gpsbomEditour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import gpsbom.plectre.com.gpsbomEditour.saves.SaveCoordinates;
import gpsbom.plectre.com.gpsbomEditour.saves.SaveFiles;
import gpsbom.plectre.com.gpsbomEditour.utils.Cap;

/**
 * Created by plectre on 09/03/17.
 * Classe etendue de BroadcastReceiver qui récupére les positions et
 * appel de la class SaveCoordinates
 * qui se charge elle de les enregistrer sur
 * la mémoire Externe du device
 */


public class MyReciever extends BroadcastReceiver {

    private String lat;
    private String lon;
    private String accuracy;
    private String bearing;
    private String speed;
    private float float_bearing;
    private Boolean fileIsOk;
    private Boolean recIsOn;

    public void isFileOk() {
        SaveFiles sf = new SaveFiles();
        this.fileIsOk = sf.getIsCreate();
    }

    private void saveCoordinates(String lon, String lat) {
        Log.i("saveCoordinates", lon + ":" + lat);
        SaveCoordinates sc = new SaveCoordinates();
        sc.saveCoor(lon, lat);
    }

    private boolean changeCap() {

        Cap cap = new Cap(float_bearing);
        boolean isCapChange = cap.delta();
        if (isCapChange == true
                && recIsOn == true
                && bearing != null
                && lat != null
                && lon != null
                && accuracy != null) {
            MainActivity ma = new MainActivity();
            ma.setLat(lat, lon, accuracy, bearing);
        }
        return isCapChange;
    }

    public void getIntent(Intent intent) {
        if (intent != null) {
            // Récuperation des coordonnées envoyé par GpsService
            this.lat = intent.getStringExtra("lat");
            this.lon = intent.getStringExtra("lon");
            this.accuracy = intent.getStringExtra("accuracy");
            this.bearing = intent.getStringExtra("str_bearing");
            this.speed = intent.getStringExtra("speed");
            this.float_bearing = intent.getFloatExtra("float_bearing", 0f);
        }

    }

    // Methode implementée par BroadcastReciever
    public void onReceive(Context context, Intent intent) {

        this.recIsOn = MainActivity.recIsOn;
        getIntent(intent);
        boolean isCapChanged = changeCap();
        // Si la coordonée reçue est un changement de cap alors on sauvgarde le point
        if (isCapChanged == true) {
            saveCoordinates(lon, lat);
        } else {
            //Log.i("pas de changement", "DE CAP !");
            //Log.i("MyReceiver", lat + ":" + lon);
            //Log.e("MyReceiver_cap Changed", String.valueOf(float_bearing));


            // Si le bouton de MAinActivity en sur enregistrement
            // on ecrit les coordonnées sur le fichier
            if (recIsOn == true) {
                isFileOk();

                // Si le fichier est sauvegarder on enregistre les
                // coordonnées
                if (fileIsOk) {

                    saveCoordinates(lon, lat);

                    MainActivity mainActivity = new MainActivity();
                    mainActivity.setLat(lat, lon, accuracy, bearing);
                }
            }
        }
    }
}