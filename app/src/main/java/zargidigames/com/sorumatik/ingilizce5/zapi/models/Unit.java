package zargidigames.com.sorumatik.ingilizce5.zapi.models;

import android.graphics.Bitmap;

/**
 * Created by ilimturan on 29/08/15.
 */
public class Unit {

    public int id;
    public String name;
    public int lesson_id;
    public int status;
    public String image;
    public Bitmap bitmap;

    @Override
    public String toString() {
        return "Unit{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lesson_id=" + lesson_id +
                ", status=" + status +
                ", image='" + image + '\'' +
                '}';
    }
}
