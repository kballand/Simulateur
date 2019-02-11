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
        int nbEntrees = 0;
        for(Passager passager : étage.passagers()) {
            if(!cabine.faireMonterPassager(passager)) {
                 break;
            } else {
                ++nbEntrees;
            }
        }
        assert cabine.porteOuverte;
        echeancier.ajouter(new EvenementFermeturePorteCabine(this.date + nbEntrees * Global.tempsPourEntrerOuSortirDeLaCabine + Global.tempsPourOuvrirOuFermerLesPortes));
    }

}
