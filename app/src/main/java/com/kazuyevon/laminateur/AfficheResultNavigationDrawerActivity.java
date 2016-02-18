/*
 * Copyright 2013 The Android Open Source Project
 * https://androidbelieve.com/material-navigation-drawer-with-header/
 */
package com.kazuyevon.laminateur;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kazuyevon.laminateur.fragment.ReglagesFragment;


/**
 * This example illustrates a common usage of the DrawerLayout widget
 * in the Android support library.
 * <p/>
 * <p>When a navigation (left) drawer is present, the host activity should detect presses of
 * the action bar's Up affordance as a signal to open and close the navigation drawer. The
 * ActionBarDrawerToggle facilitates this behavior.
 * Items within the drawer should fall into one of two categories:</p>
 * <p/>
 * <ul>
 * <li><strong>View switches</strong>. A view switch follows the same basic policies as
 * list or tab navigation in that a view switch does not create navigation history.
 * This pattern should only be used at the root activity of a task, leaving some form
 * of Up navigation active for activities further down the navigation hierarchy.</li>
 * <li><strong>Selective Up</strong>. The drawer allows the user to choose an alternate
 * parent for Up navigation. This allows a user to jump across an app's navigation
 * hierarchy at will. The application should treat this as it treats Up navigation from
 * a different task, replacing the current task stack using TaskStackBuilder or similar.
 * This is the only form of navigation drawer that should be used outside of the root
 * activity of a task.</li>
 * </ul>
 * <p/>
 * <p>Right side drawers should be used for actions, not navigation. This follows the pattern
 * established by the Action Bar that navigation should be to the left and actions to the right.
 * An action should be an operation performed on the current contents of the window,
 * for example enabling or disabling a data overlay on top of the current content.</p>
 */
public class AfficheResultNavigationDrawerActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    RecyclerView recyclerView;
    String navTitles[];
    //TypedArray navIcons;
    RecyclerView.Adapter recyclerViewAdapter;
    ActionBarDrawerToggle drawerToggle;
    private Bundle lamContainer;
    private String[] listeMenus;
    private String[] listeReglages;
    private String[] listeCommande;
    private String[] listeDecoupe;
    private String[] listePertes;
    private String logLam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afficherresult_navigationdrawer);

        /**On récupère l'objet Bundle envoyé par MainActivity*/
        lamContainer = this.getIntent().getExtras();

        /**On récupère les données du Bundle*/
        if (lamContainer != null && lamContainer.containsKey("listeMenus")) {
            listeMenus = this.getIntent().getStringArrayExtra("listeMenus");

            if (lamContainer.containsKey("listeReglages")) {
                listeReglages = this.getIntent().getStringArrayExtra("listeReglages");

                if (lamContainer.containsKey("listeCommande")) {
                    listeCommande = this.getIntent().getStringArrayExtra("listeCommande");

                    if (lamContainer.containsKey("listeDecoupe")) {
                        listeDecoupe = this.getIntent().getStringArrayExtra("listeDecoupe");

                        if (lamContainer.containsKey("listePertes")) {
                            listePertes = this.getIntent().getStringArrayExtra("listePertes");

                            if (lamContainer.containsKey("logLam")) {
                                logLam = this.getIntent().getStringExtra("logLam");

                                //aussitot arrive, on cree un nouveau bundle
                                Bundle transfert = new Bundle();
                                transfert.putStringArray("listeReglages", listeReglages);
                                transfert.putStringArray("listeCommande", listeCommande);
                                transfert.putStringArray("listeDecoupe", listeDecoupe);
                                transfert.putStringArray("listePertes", listePertes);
                                transfert.putString("logLam", logLam);
                                //Let's first set up toolbar
                                toolbar = (Toolbar) findViewById(R.id.toolbar);
                                setSupportActionBar(toolbar);
                                getSupportActionBar().setDisplayShowHomeEnabled(true);


                                //Initialize Views
                                recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                                drawerLayout = (DrawerLayout) findViewById(R.id.drawerActivity);

                                //Setup Titles and Icons of Navigation Drawer
                                //navTitles = getResources().getStringArray(R.array.navDrawerItems);
                                navTitles = listeMenus;
                                //navIcons = getResources().obtainTypedArray(R.array.navDrawerIcons);

                                /**
                                 *Here , pass the titles and icons array to the adapter .
                                 *Additionally , pass the context of 'this' activity .
                                 *So that , later we can use the fragmentManager of this activity to add/replace fragments.
                                 */

                                //recyclerViewAdapter = new RecyclerViewAdapter(navTitles,navIcons,this);
                                recyclerViewAdapter = new RecyclerViewAdapter(navTitles, transfert, this);
                                recyclerView.setAdapter(recyclerViewAdapter);

                                /**
                                 *It is must to set a Layout Manager For Recycler View
                                 *As per docs ,
                                 *RecyclerView allows client code to provide custom layout arrangements for child views.
                                 *These arrangements are controlled by the RecyclerView.LayoutManager.
                                 *A LayoutManager must be provided for RecyclerView to function.
                                 */

                                recyclerView.setLayoutManager(new LinearLayoutManager(this));

                                //Finally setup ActionBarDrawerToggle
                                drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
                                //This is necessary to change the icon of the Drawer Toggle upon state change.
                                drawerToggle.syncState();

                                //Add the Very First i.e Squad Fragment to the Container
                                // Create fragment and give it an argument for the selected article
                                Fragment reglagesFragment = new ReglagesFragment();
                                Bundle args = new Bundle();
                                args.putStringArray("listeReglages", listeReglages);
                                reglagesFragment.setArguments(args);
                                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.containerView, reglagesFragment, null);
                                fragmentTransaction.commit();

                            }
                        }
                    }
                }
            }
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_retour) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}