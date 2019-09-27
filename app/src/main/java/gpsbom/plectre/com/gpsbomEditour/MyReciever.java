package gpsbom.plectre.com.gpsbomEditour;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import gpsbom.plectre.com.gpsbomEditour.saves.SaveCoordinates;
import gpsbom.plectre.com.gpsbomEditour.saves.SaveFiles;
import gpsbom.plectre.com.gpsbomEditour.utils.Cap;
import gpsbom.plectre.com.gpsbomEditour.utils.OldCoords;

/**
 * Created by plectre on 09/03/17.
 * Classe etendue de BroadcastReceiver qui récupére les positions et
 * appel de la class SaveCoordinates
 * qui se charge elle de les enregistrer sur
 * la mémoire Externe du device
 */


public class MyReciever extends BroadcastReceiver {

    private String lat ="lat";
    private String lon ="";
    private String oldLat = "oldLat";
    private String oldLon = "oldLon";
    private String accuracy;
    private String bearing;
    private String speed;
    private float float_bearing;
    private Boolean fileIsOk;
    private Boolean recIsOn;
    private Boolean isDelayOk;

    public void isFileOk() {
        SaveFiles sf = new SaveFiles();
        this.fileIsOk = sf.getIsCreate();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void saveCoordinates(String lon, String lat) {
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
            this.isDelayOk = intent.getBooleanExtra("isDelayOk", false);
        }
    }

    private boolean isReadyToSave() {
        OldCoords oldCoords = new OldCoords();
        oldCoords.setLat(lat);
        boolean isReady  = oldCoords.isDoublon();
        Log.i("isReadyToSave ", String.valueOf(isReady));
        return isReady;
    }

    // Methode implementée par BroadcastReciever
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onReceive(Context context, Intent intent) {
        isFileOk();
        getIntent(intent);
        this.recIsOn = MainActivity.recIsOn;
        boolean isRadioButtonChecked = MainActivity.isRadioButtonEnable;
        boolean isCapChanged = changeCap();
        /* Si le bouton d'enregistrement est sur REC
        / && que le fichier est créé
        / && un radio button est checked
        / && si une latitude differente*/
        if (recIsOn && fileIsOk && isRadioButtonChecked && isReadyToSave()) {
        // Si la coordonée reçue est un changement de cap alors on sauvegarde le point
            if (isCapChanged) {
                saveCoordinates(lon, lat);
            } else if (isDelayOk) {
                    saveCoordinates(lon, lat);
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.setLat(lat, lon, accuracy, bearing);


            }
        }
    }
}