package com.kazuyevon.laminateur.models;
/**
 * Created by Fabrice on 18/02/2016.
 */
import java.io.Serializable;

public class MachineBobinot extends MachineMandrin implements Serializable{
    //http://www.technotalkative.com/android-send-object-from-one-activity-to-another-activity/

    public MachineBobinot(){

        super();
        nbCouteaux = 0;
        lisiereRecoupeGauche = 0;
    }
    public MachineBobinot(String defaut){

        //valeur par defaut mode bobinot
        super("default");
        laizeMere = 1300;
        lisiereGauche = lisiereDroite = 30;
        nbCouteaux = 15;
        lisiereRecoupeGauche = 10;
    }
    public MachineBobinot(int laizeMere, int lisiereGauche, int lisiereDroite, int nbCouteaux, int lisiereRecoupeGauche){

        super(laizeMere, lisiereGauche, lisiereDroite);
        this.nbCouteaux = nbCouteaux;
        this.lisiereRecoupeGauche = lisiereRecoupeGauche;
    }
    //nul besoin de surclasser les autres methodes pour laizeMere etc...
    public void setNbCouteaux(int nbCouteaux) {

        this.nbCouteaux = nbCouteaux;
    }

    public void setLisiereRecoupeGauche(int lisiereRecoupeGauche) {

        this.lisiereRecoupeGauche = lisiereRecoupeGauche;
    }

    public int getNbCouteaux() {

        return nbCouteaux;
    }

    public int getLisiereRecoupeGauche() {

        return lisiereRecoupeGauche;
    }

    public String getNbCouteauxToString(){

        return String.valueOf(nbCouteaux);
    }

    public String getLisiereRecoupeGaucheToString(){

        return String.valueOf(lisiereRecoupeGauche);
    }

    @Override
    public boolean isFull() {

        /**laizeMere >= 0 depuis que ResultRandomActivity existe*/
        if (laizeMere >= 0
                && lisiereGauche > 0
                && lisiereDroite > 0
                && nbCouteaux > 0
                && lisiereRecoupeGauche > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    int nbCouteaux;
    int lisiereRecoupeGauche;
}
