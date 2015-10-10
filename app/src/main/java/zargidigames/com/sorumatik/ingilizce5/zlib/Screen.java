package zargidigames.com.sorumatik.ingilizce5.zlib;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by ilimturan on 08/10/15.
 */
public class Screen {

    public int screenHeightPixels;
    public int screenWidthPixels;
    public int screenDensityDpi;
    public float screenXdpi;
    public float screenYdpi;

    public Screen(Activity activity) {

        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeightPixels = metrics.heightPixels;
        screenWidthPixels = metrics.widthPixels;
        screenDensityDpi = metrics.densityDpi;
        screenXdpi = metrics.xdpi;
        screenYdpi = metrics.ydpi;

    }

    @Override
    public String toString() {
        return "Screen{" +
                "screenHeightPixels=" + screenHeightPixels +
                ", screenWidthPixels=" + screenWidthPixels +
                ", screenDensityDpi=" + screenDensityDpi +
                ", screenXdpi=" + screenXdpi +
                ", screenYdpi=" + screenYdpi +
                '}';
    }
}
