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

public class PertesFragment extends Fragment {

    Bundle arguments;
    String[] listePertes;
    View view;
    ListView listeFragment;
    ArrayAdapter<String> adapterListePertes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Pertes");

        // handle fragment arguments
        arguments = this.getArguments();
        if (arguments != null) {
            listePertes = arguments.getStringArray("listePertes");
        }
        //Recupere la vue en cours
        view = inflater.inflate(R.layout.fragment_liste_layout, null);
        listeFragment = (ListView) view.findViewById(R.id.listefragment);

        if (listePertes != null) {
            adapterListePertes = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_1, listePertes);
            listeFragment.setAdapter(adapterListePertes);
        }
        return view;
    }
}
