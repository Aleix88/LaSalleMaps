package Modelv2;

import Model.City;
import Model.Connection;
import Utils.StandardArrayList;


public class CityInfo {

    private City city;
    private StandardArrayList<Connection> connections;
    private StandardArrayList<City> destCityConnections;


    public CityInfo(){
        connections = new StandardArrayList();
        destCityConnections = new StandardArrayList<>();
    }
    public CityInfo(City city) {
        this.city = city;
        connections = new StandardArrayList();
        destCityConnections = new StandardArrayList<>();
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

    public void setConnections(StandardArrayList<Connection> connections) {
        this.connections = connections;
    }

    public StandardArrayList<City> getDestCityConnections() {
        return destCityConnections;
    }

    public void setDestCityConnections(StandardArrayList<City> destCityConnections) {
        this.destCityConnections = destCityConnections;
    }

}
