public class EvenementOuverturePorteCabine extends Evenement {
    /* OPC: Ouverture Porte Cabine
       L'instant précis ou la porte termine de s'ouvrir.
    */

    public EvenementOuverturePorteCabine(long d) {
        super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("OPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        assert !immeuble.cabine.porteOuverte;
        immeuble.cabine.porteOuverte = true;
        int nbSorties = immeuble.cabine.faireDescendrePassagers(immeuble, this.date);
        immeuble.cabine.recalculIntention(immeuble, true);
        int nbEntrees = immeuble.cabine.étage.faireMonterPassagers(echeancier);
        assert immeuble.cabine.porteOuverte;
        echeancier.ajouter(new EvenementFermeturePorteCabine(this.date + (nbEntrees + nbSorties) * Global.tempsPourEntrerOuSortirDeLaCabine + Global.tempsPourOuvrirOuFermerLesPortes));
    }
}
