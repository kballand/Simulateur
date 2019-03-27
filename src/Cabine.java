public class Cabine extends Global {
    /* Dans cette classe, vous pouvez ajouter/enlever/modifier/corriger les méthodes, mais vous ne
       pouvez pas ajouter des attributs (variables d'instance).
    */

    public Etage étage; // actuel, là ou se trouve la Cabine, jamais null.

    public boolean porteOuverte;

    private char intention; // 'v' ou '^'

    private Passager[] tableauPassager;
    /* Ceux qui sont actuellement dans la Cabine. On ne décale jamais les élements.
       Comme toute les collections, il ne faut pas l'exporter.
       Quand on cherche une place libre, on fait le parcours de la gauche vers la droite.
     */

    public Cabine(Etage e) {
        assert e != null;
        étage = e;
        tableauPassager = new Passager[nombreDePlacesDansLaCabine];
        porteOuverte = false;
        intention = 'v';
    }

    public void afficheDans(StringBuilder buffer) {
        buffer.append("Contenu de la cabine: ");
        for (Passager p : tableauPassager) {
            if (p == null) {
                buffer.append("null");
            } else {
                p.afficheDans(buffer);
            }
            buffer.append(' ');
        }
        assert (intention == 'v') || (intention == '^');
        buffer.append("\nIntention de la cabine: " + intention);
    }

    /* Pour savoir si le passager p est bien dans la Cabine.
       Attention, c'est assez lent et c'est plutôt une méthode destinée à être 
       utilisée les asserts.
    */
    public boolean transporte(Passager p) {
        assert p != null;
        for (int i = tableauPassager.length - 1; i >= 0; i--) {
            if (tableauPassager[i] == p) {
                return true;
            }
        }
        return false;
    }

    public char intention() {
        assert (intention == 'v') || (intention == '^');
        return intention;
    }

    public void changerIntention(char s) {
        assert (s == 'v') || (s == '^');
        intention = s;
    }

    public boolean faireMonterPassager(Passager p) {
        assert p != null;
        assert !transporte(p);
        if (Global.isModeParfait()) {
            if (intention != p.sens()) {
                return false;
            }
        }
        for (int i = 0; i < tableauPassager.length; i++) {
            if (tableauPassager[i] == null) {
                tableauPassager[i] = p;
                return true;
            }
        }
        return false;
    }

    public int faireDescendrePassagers(Immeuble immeuble, long d) {
        int c = 0;
        int i = tableauPassager.length - 1;
        while (i >= 0) {
            if (tableauPassager[i] != null) {
                assert transporte(tableauPassager[i]);
                if (tableauPassager[i].étageDestination() == étage) {
                    immeuble.ajouterCumul(d - tableauPassager[i].dateDépart());
                    immeuble.nombreTotalDesPassagersSortis++;
                    tableauPassager[i] = null;
                    c++;
                }
            }
            i--;
        }
        return c;
    }

    public boolean passagersVeulentDescendre() {
        int i = tableauPassager.length - 1;
        while (i >= 0) {
            if (tableauPassager[i] != null) {
                assert transporte(tableauPassager[i]);
                if (tableauPassager[i].étageDestination() == étage) {
                    return true;
                }
            }
            i--;
        }
        return false;
    }

    public void recalculIntention(Immeuble immeuble) {
        if((this.intention == '^' && !this.étage.aDesPassagersQuiMontent() && !immeuble.passagerAuDessus(this.étage)) || (this.intention == 'v' && ! this.étage.aDesPassagersQuiDescendent() && !immeuble.passagerEnDessous(this.étage))) {
            int i = 0;
            while(i < this.tableauPassager.length && this.tableauPassager[i] == null) {
                ++i;
            }
            if(i < this.tableauPassager.length) {
                this.intention = this.tableauPassager[i].sens();
            } else {
                Passager[] passagers = this.étage.passagers();
                if(passagers.length > 0) {
                    this.changerIntention(passagers[0].sens());
                } else {
                    if ((this.intention == 'v' && !immeuble.passagerEnDessous(this.étage) && immeuble.passagerAuDessus(this.étage))) {
                        this.intention = '^';
                    } else if ((this.intention == '^' && !immeuble.passagerAuDessus(this.étage) && immeuble.passagerEnDessous(this.étage))) {
                        this.intention = 'v';
                    }
                }
            }
        }
    }

    public boolean doitOuvrirPortes(Immeuble immeuble) {
        return this.passagersVeulentDescendre() ||
                (this.intention == '^' && this.étage.aDesPassagersQuiMontent()) ||
                (this.intention == 'v' && this.étage.aDesPassagersQuiDescendent()) ||
                (this.étage.aDesPassagers() && (
                    !Global.isModeParfait() ||
                    (this.estVide() && (
                            (this.intention == '^' && !immeuble.passagerAuDessus(this.étage)) ||
                            (this.intention == 'v' && !immeuble.passagerEnDessous(this.étage))
                    ))
                ));
    }

    public boolean estVide() {
        int i = 0;
        int length = this.tableauPassager.length;
        while (i < length) {
            if (tableauPassager[i] != null) {
                return false;
            }
            i++;
        }
        return true;
    }

    public boolean peutBouger(Immeuble immeuble) {
        return (!this.étage.equals(immeuble.étageLePlusBas()) || this.intention == '^') && (!this.étage.equals(immeuble.étageLePlusHaut()) || this.intention == 'v');
    }
}
