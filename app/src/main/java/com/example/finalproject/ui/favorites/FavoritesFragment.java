package com.example.finalproject.ui.favorites;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.LandingFragment;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentFavoritesBinding;
import com.example.finalproject.ui.favorites.FavoritesViewModel;

public class FavoritesFragment extends Fragment {

    private FragmentFavoritesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        com.example.finalproject.FavoritesFragment ff = new com.example.finalproject.FavoritesFragment();
        ff.setContainerActivity(getActivity());
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fav_frag, ff);
        transaction.commit();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
