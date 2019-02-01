public class EvenementArriveePassagerPalier extends Evenement {
    /* APP: Arrivée Passager Palier
       L'instant précis ou un nouveau passager arrive sur un palier donné.
    */

    private Etage étage;

    public EvenementArriveePassagerPalier(long d, Etage edd) {
        super(d);
        étage = edd;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("APP ");
        buffer.append(étage.numéro());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        assert étage != null;
        assert immeuble.étage(étage.numéro()) == étage;
        Passager p = new Passager(date, étage, immeuble);
        assert p.étageDestination() != étage;
        assert (!immeuble.étageLePlusBas().equals(this.étage) || p.sens() == '^') && (!immeuble.étageLePlusHaut().equals(this.étage) || p.sens() == 'v');
        if(immeuble.cabine.étage.equals(this.étage)) {
            if(immeuble.cabine.porteOuverte) {
                if(immeuble.cabine.faireMonterPassager(p)) {
                    echeancier.decalerFPC();
                }
            } else {
                Evenement e = echeancier.retourneEtEnlevePremier();
                if(e instanceof EvenementOuverturePorteCabine) {
                    echeancier.ajouter(e);
                } else {
                    echeancier.ajouter(new EvenementOuverturePorteCabine(this.date + Global.tempsPourOuvrirOuFermerLesPortes));
                }
            }
        }
        this.étage.ajouter(p);
    }
}
