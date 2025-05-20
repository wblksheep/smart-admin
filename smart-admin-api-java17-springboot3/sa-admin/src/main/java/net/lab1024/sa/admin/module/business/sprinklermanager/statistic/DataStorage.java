package net.lab1024.sa.admin.module.business.sprinklermanager.statistic;

import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;

public class DataStorage {
    private static final Integer[][] MONTHBEGIN = {
            {13, 9, 20, 2, 9, 2, 10, 0, 8, 1, 0, 8, 20, 102},
            {5, 3, 8, 7, 9, 0, 6, 0, 3, 0, 0, 4, 20, 65},
            {8, 5, 22, 1, 8, 1, 7, 0, 6, 0, 0, 4, 20, 82},
            {11, 11, 11, 3, 9, 1, 10, 0, 3, 1, 0, 3, 20, 83}
    };

    public static void saveToJson(String filePath) throws IOException {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(MONTHBEGIN, writer);
        }
    }

    public static void main(String[] args) throws IOException {
        saveToJson("hello.json");
    }
}
