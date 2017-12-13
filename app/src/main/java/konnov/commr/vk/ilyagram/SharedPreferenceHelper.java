package konnov.commr.vk.ilyagram;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

/**
 * Created by ilya on 13/12/2017.
 */

public class SharedPreferenceHelper {

    public final String DEFAULT_VALUE = "";
    private Activity activity;

    public SharedPreferenceHelper(Activity activity){
        this.activity = activity;
        String PREFERENCE_FILE_KEY = "last_user_data";
        SharedPreferences sharedPref = activity.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
    }


    public void saveInSharedPref(String username, String password){
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.commit();
    }

    public ArrayList<String> getSharedPref(){
        ArrayList<String> arrayList = new ArrayList<>();
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        arrayList.add(sharedPref.getString("username", DEFAULT_VALUE));
        arrayList.add(sharedPref.getString("password", DEFAULT_VALUE));

        if(!arrayList.get(0).equals(DEFAULT_VALUE) || !arrayList.get(1).equals(DEFAULT_VALUE))
            return arrayList;
        else
            return null;
    }
}
