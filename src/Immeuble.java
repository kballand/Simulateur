public class Immeuble extends Global {
    /* Dans cette classe, vous pouvez ajouter/enlever/modifier/corriger les méthodes, mais vous ne
       pouvez pas ajouter des attributs (variables d'instance).
    */

    private Etage[] tableauDesEtages;
    /* Les étages, dans l'ordre de leur numérotation. Aucun null dans ce tableau.
       Comme toute les collections, il ne faut pas l'exporter.
    */

    public Cabine cabine; // de l'ascenseur.

    private Etage niveauDuSol; // le niveau 0 en général.

    public long cumulDesTempsDeTransport = 0;

    public long nombreTotalDesPassagersSortis = 0;

    public Etage étageLePlusBas() {
        assert tableauDesEtages[0] != null;
        return tableauDesEtages[0];
    }

    public Etage étageLePlusHaut() {
        assert tableauDesEtages[tableauDesEtages.length - 1] != null;
        return tableauDesEtages[tableauDesEtages.length - 1];
    }

    public Etage niveauDuSol() {
        assert étageLePlusHaut().numéro() >= niveauDuSol.numéro();
        assert étageLePlusBas().numéro() <= niveauDuSol.numéro();
        return niveauDuSol;
    }

    public Immeuble(Echeancier echeancier) {
	    Etage e;
        tableauDesEtages = new Etage[9];
        int n = -1;
        for (int i = 0; i < tableauDesEtages.length; ++i) {
            int fa = 41; // Pour le niveau 0 en dixieme de secondes:
            if (n != 0) {
                fa = fa * (tableauDesEtages.length - 1);
            }
	    e = new Etage(n, fa, this);
	    tableauDesEtages[i] = e;
            if (n++ == 0) {
                niveauDuSol = e;
            }
        }
        for (int i = 0; i < tableauDesEtages.length; ++i) {
            Etage etage = tableauDesEtages[i];
            long date = etage.arrivéeSuivante();
            echeancier.ajouter(new EvenementArriveePassagerPalier(date, etage));
        }
	e = étageLePlusHaut();
        cabine = new Cabine(e);
	e = étage(e.numéro() - 1);
	echeancier.ajouter(new EvenementPassageCabinePalier(tempsPourBougerLaCabineDUnEtage,e));
    }

    public void affiche(StringBuilder buffer) {
        System.out.print("Immeuble (mode ");
	System.out.print(modeParfait ? "parfait" : "infernal");
        System.out.println("):");
        int i = étageLePlusHaut().numéro();
        int min = étageLePlusBas().numéro();
        while (i >= min) {
	    buffer.setLength(0);
            étage(i).afficheDans(buffer);
	    System.out.println(buffer);
            --i;
        }
	buffer.setLength(0);
        cabine.afficheDans(buffer);
	buffer.append("\nCumul des temps de transport: ");
	buffer.append(cumulDesTempsDeTransport);
	buffer.append("\nNombre total des passagers sortis: ");
	buffer.append(nombreTotalDesPassagersSortis);
        buffer.append("\nRatio cumul temps / nb passagers : ");
	buffer.append(nombreTotalDesPassagersSortis == 0 ? 0 : cumulDesTempsDeTransport / nombreTotalDesPassagersSortis);
	System.out.println(buffer);
    }

    public Etage étage(int i) {
        assert étageLePlusBas().numéro() <= i : "trop bas" + i;
        assert étageLePlusHaut().numéro() >= i : "trop haut" + i;
        assert tableauDesEtages[i - étageLePlusBas().numéro()].numéro() == i;
        return tableauDesEtages[i - étageLePlusBas().numéro()];
    }

    public int nbEtages() {
        assert tableauDesEtages.length == (étageLePlusHaut().numéro() - étageLePlusBas().numéro() + 1);
        return tableauDesEtages.length;
    }

    public void ajouterCumul(long t){
    	cumulDesTempsDeTransport+=t;
    }

    public boolean passagerAuDessus(Etage e){
    	assert e != null;
    	int max = étageLePlusHaut().numéro();
    	for (int i=e.numéro()+1 ; i <= max; ++i) {
	    Etage et = étage(i);
	    if(et.aDesPassagers())
		return true;
    	}
    	return false;
    }

    public boolean passagerEnDessous(Etage e){
    	assert e != null;
    	int num = e.numéro();
    	for (int i = étageLePlusBas().numéro() ; i < num ; ++i) {
	    Etage et = étage(i);
	    if (et.aDesPassagers())
  		return true;
     	}
    	return false;
    }
}
