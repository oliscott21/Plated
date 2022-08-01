package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ShareFragment extends Fragment {

    private Activity containerActivity = null;

    public ShareFragment() {
        super(R.layout.activity_share_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.activity_share_fragment, container, false);

        assert getArguments() != null;
        Bundle bundle = getArguments();
        ArrayList<String> list = bundle.getStringArrayList("list");
        ListView listView = view.findViewById(R.id.list_view);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getActivity(), R.layout.ll_row, R.id.ll_row, list);
        listView.setAdapter(arrayAdapter);

        return view;
    }
}