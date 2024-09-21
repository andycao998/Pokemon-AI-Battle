package com.andycao.pokemon.pokemon_ai;

public class FieldSideEffectsHandler {
    private String activeWeather;
    private int weatherDuration;

    private int trickRoom;

    private boolean plasma;

    // WIP: All currently unimplemented:
    // private String activeTerrain;
    // private int terrainDuration;

    // private boolean magicRoom;

    // private boolean wonderRoom;

    // private int perishSong;

    // private boolean gravity;

    // private int uproar;

    // private String activeSport;

    public FieldSideEffectsHandler() {
        activeWeather = "";
    }

    /*----------Weather----------*/

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
        else if (weather.isEmpty()) { // No weather is default and valid
            return true;
        }

        return false;
    }

    public void setWeather(String weather, int duration) {
        if (!validWeather(weather)) {
            return;
        }

        // WIP: Check for abilities that prevent weather

        // Primal weather (Extremely Harsh Sunlight, Heavy Rain, and Strong Winds) can only be overridden by other primal weathers
        if (activeWeather.equals("Extremely Harsh Sunlight") || activeWeather.equals("Heavy Rain") || activeWeather.equals("Strong Winds")) {
            if (weather.equals(activeWeather)) {
                return;
            }

            // New weather is a primal weather: override current primal weather
            if (weather.equals("Extremely Harsh Sunlight") || weather.equals("Heavy Rain") || weather.equals("Strong Winds")) {
                activeWeather = weather;
                weatherDuration = 1000; // Primal weather lasts as long as Pokemon is on the field
            }
            else {
                TurnEventMessageBuilder.getInstance().appendEvent("The primal weather was not affected!");
            }
        }
        // Override current weathers normally
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

    /*----------Rooms----------*/

    public int getTrickRoomTurns() {
        return trickRoom;
    }

    public void setTrickRoom(int turns) {
        if (trickRoom > 0 && turns == 0) {
            TurnEventMessageBuilder.getInstance().appendEvent("Trick Room wore off!");
        }

        trickRoom = turns;
    }

    /*----------Plasma Fists----------*/

    public boolean getPlasmaEffect() {
        return plasma;
    }

    public void setPlasmaEffect(boolean state) {
        plasma = state;
    }
}
