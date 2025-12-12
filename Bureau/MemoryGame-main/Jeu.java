import extensions.File;
import extensions.CSVFile;

class Jeu extends Program {
    final double TEMPS_AFFICHAGE = 0.02; 
    final String REPERTOIRE = "ascii";
    final String CLASSEMENT = "./data/leaderboard.csv";
    String niveauDifficulte = "";
    CSVFile leaderboard = loadCSV(CLASSEMENT);
    String nomUtilisateur = "";
    Mode modeChoisi = new Mode();     
    
    /* Génère un tableau de chiffres aléatoires*/
     
    int[] chiffreAléatoire(int[] tab, int nombreDeChiffre){
        for(int indice = 0; indice < nombreDeChiffre; indice++){
            tab[indice] = (int) random(0, 9);
        }
        return tab;
        }

     
    /* Pause en secondes*/
     
    void attendre(double secondes){
        double fin = (double) getTime() + (double) secondes * (double) 1000.0;
        while(getTime() < fin) { }
        }
     
    /* Affiche chaque chiffre sous forme ASCII*/
    String couleurAléatoire(){
        int couleur1 = (int) random(100,255);
        int couleur3 = (int) random(100,255);
        return rgb(couleur1, 0, couleur3, true);
        }
    void afficherChiffre(int[] tabDeChiffre, double secondes){
        for(int indice = 0; indice < length(tabDeChiffre); indice++){
            attendre(secondes);
            
            

            File file = newFile(REPERTOIRE + "/" + tabDeChiffre[indice] + ".txt");
            print(couleurAléatoire());
            println("");
            println("");
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

     
    /* Convertit une chaîne numérique en tableau d'entiers*/
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
     





    /* Vérifie si la réponse correspond au tableau à mémoriser*/
     
    boolean verifierReponse(String reponse, int[] tabDeChiffre){
        int[] rep = StringtoInt(reponse);

        for(int i = 0; i < length(tabDeChiffre); i++){
            if(rep[i] != tabDeChiffre[i]){
                return false;
            }
        }
        return true;
        }

     
    /* Saisie sécurisée d'une chaîne de chiffres*/
     
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
        int colonnesMinimum = 3; // Nom, Score, Niveau de difficulté
        int colonnesFinales = (colonnesAvant < colonnesMinimum) ? colonnesMinimum : colonnesAvant;

        // Construire la matrice de données avec une ligne supplémentaire
        String[][] donneesEtendues = new String[lignesAvant + 1][colonnesFinales];

        // Copier les cellules existantes dans la matrice étendue (remplacer null ou "?" par des valeurs explicites)
        for (int ligne = 0; ligne < lignesAvant; ligne++) {
            for (int colonne = 0; colonne < colonnesFinales; colonne++) {
                if (colonne < colonnesAvant) {
                    String valeurCellule;
                    try {
                        valeurCellule = getCell(csvAvantEnregistrement, ligne, colonne);
                    } catch (Exception e) {
                        valeurCellule = null;
                    }

                    if (valeurCellule == null || equals(valeurCellule, "?")) {
                        if (colonne == 0) {
                            donneesEtendues[ligne][colonne] = "Inconnu";
                        } else if (colonne == 1) {
                            donneesEtendues[ligne][colonne] = "0";
                        } else if (colonne == 2) {
                            donneesEtendues[ligne][colonne] = "Custom";
                        } else {
                            donneesEtendues[ligne][colonne] = "";
                        }
                    } else {
                        donneesEtendues[ligne][colonne] = valeurCellule;
                    }
                } else {
                    donneesEtendues[ligne][colonne] = "";
                }
            }
        }

        // Ajouter la nouvelle ligne (nom, score, niveau de difficulté)
        donneesEtendues[lignesAvant][0] = (nomEleve == null || length(nomEleve) == 0) ? "Inconnu" : nomEleve;
        donneesEtendues[lignesAvant][1] = (scoreEleve == null || length(scoreEleve) == 0) ? "0" : scoreEleve;
        donneesEtendues[lignesAvant][2] = (niveauDifficulte == null || length(niveauDifficulte) == 0) ? "Custom" : niveauDifficulte;
        for (int colonne = 3; colonne < colonnesFinales; colonne++) {
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
        println("Nom des Joueurs - Score des Joueurs - Niveau de Difficulté");
        for (int ligne = 0; ligne < nombreLignes; ligne++) {
            String valeurNom = getCell(csvAFicher, ligne, 0);
            String valeurScore = getCell(csvAFicher, ligne, 1);
            String niveauDifficulte = getCell(csvAFicher, ligne, 2);
            if (valeurNom == null) valeurNom = "";
            if (valeurScore == null) valeurScore = "";
            if (niveauDifficulte == null) niveauDifficulte = "";
            println(valeurNom + " - " + valeurScore+ " - " + niveauDifficulte);
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

  
    /* Algorithme principal*/
     
     void choixModePrincipal(){
        File menu1 = newFile(REPERTOIRE + "/menu1.txt");
        effacerEcran();
        while(ready(menu1)){
            println(readLine(menu1));
        }
        int choix = readInt();
        if(choix == 2){
            choixDifficulter();
        } else if(choix == 3){
            effacerEcran();
            afficherClassement(leaderboard, null, null);
        } else{
            effacerEcran();
            println("Désolé mais ce mode n'est pas encore disponible pour vous...", TEMPS_AFFICHAGE);
        }
     }

     void choixCustom(){
        effacerEcran();
        println("Combien de temps d'affichage voulez-vous entre chaque chiffre ? (en secondes)", TEMPS_AFFICHAGE);
        double choixTemps = readDouble();
        println("Combien de nombre d'aide voulez-vous", TEMPS_AFFICHAGE);
        int nombreAide = readInt();
        println("Combien de chiffre voulez vous par série ?", TEMPS_AFFICHAGE);
        int nbChiffre = readInt();
        println("Le jeu va commencer dans un instant, préparer vous", TEMPS_AFFICHAGE);
        attendre(1);
        for(int indice = 3; indice>=0; indice--){
            effacerEcran();
            println("Le jeu va commencer dans " + indice);
            attendre(1);
        }
        Jeu(nomUtilisateur, choixTemps, nombreAide, nbChiffre);
     }
     void choixDifficulter(){
        effacerEcran();
        File menu2 = newFile(REPERTOIRE + "/menu2.txt");
        while(ready(menu2)){
            println(readLine(menu2));
        }
        int choix = readInt();
        
        if(choix == 1){
            println("Le jeu va commencer dans un instant, préparer vous", TEMPS_AFFICHAGE);
            niveauDifficulte = niveauDifficulte + "Facile";
        attendre(1);
        for(int indice = 3; indice>=0; indice--){
            effacerEcran();
            println("Le jeu va commencer dans " + indice);
            attendre(1);
        }
            Jeu(nomUtilisateur, 1.0, 3, 5);
        } else if(choix == 2){
         println("Le jeu va commencer dans un instant, préparer vous", TEMPS_AFFICHAGE);
        attendre(1);
            niveauDifficulte = niveauDifficulte + "Moyen";        
        for(int indice = 3; indice>=0; indice--){
            effacerEcran();
            println("Le jeu va commencer dans " + indice);
            attendre(1);
        }
            Jeu(nomUtilisateur, 0.5, 2, 5);
        } else if(choix == 3){
         println("Le jeu va commencer dans un instant, préparer vous", TEMPS_AFFICHAGE);
        attendre(1);
            niveauDifficulte = niveauDifficulte + "Difficile";
        for(int indice = 3; indice>=0; indice--){
            effacerEcran();
            println("Le jeu va commencer dans " + indice);
            attendre(1);
        }
            Jeu(nomUtilisateur, 0.2, 1, 7);
        } else if(choix == 4){
         println("Le jeu va commencer dans un instant, préparer vous", TEMPS_AFFICHAGE);
        attendre(1);
            niveauDifficulte = niveauDifficulte + "Expert";
        for(int indice = 3; indice>=0; indice--){
            effacerEcran();
            println("Le jeu va commencer dans " + indice);
            attendre(1);
        }
            Jeu(nomUtilisateur, 0.1, 0, 10);
        } else if(choix == 5){
            choixCustom();
            niveauDifficulte = niveauDifficulte + "Custom";
        }
     }
     
     
     void algorithm(){
        println("Bienvenue dans Memory Game !", TEMPS_AFFICHAGE);
        println("Vous allez devoir mémoriser une série de chiffres.", TEMPS_AFFICHAGE);

        println("Pour commencer, entrez votre nom d'utilisateur :", TEMPS_AFFICHAGE);
        nomUtilisateur = nomUtilisateur + readString();
        choixModePrincipal();
     }
    }