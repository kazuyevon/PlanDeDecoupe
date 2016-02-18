package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

public class CommandeActivity extends AppCompatActivity {

    private int laizeBobineMere = 0;
    private int lisiereGauche = 0, lisiereDroite = 0;
    private int nbCouteaux = 0;
    private int lisiereRecoupeGauche = 0;
    private int[] regLaminateur;
    private Bundle lamContainer;

    private int nbLaizeDiff = 0;
    private int laizeOrder = 0;
    private int quantiteOrder = 0;

    private int[] laizeOrderListe;
    private int[] quantiteOrderListe;
    private int countNbLaizeDiff = 0;

    private Intent intentResult;

    private EditText textNbLaizeDiff;
    private Button butNbLaizeDiff;
    private EditText textLaizeOrder;
    private Button butLaizeOrder;
    private EditText textQuantiteOrder;
    private Button butQuantiteOrder;
    private TextView textOrderListe;
    private Button butCalculer;

    private Boolean enabledTextNbLaizeDiff;
    private Boolean enabledTextLaizeOrder;
    private Boolean enabledTextQuantiteOrder;
    private Boolean enabledButCalculer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textNbLaizeDiff = (EditText) findViewById(R.id.textNbLaizeDiff);
        butNbLaizeDiff = (Button) findViewById(R.id.butNbLaizeDiff);
        textLaizeOrder = (EditText) findViewById(R.id.textLaizeOrder);
        butLaizeOrder = (Button) findViewById(R.id.butLaizeOrder);
        textQuantiteOrder = (EditText) findViewById(R.id.textQuantiteOrder);
        butQuantiteOrder = (Button) findViewById(R.id.butQuantiteOrder);
        textOrderListe = (TextView) findViewById(R.id.textOrderListe);
        butCalculer = (Button) findViewById(R.id.butCalculer);

        /**On récupère l'objet Bundle envoyé par MainActivity*/
        lamContainer = this.getIntent().getExtras();

        /**On récupère les données du Bundle*/
        if (lamContainer != null && lamContainer.containsKey("regLaminateur")) {

            regLaminateur = this.getIntent().getIntArrayExtra("regLaminateur");
            laizeBobineMere = regLaminateur[0];
            lisiereGauche = regLaminateur[1];
            lisiereDroite = regLaminateur[2];
            nbCouteaux = regLaminateur[3];
            lisiereRecoupeGauche = regLaminateur[4];
            if (nbCouteaux == 0) {
                nbCouteaux = 100;
            }
        }

