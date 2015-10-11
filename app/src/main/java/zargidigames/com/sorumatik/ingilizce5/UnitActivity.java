package zargidigames.com.sorumatik.ingilizce5;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zargidigames.com.sorumatik.ingilizce5.zapi.adapter.UnitAdapter;
import zargidigames.com.sorumatik.ingilizce5.zlib.Network;
import zargidigames.com.sorumatik.ingilizce5.zapi.RestClient;
import zargidigames.com.sorumatik.ingilizce5.zapi.converter.UnitConverter;
import zargidigames.com.sorumatik.ingilizce5.zapi.models.Unit;

public class UnitActivity extends ActionBarActivity {

    private boolean networkStatus = false;
    private ProgressBar pbr;

    private String unitType;
    private List<Unit> units = new ArrayList<>();
    public String msg = "Boş";

    private ListView unitList;

    private Animation menuAnimationZoomIn;
    private Animation btnAnimation;

    private MediaPlayer menuSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AE1A20")));

        Intent intent = getIntent();
        unitType = intent.getStringExtra("UNIT_TYPE");

        unitList = (ListView) findViewById(R.id.unit_list);
        menuAnimationZoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.menu_zoom_in_1);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.btn_vibration_1);

        checkActiveNetwork();

        if (networkStatus) {
            try {
                getUnits();
            } catch (JSONException e) {
                //e.printStackTrace();
                showAlertDialog(getString(R.string.unit_load_error_title_1), getString(R.string.unit_load_error_text_1));
            }
        }

        pbr = (ProgressBar)findViewById(R.id.progress_bar_unit);
        pbr.setProgress(0);
        pbr.setVisibility(View.VISIBLE);
        loadSounds();

    }

    private void loadSounds(){
        menuSound = MediaPlayer.create(this, R.raw.collect);
        menuSound.start();
    }

    private void getUnits() throws JSONException {

        RestClient.getUnits("units_android/", null, new JsonHttpResponseHandler() {

            //@Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                showAlertDialog(getString(R.string.unit_load_error_title_1), getString(R.string.unit_load_error_text_1));
            }

            //@Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                if (statusCode == 200) {
                    UnitConverter unitConverter = new UnitConverter(UnitActivity.this);
                    units = unitConverter.jsonToUnitArrayList(response);

                    UnitAdapter unitAdapter = new UnitAdapter(UnitActivity.this, units);
                    unitList.setAdapter(unitAdapter);
                    unitList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            view.setAnimation(menuAnimationZoomIn);
                            Unit selectedUnit = units.get(position);
                            startTestActivity(selectedUnit);
                        }
                    });
                    pbr.setVisibility(View.GONE);

                } else {
                    showAlertDialog(getString(R.string.unit_load_error_title_1), getString(R.string.unit_load_error_text_1));
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {

                showAlertDialog(getString(R.string.unit_load_error_title_1), getString(R.string.unit_load_error_text_1));
            }
        });
    }

    public void startTestActivity(Unit unit) {
        Intent intent = new Intent(this, TestActivity.class);
        intent.putExtra("UNIT_TYPE", unitType);
        intent.putExtra("UNIT_ID", unit.id);
        intent.putExtra("UNIT_NAME", unit.name);
        startActivity(intent);
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
