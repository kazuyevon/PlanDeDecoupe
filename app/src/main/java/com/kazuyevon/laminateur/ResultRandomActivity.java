package com.kazuyevon.laminateur;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kazuyevon.laminateur.func.FuncChercherLaizeMinDispo;
import com.kazuyevon.laminateur.func.FuncIsAllBobinotsUsed;
import com.kazuyevon.laminateur.func.FuncNbBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeOrderBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeUsedBobinots;
import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.MachineMandrin;
import com.kazuyevon.laminateur.models.ValeursLaminateur;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabrice on 20/03/2016.
 */
public class ResultRandomActivity extends AppCompatActivity {

    private String TAG = "ResultActivity";
    private int laizeBobineMere = 0;
    private int lisiereGauche = 0, lisiereDroite = 0;
    private int nbCouteaux = 0;

    private int lisiereRecoupeGauche = 0;
    private Bundle extras;

    private Object machine;
    private Intent intentResult;
    private Intent intentAffResult;
    private MachineMandrin machineMandrin;
    private MachineBobinot machineBobinot;
    private List<Commande> commande;

    private int nbBobinots;
    private int[] listeOrderBobinots;
    private int[] listeUsedBobinots;
    private ProgressBar progressBar1;
    private Dialog dialog;
    private int laizeProd;
    private int laizeUtile;
    private int countBobines = 1;
    private int countBobinots = 0;
    private int countBobinotsParBobine = 0;
    private int count2BobinotsParBobine = 0;
    private int nbBobinotsParBobine;
    private Boolean modeRecoupe = false;
    private int saveCoupe = 0;
    private int saveRecoupe = 0;

    private Boolean isAllBobinotsUsed = false;
    private int countBobinotsProbleme = 0;
    private int laizeMinDispo;

    private ValeursLaminateur randomLaize;
    private int[] randomLaizeListe;
    private int pertesInt;
    private List pertesListe;
    private List nbBobinesListe;
    private int minLaizeRestock;
    private int laizeRestock;
    private List laizeRestockListe;
    private List bobinotsProduits;
    private List bobinotsProduitsListe;
    private int nbRecoupe;
    private List nbRecoupesListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        machineMandrin = new MachineMandrin();
        machineBobinot = new MachineBobinot();

        /**On récupère les données de l'intent.*/
        try{
            intentResult = this.getIntent();
            extras = intentResult.getExtras();

            if (intentResult.hasExtra("machine")){
                machine = extras.getSerializable("machine");
            }
            if (intentResult.hasExtra("commande")) {
                commande = (List)extras.getSerializable("commande");
            }
            if (machine.getClass() == machineMandrin.getClass()){
                machineMandrin = (MachineMandrin) machine;
                Toast.makeText(getApplicationContext(), R.string.mode_mandrin, Toast.LENGTH_SHORT).show();
                laizeBobineMere = machineMandrin.getLaizeMere();
                lisiereGauche = machineMandrin.getLisiereGauche();
                lisiereDroite = machineMandrin.getLisiereDroite();
                nbCouteaux = 100;
                lisiereRecoupeGauche = 0;
            }
            else if (machine.getClass() == machineBobinot.getClass()){
                machineBobinot = (MachineBobinot) machine;
                Toast.makeText(getApplicationContext(), R.string.mode_bobinot, Toast.LENGTH_SHORT).show();
                laizeBobineMere = machineBobinot.getLaizeMere();
                lisiereGauche = machineBobinot.getLisiereGauche();
                lisiereDroite = machineBobinot.getLisiereDroite();
                nbCouteaux = machineBobinot.getNbCouteaux();
                lisiereRecoupeGauche = machineBobinot.getLisiereRecoupeGauche();
            }
        }
        catch (Exception e) {
            Log.e(TAG, "Pas de reception");
            this.finish();
        };

        /**Analyse de la commande*/
        /*commande = new ArrayList<Commande>();
        commande.add(new Commande(345, 6));*/
        nbBobinots = new FuncNbBobinots().CalculeNbBobinots(commande);
        listeOrderBobinots = new FuncPeuplerListeOrderBobinots().PeuplerListeOrderBobinots(commande);
        listeUsedBobinots = new FuncPeuplerListeUsedBobinots().PeuplerListeUsedBobinots(commande);

