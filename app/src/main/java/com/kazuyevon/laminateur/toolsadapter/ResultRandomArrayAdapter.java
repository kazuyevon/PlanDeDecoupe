package com.kazuyevon.laminateur.toolsadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kazuyevon.laminateur.R;
import com.kazuyevon.laminateur.models.RandomListe;

/**
 * Created by Fabrice on 24/03/2016.
 */
public class ResultRandomArrayAdapter extends ArrayAdapter{
    /**http://www.ezzylearning.com/tutorial/customizing-android-listview-items-with-custom-arrayadapter*/
    //Le contexte dans lequel est présent notre adapter
    private Context context;
    //Le layout
    private int layoutResourceId;
    // une liste
    private RandomListe[] data;
    //Un mécanisme pour gérer l'affichage graphique depuis un layout XML
    private LayoutInflater inflater;

    private int defaultColor;
    private int green;
    private int yellow;
    private int[] pertesMinPosition;
    private int[] nbBobinesMinPosition;

    public ResultRandomArrayAdapter(Context context, int layoutResourceId, RandomListe[] data) {
        /**si appelé par AfficheResultRandomActivity*/
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layoutItem = convertView;
        RandomListeHolder holder = null;
        if(layoutItem == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutItem = inflater.inflate(R.layout.list_item_random, parent, false);

            holder = new RandomListeHolder();
            holder.textview_random = (TextView)layoutItem.findViewById(R.id.textview_Random);

            layoutItem.setTag(holder);
        }
        else{
            holder = (RandomListeHolder) layoutItem.getTag();
        }

        /**si appelé par AfficheResultRandomActivity*/
        RandomListe randomListe = (RandomListe)data[position];
        holder.textview_random.setText(randomListe.getItemTexte());
        /**(4) Changement de la couleur du fond de notre item
        * le faible taux de pertes prime sur faible taux de chgt de bobine.*/

        defaultColor = layoutItem.getResources().getColor(R.color.defaultColor);
        green = layoutItem.getResources().getColor(R.color.customGreen);
        yellow = layoutItem.getResources().getColor(R.color.customYellow);

        /**Declare une couleur par défaut sinon au scrolling, tous les items passe en couleur.
        * car la vue est constamment recyclé.*/
        holder.textview_random.setBackgroundColor(defaultColor);

        /**Pour cela on commence par colorer en jaune, puis si conflit,
        * cela colorera en vert.*/

        for (int _yellow: nbBobinesMinPosition){
            if(position == _yellow){
                holder.textview_random.setBackgroundColor(yellow);
            }
        }
        for( int _green: pertesMinPosition){
            if (position == _green){
                holder.textview_random.setBackgroundColor(green);
            }
        }
        return layoutItem;
    }
    static class RandomListeHolder
    {
        TextView textview_random;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }
    public void setPertesMin(int[] pertesMinPosition){
        this.pertesMinPosition = pertesMinPosition;
    }
    public void setNbBobinesMin(int[] nbBobinesMinPosition){
        this.nbBobinesMinPosition = nbBobinesMinPosition;
    }
}