        butNbLaizeDiff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (textNbLaizeDiff.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), R.string.nbLaizeDiff_manquant, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    textNbLaizeDiff.setEnabled(false);
                    nbLaizeDiff = Integer.parseInt(textNbLaizeDiff.getText().toString());
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantCalculer();
                }
            }
        });

        butLaizeOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!textNbLaizeDiff.getText().toString().matches("")) {
                    if (textLaizeOrder.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), R.string.laizeOrder_manquant, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        textLaizeOrder.setEnabled(false);
                        laizeOrder = Integer.parseInt(textLaizeOrder.getText().toString());
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantCalculer();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), R.string.nbLaizeDiff_manquant, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        butQuantiteOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**si les EditText sont vides, on valide pas.*/
                if (!textNbLaizeDiff.getText().toString().matches("") && !textLaizeOrder.getText().toString().matches("")) {
                    if (textQuantiteOrder.getText().toString().matches("")) {
                        Toast.makeText(getApplicationContext(), R.string.quantiteOrder_manquant, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        textQuantiteOrder.setEnabled(false);
                        quantiteOrder = Integer.parseInt(textNbLaizeDiff.getText().toString());
                        /**Verifie condition avant d'activer le bouton Calculer*/
                        verifAvantCalculer();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), R.string.nbLaizeDiffOuLaizeOrder_manquants, Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
        butCalculer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                /** on va cree les listes laizeOrderListe et nbOrderListe*/
                if (nbLaizeDiff != 0 && laizeOrder != 0 && quantiteOrder != 0 && textNbLaizeDiff.isEnabled() == false && textLaizeOrder.isEnabled() == false && textQuantiteOrder.isEnabled() == false) {
                    nbLaizeDiff = Integer.parseInt(textNbLaizeDiff.getText().toString());
                    if (laizeOrderListe == null && quantiteOrderListe == null) {/**Si listes vides*/
                        laizeOrderListe = new int[nbLaizeDiff];
                        quantiteOrderListe = new int[nbLaizeDiff];
                        laizeOrder = Integer.parseInt(textLaizeOrder.getText().toString());
                        quantiteOrder = Integer.parseInt(textQuantiteOrder.getText().toString());
                        laizeOrderListe[countNbLaizeDiff] = laizeOrder;
                        quantiteOrderListe[countNbLaizeDiff] = quantiteOrder;
                        Toast.makeText(getApplicationContext(), "Creation bases de donnees et entree bases de donnees :\n" + (countNbLaizeDiff + 1) + " : " + laizeOrderListe[countNbLaizeDiff] + " x " + quantiteOrderListe[countNbLaizeDiff], Toast.LENGTH_LONG).show();

                        /**Affiche les commande à la suite*/
                        textOrderListe.setText("commande " + (countNbLaizeDiff + 1) + " : " + laizeOrder + " x " + quantiteOrder);

                        countNbLaizeDiff++;
                        textLaizeOrder.setText(null);
                        textLaizeOrder.setEnabled(true);
                        textQuantiteOrder.setText(null);
                        textQuantiteOrder.setEnabled(true);
                        return;
                    } else {/** si liste non vides*/

                        laizeOrder = Integer.parseInt(textLaizeOrder.getText().toString());
                        quantiteOrder = Integer.parseInt(textQuantiteOrder.getText().toString());
                        laizeOrderListe[countNbLaizeDiff] = laizeOrder;
                        quantiteOrderListe[countNbLaizeDiff] = quantiteOrder;
                        //Toast.makeText(getApplicationContext(), "Entree bases de donnees :\n" + (countNbLaizeDiff + 1) + " : " + laizeOrderListe[countNbLaizeDiff] + " x " + quantiteOrderListe[countNbLaizeDiff], Toast.LENGTH_LONG).show();

                        /**Affiche les commande à la suite*/
                        textOrderListe.setText(textOrderListe.getText().toString() + "\n" + "commande " + (countNbLaizeDiff + 1) + " : " + laizeOrder + " x " + quantiteOrder);

                        countNbLaizeDiff++;
                        textLaizeOrder.setText(null);
                        textLaizeOrder.setEnabled(true);
                        textQuantiteOrder.setText(null);
                        textQuantiteOrder.setEnabled(true);

                        if (countNbLaizeDiff == nbLaizeDiff) {
                            countNbLaizeDiff = 0;
                            textLaizeOrder.setEnabled(false);
                            textQuantiteOrder.setEnabled(false);

                            for (int i = 0; i < nbLaizeDiff; i++) {
                                Toast.makeText(getApplicationContext(), "Commande " + (i + 1) + " : " + laizeOrderListe[i] + " x " + quantiteOrderListe[i], Toast.LENGTH_LONG).show();
                            }
                            /**On cree un container pour passer des infos à la classe CommandeActivity*/
                            lamContainer = new Bundle();
                            regLaminateur = new int[]{laizeBobineMere, lisiereGauche, lisiereDroite, nbCouteaux, lisiereRecoupeGauche};
                            lamContainer.putIntArray("regLaminateur", regLaminateur);
                            lamContainer.putIntArray("laizeOrderListe", laizeOrderListe);
                            lamContainer.putIntArray("nbOrderListe", quantiteOrderListe);
                            intentResult = new Intent(CommandeActivity.this, ResultActivity.class);
                            intentResult.putExtras(lamContainer);
                            startActivity(intentResult);
                            CommandeActivity.this.finish();
                        }
                    }
                }
            }
        });
    }

    private void verifAvantCalculer() {

        if ((nbLaizeDiff != 0) && (laizeOrder != 0) && (quantiteOrder != 0)) {
            butCalculer.setEnabled(true);
            return;
        }
        ;
    }

    @Override
    /** Si changement d\'orientation, garde l'etat des EditBox a enabled ou non
     * voir ligne dans le manifest, android:configChanges="orientation|keyboardHidden|screenSize" */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /**Sauve les etats des composants*/
        if (outState != null) {
            enabledTextNbLaizeDiff = textNbLaizeDiff.isEnabled();
            enabledTextLaizeOrder = textLaizeOrder.isEnabled();
            enabledTextQuantiteOrder = textQuantiteOrder.isEnabled();
            outState.putBoolean("textNbLaizeDiff", enabledTextNbLaizeDiff);
            outState.putBoolean("textLaizeOrder", enabledTextLaizeOrder);
            outState.putBoolean("textQuantiteOrder", enabledTextQuantiteOrder);
            /**si les EditText sont deja desactive, on ne desactive pas le bouton*/
            if (enabledTextNbLaizeDiff == true || enabledTextLaizeOrder == true || enabledTextQuantiteOrder == true) {
                enabledButCalculer = false;
            } else {
                enabledButCalculer = true;
            }
            outState.putBoolean("butCalculer", enabledButCalculer);
            outState.putInt("nbLaizeDiff", nbLaizeDiff);
            outState.putInt("laizeOrder", laizeOrder);
            outState.putInt("quantiteOrder", quantiteOrder);
            outState.putIntArray("laizeOrderListe", laizeOrderListe);
            outState.putIntArray("quantiteOrderListe", quantiteOrderListe);
            outState.putInt("countNbLaizeDiff", countNbLaizeDiff);

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        /**Restore les etats des composants*/
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
        laizeOrderListe = savedInstanceState.getIntArray("laizeOrderListe");
        quantiteOrderListe = savedInstanceState.getIntArray("quantiteOrderListe");
        countNbLaizeDiff = savedInstanceState.getInt("countNbLaizeDiff");


    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main_commande, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// Handle action bar item clicks here. The action bar will
//		// automatically handle clicks on the Home/Up button, so long
//		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_retour) {
//
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
//	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //effacerValeurs();
            laizeBobineMere = 0;
            lisiereGauche = 0;
            lisiereDroite = 0;
            nbCouteaux = 0;
            lisiereRecoupeGauche = 0;

            nbLaizeDiff = 0;
            laizeOrder = 0;
            quantiteOrder = 0;

            laizeOrderListe = new int[]{};
            quantiteOrderListe = new int[]{};
            countNbLaizeDiff = 0;

            regLaminateur = new int[]{};
            lamContainer = new Bundle();

            CommandeActivity.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}

