package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.kazuyevon.laminateur.models.MachineMandrin;
import com.kazuyevon.laminateur.models.MachineBobinot;

public class MainActivity extends AppCompatActivity {

    private int switchCalcStatus = 0;
    private MachineMandrin machineMandrin;
    private MachineBobinot machineBobinot;
    private int parse;
    private int parse2;

    private Toolbar toolbar;
    private EditText textLaizeBobineMere;
    private Button butLaizeBobineMere;
    private EditText textLisiereGauche, textLisiereDroite;
    private Button butLisiere;
    private EditText textNbCouteaux;
    private Button butNbCouteaux;
    private EditText textLisiereRecoupeGauche, textLisiereRecoupeDroite;
    private Button butLisiereRecoupe;
    private Switch butSwitchCalc;
    private Button butCalc;

    private Intent intentCommande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textLaizeBobineMere = (EditText) findViewById(R.id.textLaizeBobineMere);
        butLaizeBobineMere = (Button) findViewById(R.id.butLaizeBobineMere);
        butLisiere = (Button) findViewById(R.id.butLisiere);
        textLisiereGauche = (EditText) findViewById(R.id.textLisiereGauche);
        textLisiereDroite = (EditText) findViewById(R.id.textLisiereDroite);
        butLisiere = (Button) findViewById(R.id.butLisiere);
        textNbCouteaux = (EditText) findViewById(R.id.textNbCouteaux);
        butNbCouteaux = (Button) findViewById(R.id.butNbCouteaux);
        textLisiereRecoupeGauche = (EditText) findViewById(R.id.textLisiereRecoupeGauche);
        textLisiereRecoupeDroite = (EditText) findViewById(R.id.textLisiereRecoupeDroite);
        butLisiereRecoupe = (Button) findViewById(R.id.butLisiereRecoupe);
        butSwitchCalc = (Switch) findViewById(R.id.butSwitchCalc);
        butCalc = (Button) findViewById(R.id.butCalc);

        textLisiereRecoupeDroite.setEnabled(false);

        machineBobinot = new MachineBobinot();
        machineMandrin = new MachineMandrin();

