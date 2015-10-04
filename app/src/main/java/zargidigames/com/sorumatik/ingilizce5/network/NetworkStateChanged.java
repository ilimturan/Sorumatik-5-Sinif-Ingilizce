package zargidigames.com.sorumatik.ingilizce5.network;

/**
 * Created by ilimturan on 29/08/15.
 */
public class NetworkStateChanged {

    private boolean mIsInternetConnected;

    public NetworkStateChanged(boolean isInternetConnected) {
        this.mIsInternetConnected = isInternetConnected;
    }

    public boolean isInternetConnected() {
        return this.mIsInternetConnected;
    }

}