        /**execution AsyncTask pour eviter de saturer l'app principale*/
        RangementListeOrderBobinots listeOrderBobinotsRange = new RangementListeOrderBobinots();
        listeOrderBobinotsRange.execute();
    }

    /**
     * AsyncTask permettant de faire le calcul.
     */
    private class RangementListeOrderBobinots extends AsyncTask<Void, Integer, Void> {

        /**
         * Méthode exécutée au début de l'exécution de la tâche asynchrone.
         */
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog = new Dialog(ResultRandomActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progressdialog);
            dialog.show();
            progressBar1 = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /**Mise à jour de la ProgressBar.*/
            progressBar1.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            int progress = 0;
            int swap = 0;
            for (int i = 0; i < (listeOrderBobinots.length - 1); i++) {
                if (i == listeOrderBobinots.length - 1) break;
                for (int j = 1; j < (listeOrderBobinots.length - i - 1); j++) {
                    if (listeOrderBobinots[j] < listeOrderBobinots[j + 1]) {
                        swap = listeOrderBobinots[j];
                        listeOrderBobinots[j] = listeOrderBobinots[j + 1];
                        listeOrderBobinots[j + 1] = swap;
                    }
                    if (j == listeOrderBobinots.length) break;
                }
                progress = (i * 100) / nbBobinots;
                publishProgress(progress);
            }
            return null;
        }

        /**
         * Méthode exécutée à la fin de l'exécution de la tâche asynchrone.
         */
        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            BinPackingDeCoupe calculDeCoupe = new BinPackingDeCoupe();
            calculDeCoupe.execute();
        }
    }

    /**
     * AsyncTask permettant de faire le calcul.
     */
    private class BinPackingDeCoupe extends AsyncTask<String, Integer, Void> {

        /**
         * Méthode exécutée au début de l'exécution de la tâche asynchrone.
         */
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            dialog = new Dialog(ResultRandomActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progressdialog);
            dialog.show();
            progressBar1 = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /**Mise à jour de la ProgressBar.*/
            progressBar1.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            int progress = 0;
            int progress2 = 0;
            /**Debut de calcul de plan de découpe*/

            randomLaize = new ValeursLaminateur();
            randomLaizeListe = randomLaize.getLaizesBobineMere();
            minLaizeRestock = 200;
            pertesListe = new ArrayList(){};
            nbBobinesListe = new ArrayList(){};
            laizeRestockListe = new ArrayList(){};
            bobinotsProduits = new ArrayList(){};
            bobinotsProduitsListe = new ArrayList(){};
            nbRecoupesListe = new ArrayList(){};
            for(int laizeProdRandom:randomLaizeListe) {

                laizeProd = laizeProdRandom;
                laizeUtile = laizeProd;
                nbBobinotsParBobine = nbCouteaux - 1;
                countBobines = 1; /**Au moins 1 bobine sera utilisée.*/
                countBobinots = 0;
                countBobinotsParBobine = 0;
                count2BobinotsParBobine = 0;
                countBobinotsProbleme = 0;
                pertesInt = 0;
                bobinotsProduits.clear();
                nbRecoupe = 0;
                //nbBobinots = 24
                for (int positionBob = 0; positionBob < nbBobinots; positionBob++) {

                    /**Verifie que le bobinot passe dans laizeProd, sinon il est écarté.*/
                    if (laizeProd - (listeOrderBobinots[positionBob] + lisiereGauche + lisiereDroite) <= 0) {
                        listeUsedBobinots[positionBob] = -1;
                        countBobinotsProbleme++;
                        /**On passe directement au suivant*/
                        positionBob++;
                        //Log.w(TAG, "Bobinot ecarte : " + listeOrderBobinots[positionBob]);
                    }

                    /**Puis on test la suite*/
                    /**Si le bobinot passe dans la laize.*/
                    if((laizeUtile - (listeOrderBobinots[positionBob] + lisiereDroite) > 0)
                            && listeUsedBobinots[positionBob] == 0
                            && countBobinotsParBobine < nbBobinotsParBobine
                            && count2BobinotsParBobine < nbBobinotsParBobine){
                        laizeUtile -= listeOrderBobinots[positionBob];
                        bobinotsProduits.add(listeOrderBobinots[positionBob]);
                        countBobinots++;
                        /**Marque bobinot comme utilisé.*/
                        listeUsedBobinots[positionBob] = 1;
                        /**test bobinots suivant */
                        if (modeRecoupe == false){
                            /** tout premier bobinot*/
                            /** c'est un peu à l'envers, mais c'est pour simplifier le programe*/
                            /**On estime que si c'est le premier bobinot, on peut deja le déduire
                             * et ensuite enlever lisièreGauche*/
                            if (countBobinotsParBobine == 0){
                                laizeUtile -= lisiereGauche;
                            }
                            countBobinotsParBobine++;
                            // Log.i(TAG, "Bobinots ajoutes en coupe : " + listeOrderBobinots[positionBob]);
                        }
                        /**test bobinots suivant en recoupe*/
                        else if (modeRecoupe == true){
                            /**tout premier bobinot en recoupe*/
                            /** c'est un peu à l'envers, mais c'est pour simplifier le programe*/
                            /**On estime que si c'est le premier bobinot, on peut deja le déduire
                             * et ensuite enlever lisièreRecoupeGauche*/
                            if (count2BobinotsParBobine == 0){
                                laizeUtile -= lisiereRecoupeGauche;
                            }
                            count2BobinotsParBobine++;
                            // Log.i(TAG, "Bobinots ajoutes en recoupe : " + listeOrderBobinots[positionBob]);
                        }
                        /**Si c'est le dernier bobinot, */
                        if(countBobinots == nbBobinots){
                            if(laizeUtile < minLaizeRestock){
                                pertesInt += laizeUtile;
                            }
                            else{
                                laizeRestock = laizeUtile;
                            }
                        }
                    }

                    /**Vérifie à chaque tour si tous les bobinots ont été utilisés.*/
                    isAllBobinotsUsed = new FuncIsAllBobinotsUsed().IsAllBobinotsUsed(commande, listeUsedBobinots);

                    /**Cherche à chaque tour le bobinot le plus petit*/
                    laizeMinDispo = new FuncChercherLaizeMinDispo().ChercherLaizeMinDispo(listeOrderBobinots, listeUsedBobinots);

                    /**Ensuite on teste si nb bobinots atteint pour le run.*/
                    if ((countBobinotsParBobine == nbBobinotsParBobine
                            || count2BobinotsParBobine == nbBobinotsParBobine)
                            /**Et qu'il reste des bobinots a produire*/
                            && isAllBobinotsUsed == false
                            /**Et que la laize utile est suffisante pour la suite.*/
                            && (laizeUtile - lisiereRecoupeGauche) > laizeMinDispo){

                        /**On deduit lisiereRecoupeGauche*/
                        laizeUtile -= lisiereRecoupeGauche;
                        modeRecoupe = true;
                        nbRecoupe++;
                        /**Si nb bobinots atteint pour le run en coupe.*/
                        if (countBobinotsParBobine == nbBobinotsParBobine){
                            saveCoupe += countBobinotsParBobine;
                            countBobinotsParBobine = 0;
                        }
                        /**Si nb bobinots atteint pour le run en recoupe.*/
                        else if (count2BobinotsParBobine == nbBobinotsParBobine ){
                            saveRecoupe += count2BobinotsParBobine;
                            count2BobinotsParBobine = 0;
                        }
                    }

                    /**Ensuite on teste si il ne reste pas assez de laize pour un bobinot
                     * alors qu'il reste des bobinots à produire.*/
                    if ((laizeUtile - lisiereDroite) < laizeMinDispo
                            && (isAllBobinotsUsed == false)) {
                        countBobinotsParBobine = 0;
                        count2BobinotsParBobine = 0;
                        saveCoupe = 0;
                        saveRecoupe = 0;
                        /**LisiereDroite tjs presente dans laizeUtile*/
                        /**On rajoute lisiereGauche en perte*/
                        pertesInt += (laizeUtile + lisiereGauche) ;
                        /**Si on etait en recoupe, on rajoute lisièreRecoupeGauche en perte*/
                        if(modeRecoupe == true){
                            pertesInt += lisiereRecoupeGauche;
                        }
                        countBobines++;
                        laizeUtile = laizeProd;
                        modeRecoupe = false;
                        //Log.i(TAG, "Nouvelle bobine mere n° " + countBobines);
                        /**On reboucle*/
                        positionBob = 0;
                    }
                    //Log.i(TAG, "Tour" + positionBob);
                    progress = (countBobinots * 100) / nbBobinots;
                    publishProgress(progress);
                }

                pertesListe.add(pertesInt);
                nbBobinesListe.add(countBobines);
                laizeRestockListe.add(laizeRestock);
                bobinotsProduitsListe.add(bobinotsProduits);
                nbRecoupesListe.add(nbRecoupe);
                //Log.i(TAG, countBobines + " bobines mere de laize " + laizeProd + "mm, pertes  : " + pertesInt + " mm au total, retour bobine mère de " + laizeRestock + "mm.");

                countBobinots = 0;
                countBobinotsParBobine = 0;
                count2BobinotsParBobine = 0;
                modeRecoupe = false;
                for (int i = 0; i<listeUsedBobinots.length; i++){
                    listeUsedBobinots[i] = 0;
                }

            }//fin de boucle foreach
            /*for(int roll=0; roll < pertesListe.size(); roll++){
                Log.i(TAG, "Liste pertes "+ pertesListe.get(roll)+" pour Laize : " + listeLaizesBobineMere[roll] + " pour nb bobines : " + nbBobinesListe.get(roll));
            }*/
            return null;
        }

        /**
         * Méthode exécutée à la fin de l'exécution de la tâche asynchrone.
         */
        @Override
        protected void onPostExecute(Void result) {
            dialog.dismiss();
            traiteResultAvantEnvoie();
        }
    }

    /**
     * Affiche Resultats
     */
    private void traiteResultAvantEnvoie() {

        /**On cree un container pour passer des infos à la classe CommandeActivity*/
        extras = new Bundle();
        int[] pertesListeConvert = new int[pertesListe.size()];
        int[] nbBobinesListeConvert = new int[nbBobinesListe.size()];
        int[] laizeRestockListeConvert = new int[laizeRestockListe.size()];
        int[] nbRecoupesListeConvert = new int[nbRecoupesListe.size()];
        for(int i = 0; i<pertesListe.size(); i++){
            pertesListeConvert[i] = (int)pertesListe.get(i);
            nbBobinesListeConvert[i] = (int)nbBobinesListe.get(i);
            laizeRestockListeConvert[i] = (int)laizeRestockListe.get(i);
            nbRecoupesListeConvert[i] = (int) nbRecoupesListe.get(i);
        }
        machineBobinot.setLaizeMere(0);
        machineBobinot.setLisiereGauche(lisiereGauche);
        machineBobinot.setLisiereDroite(lisiereDroite);
        machineBobinot.setNbCouteaux(nbCouteaux);
        machineBobinot.setLisiereRecoupeGauche(lisiereRecoupeGauche);

        extras.putIntArray("pertesListeConvert", pertesListeConvert);
        extras.putIntArray("nbBobinesListeConvert", nbBobinesListeConvert);
        extras.putIntArray("laizeRestockListeConvert", laizeRestockListeConvert);
        extras.putIntArray("nbRecoupesListeConvert", nbRecoupesListeConvert);
        extras.putSerializable("commande", (Serializable) commande);
        extras.putSerializable("machineBobinot", machineBobinot);
        intentAffResult = new Intent(ResultRandomActivity.this, AfficheResultRandomActivity.class);
        intentAffResult.putExtras(extras);
        startActivity(intentAffResult);
        ResultRandomActivity.this.finish();
        return;
    }
}

