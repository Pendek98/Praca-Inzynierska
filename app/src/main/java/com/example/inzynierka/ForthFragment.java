package com.example.inzynierka;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ForthFragment extends Fragment {



    public static ForthFragment newInstance(String param1, String param2) {
        ForthFragment fragment = new ForthFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_forth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.signOut).setOnClickListener(v -> {
            ((MainActivity)getActivity()).logout();
            //FirebaseAuth.getInstance().signOut();
            //startActivity(new Intent(this.getActivity(),LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        });

        view.findViewById(R.id.downloadList).setOnClickListener(v -> {
            ((MainActivity)getActivity()).downloadCode();
        });

        view.findViewById(R.id.shareList).setOnClickListener(v -> {
            String code = ((MainActivity)getActivity()).addShareCode();
            ((MainActivity)getActivity()).shareCode(code);
        });

    }
}