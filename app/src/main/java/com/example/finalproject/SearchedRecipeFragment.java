package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchedRecipeFragment extends Fragment {

    private Activity containerActivity;
    private View view;
    private ArrayList<String> list;
    private JSONObject recipe = null;

    public SearchedRecipeFragment() {
        list = new ArrayList<>();
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Bundle bundle = getArguments();
        try {
            assert bundle != null;
            recipe = new JSONObject(bundle.getString("meal"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_searched_recipe_fragment, container, false);

        try {
            String titleString = recipe.getString("label");
            TextView title = view.findViewById(R.id.recipe_name);
            title.setText(titleString);
            String url = recipe.getString("url");

            Button makeMe = view.findViewById(R.id.makeMe);
            makeMe.setOnClickListener(e -> {
                Intent intent = new Intent(containerActivity, RecipeWebpage.class);
                intent.putExtra("url", url);
                startActivity(intent);
            });

            String image = recipe.getString("image");
            new DownloadImage().execute(image);

            JSONArray ingredients = recipe.getJSONArray("ingredientLines");

            for (int j = 0; j < ingredients.length(); j++) {
                list.add(ingredients.getString(j));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button yes = view.findViewById(R.id.yes);
        Button no = view.findViewById(R.id.no);
        yes.setOnClickListener(e -> ((NavbarActivity) requireActivity()).addRecipe(recipe));
        no.setOnClickListener(e -> getParentFragmentManager().popBackStackImmediate());

        ListView listView = view.findViewById(R.id.recipe_lv);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.recipe_lv_row, R.id.recipe_lv_row, list);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            ((NavbarActivity) requireActivity()).addToCart(list.get(i));
            return false;
        });

        return view;
    }

    public void setImage(Bitmap bm) {
        ImageView im = view.findViewById(R.id.recipe_image);
        im.setImageBitmap(bm);
    }


    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String[] strings) {
            return getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            setImage(bitmap);
        }

        private Bitmap getBitmapFromURL(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                return BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}