package com.test.material.supitsara.materialnavigationtest.daogenerator;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "com.test.material.supitsara.materialnavigationtest");
        Entity tour = schema.addEntity("Tour");
        Entity search = schema.addEntity("Search");

        tour.addIdProperty().autoincrement();
        tour.addStringProperty("userID");
        tour.addStringProperty("boothID");
        tour.addDoubleProperty("Lat");
        tour.addDoubleProperty("Long");

        search.addIdProperty().autoincrement();
        search.addStringProperty("userID");
        search.addStringProperty("keyword");

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
