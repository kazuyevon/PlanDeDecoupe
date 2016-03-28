package com.kazuyevon.laminateur.fragment;
/**
 * Created by Fabrice on 16/02/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kazuyevon.laminateur.R;

public class ReglagesFragment extends Fragment {

    Bundle arguments;
    String[] listeReglages;
    View view;
    ListView listeFragment;
    ArrayAdapter<String> adapterListeReglages;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Reglages");

        // handle fragment arguments
        arguments = this.getArguments();
        if (arguments != null) {
            listeReglages = arguments.getStringArray("listeReglages");
        }
        //Recupere la vue en cours
        view = inflater.inflate(R.layout.fragment_liste_layout, null);
        listeFragment = (ListView) view.findViewById(R.id.listefragment);

        if (listeReglages != null) {
            adapterListeReglages = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_fragment, listeReglages);
            listeFragment.setAdapter(adapterListeReglages);
        }
        return view;
    }
}
