package com.kazuyevon.laminateur;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by Fabrice on 12/02/2016.
 */
public class CreditActivity extends AppCompatActivity {
    private TextView textView01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView01 = (TextView) findViewById(R.id.textView01);
        String texte = "Pourquoi cette application ?\n";
        texte += "J'ai travaillé à Lydall Performance Materials SAS Melrand 56310, en tant qu'aide-bobineur, entre autres.";
        texte += "C'est une industrie qui fabrique des filtres à air, notamment, de la même façon qu'une industrie papetière.";
        texte += "Je travaillais avec un bobineur sur un laminateur qui permet d'associer plusieurs couches de filtres les unes sur les autres de façon ";
        texte += "à pouvoir multiplier des pouvoirs filtrants ainsi que d'associer des propriétés mécaniques, thermiques, hydrophobes, etc..,\n";
        texte += "et de les enrouler pour former des bobinots, un peu comme des rouleaux d'essuie-tout, mais en plus gros, que le client redéroulera pour utiliser comme il l'entend.\n";
        texte += "Nous avions des commandes pour un même client tout d'abord, qui commandait des largeurs différentes pour un même produit, la commande arrive sur le bureau de la machine, et nous devions ";
        texte += "essayer de régler la machine, de façon à faire le maximum de bobinots en minimisant les pertes.\n";
        texte += "Nous devions donc, calculer un plan de découpe, parfois cela nous prenais plus d'une heure, et sans même savoir si notre plan était le plus optimiste possible.\n";
        texte += "J'ai quitté l'entreprise voilà un an, et ce problème m'est toujours resté en tête, j'ai garder contacte avec quelques collègues pour suivre cette histoire entre autre...\n";
        texte += "Je suis passionné d'informatique, et la programmation m'intéresse beaucoup, je me suis mis en tête que dès que j'aurais suffisamment de connaissances, je tenterai de ";
        texte += "créer une application en Java puis sur Android, pour créer ce calculateur de Plan de Découpe, c'est chose faite.\n";
        texte += "Le design laisse à désirer, mais je ne suis pas un artiste. Le programme est fonctionnel, je suis plutôt content. Me reste plus qu'à l'envoyer à mon collègue pour test.\n";
        texte += "\n" + "Votre serviteur : Fabrice THIEBAUT";
        textView01.setText(texte);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //Handle the back button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //effacerValeurs();
            textView01.setText("");
            CreditActivity.this.finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}