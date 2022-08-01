package com.example.finalproject.ui.search;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.LandingFragment;
import com.example.finalproject.NavbarActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentSearchBinding;
import com.example.finalproject.databinding.FragmentShoppingBinding;
import com.example.finalproject.ui.shopping.ShoppingViewModel;

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

    private FragmentSearchBinding binding;
    private Activity containerActivity = null;
    private String searchTerm = null;
    private ArrayList<String> recipesList = new ArrayList<>();
    private ArrayList<JSONObject> jsonRecipeList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.containerActivity = getActivity();

        Bundle bundle = new Bundle();
        bundle.putString("from", "true");

        com.example.finalproject.SearchFragment sf = new com.example.finalproject.SearchFragment();
        sf.setArguments(bundle);
        sf.setContainerActivity(getActivity());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.search_frag, sf);
        transaction.commit();

        return root;
    }
}
        /*
        ListView recipesLV = root.findViewById(R.id.recipes);
        EditText et = root.findViewById(R.id.search_bar);
        Button button = root.findViewById(R.id.search_button);
        button.setOnClickListener(view -> {
            searchTerm = et.getText().toString();
            adapter = new ArrayAdapter<>(containerActivity, R.layout.recipe_row, R.id.text_for_row, recipesList);
            recipesLV.setAdapter(adapter);
            DownloadRecipeTask dt = new DownloadRecipeTask();
            dt.execute();
        });
        recipesLV.setOnItemClickListener((adapterView, view, i, l) -> {
            ((NavbarActivity) requireActivity()).recipeOut(jsonRecipeList.get(i));
        });

        return root;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

         */