        butLaizeBobineMere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parse = 0;
                if (textLaizeBobineMere.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.laize_manquante, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    parse = Integer.parseInt(textLaizeBobineMere.getText().toString());
                    if (switchCalcStatus == 0) {
                        machineBobinot.setLaizeMere(parse);
                    } else {
                        machineMandrin.setLaizeMere(parse);
                    }
                    textLaizeBobineMere.setEnabled(false);
                    butLaizeBobineMere.setEnabled(false);
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantSuivant();
                }
            }
        });
        butLisiere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parse = parse2 = 0;
                if (textLisiereGauche.getText().toString().matches("") || textLisiereDroite.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    parse = Integer.parseInt(textLisiereGauche.getText().toString());
                    parse2 = Integer.parseInt(textLisiereDroite.getText().toString());
                    if (switchCalcStatus == 0) {
                        machineBobinot.setLisiereGauche(parse);
                        machineBobinot.setLisiereDroite(parse2);
                    } else {
                        machineMandrin.setLisiereGauche(parse);
                        machineMandrin.setLisiereDroite(parse2);
                    }
                    textLisiereGauche.setEnabled(false);
                    textLisiereDroite.setEnabled(false);
                    butLisiere.setEnabled(false);
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantSuivant();
                }
            }
        });
        butNbCouteaux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int parse = 0;
                if (textNbCouteaux.getText().toString().matches("") && switchCalcStatus == 0) {
                    Toast.makeText(getApplicationContext(), R.string.couteaux_manquants, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 0) {
                        parse = Integer.parseInt(textNbCouteaux.getText().toString());
                        machineBobinot.setNbCouteaux(parse);
                        textNbCouteaux.setEnabled(false);
                        butNbCouteaux.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });
        butLisiereRecoupe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int parse = 0;
                if ((textLisiereRecoupeGauche.getText().toString().matches("")) && (switchCalcStatus == 0)) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_recoupe_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 0) {
                        parse = Integer.parseInt(textLisiereRecoupeGauche.getText().toString());
                        machineBobinot.setLisiereRecoupeGauche(parse);
                        textLisiereRecoupeGauche.setEnabled(false);
                        butLisiereRecoupe.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });

        //set the switch to OFF
        butSwitchCalc.setChecked(false);
        switchCalcStatus = 0;
        //attach a listener to check for changes in state
        butSwitchCalc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    /*Mode mandrin*/
                    switchCalcStatus = 1;
                    machineMandrin = new MachineMandrin("default");
                    machineBobinot = new MachineBobinot();
                    textLaizeBobineMere.setText(machineMandrin.getLaizeMereToString());
                    textLisiereGauche.setText(machineMandrin.getLisiereGaucheToString());
                    textLisiereDroite.setText(machineMandrin.getLisiereDroiteToString());
                    textNbCouteaux.setText(null);
                    textLisiereRecoupeGauche.setText(null);
                    textNbCouteaux.setHint(R.string.hintNbCouteauxM);
                    textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGaucheM);
                    textLaizeBobineMere.setEnabled(true);
                    textLisiereGauche.setEnabled(true);
                    textLisiereDroite.setEnabled(true);
                    textNbCouteaux.setEnabled(false);
                    textLisiereRecoupeGauche.setEnabled(false);
                    butLaizeBobineMere.setEnabled(true);
                    butLisiere.setEnabled(true);
                    butNbCouteaux.setEnabled(false);
                    butLisiereRecoupe.setEnabled(false);
                    butCalc.setEnabled(false);
                } else {
                    /*Mode Bobinot*/
                    switchCalcStatus = 0;
                    machineMandrin = new MachineMandrin();
                    machineBobinot = new MachineBobinot("default");
                    textLaizeBobineMere.setText(machineBobinot.getLaizeMereToString());
                    textLisiereGauche.setText(machineBobinot.getLisiereGaucheToString());
                    textLisiereDroite.setText(machineBobinot.getLisiereDroiteToString());
                    textNbCouteaux.setText(machineBobinot.getNbCouteauxToString());
                    textLisiereRecoupeGauche.setText(machineBobinot.getLisiereRecoupeGaucheToString());
                    textNbCouteaux.setHint(R.string.hintNbCouteaux);
                    textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
                    textLaizeBobineMere.setEnabled(true);
                    textLisiereGauche.setEnabled(true);
                    textLisiereDroite.setEnabled(true);
                    textNbCouteaux.setEnabled(true);
                    textLisiereRecoupeGauche.setEnabled(true);
                    butLaizeBobineMere.setEnabled(true);
                    butLisiere.setEnabled(true);
                    butNbCouteaux.setEnabled(true);
                    butLisiereRecoupe.setEnabled(true);
                    butCalc.setEnabled(false);
                }

            }
        });
        butCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**On cree un intent pour passer des infos à la classe CommandeActivity*/
                intentCommande = new Intent(MainActivity.this, CommandeActivity.class);
                if(switchCalcStatus == 0){
                    intentCommande.putExtra("machine", machineBobinot);
                }
                else if (switchCalcStatus == 1){
                    intentCommande.putExtra("machine", machineMandrin);
                }
                razActivity();
                startActivity(intentCommande);
            }
        });
    }

    private void razActivity(){
        butSwitchCalc.setChecked(false);
        textLaizeBobineMere.setText(null);
        textLisiereGauche.setText(null);
        textLisiereDroite.setText(null);
        textNbCouteaux.setText(null);
        textLisiereRecoupeGauche.setText(null);
        textNbCouteaux.setHint(R.string.hintNbCouteaux);
        textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
        textLaizeBobineMere.setEnabled(true);
        textLisiereGauche.setEnabled(true);
        textLisiereDroite.setEnabled(true);
        textNbCouteaux.setEnabled(true);
        textLisiereRecoupeGauche.setEnabled(true);
        butLaizeBobineMere.setEnabled(true);
        butLisiere.setEnabled(true);
        butNbCouteaux.setEnabled(true);
        butLisiereRecoupe.setEnabled(true);
        butCalc.setEnabled(false);

    }

    private void verifAvantSuivant() {
        if (switchCalcStatus == 0) {
            if (machineBobinot.isFull()
                    && !butLaizeBobineMere.isEnabled()
                    && !butLisiere.isEnabled()
                    && !butNbCouteaux.isEnabled()
                    && !butLisiereRecoupe.isEnabled())
            {
                butCalc.setEnabled(true);
            }

        } else if (switchCalcStatus == 1) {
            if (machineMandrin.isFull()
                    && !butLaizeBobineMere.isEnabled()
                    && !butLisiere.isEnabled())
            {
                butCalc.setEnabled(true);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.mode_demo) {
            razActivity();
            startActivity(new Intent(MainActivity.this, DemoActivity.class));
        }
        if (id == R.id.mode_credit) {
            razActivity();
            startActivity(new Intent(MainActivity.this, CreditActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                            //.setTitle(R.string.textQuit)
                    .setMessage(R.string.textReally_quit)
                    .setPositiveButton(R.string.butYes, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //Stop the activity
                            MainActivity.this.finish();
                        }
                    })
                    .setNegativeButton(R.string.butNo, null)
                    .show();

            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}