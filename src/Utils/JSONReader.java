package Utils;

import Model.City;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Scanner;

public class JSONReader {

    public List<City> parseSchools () {

        Gson gson = new Gson();
        String nomFitxer;
        Scanner sc = new Scanner(System.in);
        System.out.print("Nom fitxer JSON Magatzems(o Path):");
        nomFitxer = sc.nextLine();
        try {
            JsonObject jsonObject =  gson.fromJson(new FileReader(nomFitxer), JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("cities");
            List<City> schoolsArray =  gson.fromJson(jsonArray, new TypeToken<List<City>>(){}.getType());
            return schoolsArray;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
