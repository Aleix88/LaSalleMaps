package Base;

import Model.City;
import Model.Connection;
import Modelv2.CityInfo;
import Utils.APISalleMaps;
import Utils.AVLTree.AVLTree;
import Utils.AVLTree.NodeData;
import Utils.HashMap.HashMap;
import Utils.OrderedArrayList.ObjectStringOrderedArrayList;
import Utils.OrderedArrayList.StringOrderedArrayList;
import Utils.StandardArrayList;

public class DataManager {

    private static DataManager instance = null;

    private StringOrderedArrayList<CityInfo> cities;
    private int[][] arrayPositionsConnections;
    private AVLTree<CityInfo> avl;
    private HashMap<String, CityInfo> hashMap;

    private DataManager() {
        cities = new StringOrderedArrayList<>();
        avl = new AVLTree<>();
        hashMap = new HashMap<>();
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void addCitiesJSON(City[] citylist) {
        cities.clear();
        avl.clear();
        hashMap.clear();
        for (int i = 0; i < citylist.length; i++) {
            CityInfo ci = new CityInfo(citylist[i]);
            cities.addOrdered(new ObjectStringOrderedArrayList(ci, ci.getCity().getName()));
            avl.insertarNode(new NodeData<CityInfo>(ci.getCity().getName(), ci));
            hashMap.put(ci.getCity().getName(), ci);
        }
    }

    public void addCityInfo(CityInfo ci) {
        cities.addOrdered(new ObjectStringOrderedArrayList(ci, ci.getCity().getName()));
        avl.insertarNode(new NodeData<CityInfo>(ci.getCity().getName(), ci));
        hashMap.put(ci.getCity().getName(), ci);
        updateArrayPositions();
    }

    private void updateArrayPositions() {
        for (int i = 0; i < cities.length(); i++) {
            cities.get(i).setArryPos(i);
        }
        arrayPositionsConnections = new int[cities.length()][];
        StandardArrayList<Connection> c;
        for (int i = 0; i < cities.length(); i++) {
            c = cities.get(i).getConnections();
            arrayPositionsConnections[i] = new int[cities.get(i).getConnections().size()];
            for (int j = 0; j < c.size(); j++) {
                arrayPositionsConnections[i][j] = cities.binarySearch(c.get(j).getTo()).getArryPos();
            }
        }
    }

    public void addConectionsJSON(Connection[] connectionsList) {
        for (int i = 0; i < connectionsList.length; i++) {
            CityInfo o = cities.binarySearch(connectionsList[i].getFrom());
            o.getConnections().add(connectionsList[i]);
        }
        updateArrayPositions();
    }

    public CityInfo searchCityInfo(String cityName, int type, int search) {
        CityInfo o = null;
        if (cityName.length() != 0) {
            long currentNanos = System.nanoTime();
            switch (type) {
                case 0: //Binary
                    o = cities.binarySearch(cityName);
                    break;
                case 1: //Lineal
                    o = cities.linealSearch(cityName);
                    break;
                case 2: //AVL
                    o = avl.search(cityName);
                    break;
                case 3: //Hash
                    o = hashMap.get(cityName);
                    break;

            }
            if (o == null && search == 1) {
                System.out.println("No s'ha trobat aquesta ciutat a la base de dades. Es buscarà a la API");
                System.out.println("Buscant......");
                APISalleMaps.getInstance().searchCity(cityName);
            }
            if (search == 1) {
                System.out.println(System.lineSeparator() + "S'ha tardat " + (System.nanoTime() - currentNanos) / 1000 +
                        " us en executar el procés de cerca");
            }

        } else {
            System.out.println("El nom de la ciutat no pot estar buit");
        }
        return o;
    }

    public StringOrderedArrayList<CityInfo> getCities() {
        return cities;
    }

    public int[][] getArrayPositionsConnections() {
        return arrayPositionsConnections;
    }
}
