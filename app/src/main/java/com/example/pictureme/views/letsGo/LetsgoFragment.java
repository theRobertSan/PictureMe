package com.example.pictureme.views.letsGo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.pictureme.R;

public class LetsgoFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_letsgo, container, false);

        Button buttonEat = view.findViewById(R.id.button_eat);
        Button buttonVisit = view.findViewById(R.id.button_visit);

        buttonEat.setOnClickListener(this);
        buttonVisit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        Navigation.findNavController(view).navigate(R.id.action_letsgoFragment_to_shakeFragment);
    }
}