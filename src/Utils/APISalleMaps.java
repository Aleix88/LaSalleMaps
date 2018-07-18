package Utils;

import Base.DataManager;
import Base.Menu;
import Model.City;
import Modelv2.CityInfo;
import Model.Connection;
import Network.DistanceParser;
import Network.GeocodeParser;
import Network.HttpRequest;
import Network.WSGoogleMaps;


import java.io.IOException;
import java.util.List;


public class APISalleMaps {

    private static APISalleMaps instance = null;
    private static final String APIkey = "AIzaSyCMG_IEevGb9kFUfR_DVgQIT0Gfqrz3S_I";


    private APISalleMaps(){
        WSGoogleMaps googleMaps = WSGoogleMaps.getInstance();
        googleMaps.setApiKey(APIkey);
    }

    public static APISalleMaps getInstance(){
        if(instance == null){
            instance = new APISalleMaps();
        }
        return instance;
    }

    public void searchCity(String cityName){
        WSGoogleMaps googleMaps = WSGoogleMaps.getInstance();
        googleMaps.geolocate(cityName, new HttpRequest.HttpReply() {
            @Override
            public void onSuccess(String data) {
                List<Model.City> cities =  GeocodeParser.getCityData(data);
                if(cities.size() != 0){
                    CityInfo ci = new CityInfo();
                    ci.setCity(cities.get(0));
                    calculateConnections(ci);
                }else{
                    System.out.println("Segons Google Maps, aquesta ciutat no existeix");
                }
            }
            @Override
            public void onError(String message) {
                System.out.println("Error al fer una petició a Google Maps");
            }
        });
    }

    private void calculateConnections(CityInfo ci) {
        WSGoogleMaps googleMaps = WSGoogleMaps.getInstance();
        StandardArrayList<City> closeCities = new StandardArrayList<>();
        int numCities = 0;
        for(int i = 0; i < DataManager.getInstance().getCities().length(); i++){
            if((distance(ci.getCity().getLatitude(), DataManager.getInstance().getCities().get(i).getCity().getLatitude(), ci.getCity().getLongitude(),
                    DataManager.getInstance().getCities().get(i).getCity().getLongitude()) < 500)){
                closeCities.add(DataManager.getInstance().getCities().get(i).getCity());
                numCities++;
                if(numCities > 50){
                    break;
                }
            }
        }

        double[] latitudes = new double[closeCities.size()];
        double[] longitudes = new double[closeCities.size()];
        int indexes[] = new int[closeCities.size()];


        for(int i = 0; i < closeCities.size(); i++){
            latitudes[i] = closeCities.get(i).getLatitude();
            longitudes[i] = closeCities.get(i).getLongitude();
            indexes[i] = i;
        }
        googleMaps.distance(ci.getCity().getLatitude(), ci.getCity().getLongitude(), latitudes, longitudes, new HttpRequest.HttpReply() {
                    @Override
                    public void onSuccess(String data) {
                        StandardArrayList<Connection> connections = new StandardArrayList<>();
                        DistanceParser.parseDistances(data, ci.getCity().getName(), closeCities/*Ciutats a les quals vols crear connexions*/, indexes, connections);
                        ci.setConnections(connections);
                        StandardArrayList<City> destCities = new StandardArrayList<>();
                        for(int i = 0; i < connections.size(); i++){
                            destCities.add(DataManager.getInstance().getCities().binarySearch(connections.get(i).getTo()).getCity());
                        }
                        ci.setDestCityConnections(destCities);
                        for(int i = 0; i < connections.size(); i++){
                            CityInfo ciAux = DataManager.getInstance().getCities().binarySearch(connections.get(i).getTo());
                            ciAux.getConnections().add(new Connection(connections.get(i).getTo(), connections.get(i).getFrom(), connections.get(i).getDistance(),
                                    connections.get(i).getDuration()));
                            ciAux.getDestCityConnections().add(ci.getCity());
                        }
                        DataManager.getInstance().addCityInfo(ci);
                        Menu.getInstance().printCityInfo(ci);
                    }

                    @Override
                    public void onError(String message) {
                        System.out.println("La ciutat s'ha trobat, pero hi hagut un error alhora de demanar les connexions. La nova ciutat no s'afegirà");
                    }
                });

    }

    /**
     * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is, the shortest distance
     * over the earth’s surface – giving an ‘as-the-crow-flies’ distance between the points (ignoring any hills they fly over, of course!).

     Haversine
     formula:	a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
     c = 2 ⋅ atan2( √a, √(1−a) )
     d = R ⋅ c
     where	φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km);
     note that angles need to be in radians to pass to trig functions!

     */
    private double distance(double lat1, double lat2, double  lon1, double lon2){
        double a = Math.sin(Math.toRadians(lat2-lat1)/2) * Math.sin(Math.toRadians(lat2-lat1)/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(Math.toRadians(lon2-lon1)/2) * Math.sin(Math.toRadians(lon2-lon1)/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return (6.371e3 * c);// Retorna en km?
    }
}
