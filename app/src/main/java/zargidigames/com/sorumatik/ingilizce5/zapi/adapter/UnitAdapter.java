package zargidigames.com.sorumatik.ingilizce5.zapi.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import zargidigames.com.sorumatik.ingilizce5.R;
import zargidigames.com.sorumatik.ingilizce5.zapi.models.Unit;

/**
 * Created by ilimturan on 07/10/15.
 */
public class UnitAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater mInflater;
    private List<Unit> unitList;

    public UnitAdapter(Activity activity_, List<Unit> units) {
        activity = activity_;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        unitList = units;
    }

    @Override
    public int getCount() {
        return unitList.size();
    }

    @Override
    public Unit getItem(int position) {
        return unitList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowUnitView;

        rowUnitView = mInflater.inflate(R.layout.row_unit, null);
        TextView textView = (TextView) rowUnitView.findViewById(R.id.unit_row_text);
        ImageView imageView = (ImageView) rowUnitView.findViewById(R.id.unit_row_image);

        Unit unit = unitList.get(position);

        Picasso.with(activity).load(unit.image).placeholder(R.drawable.unite_placeholder).error(R.drawable.unite_placeholder_error).into(imageView);

        textView.setText(unit.name);

        return rowUnitView;
    }


}