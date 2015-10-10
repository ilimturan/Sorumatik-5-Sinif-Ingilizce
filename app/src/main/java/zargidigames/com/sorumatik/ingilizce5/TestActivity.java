package zargidigames.com.sorumatik.ingilizce5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import zargidigames.com.sorumatik.ingilizce5.config.AppConfig;
import zargidigames.com.sorumatik.ingilizce5.zapi.RestClient;
import zargidigames.com.sorumatik.ingilizce5.zapi.converter.WordConverter;
import zargidigames.com.sorumatik.ingilizce5.zapi.models.Word;
import zargidigames.com.sorumatik.ingilizce5.zlib.Network;
import zargidigames.com.sorumatik.ingilizce5.zlib.Screen;

public class TestActivity extends ActionBarActivity {

    private boolean networkStatus = false;
    private boolean testIsReady = false;
    private int wordIndex = 0;
    private int wordMaxIndex = 0;

    private String unitType;
    private int unitId;
    private String unitName;
    private String barTitle;

    private List<Word> words = new ArrayList<>();
    private TextToSpeech tts;

    private ProgressBar pbr;
    private TextView wordTurkish;
    private ImageView wordImage;
    private Button wordEnglish1;
    private Button wordEnglish2;
    private ImageButton wordSoundTest1;
    private ImageButton wordSoundTest2;
    private ImageButton wordSoundLearn1;
    private ImageView wordResult1;
    private ImageView wordResult2;
    private ImageButton wordBack;
    private ImageButton wordNext;


    private LinearLayout contentLinearLayout1;
    private LinearLayout contentLinearLayout2;
    private LinearLayout contentLinearLayout3;
    private LinearLayout contentLinearLayout4;
    private LinearLayout contentLinearLayout5;

    private MediaPlayer menuSound;

    private RelativeLayout alertRelativeLayout;
    private ImageView alertImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        unitType = intent.getStringExtra("UNIT_TYPE");
        unitId = intent.getIntExtra("UNIT_ID", 0);
        unitName = intent.getStringExtra("UNIT_NAME");

        pbr = (ProgressBar) findViewById(R.id.progress_bar_test);
        wordTurkish = (TextView) findViewById(R.id.txt_word_tukish);
        wordImage = (ImageView) findViewById(R.id.iv_image);
        wordEnglish1 = (Button) findViewById(R.id.btn_word_english_1);
        wordEnglish2 = (Button) findViewById(R.id.btn_word_english_2);
        wordSoundTest1 = (ImageButton) findViewById(R.id.ib_sound_test_1);
        wordSoundTest2 = (ImageButton) findViewById(R.id.ib_sound_test_2);
        wordSoundLearn1 = (ImageButton) findViewById(R.id.ib_sound_learn_1);
        wordResult1 = (ImageView) findViewById(R.id.iv_result_1);
        wordResult2 = (ImageView) findViewById(R.id.iv_result_2);
        wordBack = (ImageButton) findViewById(R.id.btn_back);
        wordNext = (ImageButton) findViewById(R.id.btn_next);

        contentLinearLayout1 = (LinearLayout) findViewById(R.id.test_ll_c_1);
        contentLinearLayout2 = (LinearLayout) findViewById(R.id.test_ll_c_2);
        contentLinearLayout3 = (LinearLayout) findViewById(R.id.test_ll_c_3);
        contentLinearLayout4 = (LinearLayout) findViewById(R.id.test_ll_c_4);
        contentLinearLayout5 = (LinearLayout) findViewById(R.id.test_ll_c_5);

        alertRelativeLayout = (RelativeLayout) findViewById(R.id.alert_rl);
        alertImageView = (ImageView) findViewById(R.id.alert_iv);

        checkActiveNetwork();
        if (networkStatus) {
            try {
                getTests(unitId);
            } catch (JSONException e) {
                e.printStackTrace();
                showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
            }
        }
        pbr.setProgress(0);
        pbr.setVisibility(View.VISIBLE);

        if (unitType.equals("unit_learn")) {
            loadScreenDefaultForLearn();
        } else if (unitType.equals("unit_test")) {
            loadScreenDefaultForTest();
        } else {
            startMainActivity();
        }

