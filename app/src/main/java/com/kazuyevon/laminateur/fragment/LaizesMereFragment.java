package com.kazuyevon.laminateur.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kazuyevon.laminateur.R;
import com.kazuyevon.laminateur.models.ValeursLaminateur;
import com.kazuyevon.laminateur.toolsadapter.MyArrayAdapter;

/**
 * Created by Fabrice on 25/03/2016.
 */
public class LaizesMereFragment extends Fragment {

    public interface onSomeEventListener {
        //interface servant à communiquer l'item cliqué sur la listview et implémenter sur MainActivity
        public void getItemLaizesMere(String laizeMere);
        public String getValeursLaminateurPrefs();
    }

    View view;
    View view2;
    LayoutInflater inflater2;
    ListView listview_LaizesMere;
    TextView textbox_LaizeMere;
    String[] valeursLaizesMere;
    String[] listeLaizesMere;
    MyArrayAdapter laizesMeresAdapter;
    int positionItem;
    Button but_close;
    //implemente la methode de MainActity grace à l'interface
    onSomeEventListener someEventListener;
    Context context;

    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        AppCompatActivity a = null;

        if (context instanceof AppCompatActivity) {
            a = (AppCompatActivity) context;
        }

        try {
            someEventListener = (onSomeEventListener) a;
        } catch (ClassCastException e) {
            throw new ClassCastException(a.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /**Recupere la vue en cours. C'est à dire le layout fragment_laizeMere.xml.*/
        view = inflater.inflate(R.layout.fragment_laizesmere, container, false);
        /**Declare la ListView contenu dans ce layout.*/
        listview_LaizesMere = (ListView) view.findViewById(R.id.LVLaizesMere);
        /**Declare aussi le Button.*/
        but_close = (Button) view.findViewById(R.id.buttonClose);
        /**Met le background du layout en blanc, sinon il est transparent par défaut.*/
        view.setBackgroundColor(Color.WHITE);

        /**Permet au bouton de fermer cette vue.*/
        but_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                view.setVisibility(View.INVISIBLE);
            }
        });
        positionItem = 0;
        /**Charge les valeurs des laizes.*/
        ValeursLaminateur valeursLaminateur;
        if ( someEventListener.getValeursLaminateurPrefs() == null){
            valeursLaminateur = new ValeursLaminateur();
        }
        else{
            valeursLaminateur = new ValeursLaminateur(someEventListener.getValeursLaminateurPrefs());
        }

        valeursLaizesMere = valeursLaminateur.getLaizesBobineMereToString();
        /**Rajouter "autre..." à la liste valeursLaizeMere.*/
        listeLaizesMere = new String[valeursLaizesMere.length];
        for (int i = 0; i < valeursLaizesMere.length; i++) {
            listeLaizesMere[i] = valeursLaizesMere[i];
        }
        listeLaizesMere[listeLaizesMere.length - 1] = getResources().getString(R.string.autres);

        /**Prépare MyArrayAdapter personnalisé en délarant le layout list_item_fragment_laizesmere.xml à utiliser
         *  pour les items à afficher.*/
        if (listeLaizesMere != null) {

            laizesMeresAdapter = new MyArrayAdapter(getActivity().getApplicationContext(), R.layout.list_item_fragment_laizesmere, listeLaizesMere);
            /**Déclare un listener pour la selection d'un item dans la ListView listview_LaizesMere.*/
            listview_LaizesMere.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    positionItem = position;
                    Toast.makeText(getActivity().getApplicationContext(), "Laize choisi :" + listeLaizesMere[positionItem], Toast.LENGTH_LONG).show();
                    //Rend invisible
                    view.setVisibility(View.INVISIBLE);
                    //transmet itemLaizesMere
                    someEventListener.getItemLaizesMere(listeLaizesMere[positionItem]);
                }
            });

            listview_LaizesMere.setAdapter(laizesMeresAdapter);
            inflater2 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view2 = inflater2.inflate(R.layout.list_item_fragment_laizesmere, container, false);
            textbox_LaizeMere = (TextView) view2.findViewById(R.id.textbox_LaizeMere);
            textbox_LaizeMere.setTextColor(Color.RED);

        }
        // Inflate the layout for this fragment
        return view;
    }

    public int getPositionItem() {
        return positionItem;
    }
}