package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import com.kazuyevon.laminateur.func.FuncNbBobinots;
import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.MachineMandrin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommandeActivity extends AppCompatActivity {

    private String TAG ="CommandeActivity";
    private Intent intentCommande;
    private Bundle extras;
    private MachineMandrin machineMandrin;
    private MachineBobinot machineBobinot;
    private Object machine;

    private int nbLaizeDiff = 0;
    private int laizeOrder = 0;
    private int quantiteOrder = 0;

    List<Commande> commande;

    private int countNbLaizeDiff = 0;

    private Intent intentResult;

    private Toolbar toolbar;
    private EditText textNbLaizeDiff;
    private Button butNbLaizeDiff;
    private EditText textLaizeOrder;
    private EditText textQuantiteOrder;
    private Button butAjouter;
    private CheckBox checkBoxPaire;
    private int checkBoxPaireStatus;
    private TextView textOrderListe;
    private Button butCalculer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textNbLaizeDiff = (EditText) findViewById(R.id.textNbLaizeDiff);
        butNbLaizeDiff = (Button) findViewById(R.id.butNbLaizeDiff);
        textLaizeOrder = (EditText) findViewById(R.id.textLaizeOrder);
        textQuantiteOrder = (EditText) findViewById(R.id.textQuantiteOrder);
        butAjouter = (Button) findViewById(R.id.butAjouter);
        checkBoxPaire = (CheckBox) findViewById(R.id.checkBoxPaire);
        textOrderListe = (TextView) findViewById(R.id.textOrderListe);
        butCalculer = (Button) findViewById(R.id.butCalculer);

        machineMandrin = new MachineMandrin();
        machineBobinot = new MachineBobinot();

        /**On récupère les données de l'intent.*/
        try{
            intentCommande = this.getIntent();
            extras = intentCommande.getExtras();

            if (intentCommande.hasExtra("machine")){
                machine = extras.getSerializable("machine");
            }
            if (machine.getClass() == machineMandrin.getClass()){
                machineMandrin = (MachineMandrin) machine;
                Toast.makeText(getApplicationContext(), "Mode Mandrin", Toast.LENGTH_SHORT).show();
                checkBoxPaire.setVisibility(View.INVISIBLE);
            }
            else if (machine.getClass() == machineBobinot.getClass()){
                machineBobinot = (MachineBobinot) machine;
                Toast.makeText(getApplicationContext(), "Mode Bobinot", Toast.LENGTH_SHORT).show();
                checkBoxPaire.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Pas de reception");
            this.finish();
        };

        commande = new ArrayList<Commande>();

        butNbLaizeDiff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textNbLaizeDiff.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.nbLaizeDiff_manquant, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    textNbLaizeDiff.setEnabled(false);
                    nbLaizeDiff = Integer.parseInt(textNbLaizeDiff.getText().toString());
                    butNbLaizeDiff.setEnabled(false);
                    textOrderListe.setText("Nombre de commande : " + nbLaizeDiff);
                }
            }
        });

        butAjouter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**si les EditText sont vides, on valide pas.*/
                if (!butNbLaizeDiff.isEnabled()) {
                    if (textQuantiteOrder.getText().toString().isEmpty() || textLaizeOrder.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), R.string.order_manquant, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        laizeOrder = Integer.parseInt(textLaizeOrder.getText().toString());
                        quantiteOrder = Integer.parseInt(textQuantiteOrder.getText().toString());
                        commande.add(new Commande(laizeOrder, quantiteOrder));
                        textOrderListe.setText(textOrderListe.getText().toString() + "\ncommande " + (countNbLaizeDiff + 1) + " : " + commande.get(countNbLaizeDiff).getLaizeOrder() + " X " + commande.get(countNbLaizeDiff).getQuantiteOrder());
                        textLaizeOrder.setText(null);
                        textQuantiteOrder.setText(null);
                        countNbLaizeDiff++;
                        laizeOrder = 0;
                        quantiteOrder = 0;
                        nbLaizeDiff -= 1;
                        verifAvantCalculer();

                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.order_manquant, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        //set the checkBox to false
        checkBoxPaire.setChecked(false);
        checkBoxPaireStatus = 0;
        //attach a listener to check for changes in state
        checkBoxPaire.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    intentResult = new Intent(CommandeActivity.this, ResultPaireActivity.class);
                }
                else {
                    intentResult = new Intent(CommandeActivity.this, ResultActivity.class);
                }
            }
        });


        butCalculer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (new FuncNbBobinots().CalculeNbBobinots(commande) % 2 != 0) {
                    Toast.makeText(getApplicationContext(), "nb de bobinots impair", Toast.LENGTH_SHORT).show();
                    CommandeActivity.this.finish();
                }
                intentResult.putExtra("machine", (Serializable) machine);
                intentResult.putExtra("commande", (Serializable)commande);
                startActivity(intentResult);
                CommandeActivity.this.finish();
            }
        });
    }

    private void verifAvantCalculer() {

        if (nbLaizeDiff == 0) {
            textLaizeOrder.setEnabled(false);
            textQuantiteOrder.setEnabled(false);
            butCalculer.setEnabled(true);
            butAjouter.setEnabled(false);
        }
        else {
            textLaizeOrder.setFocusable(true);
            textLaizeOrder.requestFocus();
        }

    }

    /*@Override
    *//** Si changement d\'orientation, garde l'etat des EditBox a enabled ou non
     * voir ligne dans le manifest, android:configChanges="orientation|keyboardHidden|screenSize" *//*
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        *//**Sauve les etats des composants*//*
        if (outState != null) {
            enabledTextNbLaizeDiff = textNbLaizeDiff.isEnabled();
            enabledTextLaizeOrder = textLaizeOrder.isEnabled();
            enabledTextQuantiteOrder = textQuantiteOrder.isEnabled();
            outState.putBoolean("textNbLaizeDiff", enabledTextNbLaizeDiff);
            outState.putBoolean("textLaizeOrder", enabledTextLaizeOrder);
            outState.putBoolean("textQuantiteOrder", enabledTextQuantiteOrder);
            *//**si les EditText sont deja desactive, on ne desactive pas le bouton*//*
            if (enabledTextNbLaizeDiff == true || enabledTextLaizeOrder == true || enabledTextQuantiteOrder == true) {
                enabledButCalculer = false;
            } else {
                enabledButCalculer = true;
            }
            outState.putBoolean("butCalculer", enabledButCalculer);
            outState.putInt("nbLaizeDiff", nbLaizeDiff);
            outState.putInt("laizeOrder", laizeOrder);
            outState.putInt("quantiteOrder", quantiteOrder);
            //outState.putIntArray("laizeOrderListe", laizeOrderListe);
            //outState.putIntArray("quantiteOrderListe", quantiteOrderListe);
            outState.putInt("countNbLaizeDiff", countNbLaizeDiff);

        }
    }*/

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        *//**Restore les etats des composants*//*
        enabledTextNbLaizeDiff = savedInstanceState.getBoolean("textNbLaizeDiff");
        enabledTextLaizeOrder = savedInstanceState.getBoolean("textLaizeOrder");
        enabledTextQuantiteOrder = savedInstanceState.getBoolean("textQuantiteOrder");
        enabledButCalculer = savedInstanceState.getBoolean("butCalculer");
        textNbLaizeDiff.setEnabled(enabledTextNbLaizeDiff);
        textLaizeOrder.setEnabled(enabledTextLaizeOrder);
        textQuantiteOrder.setEnabled(enabledTextQuantiteOrder);
        butCalculer.setEnabled(enabledButCalculer);
        nbLaizeDiff = savedInstanceState.getInt("nbLaizeDiff");
        laizeOrder = savedInstanceState.getInt("laizeOrder");
        quantiteOrder = savedInstanceState.getInt("quantiteOrder");
        //laizeOrderListe = savedInstanceState.getIntArray("laizeOrderListe");
        //quantiteOrderListe = savedInstanceState.getIntArray("quantiteOrderListe");
        countNbLaizeDiff = savedInstanceState.getInt("countNbLaizeDiff");


    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            CommandeActivity.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

