package com.kazuyevon.laminateur.func;

import android.util.Log;

import com.kazuyevon.laminateur.models.Commande;

import java.util.List;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class FuncPeuplerListeUsedBobinots {

    private Boolean funcPeuplerListeUsedBobinots;
    private int[] listeUsedBobinots;
    private List<Commande> commande;
    private int nbBobinots;
    private String TAG = "FuncPeuplerListeUsedBobinots";

    public FuncPeuplerListeUsedBobinots(){
        funcPeuplerListeUsedBobinots = false;
    }
    public int[] PeuplerListeUsedBobinots(List<Commande> commande){

        nbBobinots = new FuncNbBobinots().CalculeNbBobinots(commande);

        int[] listeUsedBobinots = new int[nbBobinots];
        for (int i = 0; i < nbBobinots; i++) {
            listeUsedBobinots[i] = 0;
        }
        //Log.i(TAG, "listeUsedBobinots creer.");
        return listeUsedBobinots;
    }
}
