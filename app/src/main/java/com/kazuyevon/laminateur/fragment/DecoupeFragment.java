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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kazuyevon.laminateur.R;

public class DecoupeFragment extends Fragment {

    Bundle arguments;
    String[] listeDecoupe;
    View view;
    ListView listeFragment;
    ArrayAdapter<String> adapterListeDecoupe;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Plan de Decoupe");

        // handle fragment arguments
        arguments = this.getArguments();
        if (arguments != null) {
            listeDecoupe = arguments.getStringArray("listeDecoupe");
        }
        //Recupere la vue en cours
        view = inflater.inflate(R.layout.fragment_liste_layout, null);
        listeFragment = (ListView) view.findViewById(R.id.listefragment);

        if (listeDecoupe != null) {
            adapterListeDecoupe = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_1, listeDecoupe);
            listeFragment.setAdapter(adapterListeDecoupe);
        }
        return view;
    }
}
