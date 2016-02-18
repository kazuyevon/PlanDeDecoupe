package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;

public class DemoActivity extends Activity {

    private int laizeBobineMere = 1300;
    private int lisiereGauche = 30, lisiereDroite = 30;
    private int nbCouteaux = 15;
    private int lisiereRecoupeGauche = 10;
    private int[] regLaminateur;
    private Bundle lamContainer;
    private int[] laizeOrderListe = new int[]{528, 79, 178, 127};
    ;
    private int[] quantiteOrderListe = new int[]{8, 25, 45, 32};

    private Intent intentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**On cree un container pour passer des infos à la classe CommandeActivity*/
        lamContainer = new Bundle();
        regLaminateur = new int[]{laizeBobineMere, lisiereGauche, lisiereDroite, nbCouteaux, lisiereRecoupeGauche};
        lamContainer.putIntArray("regLaminateur", regLaminateur);
        lamContainer.putIntArray("laizeOrderListe", laizeOrderListe);
        lamContainer.putIntArray("nbOrderListe", quantiteOrderListe);
        intentResult = new Intent(DemoActivity.this, ResultActivity.class);
        intentResult.putExtras(lamContainer);
        startActivity(intentResult);
        DemoActivity.this.finish();
    }
}

