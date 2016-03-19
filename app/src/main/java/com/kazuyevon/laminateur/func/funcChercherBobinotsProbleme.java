package com.kazuyevon.laminateur.func;

import android.util.Log;

/**
 * Created by Fabrice on 17/03/2016.
 */
public class funcChercherBobinotsProbleme {
    private String bobinotsProbleme;
    private int[] listeOrderBobinots;
    private int[] listeUsedBobinots;
    private String TAG = "funcChercherBobinotsProbleme";
    public funcChercherBobinotsProbleme(){bobinotsProbleme = "";}
    public String chercherBobinotsProbleme(int[] listeOrderBobinots, int[] listeUsedBobinots){
        this.listeOrderBobinots = listeOrderBobinots;
        this.listeUsedBobinots = listeUsedBobinots;
        for (int i = 0; i < listeUsedBobinots.length; i++){
            if(listeUsedBobinots[i] == -1){
                bobinotsProbleme += " " + listeOrderBobinots[i];
            }
        }
        Log.i(TAG, "Les bobinots problematiques : " + bobinotsProbleme);
        return bobinotsProbleme;
    }
}
