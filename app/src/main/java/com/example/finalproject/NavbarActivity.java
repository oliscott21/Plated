package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.databinding.ActivityNavbarBinding;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class NavbarActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavbarBinding binding;

    protected HashMap<String, JSONObject[]> mealsForMonth;
    private String curDate;
    private int mealTime;
    private ArrayList<String> shoppingList;
    private ArrayList<JSONObject> favList;
    private ArrayList<String> favListNames;
    private ArrayList<String> contactList;
    private JSONObject share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityNavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavbar.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_search, R.id.nav_favorites, R.id.nav_shopping)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navbar);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        curDate = formatter.format(date);
        mealTime = 0;

        mealsForMonth = new HashMap<>();

        try {
            fillMeals();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fillShoppingList();
        try {
            fillFavList();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        contactList =new ArrayList<>();
        getContacts();
    }

    public void shareContent(JSONObject recipe) {
        share = recipe;
        ShareFragment sf = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", contactList);
        sf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, sf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void shareSearch(JSONObject recipe) {
        share = recipe;
        ShareFragment sf = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", contactList);
        sf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_frag, sf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void shareFav(JSONObject recipe) {
        share = recipe;
        ShareFragment sf = new ShareFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", contactList);
        sf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fav_frag, sf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getContacts(){
        Cursor cursor = this.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));
            contactList.add(name + " :: " + contactId);
        }
        cursor.close();
    }

    public void onContactClick(View view) throws JSONException {
        TextView tv = (TextView) view;
        String id = tv.getText().toString().split(": ")[1];
        String contact = "";

        String url = share.getString("url");

        Cursor email = getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id, null, null);
        while (email.moveToNext()) {
            contact = email.getString(email.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS));
        }
        email.close();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(
                "vnd.android.cursor.dir/email"
        );
        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {
                contact
        });

        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navbar);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void fillShoppingList() {
        shoppingList = new ArrayList<>();
        SharedPreferences pref = this.getSharedPreferences("shopping_list",
                Context.MODE_PRIVATE);

        String temp = "item #";
        int i = 1;
        String cur = pref.getString(temp + i, "-1");

        while (!cur.equals("-1")) {
            shoppingList.add(cur);
            i++;
            String key = temp + i;
            cur = pref.getString(key, "-1");
        }
    }

    private void fillFavList() throws JSONException {
        favList = new ArrayList<>();
        favListNames = new ArrayList<>();
        SharedPreferences pref = this.getSharedPreferences("fav_list",
                Context.MODE_PRIVATE);

        String temp = "fav #";
        int i = 1;
        String cur = pref.getString(temp + i, "-1");

        while (!cur.equals("-1")) {
            JSONObject item = new JSONObject(cur);
            favListNames.add(item.getString("label"));
            favList.add(item);
            i++;
            String key = temp + i;
            cur = pref.getString(key, "-1");
        }
    }

    private void fillMeals() throws JSONException {
        SharedPreferences pref = this.getSharedPreferences("meals_for_month",
                Context.MODE_PRIVATE);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day;
        for (int i = 0; i < 31; i++) {
            day = sdf.format(cal.getTime());
            JSONObject[] thisDay = new JSONObject[3];
            boolean found = false;
            for (int j = 0; j < 3; j++) {
                day += " " + j;

                String cur = pref.getString(day, "-1");
                String[] temp = day.split(" ");
                if (!cur.equals("-1")) {
                    JSONObject newObj = new JSONObject(cur);
                    thisDay[j] = newObj;
                    found = true;
                }
                day = temp[0];
            }
            if (found) {
                mealsForMonth.put(day, thisDay);
            }
            cal.add(Calendar.DATE, 1);
        }
    }

    private class AddPrefRunnable implements Runnable {

        private final Activity activity;

        public AddPrefRunnable(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            SharedPreferences pref = activity.getSharedPreferences("meals_for_month",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.clear();
            for (String s : mealsForMonth.keySet()) {
                JSONObject[] temp = mealsForMonth.get(s);
                assert temp != null;
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i] != null) {
                        String key = s + " " + i;
                        editor.putString(key, temp[i].toString());
                    }
                }
            }
            editor.apply();
        }
    }

    private class AddItemPrefRunnable implements Runnable {

        private final Activity activity;

        public AddItemPrefRunnable(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            SharedPreferences pref = activity.getSharedPreferences("shopping_list",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.clear();
            String temp = "item #";
            int i = 1;
            for (String s : shoppingList) {
                String key = temp + i;
                editor.putString(key, s);
                i++;
            }
            editor.apply();
        }
    }

    private class AddFavPrefRunnable implements Runnable {

        private final Activity activity;

        public AddFavPrefRunnable(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void run() {
            SharedPreferences pref = activity.getSharedPreferences("fav_list",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.clear();
            String temp = "fav #";
            int i = 1;
            for (JSONObject j : favList) {
                String key = temp + i;
                editor.putString(key, j.toString());
                i++;
            }
            editor.apply();
        }
    }

    public void todayOnClick(View view) {
        TodayFragment tf = new TodayFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, tf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void weeklyOnClick(View view) {
        WeeklyFragment wf = new WeeklyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, wf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void monthlyOnClick(View view) {
        MonthlyFragment mf = new MonthlyFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_view, mf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void recipeOnClick(String date, int mealTime) {
        this.curDate = date;
        this.mealTime = mealTime;

        if (!mealsForMonth.containsKey(date)){
            SearchFragment sf = new SearchFragment();
            sf.setContainerActivity(this);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_view, sf);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            JSONObject[] today = mealsForMonth.get(date);
            if (today == null || today[mealTime] == null) {
                SearchFragment sf = new SearchFragment();
                sf.setContainerActivity(this);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.content_view, sf);
                transaction.addToBackStack(null);
                transaction.commit();
            } else {
                JSONObject temp = mealsForMonth.get(date)[mealTime];
                recipe(temp, false);
            }
        }
    }

    public void recipe(JSONObject recipe, boolean search) {
        if (search) {
            SearchedRecipeFragment srf = new SearchedRecipeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("meal", recipe.toString());
            srf.setContainerActivity(this);
            srf.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_view, srf);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            RecipeFragment rf = new RecipeFragment();
            Bundle bundle = new Bundle();
            bundle.putString("meal", recipe.toString());
            bundle.putString("from", "content_view");
            rf.setContainerActivity(this);
            rf.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_view, rf);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
    public void recipeOut(JSONObject recipe) {
        RecipeFragment rf = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meal", recipe.toString());
        bundle.putString("from", "search_frag");
        rf.setContainerActivity(this);
        rf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.search_frag, rf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void recipeFav(JSONObject recipe) {
        RecipeFragment rf = new RecipeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("meal", recipe.toString());
        bundle.putString("from", "fav_frag");
        rf.setContainerActivity(this);
        rf.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fav_frag, rf);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addRecipe(JSONObject recipe) {
        if (!mealsForMonth.containsKey(curDate)) {
            JSONObject[] temp = new JSONObject[3];
            mealsForMonth.put(curDate, temp);
        }
        if (mealsForMonth.get(curDate)[mealTime] == null) {
            mealsForMonth.get(curDate)[mealTime] = recipe;
        }

        AddPrefRunnable apr = new AddPrefRunnable(this);
        apr.run();

        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
    }

    public ArrayList<String> getShoppingList() {
        return shoppingList;
    }

    public void addToCart(String item) {
        if (!shoppingList.contains(item)){
            shoppingList.add(item);
            item += " was added to your shopping list";
            AddItemPrefRunnable apr = new AddItemPrefRunnable(this);
            apr.run();
        } else {
            item += " is already in your shopping list";
        }
        Toast t = Toast.makeText(this, item, Toast.LENGTH_SHORT);
        t.show();
    }

    public void removeFromCart(int i) {
        String temp = shoppingList.get(i);
        shoppingList.remove(i);
        AddItemPrefRunnable apr = new AddItemPrefRunnable(this);
        apr.run();
        temp += " was removed from your shopping list";
        Toast t = Toast.makeText(this, temp, Toast.LENGTH_SHORT);
        t.show();
    }

    public ArrayList<JSONObject> getFavList() {
        return favList;
    }

    public boolean isFav(String recipe) {
        return favListNames.contains(recipe);
    }

    public void removeFromFav(String item) throws JSONException {
        ArrayList<JSONObject> tempFavList= new ArrayList<>();
        ArrayList<String> tempFavListNames = new ArrayList<>();
        for (int i = 0; i < favList.size(); i++) {
            JSONObject t = favList.get(i);
            String temp = t.getString("label");
            if (!temp.equals(item)) {
                tempFavList.add(t);
                tempFavListNames.add(temp);
            }
        }
        favList = tempFavList;
        favListNames = tempFavListNames;
        AddFavPrefRunnable afpr = new AddFavPrefRunnable(this);
        afpr.run();
        String mess = item + " was removed from your favorites";
        Toast to = Toast.makeText(this, mess, Toast.LENGTH_SHORT);
        to.show();
    }

    public void addToFav(JSONObject item) throws JSONException {
        String temp = item.getString("label");
        if (!favListNames.contains(temp)){
            favList.add(item);
            favListNames.add(temp);
            AddFavPrefRunnable afpr = new AddFavPrefRunnable(this);
            afpr.run();
            temp += " was added to your favorites";
        } else {
            temp += " is already in your favorites";
        }
        Toast t = Toast.makeText(this, temp, Toast.LENGTH_SHORT);
        t.show();
    }
}