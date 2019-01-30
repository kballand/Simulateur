import java.io.BufferedReader;
import java.io.InputStreamReader;
/*
  Ne pas modifier cette classe.

  Programme de simulation d'un ascenseur avec deux boutons sur chaque palier et voyant
  lumineux indiquant l'intention de la cabine.
  En mode parfait, tous les passagers respectent l'indicateur et ne montent que si l'indicateur
  de direction correspond à leur destination.
  En mode infernal, les passagers entrent dans la cabine dès qu'elle se présente. En mode infernal
  on considère que les passager appuient toujours sur les deux boutons (bouton de demande pour 
  descendre ET bouton de demande pour monter).

  Les passagers sont des sportifs et ils peuvent décider de monter ou de descendre à pieds au bout
  d'un certain délai de patience. Une fois qu'un passager à décidé de partir à pieds, il termine
  complètement son voyage à pieds, même s'il passe sur un palier ou se trouve déjà la cabine.

  Liste des événements:
  - APP dans EvenementArriveePassagerPalier.java
  - PCP dans EvenementPassageCabinePalier.java
  - OPC dans EvenementOuverturePorteCabine.java
  - FPC dans EvenementFermeturePorteCabine.java
  - PAP dans EvenementPietonArrivePalier.java

*/

public class Main extends Global {

    private static boolean assertFlag = false;
    private static long memoDate = -1;
    private static BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    private static StringBuilder buffer = new StringBuilder(1024);

    public static void main(String[] args) {
        assert (assertFlag = true);
        System.out.println("Mode de simulation ? (p) parfait ? (i) infernal ? parfait par défaut ?");
        boolean mode = true;
        modeParfait = !readLine().equals("i");
        Echeancier échéancier = new Echeancier();
        Immeuble immeuble = new Immeuble(échéancier);
        int loop = 1;
        int nbPasSimul = 0;
        // Boucle principale du simulateur:
        while (!échéancier.estVide()) {
            if (loop == 1) {
                buffer.setLength(0);
                buffer.append("----- Etat actuel du simulateur (nombre total de pas = ");
                buffer.append(nbPasSimul);
                buffer.append(assertFlag ? ", assert on " : ", assert OFF ");
                buffer.append(") -----");
                System.out.println(buffer);
                immeuble.affiche(buffer);
                échéancier.affiche(buffer, immeuble);
                System.out.println("Taper \"Enter\", ou le nombre de pas, ou q pour quitter:");
                String réponse = readLine();
                if (réponse.equals("q")) {
                    return;
                }
                loop = parseInt(réponse);
            } else {
                loop--;
            }
            Evenement evenement = échéancier.retourneEtEnlevePremier();
            assert pasDeRetourDansLePassé(evenement.date) : "Retour dans le passé:" + memoDate + "/" + evenement.date;
            evenement.traiter(immeuble, échéancier);
            nbPasSimul++;
        }
        System.out.println("Echéancier vide. Arrêt.");
    }

    private static boolean pasDeRetourDansLePassé(long nouvelleDate) {
        if (nouvelleDate >= memoDate) {
            memoDate = nouvelleDate;
            return true;
        } else {
            return false;
        }
    }

    private static String readLine() {
        String result = null;
        try {
            result = input.readLine();
        } catch (Exception e) {
        }
        return result;
    }

    private static int parseInt(String réponse) {
        int result = 1;
        try {
            result = Integer.parseInt(réponse);
        } catch (Exception e) {
        }
        return result;
    }

}
