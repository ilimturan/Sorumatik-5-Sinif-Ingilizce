package zargidigames.com.sorumatik.ingilizce5.zapi.models;

import android.graphics.Bitmap;

/**
 * Created by ilimturan on 08/10/15.
 */
public class Word {

    public int id;
    public String english;
    public String turkish;
    public int subunit_id;
    public int status;
    public String image;
    public Bitmap bitmap;

    @Override
    public String toString() {
        return "Word{" +
                ", id=" + id +
                ", english='" + english + '\'' +
                ", turkish='" + turkish + '\'' +
                ", subunit_id=" + subunit_id +
                ", status=" + status +
                ", image='" + image + '\'' +
                '}';
    }
}
