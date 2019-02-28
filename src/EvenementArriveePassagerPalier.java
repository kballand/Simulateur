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
        assert (!immeuble.étageLePlusBas().equals(étage) || p.sens() == '^')
                && (!immeuble.étageLePlusHaut().equals(étage) || p.sens() == 'v');

        boolean monte = false;
        if(immeuble.cabine.étage.equals(étage)) {
            if(immeuble.cabine.porteOuverte) {
                if(étage.faireMonterPassager(p)) {
                    echeancier.decalerFPC();
                    monte = true;
                }
            } else {
                Evenement e = echeancier.retourneEtEnlevePremier();
                if(e instanceof EvenementOuverturePorteCabine) {
                    echeancier.ajouter(e);
                } else {
                    echeancier.ajouter(new EvenementOuverturePorteCabine(date + Global.tempsPourOuvrirOuFermerLesPortes));
                }
            }
        }
        if(!monte) {
            étage.ajouter(p);
            echeancier.ajouter(new EvenementPietonArrivePalier(this.date + Global.délaiDePatienceAvantSportif, p));
        }
        immeuble.cabine.recalculIntention(immeuble);
        echeancier.ajouter(new EvenementArriveePassagerPalier(date + étage.arrivéeSuivante(), étage));
    }
}
