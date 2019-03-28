public class EvenementPietonArrivePalier extends Evenement {
    /* PAP: Pieton Arrive Palier
       L'instant précis ou un passager qui à décidé de continuer à pieds arrive sur un palier donné.
       Classe à faire complètement par vos soins.
    */
    private Passager passager;

    public EvenementPietonArrivePalier(long d, Passager p) {
        super(d);
        this.passager = p;
    }


    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("PAP");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        this.passager.changerEtagePieton(immeuble);
        Etage étageCourantPieton = this.passager.étageCourantPieton();
        if(this.passager.étageDestination() == étageCourantPieton) {
            étageCourantPieton.supprimerPieton(this.passager);
            immeuble.ajouterCumul(this.date - this.passager.dateDépart());
            ++immeuble.nombreTotalDesPassagersSortis;
        } else {
            echeancier.ajouter(new EvenementPietonArrivePalier(this.date + Global.tempsPourMonterOuDescendreUnEtageAPieds, this.passager));
        }
    }

    public Passager getPassager() {
        return passager;
    }
}
