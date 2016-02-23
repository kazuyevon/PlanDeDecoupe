package com.kazuyevon.laminateur.models;

import java.io.Serializable;

/**
 * Created by Fabrice on 20/02/2016.
 */
public class Commande implements Serializable {

    public Commande(int laizeOrder, int quantiteOrder){
        this.laizeOrder = laizeOrder;
        this.quantiteOrder = quantiteOrder;
    }
    public int getLaizeOrder(){
        return laizeOrder;
    }

    public int getQuantiteOrder() {
        return quantiteOrder;
    }

    int laizeOrder;
    int quantiteOrder;
}
