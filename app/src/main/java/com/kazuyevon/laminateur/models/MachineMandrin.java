package com.kazuyevon.laminateur.models;
/**
 * Created by Fabrice on 18/02/2016.
 */
import java.io.Serializable;

public class MachineMandrin implements Serializable{
    //http://www.technotalkative.com/android-send-object-from-one-activity-to-another-activity/

    public MachineMandrin(){

        laizeMere = 0;
        lisiereGauche = 0;
        lisiereDroite = 0;
    }
    public MachineMandrin(String defaut){

        //valeur par defaut mode coupe mandrin
        laizeMere = 1950;
        lisiereGauche = 100;
        lisiereDroite = 10;
    }
    public MachineMandrin(int laizeMere, int lisiereGauche, int lisiereDroite){

        this.laizeMere = laizeMere;
        this.lisiereGauche = lisiereGauche;
        this.lisiereDroite = lisiereDroite;

    }

    public void setLaizeMere(int laizeMere) {

        this.laizeMere = laizeMere;
    }

    public void setLisiereGauche(int lisiereGauche) {

        this.lisiereGauche = lisiereGauche;
    }

    public void setLisiereDroite(int lisiereDroite) {

        this.lisiereDroite = lisiereDroite;
    }

    public int getLaizeMere() {

        return laizeMere;
    }

    public int getLisiereGauche() {

        return lisiereGauche;
    }

    public int getLisiereDroite() {

        return lisiereDroite;
    }

    public String getLaizeMereToString() {

        return String.valueOf(laizeMere);
    }

    public String getLisiereGaucheToString(){

        return String.valueOf(lisiereGauche);
    }

    public String getLisiereDroiteToString(){

        return String.valueOf(lisiereDroite);
    }

    public boolean isFull() {

        if (laizeMere > 0 && lisiereGauche > 0 && lisiereDroite > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    int laizeMere;
    int lisiereGauche;
    int lisiereDroite;

}
