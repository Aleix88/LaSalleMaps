package Modelv2;

import Model.City;
import Model.Connection;
import Utils.StandardArrayList;


public class CityInfo {

    private City city;
    private StandardArrayList<Connection> connections;
    private int arryPos;


    public CityInfo(){
        connections = new StandardArrayList();
    }
    public CityInfo(City city) {
        this.city = city;
        connections = new StandardArrayList();
    }

    public City getCity() {
        return city;
    }

    public StandardArrayList<Connection> getConnections() {
        return connections;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public int getArryPos() {
        return arryPos;
    }

    public void setArryPos(int arryPos) {
        this.arryPos = arryPos;
    }

    public void setConnections(StandardArrayList<Connection> connections) {
        this.connections = connections;
    }

}
