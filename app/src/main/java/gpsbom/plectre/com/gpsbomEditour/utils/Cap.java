
// Si changement de cap on ajoute un point GPS

package gpsbom.plectre.com.gpsbomEditour.utils;

public class Cap {

    private boolean isCapChange = false;
    private float currentCap;
    private float oldCap;

    public Cap(float currentCap) {
        this.currentCap = currentCap;
    }

    public Cap() {
    }

    public boolean delta() {

        if (currentCap != oldCap) {
            isCapChange = true;
            oldCap = currentCap;
        } else {
            isCapChange = false;
        }
        return isCapChange;
    }

    public String formatBearing(float bearing) {

        float formatBearing = bearing;

        if (formatBearing >= 0.0 && formatBearing <= 22.5) {
            return "N";
        }
        if (formatBearing > 22.5 && formatBearing <= 67.5) {
            return "NNE";
        }
        if (formatBearing > 67.5 && formatBearing <= 112.5) {
            return "E";
        }
        if (formatBearing > 112.5 && formatBearing <= 157.5) {
            return "SSE";
        }
        if (formatBearing > 157.55 && formatBearing <= 202.5) {
            return "S";
        }
        if (formatBearing > 202.5 && formatBearing <= 247.5) {
            return "SSW";
        }
        if (formatBearing > 247.5 && formatBearing <= 292.5) {
            return "W";
        }
        if (formatBearing > 292.5 && formatBearing <= 337.5) {
            return "NNW";
        }

        if (formatBearing > 337.5 && formatBearing <= 359.9) {
            return "N";
        }

        return "__";
    }
}
