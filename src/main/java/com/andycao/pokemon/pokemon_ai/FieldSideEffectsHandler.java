package com.andycao.pokemon.pokemon_ai;

public class FieldSideEffectsHandler {
    private String activeWeather;
    private int weatherDuration;

    // private String activeTerrain;
    // private int terrainDuration;

    private int trickRoom;

    // private boolean magicRoom;

    // private boolean wonderRoom;

    // private int perishSong;

    // private boolean gravity;

    // private int uproar;

    // private String activeSport;

    private boolean plasma;

    public FieldSideEffectsHandler() {
        activeWeather = "";
    }

    public String getWeather() {
        return activeWeather;
    }

    public int getWeatherDuration() {
        return weatherDuration;
    }

    private boolean validWeather(String weather) {
        if (weather.equals("Harsh Sunlight") || weather.equals("Rain") || weather.equals("Sandstorm") || weather.equals("Hail")) {
            return true;
        }
        else if (weather.equals("Extremely Harsh Sunlight") || weather.equals("Heavy Rain") || weather.equals("Strong Winds")) {
            return true;
        }
        else if (weather.isEmpty()) {
            return true;
        }

        return false;
    }

    public void setWeather(String weather, int duration) {
        if (!validWeather(weather)) {
            return;
        }

        // check for abilities that prevent weather

        if (activeWeather.equals("Extremely Harsh Sunlight") || activeWeather.equals("Heavy Rain") || activeWeather.equals("Strong Winds")) {
            if (weather.equals(activeWeather)) {
                return;
            }

            if (weather.equals("Extremely Harsh Sunlight") || weather.equals("Heavy Rain") || weather.equals("Strong Winds")) {
                activeWeather = weather;
                weatherDuration = 1000;
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("The primal weather was not affected!");
            }
        }
        if (!weather.equals(activeWeather)) {
            activeWeather = weather;
            weatherDuration = duration;
        }
    }

    public void decrementWeatherDuration() {
        if (activeWeather.isEmpty()) {
            return;
        }

        weatherDuration -= 1;
        if (weatherDuration == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("The weather dissipated!");
            activeWeather = "";
            weatherDuration = 0;
        }
    }

    public int getTrickRoomTurns() {
        return trickRoom;
    }

    public void setTrickRoom(int turns) {
        if (trickRoom > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Trick Room wore off!");
        }

        trickRoom = turns;
    }

    public boolean getPlasmaEffect() {
        return plasma;
    }

    public void setPlasmaEffect(boolean state) {
        plasma = state;
    }
}
