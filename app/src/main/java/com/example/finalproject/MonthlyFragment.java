package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthlyFragment extends Fragment {

    private String curDate;
    private View view;

    public MonthlyFragment() {
        super(R.layout.activity_monthly_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        curDate = formatter.format(date);
    }

    @Override
    public void onResume() {
        super.onResume();

        TextView tv = view.findViewById(R.id.monthly_title);
        TextView bf = view.findViewById(R.id.breakfast_label);
        TextView lu = view.findViewById(R.id.lunch_label);
        TextView di = view.findViewById(R.id.dinner_label);

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        curDate = formatter.format(date);

        String newStr = getResources().getString(R.string.monthly_title);
        newStr += " " + curDate;
        tv.setText(newStr);

        String[] fStrings = new String[3];
        try {
            fStrings = makeStrings(curDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bf.setText(fStrings[0]);
        lu.setText(fStrings[1]);
        di.setText(fStrings[2]);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_monthly_fragment, container, false);
        TextView tv = view.findViewById(R.id.monthly_title);
        TextView bf = view.findViewById(R.id.breakfast_label);
        TextView lu = view.findViewById(R.id.lunch_label);
        TextView di = view.findViewById(R.id.dinner_label);
        CalendarView cal = view.findViewById(R.id.cal);
        String newStr = getResources().getString(R.string.monthly_title);
        newStr += " " + curDate;
        tv.setText(newStr);
        setCalendar(cal);

        String[] fStrings = new String[3];
        try {
            fStrings = makeStrings(curDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bf.setText(fStrings[0]);
        lu.setText(fStrings[1]);
        di.setText(fStrings[2]);

        cal.setOnDateChangeListener((calendarView, i, i1, i2) -> dateChange(i, i1, i2, tv, bf, lu, di));
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(curDate, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(curDate, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(curDate, 2));
        return view;
    }

    private void setCalendar(CalendarView cal) {
        Calendar calendar = Calendar.getInstance();

        long cur = cal.getDate();
        calendar.setTimeInMillis(cur);
        calendar.add(Calendar.DATE, 30);
        cal.setMinDate(cur);
        cal.setMaxDate(calendar.getTimeInMillis());
    }

    private void dateChange
            (int i, int i1, int i2, TextView tv, TextView bf, TextView lu, TextView di) {
        String temp = String.valueOf(i).substring(2);
        String month = String.valueOf(i1+1);
        if (month.length() == 1) {
            curDate = "0";
        } else {
            curDate = "";
        }

        String day = String.valueOf(i2);
        String dayTemp = day;
        if (day.length() == 1) {
            day = "0" + dayTemp;
        }

        curDate += month + "/" + day + "/" + temp;
        String newStr = getResources().getString(R.string.monthly_title);
        newStr += " " + curDate;
        tv.setText(newStr);

        String[] strings = new String[3];
        try {
            strings = makeStrings(curDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        bf.setText(strings[0]);
        lu.setText(strings[1]);
        di.setText(strings[2]);
    }

    private String[] makeStrings(String curDate) throws JSONException {
        String[] strings = new String[3];
        String bs = getResources().getString(R.string.breakfast_label);
        String ls = getResources().getString(R.string.lunch_label);
        String ds = getResources().getString(R.string.dinner_label);

        JSONObject[] mealsForToday =
                ((NavbarActivity) requireActivity()).mealsForMonth.get(curDate);
        if (mealsForToday != null) {
            if (mealsForToday[0] != null) {
                JSONObject bf = mealsForToday[0];
                bs += " " + bf.getString("label");
            }
            if (mealsForToday[1] != null) {
                JSONObject bf = mealsForToday[1];
                ls += " " + bf.getString("label");
            }
            if (mealsForToday[2] != null) {
                JSONObject bf = mealsForToday[2];
                ds += " " + bf.getString("label");
            }
        }

        strings[0] = bs;
        strings[1] = ls;
        strings[2] = ds;
        return strings;
    }
}