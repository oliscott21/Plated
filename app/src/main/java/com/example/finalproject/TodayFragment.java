package com.example.finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodayFragment extends Fragment {

    private String today;

    public TodayFragment() {
        super(R.layout.activity_today_fragment);
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        today = formatter.format(date);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_today_fragment, container, false);
        TextView tv = view.findViewById(R.id.date);
        tv.setText(today);

        Button bf = view.findViewById(R.id.breakfast);
        Button lf = view.findViewById(R.id.lunch);
        Button df = view.findViewById(R.id.dinner);
        bf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(today, 0));
        lf.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(today, 1));
        df.setOnClickListener(e -> ((NavbarActivity) requireActivity()).recipeOnClick(today, 2));

        return view;
    }
}