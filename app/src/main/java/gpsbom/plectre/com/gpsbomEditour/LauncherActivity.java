package gpsbom.plectre.com.gpsbomEditour;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import gpsbom.plectre.com.gpsbomEditour.saves.SaveBox;
import gpsbom.plectre.com.gpsbomEditour.saves.SaveFiles;

/**
 * Activitée Launcher
 * Instanciaion des Views et appel des méthodes de démarrage du GPS
 * Demarrage de l'activitée principale si le signal du GPS est acqui
 */

public class LauncherActivity extends AppCompatActivity {
    public String status_coord = "";
    private String signal;
    private Boolean fileOk;
    private TextView txt_status_gps;
    private TextView txt_location;
    private Boolean fileCreate = false;
    private Button btnSave;
    private Button btn_find_position;
    private SeekBar sb_location;
    private TextView txt_titre;
    private float updateLocation;

    static LauncherActivity launcherActivity;

    // methode appelée par MainActivity.closeApp qui kill l'activity
    public static LauncherActivity getInstance() {
        return launcherActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        launcherActivity = this;

        setContentView(R.layout.activity_launcher);
        // Vérouillage de la vue en position portrait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // Initialisation des views
        btnSave = (Button) findViewById(R.id.btn_save);
        btn_find_position = (Button) findViewById(R.id.btn_fin_position);
        txt_titre = (TextView) findViewById(R.id.txt_titre);
        txt_status_gps = (TextView) findViewById(R.id.txt_gps_status);
        txt_location = (TextView) findViewById(R.id.txt_location);
        //txt_time = (TextView) findViewById(R.id.txt_time);
        sb_location = (SeekBar) findViewById(R.id.sb_location);
        //sb_time = (SeekBar) findViewById(R.id.sb_time);

        init();

        // Manipulation de la seekBar location
        sb_location.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    seekBar.setMin(25);
                    seekBar.setMax(50);
                } else {

                    seekBar.setMax(50);
                    seekBar.setMin(25);

                }
                updateLocation = (float) progress;
                // Récupération de la string formatée dans R.string
                // string>%progress km/h</string>
                typeDeCircuit(progress);
                //txt_location.setText(getString(R.string.concat_km_h, progress));
                Log.d("progress bar", String.valueOf(progress));
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // Appel de la boite de dialogue sauvegarde
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveBox sBox = new SaveBox(LauncherActivity.this);
                sBox.show();
                btn_find_position.setVisibility(View.VISIBLE);
                btnSave.setVisibility(View.INVISIBLE);
            }
        });

        // Bouton demarrageService GPS
        btn_find_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveFiles sf = new SaveFiles();
                fileCreate = sf.getIsCreate();
                btnSave.setEnabled(false);
                if (!fileCreate) {
                    return;
                } else {
                    StartGps();
                }
                txt_status_gps.setText(R.string.status_gps);
                btnSave.setVisibility(View.INVISIBLE);
                btn_find_position.setEnabled(false);
                //sb_time.setEnabled(false);
                sb_location.setEnabled(false);
            }
        });

    }

    @Override
    protected void onStart() {
        Log.e("Launcher onStart", String.valueOf(fileCreate));
        super.onStart();
        Log.i("Launcher on start", String.valueOf(fileCreate));
        coordOk();
    }

    public void StartGps() {
        /**
         *  Demarrage du service GPS et passage des valeurs de la SeekBar vers
         *  update_time et update_location
         */

        Log.e("Launcher iscreate,String", String.valueOf(fileCreate));
        GpsService gpsService = new GpsService();
        signal = gpsService.getIscoorOk();
        Intent intent = new Intent(this, GpsService.class);
        //intent de a vitesse de collecte
        intent.putExtra("update_location", updateLocation);
        startService(intent);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ACTIVITY ", "Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stopper le service GPS
        //stopService(new Intent(LauncherActivity.this, GpsService.class));
    }

    /**
     * Reception de l'intent de GpsService afin de savoir si
     * * la position est aquise
     */

    public String coordOk() {
        status_coord = "---------------";
        SaveFiles sf = new SaveFiles();
        this.fileOk = sf.getIsCreate();
        //fileOk = true;
        Log.e(String.valueOf(fileOk), "COOR DOK");
        Intent intent = getIntent();
        /** Si les coordonnées sont aquises et le fichier sauvgardé
         // Démarrage de l'activitée principale */

        if (intent.getStringExtra("txt_status_gps") != null) {
            status_coord = intent.getStringExtra("txt_status_gps");
            Log.e(status_coord, "isatatus coord");

            // Démmarage activitée principale
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);

        } else {
            return status_coord = "Position en cours d'acquisition ...";
        }
        return status_coord;
    }

    public void init() {

        txt_status_gps.setText(status_coord);
        btn_find_position.setVisibility(View.INVISIBLE);
        txt_titre.setText(R.string.app_name);
        txt_location.setText(R.string.hyper_urbain);
    }

    public void typeDeCircuit(int i) {
        if (i >= 25 && i <= 35) {
            txt_location.setText(R.string.hyper_urbain);
        } else if (i > 35 && i <= 45) {
            txt_location.setText(R.string.urbain);
        } else if (i > 45) {
            txt_location.setText(R.string.rural);
        }
    }
}