package zargidigames.com.sorumatik.ingilizce5.zlib;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ilimturan on 07/10/15.
 */
public class Network {

    public static boolean networkStatus = false;
    public static int networkType = 0;

    public static boolean getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {

            networkStatus = true;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                networkType = 1; //Wifi
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                networkType = 2; //Mobile
            } else {
                networkType = 3; //Other
            }
        } else {
            networkStatus = false; //No connect
            networkType = 0; //No type
        }

        return networkStatus;
    }

    public static int getNetworkType() {

        return networkType;
    }
}
