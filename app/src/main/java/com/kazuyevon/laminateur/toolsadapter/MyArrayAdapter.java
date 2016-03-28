package com.kazuyevon.laminateur.toolsadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kazuyevon.laminateur.R;

/**
 * Created by Fabrice on 26/03/2016.
 */
public class MyArrayAdapter extends ArrayAdapter {
    /**http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter*/
    //Le contexte dans lequel est présent notre adapter
    private Context context;
    //Le layout
    private int layoutResourceId;
    // une liste
    private Object[] data;
    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater inflater;
    ObjectHolder holder;
    private int textColor;
    private int backgroundColor;

    public MyArrayAdapter(Context context, int layoutResourceId, Object[] data) {
        /**si appelé par AfficheResultRandomActivity*/
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layoutItem = convertView;
        holder = null;
        if(layoutItem == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutItem = inflater.inflate(R.layout.list_item_fragment_laizesmere, parent, false);

            holder = new ObjectHolder();
            holder.textbox_LaizeMere = (TextView)layoutItem.findViewById(R.id.textbox_LaizeMere);

            layoutItem.setTag(holder);
        }
        else{
            holder = (ObjectHolder) layoutItem.getTag();
        }
        /**si appelé par AfficheResultRandomActivity*/
        String liste = (String)data[position];
        holder.textbox_LaizeMere.setText(liste);


        /**(4) Changement de la couleur du fond et du texte de notre item.*/
        textColor = layoutItem.getResources().getColor(android.R.color.black);
        backgroundColor = layoutItem.getResources().getColor(android.R.color.white);

        /**declarer une couleur par défaut sinon au scrolling, tous les textes des items qui sont sont sélectionnés
         * passes en blanc car la vue est constamment recyclé.
         * ce défaut persistait tout de même, la solution a été de réinitialiser la transaction dans
         * MainActivity à chaque fois qu'on modifie le fragment.*/
        holder.textbox_LaizeMere.setBackgroundColor(backgroundColor);
        holder.textbox_LaizeMere.setTextColor(textColor);
        holder.textbox_LaizeMere.setTextSize(25);
        return layoutItem;
    }

    static class ObjectHolder {
        TextView textbox_LaizeMere;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
}