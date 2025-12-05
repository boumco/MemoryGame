import extensions.File;
import extensions.CSVFile;



class Jeu extends Program {
    final double TEMPS_AFFICHAGE = 0.02; 
    final String REPERTOIRE = "ascii";
    final String CLASSEMENT = "./data/leaderboard.csv";
    CSVFile leaderboard = loadCSV(CLASSEMENT);
     
    /* Génère un tableau de chiffres aléatoires                               */
     
    int[] chiffreAléatoire(int[] tab, int nombreDeChiffre){
        for(int indice = 0; indice < nombreDeChiffre; indice++){
            tab[indice] = (int) random(0, 9);
        }
        return tab;
    }

     
    /* Pause en secondes                                                      */
     
    void attendre(double secondes){
        double fin = (double) getTime() + (double) secondes * (double) 1000.0;
        while(getTime() < fin) { }
    }
     
    /* Affiche chaque chiffre sous forme ASCII                                */
    String couleurAléatoire(){
        int couleur1 = (int) random(0,255);
        int couleur3 = (int) random(0,255);
        return rgb(couleur1, 0, couleur3, true);
    }
    void afficherChiffre(int[] tabDeChiffre, double secondes){
        for(int indice = 0; indice < length(tabDeChiffre); indice++){
            attendre(secondes);
            
            

            File file = newFile(REPERTOIRE + "/" + tabDeChiffre[indice] + ".txt");
            print(couleurAléatoire());
            while(ready(file)){
                println(readLine(file));
            }
            print(RESET);
            
            println("Chiffre numéro : " + (indice+1) );
            attendre(secondes);
        }

        // Efface l'écran
        File fileClear = newFile(REPERTOIRE + "/clear.txt");
        while(ready(fileClear)){
            println(readLine(fileClear));
        }
    }

     
    /* Convertit une chaîne numérique en tableau d'entiers                    */
    String toString(int nombre){
        String resultat = "";
        if (nombre == 0) {
            return "0";
        }
        while (nombre > 0) {
            int chiffre = nombre % 10;
            resultat = charToString((char) (chiffre + '0')) + resultat;
            nombre = nombre / 10;
        }
        return resultat;
    }
    String charToString(char c){
        String s = "";
        s = s + c;
        return s;
    }

    int[] StringtoInt(String reponse){
        int n = length(reponse);
        int[] resultat = new int[n];

        for(int i = 0; i < n; i++){
            char c = charAt(reponse, i);
            if(c >= '0' && c <= '9'){
                resultat[i] = c - '0';
            } else {
                resultat[i] = -1;
            }
        }
        return resultat;
    }
     





    /* Vérifie si la réponse correspond au tableau à mémoriser                */
     
    boolean verifierReponse(String reponse, int[] tabDeChiffre){
        int[] rep = StringtoInt(reponse);

        for(int i = 0; i < length(tabDeChiffre); i++){
            if(rep[i] != tabDeChiffre[i]){
                return false;
            }
        }
        return true;
    }

     
    /* Saisie sécurisée d'une chaîne de chiffres                              */
     
    String saisie(String phrase){
        println(phrase);

        while(true){
            String sortie = readString();

            boolean valide = true;
            for(int i = 0; i < length(sortie); i++){
                char c = charAt(sortie, i);
                if(c < '0' || c > '9'){
                    valide = false;
                }
            }

            if(valide){
                return sortie;
            }

            println("Les caractères que vous avez saisis ne sont pas valides, recommencez :");
        }
    }


    void println(String chaine, double temps){
        for (int idx = 0; idx < length(chaine); idx++) {
            attendre(temps);
            print(charAt(chaine, idx));
        }
        println("");
}
    void print(String chaine, double temps){
        for (int idx = 0; idx < length(chaine); idx++) {
            attendre(temps);
            print(charAt(chaine, idx));
        }
}


