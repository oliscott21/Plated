package com.example.finalproject.ui.shopping;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.NavbarActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentShoppingBinding;
import com.example.finalproject.databinding.FragmentShoppingBinding;

import org.json.JSONObject;

import java.util.ArrayList;

public class ShoppingFragment extends Fragment {

    private FragmentShoppingBinding binding;
    private ArrayList<String> shoppingList = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentShoppingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = root.findViewById(R.id.shopping_list);
        ArrayList<String> shoppingList = ((NavbarActivity) requireActivity()).getShoppingList();
        adapter = new ArrayAdapter<>(
                getActivity(), R.layout.shopping_row, R.id.shopping_lv_row, shoppingList);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener((adapterView, v, i, l) -> {
                    ((NavbarActivity) requireActivity()).removeFromCart(i);
                    listView.invalidateViews();
                    return false;
                }
        );

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}