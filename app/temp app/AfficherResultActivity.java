package com.kazuyevon.laminateur;
/**
 * Created by Fabrice on 11/02/2016.
 */
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class AfficherResultActivity extends AppCompatActivity implements OnItemClickListener {
    private int currentLayoutId;
    private Bundle lamContainer;
    private int[] regLaminateur;

    private TextView textView1;

    private ListView listViewMenus;
    private ArrayAdapter<String> adapterListeMenus;

    private ListView listViewReglages;
    private ArrayAdapter<String> adapterListeReglages;
    private ListView listViewCommande;
    private ArrayAdapter<String> adapterListeCommande;
    private TextView textViewLogs;
    private ListView listViewPlanDeCoupe;
    private ArrayAdapter<String> adapterListePlanDeCoupe;
    private ListView listViewPertes;
    private ArrayAdapter<String> adapterListePertes;


    private String[] listeMenus;
    private String[] listeReglages;
    private String[] listeCommande;
    private String[] listeDecoupe;
    private String[] listePertes;
    private String logLam;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        currentLayoutId = R.layout.activity_afficherresult;
        setContentView(currentLayoutId);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView1 = (TextView) findViewById(R.id.textView1);
        listViewMenus = (ListView) findViewById(R.id.ListViewMenus);

        /**On récupère l'objet Bundle envoyé par MainActivity*/
        lamContainer = this.getIntent().getExtras();

        /**On récupère les données du Bundle*/
        if (lamContainer != null && lamContainer.containsKey("listeMenus")) {
            listeMenus = this.getIntent().getStringArrayExtra("listeMenus");

            if(lamContainer.containsKey("listeReglages")){
                listeReglages = this.getIntent().getStringArrayExtra("listeReglages");

                if(lamContainer.containsKey("listeCommande")){
                    listeCommande = this.getIntent().getStringArrayExtra("listeCommande");

                    if(lamContainer.containsKey("listeDecoupe")){
                        listeDecoupe = this.getIntent().getStringArrayExtra("listeDecoupe");

                        if(lamContainer.containsKey("listePertes")) {
                            listePertes = this.getIntent().getStringArrayExtra("listePertes");

                            if(lamContainer.containsKey("logLam")){
                                logLam = this.getIntent().getStringExtra("logLam");

                                //On lance l'affichage sous la meme condition
                                textView1.setText("Reglages Laminateur :\n");
                                adapterListeMenus = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeMenus);
                                listViewMenus.setAdapter(adapterListeMenus);
                                /**ne pas oublier d'implements OnItemClickListener et de l'import :
                                * import android.widget.AdapterView.OnItemClickListener;*/
                                listViewMenus.setOnItemClickListener(this);
                            }

                        }
                    }
                }
            }
        }
    }

    // Listener sur ListView
    public void onItemClick(AdapterView<?> adapterListe, View v, int position, long id){

        switch(position){
            case 0:
                Toast.makeText(getApplicationContext(), "Afficher les Reglages", Toast.LENGTH_LONG).show();
                currentLayoutId = R.layout.activity_viewreglages;
                setContentView(currentLayoutId);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                toolbar.setTitle("Reglages");
                listViewReglages = (ListView) findViewById(R.id.ListViewReglages);
                adapterListeReglages = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeReglages);
                listViewReglages.setAdapter(adapterListeReglages);
                break;
            case 1:
                Toast.makeText(getApplicationContext(), "Afficher la commande", Toast.LENGTH_LONG).show();
                currentLayoutId = R.layout.activity_viewcommande;
                setContentView(currentLayoutId);
                Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar);
                toolbar1.setTitle("Commande");
                listViewCommande = (ListView) findViewById(R.id.ListViewCommande);
                adapterListeCommande = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeCommande);
                listViewCommande.setAdapter(adapterListeCommande);
                break;
            case 2:
                Toast.makeText(getApplicationContext(), "Afficher le Plan de Decoupe", Toast.LENGTH_LONG).show();
                currentLayoutId = R.layout.activity_viewplandecoupe;
                setContentView(currentLayoutId);
                Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
                toolbar2.setTitle("Plan de Decoupe");
                listViewPlanDeCoupe = (ListView) findViewById(R.id.ListViewPlanDeCoupe);
                adapterListePlanDeCoupe = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeDecoupe);
                listViewPlanDeCoupe.setAdapter(adapterListePlanDeCoupe);
                break;
            case 3:
                Toast.makeText(getApplicationContext(), "Afficher les Pertes", Toast.LENGTH_LONG).show();
                currentLayoutId = R.layout.activity_viewpertes;
                setContentView(currentLayoutId);
                Toolbar toolbar3 = (Toolbar) findViewById(R.id.toolbar);
                toolbar3.setTitle("Pertes");
                listViewPertes = (ListView) findViewById(R.id.ListViewPertes);
                adapterListePertes = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listePertes);
                listViewPertes.setAdapter(adapterListePertes);
                break;
            case 4:
                Toast.makeText(getApplicationContext(), "Afficher les details de Calcul", Toast.LENGTH_LONG).show();
                currentLayoutId = R.layout.activity_viewlogs;
                setContentView(currentLayoutId);
                Toolbar toolbar4 = (Toolbar) findViewById(R.id.toolbar);
                toolbar4.setTitle("Details");
                textViewLogs = (TextView) findViewById(R.id.textViewLogs);
                textViewLogs.setText(logLam);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Pas de choix", Toast.LENGTH_LONG).show();
        }
        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //tente de recuperer le layout en cours
            //rajouter des tags au xml des layout.
            //je doit modifier le setContetnView() dans la classe resultActivity
            // pour le recuperer ensuite.
            //Si on est sur un layout fille, le bouton retour ne doit pas revenir
            //sur l'app de base, mais sur le layout parent
            // layout activity_afficherresult.xml
            // layout activity_viewlogs.xml
            //
            switch (currentLayoutId) {

                case R.layout.activity_viewreglages:
                case R.layout.activity_viewcommande:
                case R.layout.activity_viewplandecoupe:
                case R.layout.activity_viewpertes:
                case R.layout.activity_viewlogs:
                    //Recharge layout et composants
                    currentLayoutId = R.layout.activity_afficherresult;
                    setContentView(currentLayoutId);
                    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                    setSupportActionBar(toolbar);
                    textView1 = (TextView) findViewById(R.id.textView1);
                    textView1.setText("Reglages Laminateur :\n");
                    listViewMenus = (ListView) findViewById(R.id.ListViewMenus);
                    adapterListeMenus = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listeMenus);
                    listViewMenus.setAdapter(adapterListeMenus);
                    listViewMenus.setOnItemClickListener(this);
                    break;
                case R.layout.activity_afficherresult:
                default:
                    //effacerValeurs();
                    regLaminateur = new int[]{};
                    lamContainer = new Bundle();
                    textView1.setText("");
                    AfficherResultActivity.this.finish();
            }
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }
}