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

    private StandardArrayList<Boolean> T; //Nodes que queden per explorar
    private StandardArrayList<Integer> C; //Indica, desde el node actual, el seguent tal que t'acostis al node origen
    private StandardArrayList<Integer> D; //Conté el cost del camí per arribar a aquell node

    private int indexOriginCity;
    private int indexDestCity;
    private StringOrderedArrayList<CityInfo> soal;

    private int type;
    private int[][] arryPos; //Per saber els indexes de les connexions de les ciutats.

    private DijkstraAlgorithm() {
        T = new StandardArrayList<>(); //Quan es visitin no es borraran, es posaran a -1;
        C = new StandardArrayList<>();
        D = new StandardArrayList<>();
    }

    public static DijkstraAlgorithm getInstance() {
        if (instance == null) {
            instance = new DijkstraAlgorithm();
        }
        return instance;
    }

    public void executeAlgorithm(String startCity, String endCity, int type) {
        this.type = type;
        this.soal = DataManager.getInstance().getCities();
        this.arryPos = DataManager.getInstance().getArrayPositions();
        this.indexOriginCity = soal.binarySearchIndex(startCity);
        this.indexDestCity = soal.binarySearchIndex(endCity);
        if (indexOriginCity == -1) {
            System.out.println("No s'ha trobat la ciutat origen. No s'executarà dijkstra");
        } else if (indexDestCity == -1) {
            System.out.println("No s'ha trobat la ciutat destí. No s'executarà dijkstra");
        } else {
            initDijkstra();
            execute();
            parseResults();
        }

    }

    private void parseResults() {
        int distance = 0;
        String time = null;
        int timeAux = 0;
        int i = indexDestCity;
        int j;
        StandardArrayList<Integer> cami = new StandardArrayList<>();


        switch (type) {
            case 0: //distance
                distance = D.get(indexDestCity);
                while (i != indexOriginCity) {
                    cami.add(i);
                    for(j = 0; j < soal.get(i).getConnections().size(); j++){
                        if(arryPos[i][j] == C.get(i)) break;
                    }
                    i = C.get(i);
                }
                time = Menu.formatTime(timeAux);
                break;
            case 1: //time
                time = Menu.formatTime(D.get(indexDestCity));
                while (i != indexOriginCity) {
                    cami.add(i);
                    for(j = 0; j < soal.get(i).getConnections().size(); j++){
                        if(arryPos[i][j] == C.get(i)) break;
                    }
                    distance += soal.get(i).getConnections().get(j).getDistance();
                    i = C.get(i);
                }
                break;
        }
        distance = distance/1000;
        System.out.println("Ciutat origen: " + soal.get(indexOriginCity).getCity().getName());
        System.out.println("Cituat destí: " + soal.get(indexDestCity).getCity().getName());
        System.out.println("Kilometres totals: " + distance + " km");
        System.out.println("Durada total (HH:mm:ss): " + time);
        System.out.print("Camí realitzat: ");
        StringBuilder sb = new StringBuilder();
        sb.append(soal.get(indexOriginCity).getCity().getName()).append(" -> ");
        for(j = cami.size() - 1; j >= 0; j--){
            sb.append(soal.get(cami.get(j)).getCity().getName());
            if(j != 0) sb.append(" -> ");
        }
        System.out.println(sb.toString() + System.lineSeparator());
    }

    private void execute() {
        for (int i = 0; i < D.size() - 1; i++) { //Recorrem tots els nodes. -1 perque el primer ja esta explorat
            int shortest = Integer.MAX_VALUE;
            int index = 0;
            //Aqui podria fer un remove quan l'hagi explorat, pero un aixó es el mateix que copiar tot l'array de nou i demanar memoria, així que no em beneficia de re
            for (int j = 0; j < T.size(); j++) {
                if (D.get(j) < shortest && !T.get(j)) {
                    shortest = D.get(i);
                    index = j;
                }
            }
            T.set(index, true);//Hem visitat node
            StandardArrayList<Connection> con = soal.get(index).getConnections(); //Nomès cal canviar els valors
            for (int j = 0; j < con.size(); j++) {
               /* System.out.println("1:"+D.get(index));
                System.out.println("2:"+getWeight(con.get(j)));
                System.out.println("3:"+D.get(arryPos[index][j])+"\n");*/
                if (D.get(index) + getWeight(con.get(j)) < D.get(arryPos[index][j])) {
                    D.set(arryPos[index][j], D.get(index) + getWeight(con.get(j)));
                    C.set(arryPos[index][j], index);
                }
            }
        }
    }

    private void initDijkstra() {
        T.clear();
        C.clear();
        D.clear();
        for (int i = 0; i < soal.length(); i++) {
            if (indexOriginCity != i) {
                T.add(false);
            } else {
                T.add(true);
            }
            C.add(indexOriginCity);
            D.add(Integer.MAX_VALUE);
        }
        CityInfo ci = soal.get(indexOriginCity);
        for (int i = 0; i < ci.getConnections().size(); i++) {
            D.set(arryPos[indexOriginCity][i], getWeight(ci.getConnections().get(i)));
        }
        D.set(indexOriginCity,0);
    }

    private int getWeight(Connection c) {
        switch (type) {
            case 0: //Distance
                return c.getDistance();
            case 1: //Time
                return c.getDuration();
        }
        return -1;
    }
}
