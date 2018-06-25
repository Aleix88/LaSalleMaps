import java.util.Scanner;

public class Menu {

    private boolean isOn = true;
    private Scanner sc = new Scanner(System.in);

    public void createMenu() {
        int opcio = 0;
        do {
            try {
                printaMenu();
                opcio = Integer.parseInt(sc.nextLine());
                executaOpcio(opcio);
            } catch (Exception e) {
                printaOpcioIncorrecte();
            }
        } while (isOn);

    }

    private void executaOpcio (int opcio) {
        switch (opcio) {
            case 1:
                importarMapa();
                break;
            case 2:
                buscarCiutat();
                break;
            case 3:
                calcularRuta();
                break;
            case 4:
                isOn = false;
                break;
            default:
                printaOpcioIncorrecte();
                break;
        }
    }

    private void importarMapa () {
        System.out.println("Mapa");
    }

    private void buscarCiutat () {
        System.out.println("Buscar");
    }

    private  void calcularRuta () {
        System.out.println("Ruta");
    }

    private void printaOpcioIncorrecte () {
        System.out.println("Opcio incorrecte! ");
        System.out.println(" ");
    }

    private void printaMenu () {
        System.out.println("Tria una opció: ");
        System.out.println("1. Import map");
        System.out.println("2. Search city");
        System.out.println("3. Calculate route");
        System.out.println("4. Shut down");
    }

}
