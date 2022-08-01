package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WeeklyFragment extends Fragment {

    private String curDate;
    private View view;

    public WeeklyFragment() {
        super(R.layout.activity_weekly_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        curDate = formatter.format(date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.activity_weekly_fragment, container, false);

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day;
        for (int i = 0; i < 7; i++) {
            day = sdf.format(cal.getTime());
            try {
                JSONObject[] temp = getMeals(day);
                if (i == 0) {
                    setMeal(temp, day);
                } else if (i == 1) {
                    setMeal2(temp, day);
                } else if (i == 2) {
                    setMeal3(temp, day);
                } else if (i == 3) {
                    setMeal4(temp, day);
                } else if (i == 4) {
                    setMeal5(temp, day);
                } else if (i == 5) {
                    setMeal6(temp, day);
                } else {
                    setMeal7(temp, day);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cal.add(Calendar.DATE, 1);
        }


        return view;
    }

    private JSONObject[] getMeals(String curDate) throws JSONException {
        return ((NavbarActivity) requireActivity()).mealsForMonth.get(curDate);
    }

    private void setMeal(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast);
        TextView lu = view.findViewById(R.id.lunch);
        TextView di = view.findViewById(R.id.dinner);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal2(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal2);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast2);
        TextView lu = view.findViewById(R.id.lunch2);
        TextView di = view.findViewById(R.id.dinner2);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal3(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal3);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast3);
        TextView lu = view.findViewById(R.id.lunch3);
        TextView di = view.findViewById(R.id.dinner3);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal4(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal4);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast4);
        TextView lu = view.findViewById(R.id.lunch4);
        TextView di = view.findViewById(R.id.dinner4);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal5(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal5);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast5);
        TextView lu = view.findViewById(R.id.lunch5);
        TextView di = view.findViewById(R.id.dinner5);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal6(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal6);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast6);
        TextView lu = view.findViewById(R.id.lunch6);
        TextView di = view.findViewById(R.id.dinner6);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }

    private void setMeal7(JSONObject[] meal, String date) throws JSONException {
        TextView d = view.findViewById(R.id.meal7);
        d.append(date);
        TextView bf = view.findViewById(R.id.breakfast7);
        TextView lu = view.findViewById(R.id.lunch7);
        TextView di = view.findViewById(R.id.dinner7);
        if (meal != null) {
            if (meal[0] != null) {
                bf.append(meal[0].getString("label"));
            }
            if (meal[1] != null) {
                lu.append(meal[1].getString("label"));
            }
            if (meal[2] != null) {
                di.append(meal[2].getString("label"));
            }
        }
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 0));
        lu.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 1));
        di.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(date, 2));
    }
}