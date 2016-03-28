package com.kazuyevon.laminateur.func;

import android.util.Log;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class FuncChercherMemeBobinot {

    /**Cherche un jumeaux au bobinot en cours mais qu'il ne soit pas à la même position*/
    int bobinot;
    int positionBob;
    int positionBobJumeaux;
    int[] listeOrderBobinots;
    int[] listeUsedBobinots;
    String TAG = "FuncChercherMemeBobinot";

    public FuncChercherMemeBobinot(){positionBobJumeaux = -1;}
    /**public int ChercherMemeBobinot(int bobinot, int posBobinot, int[]listeOrderBobinots, int[] listeUsedBobinots)*/
    public int ChercherMemeBobinot(int bobinot, int posBobinot, int[]listeOrderBobinots, int[] listeUsedBobinots){
        this.bobinot = bobinot;
        this.positionBob = posBobinot;
        this.listeOrderBobinots = listeOrderBobinots;
        this.listeUsedBobinots = listeUsedBobinots;
        try {
            /**On demarre à 1 car bobinot est suppose etre le premier bobinot*/
            /**On incremente de 2 en 2 puisqu'on cherche les jumeaux.*/
            for (int i = 1; i < listeOrderBobinots.length; i += 2) {
                if (bobinot == listeOrderBobinots[i]
                        && posBobinot < i
                        && i != posBobinot
                        && listeUsedBobinots[i] == 0) {
                    positionBobJumeaux = i;
                    i = listeOrderBobinots.length;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Pas de paire disponible pour laize : " + bobinot);
        }
        //Log.i(TAG, "Position du jumeaux : " + positionBobJumeaux);
        return positionBobJumeaux;
    }

}
