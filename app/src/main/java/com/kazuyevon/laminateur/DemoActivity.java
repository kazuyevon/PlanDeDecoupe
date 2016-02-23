package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;



public class DemoActivity extends Activity {

    private MachineBobinot machineBobinot;
    List<Commande> commande;
    private Intent intentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**On cree un intent pour passer des infos à la classe CommandeActivity*/
        intentResult = new Intent(DemoActivity.this, ResultActivity.class);

        machineBobinot = new MachineBobinot(1300, 30, 30, 15, 10);
        commande = new ArrayList<Commande>();
        commande.add(new Commande(528, 8));  // add another row
        commande.add(new Commande(79, 25));  // add another row
        commande.add(new Commande(178, 45));  // add another row
        commande.add(new Commande(127, 32));  // add another row

        intentResult.putExtra("machine", machineBobinot);
        intentResult.putExtra("commande", (Serializable)commande);
        startActivity(intentResult);
        DemoActivity.this.finish();
    }
}

