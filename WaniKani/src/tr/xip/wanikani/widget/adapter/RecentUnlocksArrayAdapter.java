package tr.xip.wanikani.widget.adapter;

import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import tr.xip.wanikani.R;
import tr.xip.wanikani.models.BaseItem;
import tr.xip.wanikani.models.UnlockItem;
import tr.xip.wanikani.models.v2.subjects.RadicalData;
import tr.xip.wanikani.models.v2.subjects.UnlockedSubject;

/**
 * Created by xihsa_000 on 3/14/14.
 */
public class RecentUnlocksArrayAdapter extends ArrayAdapter<UnlockedSubject> {

    Context context;
    Typeface typeface;

    View mUnlockType;
    TextView mUnlockCharacter;
    ImageView mUnlockCharacterImage;
    TextView mUnlockDate;

    private ArrayList<UnlockedSubject> items;

    public RecentUnlocksArrayAdapter(Context context, int textViewResourceId, ArrayList<UnlockedSubject> objects, Typeface typeface) {
        super(context, textViewResourceId, objects);
        this.items = objects;
        this.context = context;
        this.typeface = typeface;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        UnlockedSubject item = items.get(position);

        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.item_recent_unlock, null);
        }

        mUnlockType = v.findViewById(R.id.item_recent_unlock_type);
        mUnlockCharacter = (TextView) v.findViewById(R.id.item_recent_unlock_character);
        mUnlockCharacterImage = (ImageView) v.findViewById(R.id.item_recent_unlock_character_image);
        mUnlockDate = (TextView) v.findViewById(R.id.item_recent_unlock_date);

        mUnlockCharacter.setTypeface(typeface);

        switch (item.object) {
            case "radical": mUnlockType.setBackgroundResource(R.drawable.oval_radical); break;
            case "kanji": mUnlockType.setBackgroundResource(R.drawable.oval_kanji); break;
            case "vocabulary": mUnlockType.setBackgroundResource(R.drawable.oval_vocabulary); break;
        }

        if (item.data.characters != null) {
            mUnlockCharacter.setVisibility(View.VISIBLE);
            mUnlockCharacterImage.setVisibility(View.GONE);
            mUnlockCharacter.setText(item.data.characters);
        } else {
            RadicalData data = (RadicalData) item.data;
            mUnlockCharacter.setVisibility(View.GONE);
            mUnlockCharacterImage.setVisibility(View.VISIBLE);
            Picasso.with(context)
                .load(data.character_images.get(0).url)
                .into(mUnlockCharacterImage);
            mUnlockCharacterImage.setColorFilter(context.getResources().getColor(R.color.text_gray), Mode.SRC_ATOP);
        }

        mUnlockDate.setText(item.unlocked_at.toString("MMM d"));

        return v;
    }
}