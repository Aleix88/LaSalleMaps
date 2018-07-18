package Utils;

import Model.City;
import Model.Connection;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.io.FileNotFoundException;
import java.io.FileReader;

import java.util.Scanner;

public class JSONReader {

    public String getFilename(){
        String nomFitxer;
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom fitxer JSON Magatzems(o Path):");
        nomFitxer = sc.nextLine();
        return nomFitxer;
    }
    public City[] parsecities(String filename) throws  FileNotFoundException{
        Gson gson = new Gson();
        JsonObject jsonObject =  gson.fromJson(new FileReader("files/"+filename), JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("cities");
        City[] cities =  gson.fromJson(jsonArray, City[].class);
        return cities;
    }
    public Connection[] parseConnections (String filename) throws  FileNotFoundException{
        Gson gson = new Gson();
        JsonObject jsonObject =  gson.fromJson(new FileReader("files/"+filename), JsonObject.class);
        JsonArray jsonArray = jsonObject.getAsJsonArray("connections");
        Connection[] connections =  gson.fromJson(jsonArray, Connection[].class);
        return connections;
    }



}
