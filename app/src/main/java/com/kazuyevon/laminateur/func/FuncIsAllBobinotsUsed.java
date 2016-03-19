package com.kazuyevon.laminateur.func;

import android.util.Log;

import com.kazuyevon.laminateur.models.Commande;

import java.util.List;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class FuncIsAllBobinotsUsed {

    private int bobinotsUsed;
    private List<Commande> commande;
    private int nbBobinots;
    private int[] listeUsedBobinots;
    private int testUsedBobinots = 0;
    private String TAG = "FuncIsAllBobinotsUsed";

    public FuncIsAllBobinotsUsed(){
        bobinotsUsed = 0;
    }
    public Boolean IsAllBobinotsUsed(List<Commande> commande, int[] listeUsedBobinots){
        /**Vérifie que tous les bobinots ont été utilisés.*/
        nbBobinots = new FuncNbBobinots().CalculeNbBobinots(commande);
        this.listeUsedBobinots = listeUsedBobinots;

        for (int i = 0; i < nbBobinots; i++) {
            /**Compte meme les bobinots trop large*/
            if(listeUsedBobinots[i] == -1) {
                testUsedBobinots += 1;}
            testUsedBobinots += listeUsedBobinots[i];
        }
        if (testUsedBobinots == nbBobinots) {
            Log.i(TAG, "Tous les Bobinots sont utilises.");
            return true;
        }
        else {
            Log.i(TAG, "Tous les Bobinots ne sont pas utilises.");
            return false;
        }
    }
}
