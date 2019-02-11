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
        Cabine cabine = immeuble.cabine;
        Etage étage = cabine.étage;
        assert !cabine.porteOuverte;
        cabine.porteOuverte = true;
        cabine.faireDescendrePassagers(immeuble, this.date);
        cabine.recalculIntention();
        étage.faireMonterPassagers();
        assert cabine.porteOuverte;
        echeancier.ajouter(new EvenementFermeturePorteCabine(this.date + étage.faireMonterPassagers() * Global.tempsPourEntrerOuSortirDeLaCabine + Global.tempsPourOuvrirOuFermerLesPortes));
    }

}