        setScreenElementSizes();
        loadSounds();
        loadAlertAnimation(2);

    }

    private void loadScreenDefaultForLearn() {

        wordResult1.setVisibility(View.GONE);
        wordResult2.setVisibility(View.GONE);

        barTitle = getString(R.string.title_learn) + " " + unitName;
        wordSoundTest1.setVisibility(View.GONE);
        wordSoundTest2.setVisibility(View.GONE);
        wordSoundLearn1.setVisibility(View.VISIBLE);
        contentLinearLayout4.setVisibility(View.GONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Ok
        } else {
            wordSoundTest1.setVisibility(View.GONE);
            wordSoundTest2.setVisibility(View.GONE);
            wordSoundLearn1.setVisibility(View.GONE);
        }

        setTitle(barTitle);
    }


    private void loadScreenDefaultForTest() {

        wordResult1.setVisibility(View.GONE);
        wordResult2.setVisibility(View.GONE);

        barTitle = getString(R.string.title_test) + " " + unitName;
        wordSoundTest1.setVisibility(View.VISIBLE);
        wordSoundTest2.setVisibility(View.VISIBLE);
        wordSoundLearn1.setVisibility(View.GONE);
        contentLinearLayout5.setVisibility(View.GONE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Ok
        } else {
            wordSoundTest1.setVisibility(View.GONE);
            wordSoundTest2.setVisibility(View.GONE);
            wordSoundLearn1.setVisibility(View.GONE);
        }

        setTitle(barTitle);
    }

    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadSounds() {
        menuSound = MediaPlayer.create(this, R.raw.collect);
        menuSound.start();
    }

    private void getTests(int unitId) throws JSONException {

        RestClient.getWords("unit_words_android/" + unitId, null, new JsonHttpResponseHandler() {

            //@Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONObject response) {
                showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
            }

            //@Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, JSONArray response) {
                if (statusCode == 200) {

                    testIsReady = true;
                    WordConverter wordConverter = new WordConverter(TestActivity.this);
                    words = wordConverter.jsonToWordArrayList(response);
                    pbr.setVisibility(View.GONE);
                    wordIndex = 0;
                    wordMaxIndex = words.size() - 1;
                    loadWord(wordIndex);

                } else {
                    showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
                }
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
            }
        });
    }

    public void loadWord(int index) {

        if (index >= words.size()) {
            showFinisDialog();
        } else {
            Word word = words.get(index);
            if (unitType.equals("unit_test")) {
                Word wordRandom = getRandomWord(index);
                Random random = new Random();
                if (random.nextBoolean()) {
                    wordEnglish1.setText(word.english);
                    wordEnglish2.setText(wordRandom.english);
                } else {
                    wordEnglish1.setText(wordRandom.english);
                    wordEnglish2.setText(word.english);
                }

            } else {
                wordEnglish1.setText(word.english);
            }
            wordTurkish.setText(word.turkish);

            Picasso.with(this).load(word.image).placeholder(R.drawable.words_placeholder_image).error(R.drawable.words_placeholder_image_error).into(wordImage);
            textToSpeech(word.english);
        }


    }

    private Word getRandomWord(int index) {
        int min = 0;
        int max = words.size() - 1;
        Random random = new Random();
        int randIndex = random.nextInt(max - min + 1) + min;
        if (randIndex != index) {
            return words.get(randIndex);
        }
        return getRandomWord(index);
    }


    public void navButtonClicked(View v) {
        AlphaAnimation animButton = new AlphaAnimation(0.5F, 1.0F);
        animButton.setDuration(500);
        animButton.setStartOffset(100);
        switch (v.getId()) {
            case R.id.btn_back:
                wordBack.startAnimation(animButton);
                if (wordIndex > 0) {
                    wordIndex--;
                }
                break;
            case R.id.btn_next:
                wordNext.startAnimation(animButton);
                wordIndex++;
                break;
        }
        loadWord(wordIndex);
    }


    public void tToSpeechButtonClicked(View v) {
        String word = words.get(wordIndex).english;
        switch (v.getId()) {
            case R.id.ib_sound_test_1:
                textToSpeech(word);
                break;
            case R.id.ib_sound_test_2:
                textToSpeech(word);
                break;
            case R.id.ib_sound_learn_1:
                textToSpeech(word);
                break;
        }

    }

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void textToSpeech(final String word) {

        if (word.equals("")) {
            showAlertDialog("Hata", "Kelime bulunamadı");
        } else {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.US);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //showAlertDialog("Ok", word + "SDK " + Build.VERSION.SDK_INT + "----"+ Build.VERSION_CODES.LOLLIPOP);
                            tts.speak(word, TextToSpeech.QUEUE_ADD, null, null);
                        } else {
                            //showAlertDialog("Hata", word + "SDK " + Build.VERSION.SDK_INT + "----"+ Build.VERSION_CODES.LOLLIPOP);
                        }

                    }
                }
            });
            //tts.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        }

    }


    private void loadAlertAnimation(int aType) {

        String alertFileName;

        switch (aType) {
            case 1:
                alertFileName = "alert_akillisin";
                break;
            case 2:
                alertFileName = "alert_basliyor";
                break;
            case 3:
                alertFileName = "alert_bitti";
                break;
            case 4:
                alertFileName = "alert_cevap";
                break;
            case 5:
                alertFileName = "alert_harikasin";
                break;
            case 6:
                alertFileName = "alert_haydi";
                break;
            case 7:
                alertFileName = "alert_supersin";
                break;
            case 8:
                alertFileName = "alert_zekisin";
                break;
            default:
                alertFileName = "";
                break;
        }

        int imageResourceId = getResources().getIdentifier(alertFileName, "drawable", getPackageName());

        if (imageResourceId > 0) {
            alertImageView.setImageResource(imageResourceId);
            alertImageView.setVisibility(View.VISIBLE);

            AlphaAnimation animation1 = new AlphaAnimation(0.0F, 1.0F);
            animation1.setDuration(500);
            animation1.setStartOffset(500);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AlphaAnimation animation2 = new AlphaAnimation(1.0F, 0.0F);
                    animation2.setDuration(500);
                    animation2.setStartOffset(2000);

                    animation2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            alertImageView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    alertImageView.startAnimation(animation2);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            alertImageView.startAnimation(animation1);
        }
    }

    private void showFinisDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_finish_test);

        ImageButton buttonNext = (ImageButton) dialog.findViewById(R.id.alert_btn_next);
        ImageButton buttonRestart = (ImageButton) dialog.findViewById(R.id.alert_btn_restart);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(0, 200, 200, 200)));
        dialog.setCanceledOnTouchOutside(false);

    }

    private void setScreenElementSizes() {

        if (!AppConfig.screenSetSize) return;

        Screen screenInfo = new Screen(this);
        double oWidth = (double) screenInfo.screenWidthPixels / AppConfig.screenOrjWidth;
        double oHeight = (double) screenInfo.screenHeightPixels / AppConfig.screenOrjHeight;
        double oWidthHeight = oWidth / oHeight;

        wordImage.getLayoutParams().width = (int) (wordImage.getLayoutParams().width * oWidth);
        wordImage.getLayoutParams().height = (int) (wordImage.getLayoutParams().height * oWidth);

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
                //DO NOOOOO
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
