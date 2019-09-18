package gpsbom.plectre.com.gpsbomEditour.saves;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gpsbom.plectre.com.gpsbomEditour.R;


/**
 * Created by plectre on 16/03/17.
 */


public class SavePoiBox extends Dialog implements View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button save, cancel;
    public EditText edTextName;
    public EditText edDescription;
    public Boolean receptData = false;
    private String fichierName;

    public SavePoiBox(@NonNull Activity activity) {
        super(activity);
        this.c = activity;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_poi_layout);
        save = (Button) findViewById(R.id.btn_saveFichierName);
        cancel = (Button) findViewById(R.id.btn_cancel);
        edTextName = (EditText) findViewById(R.id.ed_name);
        edDescription = (EditText) findViewById(R.id.ed_description);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    // Gestion des boutons cliqués
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saveFichierName:
                // Test si le champs est rempli
                if (edTextName.getText().toString().equals("")) {
                    dialogIsEmpty();
                } else {
                    edTexSaveOK(v);
                }
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            default:
                Log.e("Default", "Default");
                break;
        }
    }

    public void dialogIsEmpty() {
        Toast.makeText(getContext(), "Merci de nommer le service ...", Toast.LENGTH_LONG).show();
    }

    // Methode céation fichier
    public void edTexSaveOK(View v) {
        fichierName = String.valueOf(edTextName.getText());
        // Vérification des fichiers d'enregistrement
        SaveFiles saveDirectory = new SaveFiles();
        saveDirectory.testCarteSd(fichierName);
        dismiss();
        receptData = true;
    }

}