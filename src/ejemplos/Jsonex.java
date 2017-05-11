package ejemplos;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Jsonex {
    public static void main(String[] args) throws IOException {
        Writer writer = new FileWriter("Output.json");

        Gson gson = new GsonBuilder().create();
        gson.toJson("Hello", writer);
        gson.toJson(123, writer);

        writer.close();
    }
}
/*
public static void main(String[] args) throws IOException {
    try (Writer writer = new FileWriter("Output.json")) {
        Gson gson = new GsonBuilder().create();
        gson.toJson("Hello", writer);
        gson.toJson(123, writer);
    }
} */