package com.kazuyevon.laminateur.func;

import android.util.Log;

import com.kazuyevon.laminateur.models.Commande;

import java.util.List;

/**
 * Created by Fabrice on 16/03/2016.
 */
public class FuncPeuplerListeOrderBobinots {
    private int nbBobinots;
    private int index;
    private int quantite;
    private List<Commande> commande;
    private int[] listeOrderBobinots;
    private Boolean funcPeuplerListeOrderBobinots;
    private String TAG = "FuncPeuplerListeOrderBobinots";

    public FuncPeuplerListeOrderBobinots(){
        funcPeuplerListeOrderBobinots = false;
    }
    public int[] PeuplerListeOrderBobinots(List<Commande> commande){

        this.commande = commande;

        nbBobinots = new FuncNbBobinots().CalculeNbBobinots(commande);

        listeOrderBobinots = new int[nbBobinots];
        index = 0;
        quantite = 0;
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
        Log.i(TAG, "listeOrderBobinots creer.");
        return listeOrderBobinots;
    }
}
