package gpsbom.plectre.com.gpsbomEditour.utils;

import android.util.Log;

public class OldCoords {

    private boolean isReady = false;
    private static String oldLat = "000";
    private String currentLat;
    private String oldLon;


    public void setLat( String lat) {
        //Log.i("oldLat", String.valueOf(this.oldLat));
        this.currentLat = lat;
        if (!oldLat.equals(this.currentLat)) {
            this.oldLat = this.currentLat;
            isReady = true;
            //Log.i("oldLatAfter", String.valueOf(this.oldLat));
        }
    }

    public Boolean isDoublon() {
        return this.isReady;
    }
}
