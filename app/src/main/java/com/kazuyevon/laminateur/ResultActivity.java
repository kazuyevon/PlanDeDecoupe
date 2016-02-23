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
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Intent;
import java.util.List;

import com.kazuyevon.laminateur.models.Commande;
import com.kazuyevon.laminateur.models.MachineBobinot;
import com.kazuyevon.laminateur.models.MachineMandrin;




public class ResultActivity extends AppCompatActivity {

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
    private int currentLayoutId;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLayoutId = R.layout.activity_result;
        setContentView(currentLayoutId);
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
            Toast.makeText(getApplicationContext(), "Pas de reception", Toast.LENGTH_SHORT).show();
            this.finish();
        };

        laizeProd = laizeBobineMere - (lisiereGauche + lisiereDroite);
        laizeUtile = laizeProd;
        /**Au moins 1 bobine sera utilisée.*/
        countBobines = 1;
        planDeCoupe = "";
        planBobineMere = "";
        pertes = "";
        nbBobinotsParBobine = nbCouteaux - 1;

        /**Suite du traitement.*/
        listeOrderBobinots = peuplerListeOrderBobinots();
        listeUsedBobinots = peuplerListeUsedBobinots();
        /**execution AsyncTask pour eviter de saturer l'app principale*/
        RangementListeOrderBobinots listeOrderBobinotsRange = new RangementListeOrderBobinots();
        listeOrderBobinotsRange.execute();
    }

    /**
     * Affiche Resultats
     */
    private void traiteResultAvantEnvoie() {

        listeMenus = new String[]{
                "Afficher les Reglages",
                "Afficher la commande",
                "Afficher le Plan de Coupe",
                "Afficher les Pertes",
                "Afficher les détails du Calcul"};

        if (nbCouteaux < 100) {
            listeReglages = new String[7];
            listeReglages[0] = "Laize Lamination : " + laizeBobineMere;
            listeReglages[1] = "Lisiere Gauche : " + lisiereGauche + ", Lisiere Droite : " + lisiereDroite;
            listeReglages[2] = "Nombre de couteaux : " + nbCouteaux + " soit " + nbBobinotsParBobine + " bobinots max par run";
            listeReglages[3] = "Lisiere Gauche pour la recoupe : " + lisiereRecoupeGauche;
            listeReglages[4] = "Nombre bobine mere : " + countBobines + " de laize " + laizeBobineMere;
            listeReglages[5] = "Total bobinots commandés " + nbBobinots;
            listeReglages[6] = "Total bobinots produits " + countBobinots;
        } else {
            listeReglages = new String[5];
            listeReglages[0] = "Laize mandrin mere : " + laizeBobineMere;
            listeReglages[1] = "Lisieres Gauche : " + lisiereGauche + ", Lisiere Droite : " + lisiereDroite;
            listeReglages[2] = "Nombre mandrin mere : " + countBobines + " de laize " + laizeBobineMere;
            listeReglages[3] = "Total mandrins commandés " + nbBobinots;
            listeReglages[4] = "Total mandrins produits " + countBobinots;
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
        intentAffResult = new Intent(ResultActivity.this, AfficheResultNavigationDrawerActivity.class);
        intentAffResult.putExtras(extras);
        startActivity(intentAffResult);
        ResultActivity.this.finish();

        return;
    }

    private int calculeNbBobinots() {
        for (int i = 0; i < commande.size(); i++){
            nbBobinots += commande.get(i).getQuantiteOrder();
        }
        return nbBobinots;
    }

    private int[] peuplerListeOrderBobinots() {
        nbBobinots = calculeNbBobinots();
        listeOrderBobinots = new int[nbBobinots];
        int index = 0;
        int quantite = 0;
        /**Pour la premiere quantité de laize, on place les bobinots dans la listeOrderBobinots,
         * c'est à dire, si il y a 4 bobinots de laize 528, on place les 4 les uns après les autres avant de passer
         * à une autre laize et notemment à sa quantité.*/
        for (int i = 0; i < commande.size(); i++){
            quantite = commande.get(i).getQuantiteOrder();
            for(int j = 0; j < quantite; j++){
                listeOrderBobinots[index] = commande.get(i).getLaizeOrder();
                index++;
            }
        }
        return listeOrderBobinots;
    }

    protected int[] peuplerListeUsedBobinots() {
        int[] listeUsedBobinots = new int[nbBobinots];
        for (int i = 0; i < nbBobinots; i++) {
            listeUsedBobinots[i] = 0;
        }
        return listeUsedBobinots;
    }

    protected Boolean isAllBobinotsUsed() {
        /**Vérifie que tous les bobinots ont été utilisés.*/
        int testUsedBobinots = 0;
        for (int i = 0; i < nbBobinots; i++) {
            testUsedBobinots += listeUsedBobinots[i];
        }
        if (testUsedBobinots == nbBobinots) {
            return true;
        } else {
            return false;
        }
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
            dialog = new Dialog(ResultActivity.this);
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
            pertes = "Chutes restantes (hors chutes gauche : " + lisiereGauche + " et droite : " + lisiereDroite + " et hors recoupe " + lisiereRecoupeGauche + " :\n";
            logLam = "Bobine " + countBobines + " et laize restantes : " + (laizeUtile + lisiereGauche + lisiereDroite) + "mm\n";
            for (int testBobinot = 0; testBobinot < nbBobinots; testBobinot++) {

                /**Si nb bobinots atteint pour le run.*/
                if ((countBobinotsParBobine == nbBobinotsParBobine)
                        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[testBobinot] > 0)
                        && (listeUsedBobinots[testBobinot] == 0)) {
                    logLam += "nb de bobinots atteint pour ce run et laize restantes : " + laizeUtile + "mm\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + "recoupe Bobine " + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveCoupe += countBobinotsParBobine;
                    countBobinotsParBobine = 0;
                }
                /**Si nb bobinots atteint pour le run en recoupe.*/
                else if ((count2BobinotsParBobine == nbBobinotsParBobine)
                        && (laizeUtile - lisiereRecoupeGauche - listeOrderBobinots[testBobinot] > 0)
                        && (listeUsedBobinots[testBobinot] == 0)) {
                    logLam += "en 2eme recoupe (" + laizeUtile + "mm) : nb de bobinot atteint pour ce run et laize restantes : " + laizeUtile + "mm\n";
                    laizeUtile -= lisiereRecoupeGauche;
                    planDeCoupe += "\n" + "2 eme recoupe Bobine " + countBobines + " (" + laizeUtile + "mm) : ";
                    modeRecoupe = true;
                    saveRecoupe += count2BobinotsParBobine;
                    count2BobinotsParBobine = 0;
                }
                /**Si le bobinot passe dans la laize.*/
                else if ((laizeUtile - listeOrderBobinots[testBobinot] > 0)
                        && (listeUsedBobinots[testBobinot] == 0)) {

                    if (modeRecoupe == false) {
                        if (countBobinotsParBobine < nbBobinotsParBobine) {
                            logLam += "Bobinot en coupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile + "mm\n";
                            laizeUtile -= listeOrderBobinots[testBobinot];
                            countBobinotsParBobine++;
                            countBobinots++;
                            /**Marque bobinot comme utilisé.*/
                            listeUsedBobinots[testBobinot] = 1;
                            planDeCoupe += " " + listeOrderBobinots[testBobinot];
                            /**On reboucle.*/
                            testBobinot = 0;
                        }
                    } else if (modeRecoupe == true) {
                        if (count2BobinotsParBobine < nbBobinotsParBobine) {
                            logLam += "Bobinot en recoupe : " + listeOrderBobinots[testBobinot] + ", laize restantes : " + laizeUtile + "mm\n";
                            laizeUtile -= listeOrderBobinots[testBobinot];
                            count2BobinotsParBobine++;
                            countBobinots++;
                            /**Marque bobinot comme utilisé.*/
                            listeUsedBobinots[testBobinot] = 1;
                            planDeCoupe += " " + listeOrderBobinots[testBobinot];
                            /**On reboucle.*/
                            testBobinot = 0;
                        }
                    }
                }
                /**Si boucle atteinte sans avoir utilisé tous les bobinots.*/
                else if ((testBobinot == nbBobinots - 1)
                        && (isAllBobinotsUsed() == false)) {
                    if (modeRecoupe == false) {
                        planBobineMere += "Bobine " + countBobines + " (" + (countBobinotsParBobine + saveCoupe) + ")" + " : " + planDeCoupe + "\n";
                        countBobinotsParBobine = 0;
                    } else if (modeRecoupe == true) {
                        planBobineMere += "Bobine " + countBobines + " (" + (count2BobinotsParBobine + /*saveRecoupe + */saveCoupe) + ")" + " : " + planDeCoupe + "\n";
                        count2BobinotsParBobine = 0;
                    }
                    saveCoupe = 0;
                    saveRecoupe = 0;
                    planDeCoupe = "";
                    pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm\n";
                    countBobines++;
                    logLam += "Nouvelle bobine " + countBobines + " et laize restantes : " + laizeUtile + "mm\n";
                    laizeUtile = laizeProd;
                    modeRecoupe = false;
                    testBobinot = 0;
                }
                progress = (testBobinot * 100) / nbBobinots;
                publishProgress(progress);

            }
            /**Récupère la dernière bobine mère et les pertes.*/
            planBobineMere += "Bobine " + countBobines + " (" + countBobinotsParBobine + ")" + " : " + planDeCoupe;
            planDeCoupe = "";
            pertes += "de Bobine " + countBobines + " : " + laizeUtile + "mm";
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
}

