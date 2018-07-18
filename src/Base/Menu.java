package Base;


import Utils.DijkstraAlgorithm;
import Utils.JSONReader;
import Modelv2.CityInfo;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class Menu {

    private boolean isOn = true;
    private boolean mapImported = false;
    private static Menu instance = null;

    private Menu() {

    }

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    private Scanner sc = new Scanner(System.in);

    public void createMenu() {
        int opcio = 0;
        do {
            printaMenu();
            try {
                opcio = Integer.parseInt(sc.nextLine());
                executaOpcio(opcio);
            } catch (NumberFormatException nfe) {
                printaOpcioIncorrecte();
            }
        } while (isOn);

    }

    private void executaOpcio(int opcio) {
        switch (opcio) {
            case 1:
                importarMapa();
                break;
            case 2:
                if (mapImported) {
                    buscarCiutat();
                } else {
                    System.out.println("Es necessari previament haver executat la opcio 1 per establir les dades" +
                            "inicials del mapa");
                }
                break;
            case 3:
                if (mapImported) {
                    calcularRuta();
                } else {
                    System.out.println("Es necessari previament haver executat la opcio 1 per establir les dades" +
                            "inicials del mapa");
                }
                break;
            case 4:
                isOn = false;
                break;
            default:
                printaOpcioIncorrecte();
                break;
        }
    }

    private void importarMapa() {
        try {
            JSONReader jsonReader = new JSONReader();
            String filename = jsonReader.getFilename();
            Base.DataManager.getInstance().addCitiesJSON(jsonReader.parsecities(filename));
            Base.DataManager.getInstance().addConectionsJSON(jsonReader.parseConnections(filename));
            mapImported = true;
        } catch (FileNotFoundException e) {
            System.err.println("No s'ha pogut trobar el fitxer. Siusplau, comprovi si el fitxer/camí especificat es correcte");
        }
    }

    private void buscarCiutat() {
        try {
            int opcio = 0;
            do { //entre 1 i 4
                System.out.println(System.lineSeparator()+"1. Sense optimització (Cerca binària): ");
                System.out.println("2. Sense optimització (Cerca lineal): ");
                System.out.println("3. Cerca utilitzant arbre AVL: ");
                System.out.println("4. Cerca utilitzant taula de hash amb adrecament tancat: ");
                System.out.print("Opcio: ");
                opcio = Integer.parseInt(sc.nextLine());
                if((opcio < 1 || opcio > 4)){
                    System.out.println(System.lineSeparator()+"Aquesta opció no existeix" + System.lineSeparator());
                }
            } while ((opcio < 1 || opcio > 4));
            System.out.print("Nom ciutat: ");
            CityInfo o = Base.DataManager.getInstance().searchCityInfo(sc.nextLine(), opcio-1);
            if (o == null) {
                //Nothing
            } else {
                printCityInfo(o);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("L'input introduit no te un format correcte");
        }
    }

    private void calcularRuta() {
        int opcio;
        do{
            System.out.println("1.Shorter route");
            System.out.println("2.Fastest route");
            System.out.print("Opció: ");
            opcio = Integer.parseInt(sc.nextLine());
            if((opcio < 1 || opcio > 2)){
                System.out.println(System.lineSeparator()+"Aquesta opció no existeix" + System.lineSeparator());
            }
        }while(opcio < 1 || opcio > 2);
        System.out.print("Nom ciutat origen: ");
        String originCity = sc.nextLine();
        System.out.print("Nom ciutat destí: ");
        String destCity = sc.nextLine();
        DijkstraAlgorithm.getInstance().executeAlgorithm(originCity,destCity,opcio-1);
    }

    private void printaOpcioIncorrecte() {
        System.out.println("Opcio incorrecte! ");
        System.out.println(" ");
    }

    private void printaMenu() {
        System.out.println(System.lineSeparator() + "Tria una opció: ");
        System.out.println("1. Import map");
        System.out.println("2. Search city");
        System.out.println("3. Calculate route");
        System.out.println("4. Shut down");
        System.out.print("Opcio: ");
    }

    public void printCityInfo(CityInfo c) {
        System.out.println(System.lineSeparator() + "Informació ciutat:");
        System.out.println("\tNom ciutat: " + c.getCity().getName());
        System.out.println("\tCodi pais ciutat: " + c.getCity().getCountry());
        System.out.println("\tLatitud: " + c.getCity().getLatitude());
        System.out.println("\tLongitud: " + c.getCity().getLongitude());
        System.out.println(System.lineSeparator() + "\tConnections: ");
        for (int i = 0; i < c.getConnections().size(); i++) {
            System.out.println("\t\tNom ciutat desti: " + c.getConnections().get(i).getTo());
            System.out.println("\t\tCodi pais ciutat: " + c.getDestCityConnections().get(i).getCountry());
            System.out.println("\t\tLatitud: " + c.getDestCityConnections().get(i).getLatitude());
            System.out.println("\t\tLongitud: " + c.getDestCityConnections().get(i).getLongitude());
            System.out.println("\t\tTemps destinació(En format HH:mm:ss): " + formatTime(c.getConnections().get(i).getDuration()));
            System.out.println("\t\tDistancia fins a destí: " + String.valueOf((float) c.getConnections().get(i).getDistance() / (float) 1000) +
                    " km" + System.lineSeparator() + System.lineSeparator());
        }
    }


    public static String formatTime(int time) {
        long hh = time / 3600;
        time %= 3600;
        long mm = time / 60;
        time %= 60;
        long ss = time;
        return String.format("%02d:%02d:%02d", hh, mm, ss);
    }
}
