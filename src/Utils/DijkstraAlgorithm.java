package Utils;

import Base.Menu;
import Model.Connection;
import Base.DataManager;
import Modelv2.CityInfo;
import Utils.OrderedArrayList.StringOrderedArrayList;

/*Per fer dijkstra tenim 3 estructures (Arrays): T(nodes per explorar), D(Cost node inicial a node pos array), C(Node al que he de anar per, si fessim
el recorregut al revés, trobar l'origen;
Inicialment el dijkstra es volia fer generic, pero hi havia masses complicacions així que s'ha acabat fent bastant especific*/
public class DijkstraAlgorithm {

    private static DijkstraAlgorithm instance = null;

    private boolean[] T; //Nodes que queden per explorar
    private int[] C; //Indica, desde el node actual, el seguent tal que t'acostis al node origen
    private int[] D; //Conté el cost del camí per arribar a aquell node
    private int[] DAux;

    private long pastTime;
    private int indexOriginCity;
    private int indexDestCity;
    private StringOrderedArrayList<CityInfo> soal;
    private int typeValue;
    private int[][] arryPos; //Per saber els indexes de les connexions de les ciutats.

    private DijkstraAlgorithm() {
    }

    public static DijkstraAlgorithm getInstance() {
        if (instance == null) {
            instance = new DijkstraAlgorithm();
        }
        return instance;
    }

    public void executeAlgorithm(String startCity, String endCity, int typeVal, int typeSrch) {
        this.pastTime = System.nanoTime();
        this.typeValue = typeVal;
        this.soal = DataManager.getInstance().getCities();
        this.arryPos = DataManager.getInstance().getArrayPositionsConnections();
        try {
            this.indexOriginCity = DataManager.getInstance().searchCityInfo(startCity, typeSrch, 0).getArryPos();
            this.indexDestCity = DataManager.getInstance().searchCityInfo(endCity, typeSrch, 0).getArryPos();
            initDijkstra();
            if (typeVal == 0) {
                executeDistance();
            } else {
                executeDuration();
            }
            System.out.println(System.lineSeparator() + "Temps execució Dijkstra: " + ((System.nanoTime() - pastTime) / 1000) + " us");
            parseResults();
        } catch (NullPointerException npe) {
            System.out.println("No s'ha trobat la ciutat origen/destí. No s'executarà dijkstra");
        }

    }

    private void parseResults() {
        if (D[indexDestCity] == Integer.MAX_VALUE) {
            System.out.println("No s'ha pogut trobar la connexió amb el destí");
        } else {
            int distance = 0;
            String time = null;
            StringBuilder sb = new StringBuilder();
            switch (typeValue) {
                case 0: //distance
                    distance = D[indexDestCity] / 1000;
                    time = Menu.formatTime(DAux[indexDestCity]);
                    break;
                case 1: //time
                    time = Menu.formatTime(D[indexDestCity]);
                    distance = DAux[indexDestCity] / 1000;
                    break;
            }
            StandardArrayList<Integer> cami = new StandardArrayList<>();
            int i = indexDestCity;
            while (i != indexOriginCity) {
                cami.add(i);
                i = C[i];
            }
            cami.add(i);
            for (i = cami.size() - 1; i > 0; i--) {
                sb.append(soal.get(cami.get(i)).getCity().getName()).append(" --> ");
            }
            sb.append(soal.get(cami.get(i)).getCity().getName());
            System.out.println("Ciutat origen: " + soal.get(indexOriginCity).getCity().getName());
            System.out.println("Cituat destí: " + soal.get(indexDestCity).getCity().getName());
            System.out.println("Kilometres totals: " + distance + " km");
            System.out.println("Durada total (HH:mm:ss): " + time);
            System.out.print("Camí realitzat: ");
            System.out.println(sb.toString() + System.lineSeparator());
        }
    }

    private void executeDistance() {
        int shortest;
        int index;
        for (int i = 0; i < D.length - 1; i++) { //Recorrem tots els nodes. -1 perque el primer ja esta explorat
            shortest = Integer.MAX_VALUE;
            index = 0;
            //Aqui podria fer un remove quan l'hagi explorat, pero un aixó es el mateix que copiar tot l'array de nou i demanar memoria, així que no em beneficia de re
            for (int j = 0; j < T.length; j++) {
                if (!T[j] && D[j] < shortest) {
                    shortest = D[j];
                    index = j;
                }
            }
            T[index] = true;//Hem visitat node
            for (int j = 0; j < soal.get(index).getConnections().size(); j++) {
                if (D[index] + soal.get(index).getConnections().get(j).getDistance() < D[arryPos[index][j]]) {
                    D[arryPos[index][j]] = D[index] + soal.get(index).getConnections().get(j).getDistance();
                    DAux[arryPos[index][j]] = DAux[index] + soal.get(index).getConnections().get(j).getDuration();
                    C[arryPos[index][j]] = index;

                }
            }
        }
    }
    private void executeDuration() {
        int shortest;
        int index;
        for (int i = 0; i < D.length - 1; i++) { //Recorrem tots els nodes. -1 perque el primer ja esta explorat
            shortest = Integer.MAX_VALUE;
            index = 0;
            //Aqui podria fer un remove quan l'hagi explorat, pero un aixó es el mateix que copiar tot l'array de nou i demanar memoria, així que no em beneficia de re
            for (int j = 0; j < T.length; j++) {
                if (!T[j] && D[j] < shortest) {
                    shortest = D[j];
                    index = j;
                }
            }
            T[index] = true;//Hem visitat node
            for (int j = 0; j < soal.get(index).getConnections().size(); j++) {
                if (D[index] + soal.get(index).getConnections().get(j).getDuration() < D[arryPos[index][j]]) {
                    D[arryPos[index][j]] = D[index] + soal.get(index).getConnections().get(j).getDuration();
                    DAux[arryPos[index][j]] = DAux[index] + soal.get(index).getConnections().get(j).getDistance();
                    C[arryPos[index][j]] = index;

                }
            }
        }
    }

    private void initDijkstra() {
        T = new boolean[soal.length()];
        C = new int[soal.length()];
        D = new int[soal.length()];
        DAux = new int[soal.length()];
        for (int i = 0; i < soal.length(); i++) {
            if (indexOriginCity != i) {
                T[i] = false;
            } else {
                T[i] = true;
            }
            C[i] = indexOriginCity;
            D[i] = Integer.MAX_VALUE; //DAux no cal
        }
        CityInfo ci = soal.get(indexOriginCity);
        for (int i = 0; i < ci.getConnections().size(); i++) {
            D[arryPos[indexOriginCity][i]] = getWeight(ci.getConnections().get(i));
            DAux[arryPos[indexOriginCity][i]] = getWeightAux(ci.getConnections().get(i));
        }
        D[indexOriginCity] = 0;
        DAux[indexOriginCity] = 0;
    }

    private int getWeight(Connection c) {
        switch (typeValue) {
            case 0: //Distance
                return c.getDistance();
            case 1: //Time
                return c.getDuration();
        }
        return -1;
    }

    private int getWeightAux(Connection c) { //Lo mateix que getWeight, pero invertit
        switch (typeValue) {
            case 1: //Distance
                return c.getDistance();
            case 0: //Time
                return c.getDuration();
        }
        return -1;
    }
}
