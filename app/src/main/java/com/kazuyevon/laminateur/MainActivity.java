package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.kazuyevon.laminateur.fragment.LaizesMereFragment;
import com.kazuyevon.laminateur.models.MachineMandrin;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.ValeursLaminateur;

public class MainActivity extends AppCompatActivity implements LaizesMereFragment.onSomeEventListener {
    //Interface pour transfert de itemLaizesMere de la classe LaizesMereFragment

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String valeursLaminateursPrefs;

    private MachineMandrin machineMandrin;
    private MachineBobinot machineBobinot;
    private int parse;
    private int parse2;

    private Toolbar toolbar;
    private EditText editText_LaizeBobineMere;
    private Button but_LaizeBobineMere;
    private EditText editText_LisiereGauche;
    private EditText editText_LisiereDroite;
    private Button but_Lisiere;
    private EditText editText_NbCouteaux;
    private Button but_NbCouteaux;
    private EditText editText_LisiereRecoupeGauche;
    private EditText editText_LisiereRecoupeDroite;
    private Button but_LisiereRecoupe;
    private Switch switch_Calc;
    private int switchCalcStatus = 0;
    private Button button_Calc;

    private LaizesMereFragment laizesMereFragment;
    private FragmentTransaction fragmentTransaction;
    private View view_laizesMereFragment;
    private Intent intentCommande;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getPreferences(MODE_PRIVATE);

        if (preferences.contains("ValeursLaminateurs")){
            valeursLaminateursPrefs = preferences.getString("ValeursLaminateurs", valeursLaminateursPrefs);
            getValeursLaminateurPrefs();
        }
        /**Test d'ecriture et de maj des valeurs ValeursLaminateur.*/
        /*String valeursLaminateursPrefsTest = "600 800 600";
        ValeursLaminateur values2 = new ValeursLaminateur(valeursLaminateursPrefsTest);
        int[] test = values2.getLaizesBobineMere();
        editor = preferences.edit();
        editor.putString("ValeursLaminateurs", valeursLaminateursPrefsTest);
        editor.commit();
        Toast.makeText(getApplicationContext(), " valeurs testés de ValeursLaminateurs (defaut 600) : " + values2.getLaizesBobineMereToText(), Toast.LENGTH_LONG).show();*/


        /**Charge le layout qu'après avoir vérifier le SharedPreferences, car le fragment
         * LaizesMereFragment en a besoin.*/
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editText_LaizeBobineMere = (EditText) findViewById(R.id.edittext_LaizeBobineMere);
        but_LaizeBobineMere = (Button) findViewById(R.id.but_LaizeBobineMere);
        editText_LisiereGauche = (EditText) findViewById(R.id.edittext_LisiereGauche);
        editText_LisiereDroite = (EditText) findViewById(R.id.edittext_LisiereDroite);
        but_Lisiere = (Button) findViewById(R.id.but_Lisiere);
        editText_NbCouteaux = (EditText) findViewById(R.id.edittext_NbCouteaux);
        but_NbCouteaux = (Button) findViewById(R.id.but_NbCouteaux);
        editText_LisiereRecoupeGauche = (EditText) findViewById(R.id.edittext_LisiereRecoupeGauche);
        editText_LisiereRecoupeDroite = (EditText) findViewById(R.id.edittext_LisiereRecoupeDroite);
        but_LisiereRecoupe = (Button) findViewById(R.id.but_LisiereRecoupe);
        switch_Calc = (Switch) findViewById(R.id.switch_Calc);
        button_Calc = (Button) findViewById(R.id.but_Calc);

        /**Declare et prepare le fragment du layout content_main.xml*/
        laizesMereFragment = new LaizesMereFragment();
        laizesMereFragment.onAttach(getApplicationContext());
        /**Converti le fragment LaizesMereFragment pour controler sa visibilité
         * et tout ce qui touche à ses propriétés dans le layout.*/
        view_laizesMereFragment = (View) findViewById(R.id.fragment_LaizesMere);
        //empeche qu'il s'affiche maintenant
        view_laizesMereFragment.setVisibility(View.INVISIBLE);

