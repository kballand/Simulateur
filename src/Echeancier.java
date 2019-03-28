import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
/* Vous pouvez modifier cette classe comme vous voulez.
 */

public class Echeancier extends Global {

    private LinkedList<Evenement> listeEvenements;
    /* Comme toute les collections, il ne faut pas l'exporter.
     */

    public Echeancier() {
        listeEvenements = new LinkedList<>();
    }

    public boolean estVide() {
        return listeEvenements.isEmpty();
    }

    public void ajouter(Evenement e) {
        int pos = 0;
        while (pos < listeEvenements.size()) {
            if (listeEvenements.get(pos).date >= e.date) {
                listeEvenements.add(pos, e);
                return;
            } else {
                ++pos;
            }
        }
        listeEvenements.add(pos, e);
    }

    public Evenement retourneEtEnlevePremier() {
        return listeEvenements.pollFirst();
    }

    public void affiche(StringBuilder buffer, Immeuble ascenseur) {
        buffer.setLength(0);
        buffer.append("Echéancier = ");
        int index = 0;
        int taille = listeEvenements.size();
        while (index < taille) {
            listeEvenements.get(index).affiche(buffer, ascenseur);
            ++index;
            if (buffer.length() > 180) {
                index = taille;
                buffer.append(", ... (");
                buffer.append(index);
                buffer.append(")");
            } else if (index < taille) {
                buffer.append(',');
            }
        }
        System.out.println(buffer);
    }

    public void decalerFPC() {
        int index = 0;
        while (true) {
            Evenement e = listeEvenements.get(index);
            if (e instanceof EvenementFermeturePorteCabine) {
                listeEvenements.remove(index);
                EvenementFermeturePorteCabine eventFPC = (EvenementFermeturePorteCabine) e;
                eventFPC.setDate(e.date + tempsPourOuvrirOuFermerLesPortes);
                ajouter(eventFPC);
                return;
            }
            ++index;
        }
    }


    public void enleverEvenementPietonArrivePalier(Passager p){
        for(Evenement e : listeEvenements){
            if(e instanceof EvenementPietonArrivePalier && ((EvenementPietonArrivePalier)e).getPassager() == p){
                listeEvenements.remove(e);
                return;
            }
        }
    }
}
