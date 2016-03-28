package com.kazuyevon.laminateur;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kazuyevon.laminateur.models.RandomListe;
import com.kazuyevon.laminateur.toolsadapter.ResultRandomArrayAdapter;
import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.ValeursLaminateur;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Fabrice on 20/03/2016.
 */
public class AfficheResultRandomActivity extends AppCompatActivity {

    protected String TAG = "AfficheResultRandomActivity";
    protected Bundle lamContainer;
    protected TextView textview_Random;
    protected TextView textview_TauxPertes;
    protected TextView textview_TauxChgtBobine;
    protected ListView listview_Random;
    protected String texte;
    protected ValeursLaminateur valeursLaminateur;
    protected int[] listeLaizesBobineMere;
    protected int nbLaizesBobineMere;
    protected int[] pertesListeConvert;
    protected int[] nbBobinesListeConvert;
    protected int[] laizeRestockListeConvert;
    protected int[] nbRecoupesListeConvert;
    protected MachineBobinot machineBobinot;
    protected List<Commande> commande;
    protected ResultRandomArrayAdapter ResultRandomArrayAdapter;
    protected int positionItem;
    protected AlertDialog.Builder confirmBinCutting;
    protected Intent intentResult;
    protected int defaultColor;
    protected int green;
    protected int yellow;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);

        /**On récupère l'objet Bundle envoyé par ResultActivity ou ResultPaireACtivity*/
        lamContainer = this.getIntent().getExtras();
        /**On récupère les données du Bundle*/
        if (lamContainer != null
                && lamContainer.containsKey("pertesListeConvert")
                && lamContainer.containsKey("nbBobinesListeConvert")
                && lamContainer.containsKey("laizeRestockListeConvert")
                && lamContainer.containsKey("nbRecoupesListeConvert")
                && lamContainer.containsKey("commande")
                && lamContainer.containsKey("machineBobinot")) {
            pertesListeConvert = this.getIntent().getIntArrayExtra("pertesListeConvert");
            nbBobinesListeConvert = this.getIntent().getIntArrayExtra("nbBobinesListeConvert");
            laizeRestockListeConvert = this.getIntent().getIntArrayExtra("laizeRestockListeConvert");
            nbRecoupesListeConvert = this.getIntent().getIntArrayExtra("nbRecoupesListeConvert");
            commande = (List)this.getIntent().getSerializableExtra("commande");
            machineBobinot = new MachineBobinot();
            machineBobinot = (MachineBobinot)this.getIntent().getSerializableExtra("machineBobinot");

            textview_Random = (TextView) findViewById(R.id.textview_Random);

            textview_TauxPertes = (TextView) findViewById(R.id.textview_TauxPertes);
            textview_TauxChgtBobine = (TextView) findViewById(R.id.textview_TauxChgtBobine);
            defaultColor = getResources().getColor(R.color.defaultColor);
            green = getResources().getColor(R.color.customGreen);
            yellow = getResources().getColor(R.color.customYellow);
            textview_TauxPertes.setBackgroundColor(green);
            textview_TauxChgtBobine.setBackgroundColor(yellow);
            textview_TauxPertes.setText(getResources().getString(R.string.textView_tauxPertes));
            textview_TauxChgtBobine.setText(getResources().getString(R.string.textView_tauxChgtBobine));

            listview_Random =  (ListView) findViewById(R.id.listview_Random);

            texte = getResources().getString(R.string.texte_commande) + " :\n";
            valeursLaminateur = new ValeursLaminateur();
            listeLaizesBobineMere = valeursLaminateur.getLaizesBobineMere();
            nbLaizesBobineMere = valeursLaminateur.getNbLaizesBobineMere();
            for (int i = 0; i<commande.size(); i++){

                texte += commande.get(i).getLaizeOrder() +" x " + commande.get(i).getQuantiteOrder() + "\n";
            }
            texte += "\n" + getResources().getString(R.string.liste_des_essais) + " :";

            if(texte.matches("")) {
                Log.i(TAG,"Texte vide");
            }
            else{
                textview_Random.setText(texte);
            }

            /**Utilise l'objet RandomListe pour l'envoyer à l'adapter resultRandomArrayAdapter personnalisé.*/
            final RandomListe randomItem = new RandomListe();
            /**Envoie les resources pour gérer les tags de traduction.*/
            final Resources resources = getResources();
            final RandomListe[] randomListe = randomItem.setRandomListe(resources, pertesListeConvert, nbBobinesListeConvert, nbRecoupesListeConvert);

            /**Initialise l'adapter custom pour la listview en déclarant le layout personnalisé.*/
            ResultRandomArrayAdapter = new ResultRandomArrayAdapter(this, R.layout.list_item_random, randomListe);

            final int pertesMin = randomItem.getPertesMin(pertesListeConvert);
            final int[] pertesMinPosition = randomItem.getPertesMinPosition(pertesListeConvert);
            final int nbBobinesMin = randomItem.getNbBobinesMin(nbBobinesListeConvert, nbRecoupesListeConvert);
            final int[] nbBobinesMinPosition = randomItem.getNbBobinesMinPosition(nbBobinesListeConvert, nbRecoupesListeConvert);
            ResultRandomArrayAdapter.setPertesMin(pertesMinPosition);
            ResultRandomArrayAdapter.setNbBobinesMin(nbBobinesMinPosition);


            /**Initialise confirmBinCutting.*/
            confirmBinCutting = new AlertDialog.Builder(this);
            confirmBinCutting.setTitle(getResources().getString(R.string.confirmBinCutting_title));
            confirmBinCutting.setPositiveButton(R.string.butYes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.confirmBinCutting_text) + " !", Toast.LENGTH_SHORT).show();
                    intentResult = new Intent(AfficheResultRandomActivity.this, ResultActivity.class);

                    /**Remplace la laize reçu par la laize selectionné dans la liste de choix*/
                    machineBobinot.setLaizeMere(valeursLaminateur.getOneLaizeBobineMere(positionItem));
                    intentResult.putExtra("machine", machineBobinot);
                    intentResult.putExtra("commande", (Serializable) commande);
                    startActivity(intentResult);
                    AfficheResultRandomActivity.this.finish();
                }
            });
            confirmBinCutting.setNegativeButton(R.string.butNo, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            // Create the AlertDialog object
            confirmBinCutting.create();

            /**Cree la listView*/
            listview_Random.setAdapter(ResultRandomArrayAdapter);


            Log.i(TAG, " pertes " + randomItem.getPertesMin(pertesListeConvert));
            for (int i = 0; i < pertesMinPosition.length; i++){
                Log.i(TAG, "Test pertes, pertes : " + pertesMin + ", positions : " + pertesMinPosition[i]);
            }
            for (int i = 0; i < nbBobinesMinPosition.length; i++){
                Log.i(TAG, "Test chgt bobines, nb chgt : " + nbBobinesMin + " positions : " + nbBobinesMinPosition[i]);
            }
            /**Cree une liste RandomListe en version String pour l'affichage dans le dialog.*/
            final String[] randomListeToString = randomItem.getRandomListeToString(randomListe);
            listview_Random.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(getApplicationContext(), randomListe[position], Toast.LENGTH_LONG).show();
                    /**randomListe est ue liste d'objet, à l'affichage il faut que ce soit une liste de String.*/
                    positionItem = position;
                    confirmBinCutting.setMessage(randomListeToString[positionItem].toString());
                    confirmBinCutting.show();
                }
            });
        }
        else{
            Log.e(TAG, "Pas de reception");
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AfficheResultRandomActivity.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}