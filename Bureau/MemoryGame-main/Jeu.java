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
        effacerEcran();
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
// Fonction utilitaire locale pour parser un score en entier (valeur non numérique => 0)
        int parseScoreSafe(String s) {
            if (s == null) return 0;
            try {
                return Integer.parseInt(s);
            } catch (Exception e) {
                return 0;
            }
        }

        // Enregistre une nouvelle ligne dans le fichier CSV et retourne le CSV rechargé (mis à jour).
    CSVFile enregistrerClassement(String nomEleve, String scoreEleve, CSVFile csvAvantEnregistrement, String cheminFichier) {
        int lignesAvant = rowCount(csvAvantEnregistrement);
        int colonnesAvant = columnCount(csvAvantEnregistrement);
        int colonnesMinimum = 2;
        int colonnesFinales = (colonnesAvant < colonnesMinimum) ? colonnesMinimum : colonnesAvant;

        // Construire la matrice de données avec une ligne supplémentaire
        String[][] donneesEtendues = new String[lignesAvant + 1][colonnesFinales];

        // Copier les cellules existantes dans la matrice étendue (remplacer null par "")
        for (int ligne = 0; ligne < lignesAvant; ligne++) {
            for (int colonne = 0; colonne < colonnesFinales; colonne++) {
                if (colonne < colonnesAvant) {
                    String valeurCellule = getCell(csvAvantEnregistrement, ligne, colonne);
                    donneesEtendues[ligne][colonne] = (valeurCellule == null) ? "" : valeurCellule;
                } else {
                    donneesEtendues[ligne][colonne] = "";
                }
            }
        }

        // Ajouter la nouvelle ligne (nom et score) dans la dernière ligne
        donneesEtendues[lignesAvant][0] = (nomEleve == null) ? "" : nomEleve;
        donneesEtendues[lignesAvant][1] = (scoreEleve == null) ? "" : scoreEleve;
        for (int colonne = 2; colonne < colonnesFinales; colonne++) {
            donneesEtendues[lignesAvant][colonne] = "";
        }

        

        // Trier les lignes par score décroissant (tri à bulle simple adapté)
        for (int i = 0; i < donneesEtendues.length - 1; i++) {
            for (int j = 0; j < donneesEtendues.length - 1 - i; j++) {
                int scoreA = parseScoreSafe(donneesEtendues[j][1]);
                int scoreB = parseScoreSafe(donneesEtendues[j + 1][1]);
                if (scoreA < scoreB) {
                    // échange des lignes entières
                    String[] temp = donneesEtendues[j];
                    donneesEtendues[j] = donneesEtendues[j + 1];
                    donneesEtendues[j + 1] = temp;
                }
            }
        }

        // Sauvegarder et recharger le fichier pour obtenir le CSV mis à jour
        saveCSV(donneesEtendues, cheminFichier);
        CSVFile csvApresEnregistrement = loadCSV(cheminFichier);
        return csvApresEnregistrement;
    }
    void effacerEcran(){
        File fileClear = newFile(REPERTOIRE + "/clear.txt");
        while(ready(fileClear)){
            println(readLine(fileClear));
        }
    }
    // Affiche tout le contenu du CSV fourni et met en évidence l'entrée récemment ajoutée
    void afficherClassement(CSVFile csvAFicher, String nomAjoute, String scoreAjoute) {
        int nombreLignes = rowCount(csvAFicher);
        int nombreColonnes = columnCount(csvAFicher);
        attendre(0.8);
        effacerEcran();
        println("================================", TEMPS_AFFICHAGE);
        println("        LEADERBOARD            ", TEMPS_AFFICHAGE);
        println("================================", TEMPS_AFFICHAGE);
        println("Classement des " + nombreLignes + " participants :");
        println("Nom des Joueurs - Score des Joueurs");
        for (int ligne = 0; ligne < nombreLignes; ligne++) {
            String valeurNom = getCell(csvAFicher, ligne, 0);
            String valeurScore = getCell(csvAFicher, ligne, 1);
            if (valeurNom == null) valeurNom = "";
            if (valeurScore == null) valeurScore = "";
            println(valeurNom + " - " + valeurScore);
        }
        println("=================================", TEMPS_AFFICHAGE);

        String nomAffiche;
        if (nomAjoute == null) {
            nomAffiche = "";
        } else {
            nomAffiche = nomAjoute;
        }

        String scoreAffiche;
        if (scoreAjoute == null) {
            scoreAffiche = "";
        } else {
            scoreAffiche = scoreAjoute;
        }

        println("Entrée ajoutée : " + nomAffiche + " - " + scoreAffiche);
    }

    void Jeu(String nomJoueur, double tempsAffichage, int nombreDAide, int nombreDeChiffreAfficher){
    boolean recommencer = true;
    int score = 0;
    boolean drapeau = false;
    String reponseUtilisateur = "";
    while(recommencer){
        int[] tab = new int[nombreDeChiffreAfficher];
        afficherChiffre(chiffreAléatoire(tab, nombreDeChiffreAfficher), tempsAffichage);
        do{
            println("Quels étaient les chiffres affichés ? (tapez 'aide' pour utiliser une aide, il vous en reste " + nombreDAide + ")", TEMPS_AFFICHAGE);
            reponseUtilisateur = readString();
            if(equals(reponseUtilisateur, "aide")){
                if(nombreDAide > 0){
                    afficherChiffre(tab, tempsAffichage);
                    nombreDAide--;
                }
                else{
                    println("Vous n'avez plus d'aide disponible.", TEMPS_AFFICHAGE);
                }} else if(verifierString(reponseUtilisateur)){
                drapeau = true;
            }
    }while(!drapeau);
        drapeau = false;

        if(length(reponseUtilisateur) != nombreDeChiffreAfficher){
            println("Votre réponse doit contenir exactement " + nombreDeChiffreAfficher + " chiffres.", TEMPS_AFFICHAGE);
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

            for(int i = 0; i < nombreDeChiffreAfficher; i++){
                attendre(0.1);
                print(tab[i]);
            }
            println("");

            recommencer = false;
        }
    
    }
    partieFini(score, nomJoueur);
    }

    // Alternative : vérifie que la chaîne ne contient que des chiffres (et non vide)
    boolean verifierString(String reponseUtilisateur){
        if(length(reponseUtilisateur) <= 0){
            println("Votre réponse ne peut pas être vide. Veuillez réessayer.", TEMPS_AFFICHAGE);
            return false;
        }
        for(int i = 0; i < length(reponseUtilisateur); i++){
            char c = charAt(reponseUtilisateur, i);
            if(c < '0' || c > '9'){
                println("La réponse doit contenir uniquement des chiffres. Veuillez réessayer.", TEMPS_AFFICHAGE);
                return false;
            }
        }
        return true;
    }

    void partieFini(int score, String nomUtilisateur){
        println("Partie terminée. Votre score : " + score, TEMPS_AFFICHAGE);
        
        CSVFile nouveauClassement = enregistrerClassement(nomUtilisateur, toString(score), leaderboard, CLASSEMENT);
        println("Classement des joueurs :", TEMPS_AFFICHAGE);
        afficherClassement(nouveauClassement , nomUtilisateur, toString(score));
    }

    /* Algorithme principal                                                   */
     void algorithm(){
        println("Bienvenue dans Memory Game !", TEMPS_AFFICHAGE);
        println("Vous allez devoir mémoriser une série de chiffres.", TEMPS_AFFICHAGE);

        println("Pour commencer, entrez votre nom d'utilisateur :", TEMPS_AFFICHAGE);
        String nomUtilisateur = readString();
        Jeu(nomUtilisateur, 1.0, 3, 5);
     }
    
    }

  