package com.example.luis.placerest;


import com.factual.driver.Factual;

public class FactualSingleton {
    private static Factual factual;
    private static String key = "kRl77lDaojvkBTQqw2zq4D3k4NykdILgAW0AiV1S";
    private static String secret = "TR3Y2YVaCVbqIln5Yx9Zvc8S8n5DFSEKv2rQki6b";

   private FactualSingleton(){

   }
    public static synchronized Factual getFactual() {
        if (factual == null) {
            factual = new Factual(key,secret,false);
        }
        return factual;
    }
}
