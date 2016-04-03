package zargidigames.com.sorumatik.ingilizce5;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.ActionBar;
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
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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

    private String unitType;
    private int unitId;
    private String unitName;
    private String barTitle;

    private Word wordActive;
    private Word wordRandom;
    private int wordIndex = 0;
    private int wordSize = 0;
    private int wordMaxIndex = 0;

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
    private ImageButton wordBackButton;
    private ImageButton wordNextButton;

    private LinearLayout contentLinearLayout1;
    private LinearLayout contentLinearLayout2;
    private LinearLayout contentLinearLayout3;
    private LinearLayout contentLinearLayout4;
    private LinearLayout contentLinearLayout5;

    private MediaPlayer menuSound;
    private MediaPlayer buttonSound;
    private MediaPlayer answerCorrectSound;
    private MediaPlayer answerWrongSound;
    private MediaPlayer alertSound;

    private RelativeLayout alertRelativeLayout;
    private ImageView alertImageView;

    private HashMap<Integer, Integer> userAnswers = new HashMap<>();
    private int answerTrueRow = 0;
    private int answerUserRow = 0;

    private String wordTestRow1Text;
    private String wordTestRow2Text;
    private String wordLearnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#AE1A20")));

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
        wordBackButton = (ImageButton) findViewById(R.id.btn_back);
        wordNextButton = (ImageButton) findViewById(R.id.btn_next);

        contentLinearLayout1 = (LinearLayout) findViewById(R.id.test_ll_c_1);
        contentLinearLayout2 = (LinearLayout) findViewById(R.id.test_ll_c_2);
        contentLinearLayout3 = (LinearLayout) findViewById(R.id.test_ll_c_3);
        contentLinearLayout4 = (LinearLayout) findViewById(R.id.test_ll_c_4);
        contentLinearLayout5 = (LinearLayout) findViewById(R.id.test_ll_c_5);

        alertRelativeLayout = (RelativeLayout) findViewById(R.id.alert_rl);
        alertImageView = (ImageView) findViewById(R.id.alert_iv);

        buttonSound = MediaPlayer.create(this, R.raw.pope);
        answerCorrectSound = MediaPlayer.create(this, R.raw.correct);
        answerWrongSound = MediaPlayer.create(this, R.raw.wrong);
        alertSound = MediaPlayer.create(this, R.raw.success);

        wordTurkish.setAllCaps(false);
        wordEnglish1.setAllCaps(false);
        wordEnglish2.setAllCaps(false);

        checkActiveNetwork();
        if (networkStatus && unitId > 0) {
            try {
                getTests(unitId);
            } catch (JSONException e) {
                //e.printStackTrace();
                showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
            }
        }else{

            showAlertDialog(getString(R.string.word_load_error_title_1), getString(R.string.word_load_error_text_1));
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
            showAndroidVersionToastMessage();
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
            showAndroidVersionToastMessage();
            wordSoundTest1.setVisibility(View.GONE);
            wordSoundTest2.setVisibility(View.GONE);
            wordSoundLearn1.setVisibility(View.GONE);
        }

        setTitle(barTitle);
    }

    private void showAndroidVersionToastMessage() {

        Toast.makeText(this, getString(R.string.text_to_speech_android_version_message), Toast.LENGTH_LONG).show();
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
                    wordSize = words.size();
                    wordMaxIndex = wordSize - 1;

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

        if (index >= wordSize) {
            showFinisDialog();
        } else {

            wordActive = words.get(index);
            wordRandom = getRandomWord(index);

            answerUserRow = 0;
            answerTrueRow = 0;
            wordNextButton.setEnabled(true);
            wordBackButton.setEnabled(true);
            wordEnglish1.setEnabled(true);
            wordEnglish2.setEnabled(true);
            wordEnglish1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f6fb8")));
            wordEnglish2.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f6fb8")));
            wordSoundTest1.setBackgroundResource(R.drawable.sound_girl_pasif);
            wordSoundTest2.setBackgroundResource(R.drawable.sound_girl_pasif);
            wordSoundLearn1.setBackgroundResource(R.drawable.sound_girl_pasif);
            wordResult1.setBackgroundResource(R.drawable.icon_true);
            wordResult2.setBackgroundResource(R.drawable.icon_true);
            wordResult1.setVisibility(View.GONE);
            wordResult2.setVisibility(View.GONE);


            if (unitType.equals("unit_test")) {
                Random random = new Random();
                if (random.nextBoolean()) {
                    wordEnglish1.setText(wordActive.english);
                    wordEnglish2.setText(wordRandom.english);

                    wordTestRow1Text = wordActive.english;
                    wordTestRow2Text = wordRandom.english;
                    wordLearnText = wordRandom.english;

                    answerTrueRow = 1;
                } else {
                    wordEnglish1.setText(wordRandom.english);
                    wordEnglish2.setText(wordActive.english);

                    wordTestRow2Text = wordActive.english;
                    wordTestRow1Text = wordRandom.english;
                    wordLearnText = wordRandom.english;

                    answerTrueRow = 2;
                }


            } else {
                wordEnglish1.setText(wordActive.english);
                wordLearnText = wordActive.english;
                textToSpeech(wordActive.english);
            }
            wordTurkish.setText(wordActive.turkish);
            Picasso.with(this).load(wordActive.image).placeholder(R.drawable.words_placeholder_image).error(R.drawable.words_placeholder_image_error).into(wordImage);


        }


    }

    private Word getRandomWord(int index) {
        int min = 0;
        int max = wordMaxIndex;
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
        buttonSound.start();

        switch (v.getId()) {
            case R.id.btn_back:
                wordBackButton.startAnimation(animButton);
                if (unitType.equals("unit_test")) {
                    if (answerUserRow == 0) {
                        //loadAlertAnimation(4);
                        return;
                    } else {
                        wordIndex--;
                    }
                } else {
                    if (wordIndex > 0) {
                        wordIndex--;
                    }
                }

                loadWord(wordIndex);
                break;
            case R.id.btn_next:
                wordNextButton.startAnimation(animButton);

                if (unitType.equals("unit_test")) {
                    if (answerUserRow == 0) {
                        loadAlertAnimation(4);
                        return;
                    } else {
                        wordIndex++;
                    }
                } else {
                    wordIndex++;
                }


                loadWord(wordIndex);
                break;
        }


    }


    public void checkUserAnswer(View v) {
        switch (v.getId()) {
            case R.id.btn_word_english_1:
                answerUserRow = 1;
                if (unitType.equals("unit_test")) {
                    if (answerTrueRow == answerUserRow) {
                        answerCorrectSound.start();
                        wordResult1.setBackgroundResource(R.drawable.icon_true);
                        wordEnglish1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#558B2F")));
                        userAnswers.put(wordIndex, 1);
                        showSuccesMessage();
                    } else {
                        answerWrongSound.start();
                        wordResult1.setBackgroundResource(R.drawable.icon_false);
                        wordEnglish1.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB1A1C")));
                        userAnswers.put(wordIndex, 0);
                    }
                    wordResult1.setVisibility(View.VISIBLE);
                }

                break;
            case R.id.btn_word_english_2:
                answerUserRow = 2;
                if (unitType.equals("unit_test")) {
                    if (answerTrueRow == answerUserRow) {
                        answerCorrectSound.start();
                        wordResult2.setBackgroundResource(R.drawable.icon_true);
                        wordEnglish2.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#558B2F")));
                        userAnswers.put(wordIndex, 1);
                        showSuccesMessage();
                    } else {
                        answerWrongSound.start();
                        wordResult2.setBackgroundResource(R.drawable.icon_false);
                        wordEnglish2.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#DB1A1C")));
                        userAnswers.put(wordIndex, 0);
                    }
                    wordResult2.setVisibility(View.VISIBLE);
                }

                break;
        }

        wordEnglish1.setEnabled(false);
        wordEnglish2.setEnabled(false);

    }

    public void tToSpeechButtonClicked(View v) {


        switch (v.getId()) {
            case R.id.ib_sound_test_1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    wordSoundTest1.setBackgroundResource(R.drawable.sound_girl_aktif);
                }
                textToSpeech(wordTestRow1Text);
                break;
            case R.id.ib_sound_test_2:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    wordSoundTest2.setBackgroundResource(R.drawable.sound_girl_aktif);
                }
                textToSpeech(wordTestRow2Text);
                break;
            case R.id.ib_sound_learn_1:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    wordSoundLearn1.setBackgroundResource(R.drawable.sound_girl_aktif);
                }
                textToSpeech(wordLearnText);
                break;
        }



    }

    //@TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void textToSpeech(final String text) {

        if (text.equals("")) {
            //showAlertDialog("Hata", "Kelime bulunamadı");
        } else {
            tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        tts.setLanguage(Locale.US);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

                            tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                                @Override
                                public void onStart(String utteranceId) {

                                }

                                @Override
                                public void onDone(String utteranceId) {

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                        wordSoundTest1.setBackgroundResource(R.drawable.sound_girl_pasif);
                                        wordSoundTest2.setBackgroundResource(R.drawable.sound_girl_pasif);
                                        wordSoundLearn1.setBackgroundResource(R.drawable.sound_girl_pasif);
                                    }

                                }

                                @Override
                                public void onError(String utteranceId) {

                                }
                            });

                        }
                    }
                }


            });
        }

    }


    private void loadAlertAnimation(final int aType) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 1 seconds
                loadAlertAnimationDelay(aType);
            }
        }, 500);


    }

    private void showSuccesMessage() {
        /**
         * Son 28, 19, 12, 7 doğru cevaba göre mesaj göster
         */
        int lastTrueCount = 0;
        for (int i = userAnswers.size() - 1; i >= 0; i--) {
            if (1 == userAnswers.get(i)) {
                lastTrueCount++;
            }else{
                break;
            }
        }

        if (lastTrueCount == 28) {
            loadAlertAnimation(7);
        } else if (lastTrueCount == 19) {
            loadAlertAnimation(8);
        } else if (lastTrueCount == 12) {
            loadAlertAnimation(1);
        } else if (lastTrueCount == 7) {
            loadAlertAnimation(5);
        }
    }

    private void loadAlertAnimationDelay(int aType){
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
            alertSound.start();
            alertImageView.setImageResource(imageResourceId);
            alertImageView.setVisibility(View.VISIBLE);

            AlphaAnimation animation1 = new AlphaAnimation(0.0F, 1.0F);
            animation1.setDuration(500);
            animation1.setStartOffset(100);

            animation1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    AlphaAnimation animation2 = new AlphaAnimation(1.0F, 0.0F);
                    animation2.setDuration(500);
                    animation2.setStartOffset(1000);

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


    private void setScreenElementSizes() {

        if (!AppConfig.screenSetSize) return;

        Screen screenInfo = new Screen(this);
        double oWidth = (double) screenInfo.screenWidthPixels / AppConfig.screenOrjWidth;
        double oHeight = (double) screenInfo.screenHeightPixels / AppConfig.screenOrjHeight;
        double oWidthHeight = oWidth / oHeight;

        int checkHeight = (int) (wordImage.getLayoutParams().height * oWidth);
        if(checkHeight < AppConfig.screenOrjHeight / 2){
            wordImage.getLayoutParams().width = (int) (wordImage.getLayoutParams().width * oWidth  * 1.3);
            wordImage.getLayoutParams().height = (int) (wordImage.getLayoutParams().height * oWidth * 1.3);
        }


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

    private void showFinisDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_finish_test);

        ImageButton buttonNext = (ImageButton) dialog.findViewById(R.id.alert_btn_next);
        ImageButton buttonRestart = (ImageButton) dialog.findViewById(R.id.alert_btn_restart);

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickNext();
            }
        });

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                buttonClickRestart();
            }
        });

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.argb(0, 200, 200, 200)));
        dialog.setCanceledOnTouchOutside(false);

    }

    public void buttonClickNext() {
        Intent intent = new Intent(this, UnitActivity.class);
        intent.putExtra("UNIT_TYPE", unitType);
        startActivity(intent);
    }


    public void buttonClickRestart() {
        wordIndex = 0;
        userAnswers.clear();
        if (unitType.equals("unit_learn")) {
            loadScreenDefaultForLearn();
        } else if (unitType.equals("unit_test")) {
            loadScreenDefaultForTest();
        }
        setScreenElementSizes();
        loadSounds();
        loadAlertAnimation(2);
        loadWord(wordIndex);
    }
}
