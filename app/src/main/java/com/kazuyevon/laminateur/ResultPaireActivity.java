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

import com.kazuyevon.laminateur.func.FuncChercherMemeBobinot;
import com.kazuyevon.laminateur.func.FuncIsAllBobinotsUsed;
import com.kazuyevon.laminateur.func.FuncNbBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeOrderBobinots;
import com.kazuyevon.laminateur.func.FuncPeuplerListeUsedBobinots;
import com.kazuyevon.laminateur.func.funcChercherBobinotsProbleme;
import com.kazuyevon.laminateur.func.funcChercherLaizeMinDispo;
import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.MachineMandrin;

import java.util.List;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class ResultPaireActivity extends AppCompatActivity {

    //faire en sorte que lors du bin packaging, bu que la liste des commandes est en mode decroissant,
    // eviter de reboucler apres la plus petite laize passer

    private String TAG = "ResultPaireActivity";
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
    private int positionBob;
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

    private int positionBobJumeaux = 0;
    private Boolean isAllBobinotsUsed = false;
    private String bobinotsProbleme = null;
    private int countBobinotsProbleme = 0;
    private int laizeMinDispo;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
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
            /**Determine de quel mode mandrin ou bobinot l'intent provient.*/
            if (machine.getClass() == machineMandrin.getClass()){
                machineMandrin = (MachineMandrin) machine;
                Toast.makeText(getApplicationContext(), "Mode Mandrin", Toast.LENGTH_SHORT).show();
                laizeBobineMere = machineMandrin.getLaizeMere();
                lisiereGauche = machineMandrin.getLisiereGauche();
                lisiereDroite = machineMandrin.getLisiereDroite();
                nbCouteaux = 100;
                lisiereRecoupeGauche = 0;
            }
            else if (machine.getClass() == machineBobinot.getClass()){
                machineBobinot = (MachineBobinot) machine;
                Toast.makeText(getApplicationContext(), "Mode Bobinot", Toast.LENGTH_SHORT).show();
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

        /**Analyse de la Commande.*/
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
            dialog = new Dialog(ResultPaireActivity.this);
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
            dialog = new Dialog(ResultPaireActivity.this);
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
            /**Debut de calcul de plan de découpe*/

            /**On commence avec une bobine mère*/
            laizeProd = laizeBobineMere - (lisiereGauche + lisiereDroite);
            laizeUtile = laizeProd;
            nbBobinotsParBobine = nbCouteaux - 1;
            countBobines = 1; /*Au moins 1 bobine sera utilisée.*/
            planDeCoupe = "";
            planBobineMere = "";
            pertes = "Chutes restantes (hors chutes gauche : " + lisiereGauche + " et droite : " + lisiereDroite + " et hors recoupe " + lisiereRecoupeGauche + " :\n";
            logLam = "Bobine " + countBobines + " de laize : " + (laizeUtile + lisiereGauche + lisiereDroite) + "mm,\n";
            logLam += "déduire chutes de chaques cotés : " + lisiereGauche + "mm et " + lisiereDroite + "mm.\n";

            /**fin de boucle a nbBobinots +2*/
            for (positionBob = 0; positionBob < (nbBobinots+2); positionBob += 2) {

                /**previent la fin de boucle si tous les bobinots n'ont pas ete utilises
                 *Si le la boucle pointe juste apres le bobinot du dernier couple de bobinots et que
                 * le nb de bobinots n'est pas atteint : */
                if(positionBob > (nbBobinots - 2) && (countBobinots + countBobinotsProbleme) != nbBobinots){positionBob = 0;}

                /**Sélectionne aussi le double non utlisé du bobinot en cours. Risque qu'il retourne null.*/
                try{
                    //soucis : teste meme les bobinots utilises à la position positionBob donc j'ai fais ça,
                    // probleme : ça telescope le rajout de bobine mere.
                    while (listeUsedBobinots[positionBob] != 0){
                        positionBob += 2;
                    }
                    positionBobJumeaux = new FuncChercherMemeBobinot().ChercherMemeBobinot(listeOrderBobinots[positionBob], positionBob,  listeOrderBobinots, listeUsedBobinots);
                    Log.i(TAG, "Bobinots testes : " + listeOrderBobinots[positionBob] + " et " + listeOrderBobinots[positionBobJumeaux]);
                }
                catch(Exception e){
                    positionBobJumeaux = 0;
                    Log.w(TAG, "Pas de double disponible.");
                    break;
                }

                /**Vérifie à chaque tour si tous les bobinots ont été utilisés.*/
                isAllBobinotsUsed = new FuncIsAllBobinotsUsed().IsAllBobinotsUsed(commande, listeUsedBobinots);

                /**Cherche à chaque tour le bobinot le plus petit*/
                laizeMinDispo = new funcChercherLaizeMinDispo().ChercherLaizeMinDispo(listeOrderBobinots, listeUsedBobinots);

                /**Si nb bobinots atteint pour le run.*/
                if ((countBobinotsParBobine == nbBobinotsParBobine)
                        /**Et que la laize utile est suffisante pour la suite.*/
                        && (laizeUtile - (lisiereRecoupeGauche + listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]) > 0)
                        /**Et que les bobinots sélectionnés sont libres.*/
                        && (listeUsedBobinots[positionBob] == 0)
                        && (listeUsedBobinots[positionBobJumeaux] == 0)) {
                    logLam += "Nb de bobinots atteint pour ce run et laize restante : " + laizeUtile + "mm,\n";
                    logLam += "prévoir d'enlever " + lisiereRecoupeGauche + "mm de chutes en recoupe.\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + "recoupe Bobine " + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveCoupe += countBobinotsParBobine;
                    countBobinotsParBobine = 0;
                    /**On decremente de 1 pour relire le bobinot.*/
                    positionBob--;
                }
                /**Si nb bobinots atteint pour le run en recoupe.
                 * très peu utilisé, rare sont les fois ont on coupe 3 fois de suite la même bobine mère.*/
                else if ((count2BobinotsParBobine == nbBobinotsParBobine)
                        /**Et que la laize utile est suffisante pour la suite.*/
                        && (laizeUtile - (lisiereRecoupeGauche + listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]) > 0)
                        /**Et que les bobinots sélectionnés sont libres.*/
                        && (listeUsedBobinots[positionBob] == 0)
                        && (listeUsedBobinots[positionBobJumeaux] == 0)) {
                    logLam += "en 2ème recoupe (" + laizeUtile + "mm) : nb de bobinot atteint pour ce run et laize restante : " + laizeUtile + "mm\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + "2 eme recoupe Bobine " + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveRecoupe += count2BobinotsParBobine;
                    count2BobinotsParBobine = 0;
                    /**On decremente de 1 pour relire le bobinot.*/
                    positionBob--;
                }

                /**Si le bobinot passe en double dans la laize.*/
                else if ((laizeUtile - (listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]) > 0)
                        && (listeUsedBobinots[positionBob] == 0)
                        && (listeUsedBobinots[positionBobJumeaux] == 0)) {

                    if (modeRecoupe == false && countBobinotsParBobine < nbBobinotsParBobine) {
                        logLam += "Laize restante : " + laizeUtile + "mm, " + "Bobinot en coupe : " + listeOrderBobinots[positionBob] + " x2\n" ;
                        laizeUtile -= (listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]);
                        countBobinotsParBobine += 2;
                        countBobinots += 2;
                        /**Marque  les 2 bobinots comme utilisés.*/
                        listeUsedBobinots[positionBob] = 1;
                        listeUsedBobinots[positionBobJumeaux] = 1;
                        planDeCoupe += " " + listeOrderBobinots[positionBob] + " " + listeOrderBobinots[positionBobJumeaux];
                        Log.i(TAG, "Bobinots ajoutes : " + listeOrderBobinots[positionBob] + " et " + listeOrderBobinots[positionBobJumeaux]);
                    }
                    else if (modeRecoupe == true && count2BobinotsParBobine < nbBobinotsParBobine) {
                        logLam += "Laize restante : " + laizeUtile + "mm, " + "Bobinot en recoupe : " + listeOrderBobinots[positionBob] + " x2\n";
                        laizeUtile -= (listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]);
                        count2BobinotsParBobine += 2;
                        countBobinots += 2;
                        /**Marque les 2 bobinots comme utilisés.*/
                        listeUsedBobinots[positionBob] = 1;
                        listeUsedBobinots[positionBobJumeaux] = 1;
                        planDeCoupe += " " + listeOrderBobinots[positionBob] + " " + listeOrderBobinots[positionBobJumeaux];
                        Log.i(TAG, "Bobinots ajoutes en recoupe : " + listeOrderBobinots[positionBob] + " et " + listeOrderBobinots[positionBobJumeaux]);
                    }
                }
                /**Si boucle atteinte sans avoir utilisé tous les bobinots.
                 * Sachant que la liste commence de 0, et que on increment par 2,
                 * positionBob == nbBobinots - 2
                 * et
                 * positionBobJumeaux == nbBobinots - 1 */
                //else if ((positionBob >= (nbBobinots - 2) || positionBobJumeaux >= (nbBobinots - 1))
                /**Changement de tactique, utilise la laize utile restante plutot que le compteur de bobine*/
                  else if (laizeUtile < laizeMinDispo
                        && (isAllBobinotsUsed == false)) {
                    if (modeRecoupe == false) {
                        planBobineMere += "Bobine " + countBobines + " (" + countBobinotsParBobine + " bob) :\n " + planDeCoupe + "\n";
                        countBobinotsParBobine = 0;
                    } else if (modeRecoupe == true) {
                        planBobineMere += "Bobine " + countBobines + " (" + (saveCoupe + saveRecoupe) + " bob + " + count2BobinotsParBobine  + " bob en recoupe) :\n" + planDeCoupe + "\n";
                        count2BobinotsParBobine = 0;
                    }
                    saveCoupe = 0;
                    saveRecoupe = 0;
                    planDeCoupe = "";
                    if(modeRecoupe == false) {
                        pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm, (total : " + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                    }
                    else {
                        pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm, (total :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                        pertes += "dont " + lisiereRecoupeGauche + "mm en recoupe.\n";
                    }
                    countBobines++;
                    logLam += "Laize restante : " + laizeUtile + "mm, Nouvelle bobine " + countBobines + ",\n";
                    logLam += "déduire chutes de chaques cotés : " + lisiereGauche + "mm et " + lisiereDroite + "mm.\n";
                    laizeUtile = laizeProd;
                    modeRecoupe = false;
                    Log.i(TAG, "Nouvelle bobine mere n° " + countBobines);
                    /**On reboucle*/
                    positionBob = 0;
                }
                /**Verifie que la paire de bobinots passe dans laizeProd, sinon ils sont ecartes.*/
                else if (laizeProd - (listeOrderBobinots[positionBob] + listeOrderBobinots[positionBobJumeaux]) <= 0){
                    listeUsedBobinots[positionBob] = -1;
                    listeUsedBobinots[positionBobJumeaux] = -1;
                    countBobinotsProbleme += 2;
                    Log.w(TAG, "Bobinots ecartes : " + listeOrderBobinots[positionBob] + " et " + listeOrderBobinots[positionBobJumeaux]);
                }
                Log.i(TAG, "Tour" + positionBob);
                progress = (positionBob * 100) / nbBobinots;
                publishProgress(progress);

            }
            /**Récupère la dernière bobine mère et les pertes.*/

            if(modeRecoupe == false) {
                planBobineMere += "Bobine " + countBobines + " (" + countBobinotsParBobine + " bob) :\n " + planDeCoupe + "\n";
                if (laizeUtile+lisiereDroite > 200){
                    pertes += "de Bobine " + countBobines + " : " + (laizeUtile + lisiereDroite) + "mm, (total :" + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                    pertes += "soit " + (laizeUtile+lisiereDroite) + "mm à remettre en stock, pas de chutes droite.";
                }
                else{
                    pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm, (total :" + (laizeUtile + lisiereGauche + lisiereDroite) + "mm).\n";
                }
            }
            else {
                planBobineMere += "Bobine " + countBobines + " (" + (saveCoupe + saveRecoupe) + " bob + " + count2BobinotsParBobine  + " bob en recoupe) :\n" + planDeCoupe + "\n";
                if (laizeUtile+lisiereDroite > 200){
                    pertes += "de Bobine " + countBobines + " : " + (laizeUtile + lisiereDroite) + "mm, (total :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                    pertes += "dont " + lisiereRecoupeGauche + "mm en recoupe.\n";
                    pertes += "soit " + (laizeUtile+lisiereDroite) + "mm à remettre en stock, pas de chute droite.";
                }
                else{
                    pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm, (total :" + (laizeUtile + lisiereGauche + lisiereDroite + lisiereRecoupeGauche) + "mm),\n";
                    pertes += "dont " + lisiereRecoupeGauche + "mm en recoupe.\n";
                }
            }
            planDeCoupe = "";

            bobinotsProbleme = new funcChercherBobinotsProbleme().chercherBobinotsProbleme(listeOrderBobinots, listeUsedBobinots);
            if(bobinotsProbleme != ""){
                planBobineMere += "\nDes bobinots ne sont pas passes : \n" + bobinotsProbleme;
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
                "Afficher les Reglages",
                "Afficher la Commande",
                "Afficher le Plan de Coupe",
                "Afficher les Pertes",
                "Afficher les détails du Calcul"};

        if (nbCouteaux < 100) {
            listeReglages = new String[9];
            listeReglages[0] = "Laize Lamination : " + laizeBobineMere;
            listeReglages[1] = "Lisiere Gauche : " + lisiereGauche + ", Lisiere Droite : " + lisiereDroite;
            listeReglages[2] = "Nombre de couteaux : " + nbCouteaux + " soit " + nbBobinotsParBobine + " bobinots max par run";
            listeReglages[3] = "Lisiere Gauche pour la recoupe : " + lisiereRecoupeGauche;
            listeReglages[4] = "Nombre bobine mere : " + countBobines + " de laize " + laizeBobineMere;
            listeReglages[5] = "Total bobinots commandés : " + nbBobinots;
            listeReglages[6] = "Total bobinots produits : " + countBobinots;
            if(bobinotsProbleme != ""){
                listeReglages[7] = "Total bobinots trop larges : " + countBobinotsProbleme;
                listeReglages[8] = "Les bobinots ne sont pas passés : " + bobinotsProbleme;
            }
        } else {
            listeReglages = new String[6];
            listeReglages[0] = "Laize mandrin mere : " + laizeBobineMere;
            listeReglages[1] = "Lisieres Gauche : " + lisiereGauche + ", Lisiere Droite : " + lisiereDroite;
            listeReglages[2] = "Nombre mandrin mere : " + countBobines + " de laize " + laizeBobineMere;
            listeReglages[3] = "Total mandrins commandés : " + nbBobinots;
            listeReglages[4] = "Total mandrins produits : " + countBobinots;
            listeReglages[5] = "Rappel : mode mandrin actif";
            planBobineMere += "Rappel: mode mandrin actif";
            pertes += "\nRappel: mode mandrin actif";
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
        //intentAffResult = new Intent (ResultActivity.this, AfficherResultActivity.class);
        intentAffResult = new Intent(ResultPaireActivity.this, AfficheResultNavigationDrawerActivity.class);
        intentAffResult.putExtras(extras);
        startActivity(intentAffResult);
        ResultPaireActivity.this.finish();

        return;
    }
}
