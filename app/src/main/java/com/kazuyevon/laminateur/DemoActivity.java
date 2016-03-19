package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;



public class DemoActivity extends AppCompatActivity {

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

        laizeBobineMere = 1300;
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
        intentResult = new Intent(DemoActivity.this, ResultActivity.class);
        intentResult.putExtra("machine", machineBobinot);
        intentResult.putExtra("commande", (Serializable)commande);
        startActivity(intentResult);
        DemoActivity.this.finish();
    }
}

