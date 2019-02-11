public class EvenementFermeturePorteCabine extends Evenement {
    /* FPC: Fermeture Porte Cabine
       L'instant précis ou la porte termine de se fermer.
       Tant que la porte n'est pas complètement fermée, il est possible pour un passager
       de provoquer la réouverture de la porte. Dans ce cas le FPC est décalé dans le temps
       selon la méthode decalerFPC qui est dans l'échéancier.
    */

    public EvenementFermeturePorteCabine(long d) {
        super(d);
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("FPC");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {
        Cabine cabine = immeuble.cabine;
        assert cabine.porteOuverte;
        cabine.porteOuverte = false;
        if(cabine.intention() == '^') {
            echeancier.ajouter(new EvenementPassageCabinePalier(this.date + Global.tempsPourBougerLaCabineDUnEtage, immeuble.étage(cabine.étage.numéro() + 1)));
        } else {
            echeancier.ajouter(new EvenementPassageCabinePalier(this.date + Global.tempsPourBougerLaCabineDUnEtage, immeuble.étage(cabine.étage.numéro() - 1)));
        }
        assert !cabine.porteOuverte;
    }


    public void setDate(long d) {
        this.date = d;
    }

}
