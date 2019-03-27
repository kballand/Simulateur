import java.util.ArrayList;

public class Etage extends Global {
    /* Dans cette classe, vous pouvez ajouter/enlever/modifier/corriger les méthodes, mais vous ne
       pouvez pas ajouter des attributs (variables d'instance).
    */

    private int numéro;
    /* Le numéro de l'Etage du point de vue de l'usager (et non pas l'index correspondant
       dans le tableau.
    */

    private Immeuble immeuble;
    /* Back-pointer vers l'immeuble correspondant.
     */

    private LoiDePoisson poissonFrequenceArrivee;
    /* Pour cet étage.
     */

    private ArrayList<Passager> passagers = new ArrayList<Passager>();
    /* Les passagers qui attendent devant la porte et qui espèrent pouvoir monter
       dans la cabine.
       Comme toute les collections, il ne faut pas l'exporter.
    */

    private ArrayList<Passager> pietons = new ArrayList<Passager>();
    /* Les passager qui sont à pieds et qui sont actuellement arrivés ici.
       Comme toute les collections, il ne faut pas l'exporter.
    */

    public Etage(int n, int fa, Immeuble im) {
        numéro = n;
        immeuble = im;
        int germe = n << 2;
        if (germe <= 0) {
            germe = -germe + 1;
        }
        poissonFrequenceArrivee = new LoiDePoisson(germe, fa);
    }

    public void afficheDans(StringBuilder buffer) {
        if (numéro() >= 0) {
            buffer.append(' ');
        }
        buffer.append(numéro());
        if (this == immeuble.cabine.étage) {
            buffer.append(" C ");
            if (immeuble.cabine.porteOuverte) {
                buffer.append("[  ]: ");
            } else {
                buffer.append(" [] : ");
            }
        } else {
            buffer.append("   ");
            buffer.append(" [] : ");
        }
        int i = 0;
        while (((buffer.length() < 50) && (i < passagers.size()))) {
            passagers.get(i).afficheDans(buffer);
            i++;
            buffer.append(' ');
        }
        if (i < passagers.size()) {
            buffer.append("...(");
            buffer.append(passagers.size());
            buffer.append(')');
        }
        while (buffer.length() < 80) {
            buffer.append(' ');
        }
        buffer.append("| ");
        i = 0;
        while (((buffer.length() < 130) && (i < pietons.size()))) {
            pietons.get(i).afficheDans(buffer);
            i++;
            buffer.append(' ');
        }
        if (i < pietons.size()) {
            buffer.append("...(");
            buffer.append(pietons.size());
            buffer.append(')');
        }
    }

    public int numéro() {
        return this.numéro;
    }

    public void ajouter(Passager passager) {
        assert passager != null;
        passagers.add(passager);
    }

    public long arrivéeSuivante() {
        return poissonFrequenceArrivee.suivant();
    }

    public boolean aDesPassagersQuiMontent() {
        for (Passager p : passagers) {
            if (p.sens() == '^') {
                return true;
            }
        }
        return false;
    }

    public boolean aDesPassagersQuiDescendent() {
        for (Passager p : passagers) {
            if (p.sens() == 'v') {
                return true;
            }
        }
        return false;
    }

    public boolean aDesPassagers() {
        return (!passagers.isEmpty());
    }

    public Passager[] passagers() {
        Passager[] passagers = new Passager[this.passagers.size()];
        this.passagers.toArray(passagers);
        return passagers;
    }

    public void ajouterPieton(Passager passager) {
        this.passagers.remove(passager);
        this.pietons.add(passager);
    }

    public void supprimerPieton(Passager passager) {
        assert this.pietons.contains(passager);
        this.pietons.remove(passager);
    }

    public int faireMonterPassagers(Echeancier echeancier) {
        assert immeuble.cabine.étage == this;
        int nbSorties = 0;
        int taille = this.passagers.size();
        for(int i = 0; i < taille; i++) {
            int numero = i - nbSorties;
            Passager p = this.passagers.get(numero);
            if(immeuble.cabine.faireMonterPassager(p)) {
                ++nbSorties;
                echeancier.enleverEvenementPietonArrivePalier(p);
                this.passagers.remove(numero);
            }
        }
        return nbSorties;
    }

    public boolean faireMonterPassager(Passager p) {
        assert this.immeuble.cabine.étage == this;
        if(this.immeuble.cabine.faireMonterPassager(p)) {
            this.passagers.remove(p);
            return true;
        }
        return false;
    }
}
