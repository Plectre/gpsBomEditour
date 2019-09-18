package gpsbom.plectre.com.gpsbomEditour.saves;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import gpsbom.plectre.com.gpsbomEditour.R;


/**
 * Created by plectre on 16/03/17.
 */


public class SaveBox extends Dialog implements android.view.View.OnClickListener {

    public Activity c;
    public Dialog d;
    public Button save, cancel;
    public EditText edText;
    public Boolean receptData = false;
    private String fichierName;


    public SaveBox(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);
        save = (Button) findViewById(R.id.btn_saveFichierName);
        cancel = (Button) findViewById(R.id.btn_cancel);
        edText = (EditText) findViewById(R.id.etSave);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    // Gestion des boutons cliqués
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_saveFichierName:
                // Test si le champs est rempli
                if (edText.getText().toString().equals("")) {
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
        Toast.makeText(getContext(), "Merci de nommer le fichier ...", Toast.LENGTH_LONG).show();
    }

    // Methode céation fichier
    public void edTexSaveOK(View v) {
        fichierName = String.valueOf(edText.getText());
        // Vérification des fichiers d'enregistrement
        SaveFiles saveDirectory = new SaveFiles(getContext(), c);
        saveDirectory.testCarteSd(fichierName);
        receptData = true;
        dismiss();
    }
}