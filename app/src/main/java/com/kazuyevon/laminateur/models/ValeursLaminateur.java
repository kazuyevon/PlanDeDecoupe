package com.kazuyevon.laminateur.models;

import android.content.SharedPreferences;

/**
 * Created by Fabrice on 20/03/2016.
 */
public class ValeursLaminateur {
    private int[] laizesBobineMere;

    public ValeursLaminateur(){

        //laizesBobineMere = new int[]{ 1300, 1330, 1360, 1390, 1420, 1450, 1480, 1510, 1540, 1570, 1600, 1660, 1720, 1780, 1840, 1900, 1960};
                                          //  +30,  +30,  +30,  +30,  +30,  +30,  +30,  +30,  +30,  +30,  +60,  +60,  +60,  +60,  +60,  +60.

        if (laizesBobineMere == null){
            laizesBobineMere = new int[]{ 600, 1200, 1250, 1300, 1350, 1400, 1450, 1500, 1550, 1600, 1650, 1700, 1750, 1800, 1850, 1900, 1950, 2000};
            //   +600,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,  +50,
        }

    }
    public ValeursLaminateur(String valeursLaminateurPrefs){
        String[] laizesBobineMereToString = valeursLaminateurPrefs.split(" ");
        laizesBobineMere = new int[laizesBobineMereToString.length];
        for (int i = 0; i < laizesBobineMereToString.length; i++){
            laizesBobineMere[i] = Integer.parseInt(laizesBobineMereToString[i]);
        }
    }

    public int[] getLaizesBobineMere() {
        return laizesBobineMere;
    }
    public String[] getLaizesBobineMereToString(){
        final String[] laizesBobineMereToString = new String[laizesBobineMere.length];
        for (int i = 0; i < laizesBobineMere.length; i++){
            laizesBobineMereToString[i] = String.valueOf(laizesBobineMere[i]);
        }
        return laizesBobineMereToString;
    }
    public int getNbLaizesBobineMere(){
        return laizesBobineMere.length;
    }
    public int getOneLaizeBobineMere(int nb){
        return laizesBobineMere[nb];
    }
    public int getLaizeBobinesMereMin() {
        return laizesBobineMere[0];
    }
    public int getLaizeBobinesMereMax() {
        return laizesBobineMere[laizesBobineMere.length - 1];
    }
    public String getLaizesBobineMereToText(){
        String texte = "";
        for (int i = 0; i < laizesBobineMere.length; i++){
            texte += laizesBobineMere[i] + " ";
        }
        return texte;
    }
}
