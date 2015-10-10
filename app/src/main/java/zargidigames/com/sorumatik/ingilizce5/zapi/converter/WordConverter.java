package zargidigames.com.sorumatik.ingilizce5.zapi.converter;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zargidigames.com.sorumatik.ingilizce5.zapi.models.Word;

/**
 * Created by ilimturan on 08/10/15.
 */
public class WordConverter {

    public Activity activity;
    public List<Word> words = new ArrayList<>();

    public WordConverter(Activity activity_) {
        activity = activity_;
    }

    public List<Word> jsonToWordArrayList(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final Word word = new Word();
                word.id = jsonObject.getInt("id");
                word.english = jsonObject.getString("english");
                word.turkish = jsonObject.getString("turkish");
                word.subunit_id = jsonObject.getInt("subunit_id");
                word.status = jsonObject.getInt("status");
                word.image = jsonObject.getString("image");
                words.add(word);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return words;

    }
}
