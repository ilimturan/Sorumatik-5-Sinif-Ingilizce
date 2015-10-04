package zargidigames.com.sorumatik.ingilizce5.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;

/**
 * Created by ilimturan on 29/08/15.
 */
public class NetworkStateReceiver extends BroadcastReceiver {

    // post event if there is no Internet connection
    public void onReceive(Context context, Intent intent) {
        //super.onReceive(context, intent);
        if (intent.getExtras() != null) {
            NetworkInfo ni = (NetworkInfo) intent.getExtras().get(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
            if (ni != null && ni.getState() == NetworkInfo.State.CONNECTED) {
                // there is Internet connection
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                // no Internet connection, send network state changed
                EventBus.getDefault().post(new NetworkStateChanged(false));
            }
        }
    }
}