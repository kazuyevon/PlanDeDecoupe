package com.kazuyevon.laminateur;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabrice on 20/03/2016.
 */
public class DemoRandomActivity extends AppCompatActivity {

    private int laizeBobineMere;
    private int lisiereGauche;
    private int lisiereDroite;
    private int nbCouteaux;
    private int lisiereRecoupeGauche;
    private MachineBobinot machineBobinot;
    private List<Commande> commande;
    private Intent intentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        laizeBobineMere = 0;
        lisiereGauche = lisiereDroite = 30;
        nbCouteaux = 15;
        lisiereRecoupeGauche = 10;
        machineBobinot = new MachineBobinot(laizeBobineMere, lisiereGauche, lisiereDroite, nbCouteaux, lisiereRecoupeGauche);
        commande = new ArrayList<Commande>();
        /*commande.add(new Commande(laizeOrder, quantiteOrder);*/
        commande.add(new Commande(528, 8));  // add another row
        commande.add(new Commande(79, 25));  // add another row
        commande.add(new Commande(178, 45));  // add another row
        commande.add(new Commande(127, 32));  // add another row
        /*nbBobinots = 110*/

        /**On cree un intent pour passer des infos à la classe ResultActivity*/
        intentResult = new Intent(DemoRandomActivity.this, ResultRandomActivity.class);
        intentResult.putExtra("machine", machineBobinot);
        intentResult.putExtra("commande", (Serializable)commande);
        startActivity(intentResult);
        DemoRandomActivity.this.finish();
    }
}