        editText_LisiereRecoupeDroite.setEnabled(false);

        machineBobinot = new MachineBobinot();
        machineMandrin = new MachineMandrin();

        but_LaizeBobineMere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parse = 0;
                if (switchCalcStatus == 0) {
                    if (editText_LaizeBobineMere.getText().toString().isEmpty()
                    /**Annulé pour pouvoir passer à ResultRandomActivity
                     * pour le moment uniquement dispo pour le mode Bobinot.*/
                        /*|| editText_LaizeBobineMere.getText().toString().matches("0")
                        * Valeur 0 géré par CommandeActivity*/) {
                        //Toast.makeText(getApplicationContext(), R.string.laize_manquante, Toast.LENGTH_LONG).show();
                        /**Force à cacher le clavier*/
                        hideSoftKeyboard(MainActivity.this);
                        /**Lance le fragment LaizesMereFragment*/
                        /**Prepare le mecanisme de transaction pour le fragment*/
                        /**Je serais obliger de réinitialiser cette transaction pour d'autres modification,
                         * sinon une erreur me disant que commit() a été déjà effectué*/
                        if (view_laizesMereFragment.getVisibility() == View.INVISIBLE){
                            /**Au cas où la vue est fermé par la petite croix*/
                            try{
                                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.remove(laizesMereFragment);
                                fragmentTransaction.commit();
                            }catch (Exception e){}
                        }

                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.fragment_LaizesMere, laizesMereFragment);
                        fragmentTransaction.show(laizesMereFragment);
                        /**Annule l'action relatif à l'appuie du bouton retour.*/
                        fragmentTransaction.addToBackStack(null);
                        /**Confirme la transaction.*/
                        fragmentTransaction.commit();
                        /**Rend la vue fragment visible.*/
                        view_laizesMereFragment.setVisibility(View.VISIBLE);

                        return;
                    } else {
                        parse = Integer.parseInt(editText_LaizeBobineMere.getText().toString());
                        machineBobinot.setLaizeMere(parse);
                        editText_LaizeBobineMere.setEnabled(false);
                        but_LaizeBobineMere.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                } else {
                    if (editText_LaizeBobineMere.getText().toString().isEmpty()
                            || editText_LaizeBobineMere.getText().toString().matches("0")) {
                        Toast.makeText(getApplicationContext(), R.string.laize_manquante, Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        parse = Integer.parseInt(editText_LaizeBobineMere.getText().toString());
                        machineMandrin.setLaizeMere(parse);
                        editText_LaizeBobineMere.setEnabled(false);
                        but_LaizeBobineMere.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });

        but_Lisiere.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                parse = parse2 = 0;
                if (editText_LisiereGauche.getText().toString().isEmpty()
                        || editText_LisiereDroite.getText().toString().isEmpty()
                        || editText_LisiereGauche.getText().toString().matches("0")
                        || editText_LisiereDroite.getText().toString().matches("0")) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    parse = Integer.parseInt(editText_LisiereGauche.getText().toString());
                    parse2 = Integer.parseInt(editText_LisiereDroite.getText().toString());
                    if (switchCalcStatus == 0) {
                        machineBobinot.setLisiereGauche(parse);
                        machineBobinot.setLisiereDroite(parse2);
                    } else {
                        machineMandrin.setLisiereGauche(parse);
                        machineMandrin.setLisiereDroite(parse2);
                    }
                    editText_LisiereGauche.setEnabled(false);
                    editText_LisiereDroite.setEnabled(false);
                    but_Lisiere.setEnabled(false);
                    /**Verifie condition avant d'activer le bouton Suivant*/
                    verifAvantSuivant();
                }
            }
        });
        but_NbCouteaux.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int parse = 0;
                if (editText_NbCouteaux.getText().toString().isEmpty()
                        || editText_NbCouteaux.getText().toString().matches("0")
                        && switchCalcStatus == 0) {
                    Toast.makeText(getApplicationContext(), R.string.couteaux_manquants, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 0) {
                        parse = Integer.parseInt(editText_NbCouteaux.getText().toString());
                        machineBobinot.setNbCouteaux(parse);
                        editText_NbCouteaux.setEnabled(false);
                        but_NbCouteaux.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });
        but_LisiereRecoupe.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int parse = 0;
                if ((editText_LisiereRecoupeGauche.getText().toString().isEmpty())
                        || editText_LisiereRecoupeGauche.getText().toString().matches("0")
                        && (switchCalcStatus == 0)) {
                    Toast.makeText(getApplicationContext(), R.string.lisieres_recoupe_manquantes, Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (switchCalcStatus == 0) {
                        parse = Integer.parseInt(editText_LisiereRecoupeGauche.getText().toString());
                        machineBobinot.setLisiereRecoupeGauche(parse);
                        editText_LisiereRecoupeGauche.setEnabled(false);
                        but_LisiereRecoupe.setEnabled(false);
                        /**Verifie condition avant d'activer le bouton Suivant*/
                        verifAvantSuivant();
                    }
                }
            }
        });

        //set the switch to OFF
        switch_Calc.setChecked(false);
        switchCalcStatus = 0;
        //attach a listener to check for changes in state
        switch_Calc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    /*Mode mandrin*/
                    switchCalcStatus = 1;
                    machineMandrin = new MachineMandrin("default");
                    machineBobinot = new MachineBobinot();
                    editText_LaizeBobineMere.setText(machineMandrin.getLaizeMereToString());
                    editText_LisiereGauche.setText(machineMandrin.getLisiereGaucheToString());
                    editText_LisiereDroite.setText(machineMandrin.getLisiereDroiteToString());
                    editText_NbCouteaux.setText(null);
                    editText_LisiereRecoupeGauche.setText(null);
                    editText_NbCouteaux.setHint(R.string.hintNbCouteauxM);
                    editText_LisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGaucheM);
                    editText_LaizeBobineMere.setEnabled(true);
                    editText_LisiereGauche.setEnabled(true);
                    editText_LisiereDroite.setEnabled(true);
                    editText_NbCouteaux.setEnabled(false);
                    editText_LisiereRecoupeGauche.setEnabled(false);
                    but_LaizeBobineMere.setEnabled(true);
                    but_Lisiere.setEnabled(true);
                    but_NbCouteaux.setEnabled(false);
                    but_LisiereRecoupe.setEnabled(false);
                    button_Calc.setEnabled(false);
                } else {
                    /*Mode Bobinot*/
                    switchCalcStatus = 0;
                    machineMandrin = new MachineMandrin();
                    machineBobinot = new MachineBobinot("default");
                    editText_LaizeBobineMere.setText(machineBobinot.getLaizeMereToString());
                    editText_LisiereGauche.setText(machineBobinot.getLisiereGaucheToString());
                    editText_LisiereDroite.setText(machineBobinot.getLisiereDroiteToString());
                    editText_NbCouteaux.setText(machineBobinot.getNbCouteauxToString());
                    editText_LisiereRecoupeGauche.setText(machineBobinot.getLisiereRecoupeGaucheToString());
                    editText_NbCouteaux.setHint(R.string.hintNbCouteaux);
                    editText_LisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
                    editText_LaizeBobineMere.setEnabled(true);
                    editText_LisiereGauche.setEnabled(true);
                    editText_LisiereDroite.setEnabled(true);
                    editText_NbCouteaux.setEnabled(true);
                    editText_LisiereRecoupeGauche.setEnabled(true);
                    but_LaizeBobineMere.setEnabled(true);
                    but_Lisiere.setEnabled(true);
                    but_NbCouteaux.setEnabled(true);
                    but_LisiereRecoupe.setEnabled(true);
                    button_Calc.setEnabled(false);
                }
            }
        });
        button_Calc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /**On cree un intent pour passer des infos à la classe CommandeActivity*/
                intentCommande = new Intent(MainActivity.this, CommandeActivity.class);
                if (switchCalcStatus == 0) {
                    intentCommande.putExtra("machine", machineBobinot);
                } else if (switchCalcStatus == 1) {
                    intentCommande.putExtra("machine", machineMandrin);
                }
                razActivity();
                startActivity(intentCommande);
            }
        });
    }

    private void razActivity(){
        switch_Calc.setChecked(false);
        editText_LaizeBobineMere.setText(null);
        editText_LisiereGauche.setText(null);
        editText_LisiereDroite.setText(null);
        editText_NbCouteaux.setText(null);
        editText_LisiereRecoupeGauche.setText(null);
        editText_NbCouteaux.setHint(R.string.hintNbCouteaux);
        editText_LisiereRecoupeGauche.setHint(R.string.hintLisiereRecoupeGauche);
        editText_LaizeBobineMere.setEnabled(true);
        editText_LisiereGauche.setEnabled(true);
        editText_LisiereDroite.setEnabled(true);
        editText_NbCouteaux.setEnabled(true);
        editText_LisiereRecoupeGauche.setEnabled(true);
        but_LaizeBobineMere.setEnabled(true);
        but_Lisiere.setEnabled(true);
        but_NbCouteaux.setEnabled(true);
        but_LisiereRecoupe.setEnabled(true);
        button_Calc.setEnabled(false);

    }

    private void verifAvantSuivant() {
        if (switchCalcStatus == 0) {
            if (machineBobinot.isFull()
                    && !but_LaizeBobineMere.isEnabled()
                    && !but_Lisiere.isEnabled()
                    && !but_NbCouteaux.isEnabled()
                    && !but_LisiereRecoupe.isEnabled())
            {
                button_Calc.setEnabled(true);
            }

        } else if (switchCalcStatus == 1) {
            if (machineMandrin.isFull()
                    && !but_LaizeBobineMere.isEnabled()
                    && !but_Lisiere.isEnabled())
            {
                button_Calc.setEnabled(true);
            }
        }
    }
    @Override
    public void getItemLaizesMere(String laizeMere) {
        /**Méthode de l'interface onSomeEventListener de la classe LaizesMereFragment.class
         * servant à transférer ici le choix fait dans MyArrayAdapter de la ListView contentu dans le layout
         * fragment_laizesmere.*/

        if (!laizeMere.isEmpty() && laizeMere != "Autre..."){
            parse = Integer.parseInt(laizeMere);
            editText_LaizeBobineMere.setText(laizeMere);
            machineBobinot.setLaizeMere(parse);
            editText_LaizeBobineMere.setEnabled(false);
            but_LaizeBobineMere.setEnabled(false);
            /**Cache la vue du fragment.*/
            view_laizesMereFragment.setVisibility(View.INVISIBLE);
            /**On réinitialise la transaction avant d'envoyer.*/
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(laizesMereFragment);
            fragmentTransaction.commit();
            /**Verifie condition avant d'activer le bouton Suivant*/
            verifAvantSuivant();
        }
        else if (laizeMere == getResources().getString(R.string.autres)){
            /**Cache la vue du fragment.*/
            view_laizesMereFragment.setVisibility(View.INVISIBLE);
            /**On réinitialise la transaction avant d'envoyer.*/
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            /**On enlève le fragment.*/
            fragmentTransaction.remove(laizesMereFragment);
            fragmentTransaction.commit();
            /**On force la sélection sur l'EditText editText_LaizeBobineMere. */
            editText_LaizeBobineMere.setFocusable(true);
            /**On force l'affichage clavier pour saisie.*/
            showSoftKeyboard(MainActivity.this);
        }
    }
    @Override
    public String getValeursLaminateurPrefs(){
        if (preferences.contains("ValeursLaminateurs")){
            valeursLaminateursPrefs = preferences.getString("ValeursLaminateurs", valeursLaminateursPrefs);
        }

        return valeursLaminateursPrefs;
    }
    private static void hideSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
    private static void showSoftKeyboard(AppCompatActivity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 1);
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
        if (id == R.id.mode_demo_paire) {
            razActivity();
            startActivity(new Intent(MainActivity.this, DemoPaireActivity.class));
        }
        if(id == R.id.mode_demo_random){
            razActivity();
            startActivity(new Intent(MainActivity.this, DemoRandomActivity.class));
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