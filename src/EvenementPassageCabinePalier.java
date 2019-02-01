public class EvenementPassageCabinePalier extends Evenement {
    /* PCP: Passage Cabine Palier
       L'instant précis où la cabine passe juste en face d'un étage précis.
       Vous pouvez modifier cette classe comme vous voulez (ajouter/modifier des méthodes etc.).
    */

    private Etage étage;

    public EvenementPassageCabinePalier(long d, Etage e) {
        super(d);
        étage = e;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("PCP ");
        buffer.append(étage.numéro());
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Cabine cabine = immeuble.cabine;
        assert !cabine.porteOuverte;
        assert étage.numéro() != cabine.étage.numéro();
        assert cabine.intention() == '^' || cabine.intention() == 'v';
        if((!cabine.étage.equals(immeuble.étageLePlusBas()) || cabine.intention() == '^') && (!cabine.étage.equals(immeuble.étageLePlusHaut()) || cabine.intention() == 'v')) {
            cabine.étage = this.étage;
        }
        if(cabine.passagersVeulentDescendre() || étage.aDesPassagers()) {
            echeancier.ajouter(new EvenementOuverturePorteCabine(this.date + Global.tempsPourOuvrirOuFermerLesPortes));
        } else if((!this.étage.equals(immeuble.étageLePlusBas()) || cabine.intention() == '^') && (!this.étage.equals(immeuble.étageLePlusHaut()) || cabine.intention() == 'v')) {
            echeancier.ajouter(new EvenementPassageCabinePalier(this.date + Global.tempsPourBougerLaCabineDUnEtage, immeuble.étage(this.étage.numéro() + (cabine.intention() == 'v' ? - 1 : 1))));
        }
    }
}
