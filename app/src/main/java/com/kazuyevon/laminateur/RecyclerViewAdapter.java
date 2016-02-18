package com.kazuyevon.laminateur;

/**
 * Created by Fabrice on 16/02/2016.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kazuyevon.laminateur.fragment.CommandeFragment;
import com.kazuyevon.laminateur.fragment.DetailsFragment;
import com.kazuyevon.laminateur.fragment.PertesFragment;
import com.kazuyevon.laminateur.fragment.ReglagesFragment;
import com.kazuyevon.laminateur.fragment.DecoupeFragment;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    String[] titles;
    //TypedArray icons;
    Bundle transfert;
    Context context;
    private String[] listeReglages;
    private String[] listeCommande;
    private String[] listeDecoupe;
    private String[] listePertes;
    private String logLam;

    // The default constructor to receive titles,icons and context from MainActivity.
    //RecyclerViewAdapter(String[] titles , TypedArray icons , Context context){
    RecyclerViewAdapter(String[] titles, Bundle transfert, Context context) {

        this.titles = titles;
        //this.icons = icons;
        this.transfert = transfert;
        this.context = context;
    }

    /**
     * This is called every time when we need a new ViewHolder and a new ViewHolder is required for every item in RecyclerView.
     * Then this ViewHolder is passed to onBindViewHolder to display items.
     */

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        //on charge les vues
        if (viewType == 1) {
            View itemLayout = layoutInflater.inflate(R.layout.drawer_item_layout, null);
            return new ViewHolder(itemLayout, viewType, context);
        } else if (viewType == 0) {
            View itemHeader = layoutInflater.inflate(R.layout.header_layout, null);
            return new ViewHolder(itemHeader, viewType, context);
        }


        return null;
    }

    /**
     * This method is called by RecyclerView.Adapter to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     * So here , if position!=0 it implies its a row_item and we set the title and icon of the view.
     */

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {

        if (position != 0) {
            holder.navTitle.setText(titles[position - 1]);
            //holder.navIcon.setImageResource(icons.getResourceId(position-1,-1));
        }

    }

    /**
     * It returns the total no. of items . We +1 count to include the header view.
     * So , it the total count is 5 , the method returns 6.
     * This 6 implies that there are 5 row_items and 1 header view with header at position zero.
     */

    @Override
    public int getItemCount() {
        return titles.length + 1;
    }

    /**
     * This methods returns 0 if the position of the item is '0'.
     * If the position is zero its a header view and if its anything else
     * its a row_item with a title and icon.
     */

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        else return 1;
    }

    /**
     * Its a inner class to RecyclerViewAdapter Class.
     * This ViewHolder class implements View.OnClickListener to handle click events.
     * If the itemType==1 ; it implies that the view is a single row_item with TextView and ImageView.
     * This ViewHolder describes an item view with respect to its place within the RecyclerView.
     * For every item there is a ViewHolder associated with it .
     */

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView navTitle;
        //ImageView navIcon;
        Context context;

        public ViewHolder(View drawerItem, int itemType, Context context) {

            super(drawerItem);
            this.context = context;
            drawerItem.setOnClickListener(this);
            if (itemType == 1) {
                navTitle = (TextView) itemView.findViewById(R.id.tv_NavTitle);
                //navIcon = (ImageView) itemView.findViewById(R.id.iv_NavIcon);
            }
        }

        /**
         * This defines onClick for every item with respect to its position.
         */

        @Override
        public void onClick(View v) {

            AfficheResultNavigationDrawerActivity drawerActivity = (AfficheResultNavigationDrawerActivity) context;
            drawerActivity.drawerLayout.closeDrawers();
            FragmentTransaction fragmentTransaction = drawerActivity.getSupportFragmentManager().beginTransaction();

            switch (getPosition()) {
                case 1:
                    listeReglages = transfert.getStringArray("listeReglages");
                    Fragment reglagesFragment = new ReglagesFragment();
                    Bundle args1 = new Bundle();
                    args1.putStringArray("listeReglages", listeReglages);
                    reglagesFragment.setArguments(args1);
                    fragmentTransaction.replace(R.id.containerView, reglagesFragment);
                    fragmentTransaction.commit();
                    break;
                case 2:
                    listeCommande = transfert.getStringArray("listeCommande");
                    Fragment commandeFragment = new CommandeFragment();
                    Bundle args2 = new Bundle();
                    args2.putStringArray("listeCommande", listeCommande);
                    commandeFragment.setArguments(args2);
                    fragmentTransaction.replace(R.id.containerView, commandeFragment);
                    fragmentTransaction.commit();
                    break;
                case 3:
                    listeDecoupe = transfert.getStringArray("listeDecoupe");
                    Fragment decoupeFragment = new DecoupeFragment();
                    Bundle args3 = new Bundle();
                    args3.putStringArray("listeDecoupe", listeDecoupe);
                    decoupeFragment.setArguments(args3);
                    fragmentTransaction.replace(R.id.containerView, decoupeFragment);
                    fragmentTransaction.commit();
                    break;
                case 4:
                    listePertes = transfert.getStringArray("listePertes");
                    Fragment pertesFragment = new PertesFragment();
                    Bundle args4 = new Bundle();
                    args4.putStringArray("listePertes", listePertes);
                    pertesFragment.setArguments(args4);
                    fragmentTransaction.replace(R.id.containerView, pertesFragment);
                    fragmentTransaction.commit();
                    break;
                case 5:
                    logLam = transfert.getString("logLam");
                    Fragment detailsFragment = new DetailsFragment();
                    Bundle args5 = new Bundle();
                    args5.putString("logLam", logLam);
                    detailsFragment.setArguments(args5);
                    fragmentTransaction.replace(R.id.containerView, detailsFragment);
                    fragmentTransaction.commit();
                    break;
            }
        }
    }


}
