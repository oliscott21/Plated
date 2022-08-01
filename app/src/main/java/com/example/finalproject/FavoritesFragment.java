package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    private Activity containerActivity = null;
    private ArrayList<JSONObject> favList = new ArrayList<>();

    public FavoritesFragment() {
        super(R.layout.activity_favorites_fragment);
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_favorites_fragment, container, false);

        ListView listView = view.findViewById(R.id.fav_list);
        favList = ((NavbarActivity) requireActivity()).getFavList();
        ArrayList<String> list = new ArrayList<>();

        if (favList != null ) {
            for (JSONObject j : favList) {
                try {
                    list.add(j.getString("label"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                containerActivity, R.layout.fav_row, R.id.fav_lv_row, list);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, v, i, l) -> ((NavbarActivity) requireActivity()).recipeFav(favList.get(i)));

        return view;
    }
}