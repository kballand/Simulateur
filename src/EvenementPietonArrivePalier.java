public class EvenementPietonArrivePalier extends Evenement {
    /* PAP: Pieton Arrive Palier
       L'instant précis ou un passager qui à décidé de continuer à pieds arrive sur un palier donné.
       Classe à faire complètement par vos soins.
    */
    private Passager passager;

    public EvenementPietonArrivePalier(long d, Passager p) {
        // Signature approximative et temporaire... juste pour que cela compile.
        super(d);
        this.passager = p;
    }

    public void afficheDetails(StringBuilder buffer, Immeuble immeuble) {
        buffer.append("PAP");
    }

    public void traiter(Immeuble immeuble, Echeancier echeancier) {}

    public Passager getPassager() {
        return passager;
    }
}
