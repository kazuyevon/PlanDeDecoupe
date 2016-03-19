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
 * Created by Fabrice on 17/03/2016.
 */
public class DemoPaireActivity extends AppCompatActivity {
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

        /*test paire*/
        laizeBobineMere = 1900;
        lisiereGauche = lisiereDroite = 30;
        nbCouteaux = 15;
        lisiereRecoupeGauche = 10;
        machineBobinot = new MachineBobinot(laizeBobineMere, lisiereGauche, lisiereDroite, nbCouteaux, lisiereRecoupeGauche);
        commande = new ArrayList<Commande>();
        /*commande.add(new Commande(laizeOrder, quantiteOrder);*/
        commande.add(new Commande(940, 2));
        commande.add(new Commande(170, 4));
        commande.add(new Commande(240, 4));
        commande.add(new Commande(400, 4));
        commande.add(new Commande(90, 4));
        commande.add(new Commande(510, 4));
        commande.add(new Commande(580, 2));
        /*nbBobinots = 24*/

        /**On cree un intent pour passer des infos à la classe ResultPaireActivity*/
        intentResult = new Intent(DemoPaireActivity.this, ResultPaireActivity.class);
        intentResult.putExtra("machine", machineBobinot);
        intentResult.putExtra("commande", (Serializable)commande);
        startActivity(intentResult);
        DemoPaireActivity.this.finish();
    }
}
