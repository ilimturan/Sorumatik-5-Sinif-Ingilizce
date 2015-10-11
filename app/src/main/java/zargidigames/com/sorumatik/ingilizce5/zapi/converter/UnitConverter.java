package zargidigames.com.sorumatik.ingilizce5.zapi.converter;

import android.app.Activity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import zargidigames.com.sorumatik.ingilizce5.zapi.models.Unit;

/**
 * Created by ilimturan on 07/10/15.
 */
public class UnitConverter {

    public Activity activity;
    public List<Unit> units = new ArrayList<>();

    public UnitConverter(Activity activity_) {
        activity = activity_;
    }

    public List<Unit> jsonToUnitArrayList(JSONArray jsonArray) {

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                final Unit unit = new Unit();
                unit.id = jsonObject.getInt("id");
                unit.name = jsonObject.getString("name");
                unit.lesson_id = jsonObject.getInt("lesson_id");
                unit.status = jsonObject.getInt("status");
                unit.image = jsonObject.getString("image");
                units.add(unit);

            } catch (JSONException e) {
                //e.printStackTrace();
            }
        }

        return units;

    }
}
