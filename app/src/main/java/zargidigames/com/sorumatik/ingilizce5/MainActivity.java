package zargidigames.com.sorumatik.ingilizce5;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import zargidigames.com.sorumatik.ingilizce5.config.AppConfig;
import zargidigames.com.sorumatik.ingilizce5.zlib.Network;
import zargidigames.com.sorumatik.ingilizce5.zlib.Screen;


public class MainActivity extends ActionBarActivity {

    private boolean networkStatus = false;
    public Context context;

    private RelativeLayout menuRelativeLayout1;
    private RelativeLayout menuRelativeLayout2;
    private RelativeLayout menuRelativeLayout3;

    private LinearLayout menuLinearLayout1;
    private ImageView menuImageView1;
    private ImageView menuImageView2;
    private ImageButton menuBtnLearn;
    private ImageButton menuBtnTest;

    private Animation menuAnimationZoomIn;
    private Animation menuAnimationZoomOut;
    private Animation btnAnimation;

    private MediaPlayer menuSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        menuRelativeLayout1 = (RelativeLayout) findViewById(R.id.mm_rl_1);
        menuRelativeLayout2 = (RelativeLayout) findViewById(R.id.mm_rl_3);
        menuRelativeLayout3 = (RelativeLayout) findViewById(R.id.mm_rl_2);
        menuLinearLayout1 = (LinearLayout) findViewById(R.id.mm_ll_1);
        menuImageView1 = (ImageView) findViewById(R.id.mm_iv_1);
        menuImageView2 = (ImageView) findViewById(R.id.mm_iv_2);
        menuBtnLearn = (ImageButton) findViewById(R.id.mm_btn_learn);
        menuBtnTest = (ImageButton) findViewById(R.id.mm_btn_test);
        setScreenElementSizes();

        menuAnimationZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_zoom_in_1);
        menuAnimationZoomOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_zoom_out_1);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_vibration_1);

        //menuRelativeLayout1.startAnimation(menuAnimationZoomIn);
        menuLinearLayout1.startAnimation(menuAnimationZoomIn);
        menuBtnLearn.startAnimation(btnAnimation);
        btnAnimation.setDuration(413);
        menuBtnTest.startAnimation(btnAnimation);

        context = getApplicationContext();
        checkActiveNetwork();
        loadSounds();
    }

    private void loadSounds() {
        menuSound = MediaPlayer.create(this, R.raw.collect);
        menuSound.start();
    }

    public void mainMenuButtonClick(View v) {
        switch (v.getId()) {
            case R.id.mm_btn_learn:
                startUnitActivity("unit_learn");
                break;
            case R.id.mm_btn_test:
                startUnitActivity("unit_test");
                break;
            default:
                break;
        }
    }

    public void startUnitActivity(String unitType) {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("UNIT_TYPE", unitType);
        startActivity(intent);
    }

    private void setScreenElementSizes() {

        if(!AppConfig.screenSetSize) return;

        Screen screenInfo = new Screen(this);
        double oWidth = (double) screenInfo.screenWidthPixels / AppConfig.screenOrjWidth;
        double oHeight = (double) screenInfo.screenHeightPixels / AppConfig.screenOrjHeight;
        double oWidthHeight = oWidth / oHeight;

        menuImageView1.getLayoutParams().width = (int) (menuImageView1.getLayoutParams().width * oWidth);
        menuImageView1.getLayoutParams().height = (int) (menuImageView1.getLayoutParams().height * oWidth);

        menuImageView2.getLayoutParams().width = (int) (menuImageView2.getLayoutParams().width * oWidth);
        menuImageView2.getLayoutParams().height = (int) (menuImageView2.getLayoutParams().height * oWidth);

        menuLinearLayout1.getLayoutParams().width = (int) (menuLinearLayout1.getLayoutParams().width * oWidth);
        menuLinearLayout1.getLayoutParams().height = (int) (menuLinearLayout1.getLayoutParams().height * oWidth);

        menuBtnLearn.getLayoutParams().width = (int) (menuBtnLearn.getLayoutParams().width * oWidth);
        menuBtnLearn.getLayoutParams().height = (int) (menuBtnLearn.getLayoutParams().height * oWidth);
        //TODO set margins

        menuBtnTest.getLayoutParams().width = (int) (menuBtnTest.getLayoutParams().width * oWidth);
        menuBtnTest.getLayoutParams().height = (int) (menuBtnTest.getLayoutParams().height * oWidth);
        //TODO set margins

    }


    private void checkActiveNetwork() {

        networkStatus = Network.getNetworkStatus(this);

        if (networkStatus) {
            //Connection is ok
        } else {
            showAlertDialog(getString(R.string.connection_title_error), getString(R.string.connection_text_error));
        }
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(getString(R.string.alert_positive_button), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //checkActiveNetwork();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return false;
    }


}
