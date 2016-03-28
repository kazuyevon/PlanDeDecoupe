package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import java.util.List;

import com.kazuyevon.laminateur.func.FuncChercherLaizeMinDispo;
import com.kazuyevon.laminateur.func.FuncIsAllBobinotsUsed;
import com.kazuyevon.laminateur.func.FuncNbBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeOrderBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeUsedBobinots;
import com.kazuyevon.laminateur.func.FuncChercherBobinotsProbleme;
import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.MachineMandrin;

public class ResultActivity extends AppCompatActivity {

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
    private ProgressBar progressBar;
    private Dialog dialog;
    private int laizeProd;
    private int laizeUtile;
    private int countBobines = 1;
    private int countBobinots = 0;
    private int countBobinotsParBobine = 0;
    private int count2BobinotsParBobine = 0;
    private int nbBobinotsParBobine;
    private String planDeCoupe = "";
    private String planBobineMere = "";
    private String pertes = "";
    private Boolean modeRecoupe = false;
    private int saveCoupe = 0;
    private int saveRecoupe = 0;
    private String logLam = "";
    private String[] listeMenus;
    private String[] listeReglages;
    private String[] listeCommande;
    private String[] listeDecoupe;
    private String[] listePertes;

    private Boolean isAllBobinotsUsed = false;
    private String bobinotsProbleme = null;
    private int countBobinotsProbleme = 0;
    private int laizeMinDispo;
    private int tour;

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
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.mode_mandrin), Toast.LENGTH_SHORT).show();
                laizeBobineMere = machineMandrin.getLaizeMere();
                lisiereGauche = machineMandrin.getLisiereGauche();
                lisiereDroite = machineMandrin.getLisiereDroite();
                nbCouteaux = 100;
                lisiereRecoupeGauche = 0;
            }
            else if (machine.getClass() == machineBobinot.getClass()){
                machineBobinot = (MachineBobinot) machine;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.mode_bobinot), Toast.LENGTH_SHORT).show();
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
            dialog = new Dialog(ResultActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progressdialog);
            dialog.show();
            progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /**Mise à jour de la ProgressBar.*/
            progressBar.setProgress(values[0]);
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
            dialog = new Dialog(ResultActivity.this);
            dialog.setCancelable(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.progressdialog);
            dialog.show();
            progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar1);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /**Mise à jour de la ProgressBar.*/
            progressBar.setProgress(values[0]);
        }

        @Override
        protected Void doInBackground(String... arg0) {
            int progress = 0;
            /**Debut de calcul de plan de découpe*/

            /**On commence avec une bobine mère*/
            laizeProd = laizeBobineMere - (lisiereGauche + lisiereDroite);
            laizeUtile = laizeProd;
            nbBobinotsParBobine = nbCouteaux - 1;
            countBobines = 1; /**Au moins 1 bobine sera utilisée.*/
            planDeCoupe = "";
            planBobineMere = "";
            pertes = getResources().getString(R.string.pertes1) + " : " + + lisiereGauche + " "+ getResources().getString(R.string.pertes2) + " : " + lisiereDroite + " " + getResources().getString(R.string.pertes3) + " " + lisiereRecoupeGauche + " :\n";
            logLam = getResources().getString(R.string.logLam1) +" " + countBobines + " " + getResources().getString(R.string.loglam2) + " : " + (laizeUtile + lisiereGauche + lisiereDroite) + "mm,\n";
            logLam += getResources().getString(R.string.loglam3) + " : " + lisiereGauche + getResources().getString(R.string.loglam4) + " " + lisiereDroite + "mm.\n";
            tour = 0;
            for (int positionBob = 0; positionBob < nbBobinots; positionBob++) {

                /**previent la fin de boucle si tous les bobinots n'ont pas ete utilises
                 *Si le la boucle pointe juste apres le dernier bobinot et que
                 * le nb de bobinots n'est pas atteint : */
                if(positionBob > (nbBobinots - 1) && (countBobinots + countBobinotsProbleme) != nbBobinots){positionBob = 0;}

                //soucis : teste meme les bobinots utilises à la position positionBob donc j'ai fais ça,
                // probleme : ça telescope le rajout de bobine mere.
                try{
                    while (listeUsedBobinots[positionBob] != 0){
                        positionBob += 1;
                    }
                    Log.i(TAG, "Bobinots testes : " + listeOrderBobinots[positionBob]);
                }
                catch(Exception e){
                    break;
                }

                /**Vérifie à chaque tour si tous les bobinots ont été utilisés.*/
                isAllBobinotsUsed = new FuncIsAllBobinotsUsed().IsAllBobinotsUsed(commande, listeUsedBobinots);

                /**Cherche à chaque tour le bobinot le plus petit*/
                laizeMinDispo = new FuncChercherLaizeMinDispo().ChercherLaizeMinDispo(listeOrderBobinots, listeUsedBobinots);

                /**Si nb bobinots atteint pour le run.*/
                if ((countBobinotsParBobine == nbBobinotsParBobine)
                        /**Et que la laize utile est suffisante pour la suite.*/
                        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[positionBob] > 0)
                        /**Et que les bobinots sélectionnés sont libres.*/
                        && (listeUsedBobinots[positionBob] == 0)) {
                    logLam += getResources().getString(R.string.loglam5) + " : " + laizeUtile + "mm,\n";
                    logLam += getResources().getString(R.string.loglam6) + " " + lisiereRecoupeGauche + getResources().getString(R.string.loglam7) + "\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + getResources().getString(R.string.planDeCoupe1) + " " + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveCoupe += countBobinotsParBobine;
                    countBobinotsParBobine = 0;
                    /**On decremente de 1 pour relire le bobinot.*/
                    positionBob--;
                }
                /**Si nb bobinots atteint pour le run en recoupe.*/
                else if ((count2BobinotsParBobine == nbBobinotsParBobine)
                        /**Et que la laize utile est suffisante pour la suite.*/
                        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[positionBob] > 0)
                        /**Et que les bobinots sélectionnés sont libres.*/
                        && (listeUsedBobinots[positionBob] == 0)) {
                    logLam += getResources().getString(R.string.loglam8)+ " (" + laizeUtile + getResources().getString(R.string.loglam9) + " : " + laizeUtile + "mm\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + getResources().getString(R.string.planDeCoupe2) + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveRecoupe += count2BobinotsParBobine;
                    count2BobinotsParBobine = 0;
                    /**On decremente de 1 pour relire le bobinot.*/
                    positionBob--;
                }
                /**Si le bobinot passe dans la laize.*/
                else if ((laizeUtile - listeOrderBobinots[positionBob] > 0)
                        && (listeUsedBobinots[positionBob] == 0)) {

                    if (modeRecoupe == false) {
                        if (countBobinotsParBobine < nbBobinotsParBobine) {
                            logLam += getResources().getString(R.string.loglam10) + " : " + laizeUtile + "mm, " + getResources().getString(R.string.loglam11) + " : " + listeOrderBobinots[positionBob] + "\n" ;
                            laizeUtile -= listeOrderBobinots[positionBob];
                            countBobinotsParBobine++;
                            countBobinots++;
                            /**Marque bobinot comme utilisé.*/
                            listeUsedBobinots[positionBob] = 1;
                            planDeCoupe += " " + listeOrderBobinots[positionBob];
                            Log.i(TAG, "Bobinots ajoutes : " + listeOrderBobinots[positionBob]);
                        }
                    } else if (modeRecoupe == true) {
                        if (count2BobinotsParBobine < nbBobinotsParBobine) {
                            logLam += getResources().getString(R.string.loglam10) + " : " + laizeUtile + "mm, " + getResources().getString(R.string.loglam12) + " : " + listeOrderBobinots[positionBob] + "\n" ;
                            laizeUtile -= listeOrderBobinots[positionBob];
                            count2BobinotsParBobine++;
                            countBobinots++;
                            /**Marque bobinot comme utilisé.*/
                            listeUsedBobinots[positionBob] = 1;
                            planDeCoupe += " " + listeOrderBobinots[positionBob];
                            Log.i(TAG, "Bobinots ajoutes en recoupe : " + listeOrderBobinots[positionBob]);
                        }
                    }
                }
                /**Si boucle atteinte sans avoir utilisé tous les bobinots.*/
                //else if ((positionBob == nbBobinots - 1)
                    else if (laizeUtile < laizeMinDispo
                        && (isAllBobinotsUsed == false)) {
                    if (modeRecoupe == false) {
                        planBobineMere += getResources().getString(R.string.planBobineMere1) + " " + countBobines + " (" + countBobinotsParBobine + " " + getResources().getString(R.string.planBobineMere2) + ") :\n " + planDeCoupe + "\n";
                        countBobinotsParBobine = 0;
                    } else if (modeRecoupe == true) {
                        planBobineMere += getResources().getString(R.string.planBobineMere1) + " " + countBobines + " (" + (saveCoupe + saveRecoupe) + " " + getResources().getString(R.string.planBobineMere2) + " + " + count2BobinotsParBobine  + " "+ getResources().getString(R.string.planBobineMere3) + ") :\n" + planDeCoupe + "\n";
                        count2BobinotsParBobine = 0;
                    }
                    saveCoupe = 0;
                    saveRecoupe = 0;
                    planDeCoupe = "";
                    if(modeRecoupe == false) {
                        pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + laizeUtile + "mm, (" + getResources().getString(R.string.pertes5) + " : " + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                    }
                    else {
                        pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + laizeUtile + "mm, (" + getResources().getString(R.string.pertes5) + " :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                        pertes += getResources().getString(R.string.pertes6) + " " + lisiereRecoupeGauche + "mm "+ getResources().getString(R.string.pertes7) + ".\n";
                    }
                    countBobines++;
                    logLam += getResources().getString(R.string.loglam10) + " : " + laizeUtile + "mm, " + getResources().getString(R.string.loglam13) + " " + countBobines + ",\n";
                    logLam += getResources().getString(R.string.loglam3) + " : " + lisiereGauche + "mm "+ getResources().getString(R.string.loglam14) + " " + lisiereDroite + "mm.\n";
                    laizeUtile = laizeProd;
                    modeRecoupe = false;
                    Log.i(TAG, "Nouvelle bobine mere n° " + countBobines);
                    /**On reboucle*/
                    positionBob = 0;
                }
                /**Verifie que le bobinot passe dans laizeProd, sinon il est écarté.*/
                else if (laizeProd - listeOrderBobinots[positionBob] <= 0){
                    listeUsedBobinots[positionBob] = -1;
                    countBobinotsProbleme++;
                    Log.w(TAG, "Bobinot ecarte : " + listeOrderBobinots[positionBob]);
                }
                Log.i(TAG, "Tour" + positionBob);
                tour++;
                progress = (tour * 100) / nbBobinots;
                publishProgress(progress);

            }
            /**Récupère la dernière bobine mère et les pertes.*/

            if(modeRecoupe == false) {
                planBobineMere += getResources().getString(R.string.planBobineMere1) + " " + countBobines + " (" + countBobinotsParBobine + " " + getResources().getString(R.string.planBobineMere2) + ") :\n " + planDeCoupe + "\n";
                if (laizeUtile+lisiereDroite > 200){
                    pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + (laizeUtile + lisiereDroite) + "mm, (" + getResources().getString(R.string.pertes5) + " :" + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                    pertes += getResources().getString(R.string.pertes8) + " " + (laizeUtile+lisiereDroite) + getResources().getString(R.string.pertes9);
                }
                else{
                    pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + laizeUtile + "mm, (" + getResources().getString(R.string.pertes5) + " :" + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                }
            }
            else {
                planBobineMere += getResources().getString(R.string.planBobineMere1) + " " + countBobines + " (" + (saveCoupe + saveRecoupe) + " " + getResources().getString(R.string.planBobineMere2) + " + " + count2BobinotsParBobine  + " "+ getResources().getString(R.string.planBobineMere3) + ") :\n" + planDeCoupe + "\n";
                if (laizeUtile+lisiereDroite > 200){
                    pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + (laizeUtile + lisiereDroite) + "mm, (" + getResources().getString(R.string.pertes5) + " :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                    pertes += getResources().getString(R.string.pertes6) + " " + lisiereRecoupeGauche + "mm " + getResources().getString(R.string.pertes7) + ".\n";
                    pertes += getResources().getString(R.string.pertes8) + " " + (laizeUtile+lisiereDroite) + getResources().getString(R.string.pertes9);
                }
                else{
                    pertes += getResources().getString(R.string.pertes4) + " " + countBobines + " : " + laizeUtile + "mm, (" + getResources().getString(R.string.pertes5) + " :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                    pertes += getResources().getString(R.string.pertes6) + " " + lisiereRecoupeGauche + "mm " + getResources().getString(R.string.pertes7) + ".\n";
                }
            }
            planDeCoupe = "";

            bobinotsProbleme = new FuncChercherBobinotsProbleme().chercherBobinotsProbleme(listeOrderBobinots, listeUsedBobinots);
            if(bobinotsProbleme != ""){
                planBobineMere += "\n" + getResources().getString(R.string.planBobineMere4) + " : \n" + bobinotsProbleme;
            }
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

        listeMenus = new String[]{
                getResources().getString(R.string.afficher_reglages),
                getResources().getString(R.string.afficher_commande),
                getResources().getString(R.string.afficher_planDeCoupe),
                getResources().getString(R.string.afficher_pertes),
                getResources().getString(R.string.afficher_detailsCalcul)};

        if (nbCouteaux < 100) {
            listeReglages = new String[9];
            listeReglages[0] = getResources().getString(R.string.listeReglages_laizeLamination) + " : " + laizeBobineMere;
            listeReglages[1] = getResources().getString(R.string.listeReglages_lisiereGauche) + " : " + lisiereGauche + ", " + getResources().getString(R.string.listeReglages_lisisereDroite) + " : " + lisiereDroite;
            listeReglages[2] = getResources().getString(R.string.listeReglages_nombreCouteaux1) + " : " + nbCouteaux + " "+ getResources().getString(R.string.listeReglages_nombreCouteaux2) + " " + nbBobinotsParBobine + " bobinots max par run";
            listeReglages[3] = getResources().getString(R.string.listeReglages_lisiereGaucheRecoupe) + " : " + lisiereRecoupeGauche;
            listeReglages[4] = getResources().getString(R.string.listeReglages_nbBobineMere1) + " : " + countBobines + " " + getResources().getString(R.string.listeReglages_nbBobineMere2) + " " + laizeBobineMere;
            listeReglages[5] = getResources().getString(R.string.listeReglages_totalBobinotsCommandes) + " : " + nbBobinots;
            listeReglages[6] = getResources().getString(R.string.listeReglages_totalBobinotsProduits) + " : " + countBobinots;
            if(bobinotsProbleme != ""){
                listeReglages[7] = getResources().getString(R.string.listeReglages_totalBobinotsTropLarges1) + " : " + countBobinotsProbleme;
                listeReglages[8] = getResources().getString(R.string.listeReglages_totalBobinotsTropLarges2) + " : " + bobinotsProbleme;
            }
        } else {
            listeReglages = new String[6];
            listeReglages[0] = getResources().getString(R.string.listeReglages_laizeLamination) + " : " + laizeBobineMere;
            listeReglages[1] = getResources().getString(R.string.listeReglages_lisiereGauche) + " : " + lisiereGauche + ", " + getResources().getString(R.string.listeReglages_lisisereDroite) + " : " + lisiereDroite;
            listeReglages[2] = getResources().getString(R.string.listeReglages_nbMandrinMere) + " : " + countBobines + " " + getResources().getString(R.string.listeReglages_nbBobineMere2) + " " + laizeBobineMere;
            listeReglages[3] = getResources().getString(R.string.listeReglages_totalMandrinsCommandes) + " : " + nbBobinots;
            listeReglages[4] = getResources().getString(R.string.listeReglages_totalMandinsProduits) + " : " + countBobinots;
            listeReglages[5] = getResources().getString(R.string.listeReglages_mandrinActif) + ".";
            planBobineMere += getResources().getString(R.string.listeReglages_mandrinActif) + ".";
            pertes += "\n" + getResources().getString(R.string.listeReglages_mandrinActif) + ".";
        }

        listeCommande = new String[commande.size()];
        for (int i = 0; i < commande.size(); i++){
            listeCommande[i] = commande.get(i).getLaizeOrder() + " X " + commande.get(i).getQuantiteOrder();
        }

        listeDecoupe = new String[]{};
        listeDecoupe = planBobineMere.split("\n");

        listePertes = new String[]{};
        listePertes = pertes.split("\n");

        //pour que tout s'affiche dans le fragment_texte_layout
        logLam += "---------------------------------------------------------------------------------------------------------" +
                "-----------------------------------------------------------------------------------------------------------";

        /**On cree un container pour passer des infos à la classe CommandeActivity*/
        extras = new Bundle();
        extras.putStringArray("listeMenus", listeMenus);
        extras.putStringArray("listeReglages", listeReglages);
        extras.putStringArray("listeCommande", listeCommande);
        extras.putStringArray("listeDecoupe", listeDecoupe);
        extras.putStringArray("listePertes", listePertes);
        extras.putString("logLam", logLam);
        intentAffResult = new Intent(ResultActivity.this, AfficheResultNavigationDrawerActivity.class);
        intentAffResult.putExtras(extras);
        startActivity(intentAffResult);
        ResultActivity.this.finish();
        return;
    }
}

