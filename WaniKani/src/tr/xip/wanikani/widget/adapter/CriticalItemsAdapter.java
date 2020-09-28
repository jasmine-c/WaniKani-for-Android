package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.CriticalItem;
import tr.xip.wanikani.models.v2.subjects.CriticalSubject;
import tr.xip.wanikani.models.v2.subjects.RadicalData;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class CriticalItemsAdapter extends ArrayAdapter<CriticalSubject> {

    Context context;
    Typeface typeface;

    View mItemType;
    TextView mItemCharacter;
    ImageView mItemCharacterImage;
    TextView mItemPercentage;

    private ArrayList<CriticalSubject> items;

    public CriticalItemsAdapter(Context context, int textViewResourceId, ArrayList<CriticalSubject> objects, Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.typeface = typeface;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        CriticalSubject item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_critical, null);
        }

        mItemType = v.findViewById(R.id.item_critical_type);
        mItemCharacter = (TextView) v.findViewById(R.id.item_critical_character);
        mItemCharacterImage = (ImageView) v.findViewById(R.id.item_critical_character_image);
        mItemPercentage = (TextView) v.findViewById(R.id.item_critical_percentage);

        mItemCharacter.setTypeface(typeface);

        switch (item.object) {
            case "radical": mItemType.setBackgroundResource(R.drawable.oval_radical); break;
            case "kanji": mItemType.setBackgroundResource(R.drawable.oval_kanji); break;
            case "vocabulary": mItemType.setBackgroundResource(R.drawable.oval_vocabulary); break;
        }

        if (item.data.characters != null) {
            mItemCharacter.setVisibility(View.VISIBLE);
            mItemCharacterImage.setVisibility(View.GONE);
            mItemCharacter.setText(item.data.characters);
        } else {
            mItemCharacter.setVisibility(View.GONE);
            mItemCharacterImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                    .load(((RadicalData) item.data).character_images.get(0).url)
                    .into(mItemCharacterImage);
            mItemCharacterImage.setColorFilter(context.getResources().getColor(R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
        }

        mItemPercentage.setText(item.percentage + "");

        return v;
    }
}