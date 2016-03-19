package com.kazuyevon.laminateur.func;

import android.util.Log;

import com.kazuyevon.laminateur.models.Commande;

import java.util.List;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class FuncNbBobinots {
    private List<Commande> commande;
    private int nbBobinots;
    private String TAG = "FuncNbBobinots";
    public FuncNbBobinots(){
        nbBobinots = 0;
    }
    public int CalculeNbBobinots(List<Commande> commande){

        this.commande = commande;

        for (int i = 0; i < commande.size(); i++){
            nbBobinots += commande.get(i).getQuantiteOrder();
        }
        Log.i(TAG, "nb bobinots : " + nbBobinots);
        return nbBobinots;
    }
}
