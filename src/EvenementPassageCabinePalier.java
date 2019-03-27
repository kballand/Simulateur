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
        assert Math.abs(cabine.étage.numéro() - étage.numéro()) == 1;


        cabine.étage = étage;
        cabine.recalculIntention(immeuble, false);
        if (cabine.doitOuvrirPortes(immeuble)) {
            echeancier.ajouter(new EvenementOuverturePorteCabine(date + Global.tempsPourOuvrirOuFermerLesPortes));
        } else if(cabine.peutBouger(immeuble)) {
            echeancier.ajouter(new EvenementPassageCabinePalier(date + Global.tempsPourBougerLaCabineDUnEtage, immeuble.étage(étage.numéro() + (cabine.intention() == 'v' ? -1 : 1))));
        }
    }
}


