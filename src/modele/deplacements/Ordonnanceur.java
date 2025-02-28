package modele.deplacements;

import java.util.ArrayList;
import java.util.Observable;

import modele.plateau.Jeu;

import static java.lang.Thread.*;

public class Ordonnanceur extends Observable implements Runnable {
    private final Jeu jeu;
    private final ArrayList<RealisateurDeDeplacement> lstDeplacements = new ArrayList<>();
    private long pause;
    public void add(RealisateurDeDeplacement deplacement) {
        lstDeplacements.add(deplacement);
    }

    public Ordonnanceur(Jeu _jeu) {
        jeu = _jeu;
    }

    public void start(long _pause) {
        pause = _pause;
        new Thread(this).start();
    }

    @Override
    public void run() {
        boolean update = false;

        while(true) {
            jeu.resetCompteurDeplacement();
            for (RealisateurDeDeplacement d : lstDeplacements) {
                if (d.realiserDeplacement())
                    update = true;
            }

            RealisateurMouvement.getInstance().resetDirection();

            if (update) {
                setChanged();
                notifyObservers();
            }

            try {
                sleep(pause);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
