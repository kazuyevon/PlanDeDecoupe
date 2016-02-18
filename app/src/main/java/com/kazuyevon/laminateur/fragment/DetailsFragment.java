package com.kazuyevon.laminateur.fragment;
/**
 * Created by Fabrice on 16/02/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kazuyevon.laminateur.R;

public class DetailsFragment extends Fragment {

    Bundle arguments;
    String logLam;
    View view;
    TextView texteFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Details");

        // handle fragment arguments
        arguments = this.getArguments();
        if (arguments != null) {
            logLam = arguments.getString("logLam");
        }
        //Recupere la vue en cours
        view = inflater.inflate(R.layout.fragment_texte_layout, null);
        texteFragment = (TextView) view.findViewById(R.id.textefragment);

        if (logLam != null) {
            texteFragment.setText(logLam);
        }
        return view;
    }
}
