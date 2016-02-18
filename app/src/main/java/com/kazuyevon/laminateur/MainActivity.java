package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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


public class MainActivity extends AppCompatActivity {
    private int laizeBobineMere = 0;
    private int lisiereGauche = 0, lisiereDroite = 0;
    private int nbCouteaux = 0;
    private int lisiereRecoupeGauche = 0;
    private int switchCalcStatus = 0;

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

    private Bundle lamContainer;
    private int[] regLaminateur;
    private Intent intentCommande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        butLaizeBobineMere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textLaizeBobineMere.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.laize_manquante, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    textLaizeBobineMere.setEnabled(false);
                    laizeBobineMere = Integer.parseInt(textLaizeBobineMere.getText().toString());
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantSuivant();
                }
            }
        });
        butLisiere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textLisiereGauche.getText().toString().matches("") || textLisiereDroite.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    textLisiereGauche.setEnabled(false);
                    textLisiereDroite.setEnabled(false);
                    lisiereGauche = Integer.parseInt(textLisiereGauche.getText().toString());
                    lisiereDroite = Integer.parseInt(textLisiereDroite.getText().toString());
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantSuivant();
                }
            }
        });
        butNbCouteaux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textNbCouteaux.getText().toString().matches("") && switchCalcStatus == 0) {
                    Toast.makeText(getApplicationContext(), R.string.couteaux_manquants, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 1) {
                        nbCouteaux = 100;
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    } else {
                        textNbCouteaux.setEnabled(false);
                        nbCouteaux = Integer.parseInt(textNbCouteaux.getText().toString());
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });
        butLisiereRecoupe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((textLisiereRecoupeGauche.getText().toString().matches("")) && (switchCalcStatus == 0)) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_recoupe_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 1) {
                        lisiereRecoupeGauche = 0;
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    } else {
                        textLisiereRecoupeGauche.setEnabled(false);
                        lisiereRecoupeGauche = Integer.parseInt(textLisiereRecoupeGauche.getText().toString());
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });

        //set the switch to OFF
        butSwitchCalc.setChecked(false);
        //attach a listener to check for changes in state
        butSwitchCalc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    switchCalcStatus = 1;
                    textLaizeBobineMere.setText("1950");
                    textLaizeBobineMere.setEnabled(true);
                    textLisiereGauche.setText("50");
                    textLisiereGauche.setEnabled(true);
                    textLisiereDroite.setText("10");
                    textLisiereDroite.setEnabled(true);
                    textNbCouteaux.setText(null);
                    textNbCouteaux.setHint(R.string.hintNbCouteauxM);
                    textNbCouteaux.setEnabled(false);
                    textLisiereRecoupeGauche.setText(null);
                    textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGaucheM);
                    textLisiereRecoupeGauche.setEnabled(false);
                    butCalc.setEnabled(false);
                } else {
                    switchCalcStatus = 0;
                    textLaizeBobineMere.setText("1300");
                    textLaizeBobineMere.setEnabled(true);
                    textLisiereGauche.setText("30");
                    textLisiereGauche.setEnabled(true);
                    textLisiereDroite.setText("30");
                    textLisiereDroite.setEnabled(true);
                    textNbCouteaux.setText("15");
                    textNbCouteaux.setHint(R.string.hintNbCouteaux);
                    textNbCouteaux.setEnabled(true);
                    textLisiereRecoupeGauche.setText("10");
                    textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
                    textLisiereRecoupeGauche.setEnabled(true);
                    butCalc.setEnabled(false);
                }

            }
        });
        butCalc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**On cree un container pour passer des infos à la classe CommandeActivity*/
                lamContainer = new Bundle();
                regLaminateur = new int[]{laizeBobineMere, lisiereGauche, lisiereDroite, nbCouteaux, lisiereRecoupeGauche};
                lamContainer.putIntArray("regLaminateur", regLaminateur);

                intentCommande = new Intent(MainActivity.this, CommandeActivity.class);
                intentCommande.putExtras(lamContainer);
                textLaizeBobineMere.setEnabled(true);
                textLaizeBobineMere.setText("");
                textLisiereGauche.setEnabled(true);
                textLisiereGauche.setText("");
                textLisiereDroite.setEnabled(true);
                textLisiereDroite.setText("");
                textNbCouteaux.setEnabled(true);
                textNbCouteaux.setText("");
                textNbCouteaux.setHint(R.string.hintNbCouteaux);
                textLisiereRecoupeGauche.setEnabled(true);
                textLisiereRecoupeGauche.setText("");
                textLisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
                butSwitchCalc.setChecked(false);

                startActivity(intentCommande);
            }

            ;
        });
    }

    private void verifAvantSuivant() {
        if (switchCalcStatus == 0) {
            if ((laizeBobineMere != 0) && (lisiereGauche != 0) && (lisiereDroite != 0) && (nbCouteaux != 0) && (lisiereRecoupeGauche != 0)) {
                butCalc.setEnabled(true);
                return;
            }
            ;
        } else if (switchCalcStatus == 1) {
            if ((laizeBobineMere != 0) && (lisiereGauche != 0) && (lisiereDroite != 0)) {
                butCalc.setEnabled(true);
                return;
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
            startActivity(new Intent(MainActivity.this, DemoActivity.class));
        }
        if (id == R.id.mode_credit) {
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
