import extensions.File;

class Jeu extends Program {

    final String REPERTOIRE = "ascii";
    
     
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
     
    /* Algorithme principal                                                   */
     
    void algorithm(){
        println("test", 0.05);
        println("Bienvenue dans Memory Game !", 0.05);
        println("Vous allez devoir mémoriser une série de chiffres.", 0.05);

        println("Combien de chiffres voulez-vous mémoriser dans chaque chaîne ?", 0.05);
        int nombreDeChiffre = readInt();
        
        println("Combien de secondes voulez-vous pour mémoriser chaque chiffre ?", 0.05);
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
                println("Votre réponse doit contenir exactement " + nombreDeChiffre + " chiffres.", 0.05);
                break;
            }

            if(verifierReponse(reponseUtilisateur, tab)){
                println("Bravo, vous avez gagné !", 0.05);
                score++;

                println("Voulez-vous rejouer ? (oui/non)", 0.05);
                if(equals(readString(), "non")){
                    recommencer = false;
                }
            }
            else{
                println("Dommage, ce n'était pas la bonne réponse.", 0.05);
                print("La bonne réponse était : ", 0.05);

                for(int i = 0; i < nombreDeChiffre; i++){
                    print(tab[i]);
                }
                println("");

                recommencer = false;
            }
        }

        println("Partie terminée. Votre score : " + score, 0.05);
    }
}



/* A faire : 
- Classe Mode pour définir les modes de jeu                             
- Menu de sélection du mode de jeu

*/   