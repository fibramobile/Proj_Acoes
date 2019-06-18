package action.dev.project15.fragment.radar;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import action.dev.project15.helper.FileIO;

class Store {

    JSONObject map = new JSONObject();

    void save(Context context){
        FileIO.save(context, map.toString(), "radar.json");
    }

    void load(Context context) {
        try {
            String text = FileIO.load(context, "radar.json");
            if (text == null) return;
            map = new JSONObject(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
