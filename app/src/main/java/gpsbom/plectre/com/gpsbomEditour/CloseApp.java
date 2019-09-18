package gpsbom.plectre.com.gpsbomEditour;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

/**
 * Created by plectre on 12/07/17.
 */

public class CloseApp extends Dialog implements android.view.View.OnClickListener  {

    private Button ok;
    private Button cancel;

    public CloseApp(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_layout);
        ok = (Button) findViewById(R.id.btn_fermer);
        cancel = (Button) findViewById(R.id.btn_annuler);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_fermer:
                break;
            case R.id.btn_annuler:
                dismiss();
                break;
        }
    }
}
