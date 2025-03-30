package com.andycao.pokemon.pokemon_ai;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RandbatReader {
    private List<String> sets;

    // Grab contents from online json
    public RandbatReader() {
        URL url;
        try {
            url = new URI("https://pkmn.github.io/randbats/data/gen8randombattle.json").toURL();
            sets = new ArrayList<String>();
            stream(url);
        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void stream(URL url) {
        try (InputStream input = url.openStream()) {
            InputStreamReader inputReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sets.add(line);
            }

            System.out.println(sets.size());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSize() {
        return sets.size();
    }

    public String getRandomPokemonSet(int rand) {
        return sets.get(rand);
    }
}
