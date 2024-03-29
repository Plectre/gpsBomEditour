package gpsbom.plectre.com.gpsbomEditour;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import gpsbom.plectre.com.gpsbomEditour.utils.Cap;

/**
 * Created by plectre on 08/03/17.
 * Classe Service se charge du GPS et envoi
 * les positions au Broadcast reciever qui les enregistre
 * dans la mémoire Externe du device
 */

public class GpsService extends Service {

    private LocationManager locationMgr = null;

    private Boolean firstCoorInbound = true;

    private int vitesseDeCollecte;


    private double latitude;
    private double longitude;
    private float bearing;
    private float accuracy;

    private String isCoordOK = "Position en cours !";

    private Boolean isFirstime = false; // boolean qui verifie si le toast dispo à etait affiché

    public String getIscoorOk() {
        return isCoordOK;
    }

    /**
     * Intent se chargeant d'envoyer à MainActivity
     * le fait d'avoir recus les premiéres Coordonnées
     */
    private void intentStatusPosition() {
        if (firstCoorInbound) {
            isCoordOK = "Position aquise !";
            KmlFactory kml = new KmlFactory();
            kml.headerKml(String.valueOf(latitude), String.valueOf(longitude));

            Log.i("Appel", "Intent");
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("lati", String.valueOf(latitude));
            intent.putExtra("longi", String.valueOf(longitude));

            startActivity(intent);
        }
        firstCoorInbound = false;
    }


    private LocationListener onLocationChange = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            // Get datas from GPS device

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            bearing = location.getBearing();
            accuracy = location.getAccuracy();
            Log.e("Bearing", ":" + bearing);
            int intAccuracy = (int) accuracy;


            //int intBearing = (int) bearing;
            // A la premiere aquisition de la position
            // Appel de la méthode intentStatusPosition
            if (firstCoorInbound) {
                //Log.e("Premiere", "Reception");
                intentStatusPosition();
            }


            /** Envoyer les données aux classe abonnées (MyReciever.class) par l'intermediare
             *   d'un Broadcast
             * Envoyer les données avec un put StringExtra à l'activitée principale afin de renseigner les TextView
             * txt_lat et txt_lon
             */

            Intent intent = new Intent(getApplicationContext(), MyReciever.class);
            intent.setAction("com.bom.service");
            //intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            String str_lat = String.valueOf(latitude);
            String str_lon = String.valueOf(longitude);
            String str_accuracy = String.valueOf(intAccuracy);
            // Appel methode pour formatage du cap pour affichage...
            Cap cap = new Cap();
            String st_bearing = cap.formatBearing(bearing);
            intent.putExtra("lat", str_lat);                    // Latitude
            intent.putExtra("lon", str_lon);                    // longitude
            intent.putExtra("accuracy", str_accuracy);          // Précision
            intent.putExtra("str_bearing", st_bearing);         // Cap pour affichage
            intent.putExtra("float_bearing", bearing);          // Cap
            Log.e("GpsService_st_bearing", st_bearing);
            //Log.e("GpsService_precision", str_accuracy);
            sendBroadcast(intent);
        }

        // Methode appelée par onLocationChanged si le step entre deux points et depassé
        private void sendToSave(String typeDeCollecte) {
            KmlFactory kmlFactory = new KmlFactory();
            //kmlFactory.setKml(typeDeCollecte, String.valueOf(latitude), String.valueOf(longitude));
            kmlFactory.setKml(typeDeCollecte);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String newStatus = null;

            switch (status) {
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    newStatus = " temporairement innactif";
                    isFirstime = false;
                    break;
                case LocationProvider.AVAILABLE:
                    newStatus = " disponible";
                    isFirstime = true;
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    newStatus = " hors service";
                    isFirstime = false;
                    break;
            }
            if (isFirstime = false) {
                Toast.makeText(getBaseContext(), provider + newStatus, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(getBaseContext(), "GPS actif", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getBaseContext(), "GPS innactif", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getBaseContext(),
                "Fin de service \"GPS\" ", Toast.LENGTH_LONG).show();
        Log.e("Appel fin service", "GPS");
        // On se desabonne du service GPS
        locationMgr.removeUpdates(onLocationChange);
    }

    // Methode appellée au demarrage du service
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Récupération des données envoyées par launcherActivity

        if (intent != null) {
            //Récupération de la valeur de la seekBar
            float seekBarProgress = intent.getFloatExtra("update_location", 3);

            if(seekBarProgress <= 0) {
                seekBarProgress = 25;
            }

            vitesseDeCollecte = Math.round(seekBarProgress);
            seekBarProgress = vitesseDeCollecte * 200;
            vitesseDeCollecte *= 3.5;

            abonementGps((long) seekBarProgress, vitesseDeCollecte);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void abonementGps(long time, float distanceMetre) {

        locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // gestion des permissions utilisateur
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        /*
        * Calcul de la distance en métres (vitesse de collecte * 4)
        * Calcul du temps entre chaque récupération des points (seekBar * 30)
         */
        //int maxTimeToPlot = (int) (seekBarProgress * 30);
        Log.i("GPS service", "time " + time);
        Log.i("APP", "distanceMetre " + distanceMetre);
        //requestLocationUpdates(String provider, long minTime, float mniDistance, Locationlistener listener)
        locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, time,
                distanceMetre, onLocationChange);
    }
}