    void enregistrerClassement(String nomEleve, String scoreEleve, CSVFile csvFile, String cheminFichier, String cheminFichierIgnore) {
    int nombreLignes = rowCount(csvFile);
    int nombreColonnesExistantes = columnCount(csvFile);
    int nombreColonnesFinal;
    if (nombreColonnesExistantes < 2) {
        nombreColonnesFinal = 2;
    } else {
        nombreColonnesFinal = nombreColonnesExistantes;
    }
    String[][] donnees = new String[nombreLignes + 1][nombreColonnesFinal];

    // Copier l'existant
    for (int indexLigne = 0; indexLigne < nombreLignes; indexLigne++) {
        for (int indexCol = 0; indexCol < nombreColonnesFinal; indexCol++) {
            if (indexCol < nombreColonnesExistantes) {
                String valeurCellule = getCell(csvFile, indexLigne, indexCol);
                if (valeurCellule == null) {
                    donnees[indexLigne][indexCol] = "";
                } else {
                    donnees[indexLigne][indexCol] = valeurCellule;
                }
            } else {
                donnees[indexLigne][indexCol] = "";
            }
        }
    }

    // Ajouter la nouvelle ligne
    donnees[nombreLignes][0] = nomEleve;
    donnees[nombreLignes][1] = scoreEleve;
    for (int indexCol = 2; indexCol < nombreColonnesFinal; indexCol++) {
        donnees[nombreLignes][indexCol] = "";
    }

    // Sauvegarder (écrase le fichier existant avec le contenu étendu)
    saveCSV(donnees, cheminFichier);
}

void afficherClassement(CSVFile csvFile) {
    for (int indice = 0; indice < rowCount(csvFile); indice++) {
        println(getCell(csvFile, indice, 0) + " - " + getCell(csvFile, indice, 1));
    }
}



    /* Algorithme principal                                                   */
     
    void algorithm(){
        println("Bienvenue dans Memory Game !", TEMPS_AFFICHAGE);
        println("Vous allez devoir mémoriser une série de chiffres.", TEMPS_AFFICHAGE);

        println("Pour commencer, entrez votre nom d'utilisateur :", TEMPS_AFFICHAGE);
        String nomUtilisateur = readString();


        println("Combien de chiffres voulez-vous mémoriser dans chaque chaîne ?", TEMPS_AFFICHAGE);
        int nombreDeChiffre = readInt();
        
        println("Combien de secondes voulez-vous pour mémoriser chaque chiffre ?", TEMPS_AFFICHAGE);
        double temps = readDouble();
        int score = 0;
        boolean recommencer = true;

        while(recommencer){

            int[] tab = new int[nombreDeChiffre];
            chiffreAléatoire(tab, nombreDeChiffre);

            println("Voici les chiffres à mémoriser :");
            afficherChiffre(tab, temps);

            String reponseUtilisateur = saisie("Quels étaient les chiffres affichés ?");

            if(length(reponseUtilisateur) != nombreDeChiffre){
                println("Votre réponse doit contenir exactement " + nombreDeChiffre + " chiffres.", TEMPS_AFFICHAGE);
                break;
            }

            if(verifierReponse(reponseUtilisateur, tab)){
                println("Bravo, vous avez gagné !", TEMPS_AFFICHAGE);
                score++;

                println("Voulez-vous rejouer ? (oui/non)", TEMPS_AFFICHAGE);
                if(equals(readString(), "non")){
                    recommencer = false;
                }
            }
            else{
                println("Dommage, ce n'était pas la bonne réponse.", TEMPS_AFFICHAGE);
                print("La bonne réponse était : ", TEMPS_AFFICHAGE);

                for(int i = 0; i < nombreDeChiffre; i++){
                    print(tab[i]);
                }
                println("");

                recommencer = false;
            }
        }

        println("Partie terminée. Votre score : " + score, TEMPS_AFFICHAGE);
        
        enregistrerClassement(nomUtilisateur, toString(score), leaderboard, CLASSEMENT, CLASSEMENT);
        println("Classement des joueurs :", TEMPS_AFFICHAGE);
        afficherClassement(leaderboard);
        /*toString(score) = getCell(leaderboard, 1, 1) ;*/
        
    }
}



/* A faire : 
hkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkj
hkjhkjhkjhkjhkjhkjhkjhkjhkjhkj
hkjhkjhkjhkjhkjhkjhkjhkjhkjhkj
hkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkjhkj
- Classe Mode pour définir les modes de jeu                             
- Menu de sélection du mode de jeu

*/   