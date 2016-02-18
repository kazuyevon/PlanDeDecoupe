package com.kazuyevon.laminateur.fragment;
/**
 * Created by Fabrice on 16/02/2016.
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kazuyevon.laminateur.R;

public class CommandeFragment extends android.support.v4.app.Fragment {
    Bundle arguments;
    String[] listeCommande;
    View view;
    ListView listeFragment;
    ArrayAdapter<String> adapterListeCommande;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // handle fragment arguments
        arguments = this.getArguments();
        if (arguments != null) {
            listeCommande = arguments.getStringArray("listeCommande");
        }
        //Recupere la vue en cours
        view = inflater.inflate(R.layout.fragment_liste_layout, null);
        listeFragment = (ListView) view.findViewById(R.id.listefragment);

        if (listeCommande != null) {
            adapterListeCommande = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_item_1, listeCommande);
            listeFragment.setAdapter(adapterListeCommande);
        }
        return view;
    }
}
