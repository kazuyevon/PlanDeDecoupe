package com.kazuyevon.laminateur.func;

import android.util.Log;
import android.widget.Toast;

/**
 * Created by Fabrice on 17/03/2016.
 */
public class funcChercherLaizeMinDispo {
    private int laizeMin;
    private int[] listeOrderBobinots;
    private int[] listeUsedBobinots;
    private String TAG = "funcChercherLaizeMinDispo";

    public funcChercherLaizeMinDispo(){laizeMin = 0;}
    /**public int funcChercherLaizeMinDispo(int[]listeOrderBobinots, int[] listeUsedBobinots)*/
    public int ChercherLaizeMinDispo(int[] listeOrderBobinots, int[] listeUsedBobinots){
        this.listeOrderBobinots = listeOrderBobinots;
        this.listeUsedBobinots = listeUsedBobinots;
        try {
            for (int i = 0; i < listeOrderBobinots.length; i++) {
                for (int j = 1; j < listeOrderBobinots.length; j++){
                    if (listeOrderBobinots[i] < listeOrderBobinots[j] && listeUsedBobinots[i] == 0) {
                        laizeMin = listeOrderBobinots[i];
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "pas de laize min disponible");
        }
        Log.i(TAG, "Laize min de la commande : " + laizeMin);
        return laizeMin;
    }
}
