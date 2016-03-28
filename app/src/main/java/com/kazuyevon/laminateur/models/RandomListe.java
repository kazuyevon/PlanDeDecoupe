package com.kazuyevon.laminateur.models;

import android.content.res.Resources;

import com.kazuyevon.laminateur.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Fabrice on 24/03/2016.
 */

public class RandomListe {

    private String itemTexte;
    private RandomListe randomItem;
    private RandomListe[] randomListe;
    private Resources resources;
    public RandomListe() {
        super();
    }
    private RandomListe(Resources resources, int pertes, int laizeBobineMere, int nbBobines, int largeurTotal, int pourcentagePertes, int nbRecoupes, int nbChgtBobines) {
        /**Doir récuperer les resources pout mettre les tags de traduction en place.*/
        itemTexte = resources.getString(R.string.item_Texte1) + " " + nbBobines + " " + resources.getString(R.string.item_Texte2) + " " + laizeBobineMere + "mm,\n"
                + resources.getString(R.string.item_Texte3) + " : " + pertes + " mm (" + pourcentagePertes + "%)\n"
                + resources.getString(R.string.item_Texte4) + " " + nbRecoupes + " " + resources.getString(R.string.item_Texte5) + ",\n"
                + resources.getString(R.string.item_Texte6) + " " + (nbChgtBobines) + " " + resources.getString(R.string.item_Texte7) + ".\n";
    }
    public RandomListe[] setRandomListe (Resources resources, int[] pertesListeConvert, int[] nbBobinesListeConvert, int[] nbRecoupesListeConvert ){
        /**Peuple la liste d'objet randomListe*/
        /**Recupère et transfert les resources pour les tags de traduction.*/
        this.resources = resources;
        ValeursLaminateur randomLaize = new ValeursLaminateur();
        randomListe = new RandomListe[pertesListeConvert.length];

        for (int i = 0; i < pertesListeConvert.length; i++){

            int largeurTotal = nbBobinesListeConvert[i] * randomLaize.getOneLaizeBobineMere(i);
            int pourcentagePertes = (int)((pertesListeConvert[i]/largeurTotal)*100);
            int nbChgtBobines = nbBobinesListeConvert[i] + nbRecoupesListeConvert[i];
            randomItem = new RandomListe(resources, pertesListeConvert[i], randomLaize.getOneLaizeBobineMere(i), nbBobinesListeConvert[i], largeurTotal, pourcentagePertes, nbRecoupesListeConvert[i], nbChgtBobines);
            randomListe[i] = randomItem;
        }

        return randomListe;
    }
    public RandomListe[] getRandomListe(){
        /**Renvoie juste la liste d'objet RandomListe.*/
        return randomListe;
    }
    public String getItemTexte(){
        /**public car BCRandomAdapter s'en sert.*/
        return itemTexte;
    }
    public int getPertesMin(int[] pertesListeConvert){
        /**Permet de recuperer la perte la plus petite*/
        /**on cree une copie pour ne pas supprimer la liste pertesListeConverte.*/
        int[] copy = new int[pertesListeConvert.length];
        for (int i = 0; i < pertesListeConvert.length; i++) {
            copy[i] = pertesListeConvert[i];
        }
        /**On range par ordre croissant.*/
        Arrays.sort(copy);
        /**La valeur min est forcement position 0.*/
        int pertesMin = copy[0];
        return pertesMin;
    }
    public int[] getPertesMinPosition(int[] pertesListeConvert) {
        /**Recupere les positions des plus faibles taux de pertes.*/
        int pertesMin = getPertesMin(pertesListeConvert);
        ArrayList position = new ArrayList();
        for (int i = 0; i < pertesListeConvert.length; i++) {
            if (pertesListeConvert[i] == pertesMin) {
                position.add(i);
            }
        }
        /**Transforme l'ArrayList en int[] maintenant qu'on connait le nb d'essai
         * qui ont la plus petite perte.*/
        int[] positionInt = new int[position.size()];
        for (int i = 0; i < position.size(); i++) {
            positionInt[i] = (Integer) position.get(i);
        }
        return positionInt;
    }
    public int getNbBobinesMin(int[] nbBobinesListeConvert, int[] nbRecoupesListeConvert){
        /**Permet de recuperer le nb totale de changement bobine le plus petit,
         * on inclut donc le nb de recoupe, puisque c'est du temps de chgt de bobine.*/
        /**on cree une copie pour ne pas supprimer les liste nbBobinesListeConverte et
         * nbRecoupesListeConvert.*/
        int[] copy = new int[nbBobinesListeConvert.length];
        /**On additionne les 2 listes pour avoir le nb total.*/
        for (int i = 0; i < nbBobinesListeConvert.length; i++) {
            copy[i] = nbBobinesListeConvert[i] + nbRecoupesListeConvert[i];
        }
        /**On range par ordre croissant.*/
        Arrays.sort(copy);
        /**La valeur min est forcement position 0.*/
        int nbBobinesMin = copy[0];
        return nbBobinesMin;
    }
    public int[] getNbBobinesMinPosition(int[] nbBobinesListeConvert, int[] nbRecoupesListeConvert) {
        /**Recupere les positions des plus faibles nb bobines.*/
        int nbBobinesMin = getNbBobinesMin(nbBobinesListeConvert, nbRecoupesListeConvert);
        ArrayList position = new ArrayList();
        for(int i = 0; i < nbBobinesListeConvert.length; i++){
            if((nbBobinesListeConvert[i] + nbRecoupesListeConvert[i]) == nbBobinesMin){
                position.add(i);
            }
        }
        /**Transforme l'ArrayList en int[] maintenant qu'on connait le nb d'essai
         * qui ont le plus petit nb de chgt bobine.*/
        int[] positionInt = new int[position.size()];
        for(int i = 0; i < position.size(); i++){
            positionInt[i] = (Integer) position.get(i);
        }
        return positionInt;
    }
    public String[] getRandomListeToString(RandomListe[] randomListe){
        /**Transforme la liste d'objet en liste de String pour l'affichage dans l'alertDialog.*/
        String[] randomListeTosString = new String[randomListe.length];

        for(int i = 0; i < randomListe.length; i++){
            randomListeTosString[i] = randomListe[i].getItemTexte();
        }

        return randomListeTosString;
    }
}
