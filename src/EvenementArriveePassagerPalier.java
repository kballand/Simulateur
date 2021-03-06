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

        long dateActuelle = this.date;
        this.date += étage.arrivéeSuivante();
        echeancier.ajouter(this);

        if(immeuble.cabine.étage.equals(étage) && immeuble.cabine.porteOuverte && étage.faireMonterPassager(p)) {
            echeancier.decalerFPC();
        } else {
            étage.ajouter(p);
            echeancier.ajouter(new EvenementPietonArrivePalier(dateActuelle + Global.délaiDePatienceAvantSportif, p));
        }
    }
}
