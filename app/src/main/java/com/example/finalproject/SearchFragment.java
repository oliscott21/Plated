package com.example.finalproject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private Activity containerActivity = null;
    private String searchTerm = null;
    private ArrayList<String> recipesList = new ArrayList<>();
    private ArrayList<JSONObject> jsonRecipeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public SearchFragment() {
        super(R.layout.activity_search_fragment);
    }

    public void setContainerActivity(Activity containerActivity) {
        this.containerActivity = containerActivity;
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search_fragment, container, false);

        ListView recipesLV = v.findViewById(R.id.recipes);
        EditText et = v.findViewById(R.id.search_bar);
        Button button = v.findViewById(R.id.search_button);
        button.setOnClickListener(view -> {
            searchTerm = et.getText().toString();
            adapter = new ArrayAdapter<>(containerActivity, R.layout.search_row, R.id.text_for_row, recipesList);
            recipesLV.setAdapter(adapter);
            DownloadRecipeTask dt = new DownloadRecipeTask();
            dt.execute();
        });

        Bundle bundle = getArguments();

        recipesLV.setOnItemClickListener((adapterView, view, i, l) -> {
            if (bundle == null) {
                ((NavbarActivity) requireActivity()).recipe(jsonRecipeList.get(i), true);
            } else {
                ((NavbarActivity) requireActivity()).recipeOut(jsonRecipeList.get(i));
            }
        });

        return v;
    }

    private void updateUIFor(JSONObject recipeObject) {
        try {
            JSONArray recipes = recipeObject.getJSONArray("hits");
            for (int i = 0; i < recipes.length(); i++) {

                JSONObject item = recipes.getJSONObject(i);
                JSONObject recipe = item.getJSONObject("recipe");

                jsonRecipeList.add(recipe);
                String title = recipe.getString("label");
                System.out.println("LABEL: " + title);

                String url = recipe.getString("url");
                System.out.println("URL: " + url);

                String image = recipe.getString("image");
                System.out.println("IMAGE: " + image);

                JSONArray ingredients = recipe.getJSONArray("ingredientLines");

                List<String> list = new ArrayList<>();
                for (int j = 0; j < ingredients.length(); j++) {
                    list.add(ingredients.getString(j));
                }
                System.out.println("INGREDIENTS: " + list);
                System.out.println(" ");

                recipesList.add(title);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject fetchRecipes() {
        try {
            String json = "";
            String line;
            URL url = new URL(formURL(searchTerm));
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.284");
            BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            while ((line = in.readLine()) != null) {
                System.out.println("JSON LINE " + line);
                json += line;
            }
            in.close();
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String formURL(String searchTerm) {
        return "https://api.edamam.com/search?q=" + searchTerm + "&app_id=6e9a38a7&app_key=b051ec4e53b29ba69e5e89d05cdc255f&&from=0&to=20&calories=591-722&health=alcohol-free";
    }

    private class DownloadRecipeTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject object = fetchRecipes();
            return object;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            updateUIFor(object);
        }
    }
}