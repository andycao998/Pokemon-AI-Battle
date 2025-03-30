package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonStateDto;

// DTO -> JSON containing active Pokemon and field details
public class BattleStateDto {
    public String playerPokemonName;
    public int playerPokemonLevel;
    public int playerPokemonCurrentHp;
    public int playerPokemonMaxHp;
    public String playerPokemonStatus;
    public String playerPokemonMove1;
    public String playerPokemonMove2;
    public String playerPokemonMove3;
    public String playerPokemonMove4;
    public String playerPokemonSpriteUrl;

    public String botPokemonName;
    public int botPokemonLevel;
    public int botPokemonCurrentHp;
    public int botPokemonMaxHp;
    public String botPokemonStatus;
    public String botPokemonMove1;
    public String botPokemonMove2;
    public String botPokemonMove3;
    public String botPokemonMove4;
    public String botPokemonSpriteUrl;

    public String weather;

    public boolean trickRoom;

    public boolean playerSideStealthRock;
    public boolean playerSideSpikes;
    public boolean playerSideToxicSpikes;
    public boolean playerSideStickyWeb;
    public boolean botSideStealthRock;
    public boolean botSideSpikes;
    public boolean botSideToxicSpikes;
    public boolean botSideStickyWeb;

    public boolean playerReflect;
    public boolean playerLightScreen;
    public boolean playerAuroraVeil;
    public boolean botReflect;
    public boolean botLightScreen;
    public boolean botAuroraVeil;

    public BattleStateDto(PokemonStateDto playerPokemonDto, PokemonStateDto botPokemonDto) throws InvalidIdentifierException {
        playerPokemonName = playerPokemonDto.name;
        playerPokemonLevel = playerPokemonDto.level;
        playerPokemonCurrentHp = playerPokemonDto.currentHp;
        playerPokemonMaxHp = playerPokemonDto.maxHp;
        playerPokemonStatus = playerPokemonDto.status;
        playerPokemonMove1 = playerPokemonDto.move1;
        playerPokemonMove2 = playerPokemonDto.move2;
        playerPokemonMove3 = playerPokemonDto.move3;
        playerPokemonMove4 = playerPokemonDto.move4;
        playerPokemonSpriteUrl = playerPokemonDto.spriteUrl;

        botPokemonName = botPokemonDto.name;
        botPokemonLevel = botPokemonDto.level;
        botPokemonCurrentHp = botPokemonDto.currentHp;
        botPokemonMaxHp = botPokemonDto.maxHp;
        botPokemonStatus = botPokemonDto.status;
        botPokemonMove1 = botPokemonDto.move1;
        botPokemonMove2 = botPokemonDto.move2;
        botPokemonMove3 = botPokemonDto.move3;
        botPokemonMove4 = botPokemonDto.move4;
        botPokemonSpriteUrl = botPokemonDto.spriteUrl;

        weather = "";
    }

    public String getPlayerPokemonName() {
        return playerPokemonName;
    }

    public void setPlayerPokemonName(String playerPokemonName) {
        this.playerPokemonName = playerPokemonName;
    }

    public int getPlayerPokemonLevel() {
        return playerPokemonLevel;
    }

    public void setPlayerPokemonLevel(int playerPokemonLevel) {
        this.playerPokemonLevel = playerPokemonLevel;
    }

    public int getPlayerPokemonCurrentHp() {
        return playerPokemonCurrentHp;
    }

    public void setPlayerPokemonCurrentHp(int playerPokemonCurrentHp) {
        this.playerPokemonCurrentHp = playerPokemonCurrentHp;
    }

    public int getPlayerPokemonMaxHp() {
        return playerPokemonMaxHp;
    }

    public void setPlayerPokemonMaxHp(int playerPokemonMaxHp) {
        this.playerPokemonMaxHp = playerPokemonMaxHp;
    }

    public String getPlayerPokemonStatus() {
        return playerPokemonStatus;
    }

    public void setPlayerPokemonStatus(String playerPokemonStatus) {
        this.playerPokemonStatus = playerPokemonStatus;
    }

    public String getPlayerPokemonMove1() {
        return playerPokemonMove1;
    }

    public void setPlayerPokemonMove1(String playerPokemonMove1) {
        this.playerPokemonMove1 = playerPokemonMove1;
    }

    public String getPlayerPokemonMove2() {
        return playerPokemonMove2;
    }

    public void setPlayerPokemonMove2(String playerPokemonMove2) {
        this.playerPokemonMove2 = playerPokemonMove2;
    }

    public String getPlayerPokemonMove3() {
        return playerPokemonMove3;
    }

    public void setPlayerPokemonMove3(String playerPokemonMove3) {
        this.playerPokemonMove3 = playerPokemonMove3;
    }

    public String getPlayerPokemonMove4() {
        return playerPokemonMove4;
    }

    public void setPlayerPokemonMove4(String playerPokemonMove4) {
        this.playerPokemonMove4 = playerPokemonMove4;
    }

    public String getPlayerPokemonSpriteUrl() {
        return playerPokemonSpriteUrl;
    }

    public void setPlayerPokemonSpriteUrl(String playerPokemonSpriteUrl) {
        this.playerPokemonSpriteUrl = playerPokemonSpriteUrl;
    }

    public String getBotPokemonName() {
        return botPokemonName;
    }

    public void setBotPokemonName(String botPokemonName) {
        this.botPokemonName = botPokemonName;
    }

    public int getBotPokemonLevel() {
        return botPokemonLevel;
    }

    public void setBotPokemonLevel(int botPokemonLevel) {
        this.botPokemonLevel = botPokemonLevel;
    }

    public int getBotPokemonCurrentHp() {
        return botPokemonCurrentHp;
    }

    public void setBotPokemonCurrentHp(int botPokemonCurrentHp) {
        this.botPokemonCurrentHp = botPokemonCurrentHp;
    }

    public int getBotPokemonMaxHp() {
        return botPokemonMaxHp;
    }

    public void setBotPokemonMaxHp(int botPokemonMaxHp) {
        this.botPokemonMaxHp = botPokemonMaxHp;
    }

    public String getBotPokemonStatus() {
        return botPokemonStatus;
    }

    public void setBotPokemonStatus(String botPokemonStatus) {
        this.botPokemonStatus = botPokemonStatus;
    }

    public String getBotPokemonMove1() {
        return botPokemonMove1;
    }

    public void setBotPokemonMove1(String botPokemonMove1) {
        this.botPokemonMove1 = botPokemonMove1;
    }

    public String getBotPokemonMove2() {
        return botPokemonMove2;
    }

    public void setBotPokemonMove2(String botPokemonMove2) {
        this.botPokemonMove2 = botPokemonMove2;
    }

    public String getBotPokemonMove3() {
        return botPokemonMove3;
    }

    public void setBotPokemonMove3(String botPokemonMove3) {
        this.botPokemonMove3 = botPokemonMove3;
    }

    public String getBotPokemonMove4() {
        return botPokemonMove4;
    }

    public void setBotPokemonMove4(String botPokemonMove4) {
        this.botPokemonMove4 = botPokemonMove4;
    }

    public String getBotPokemonSpriteUrl() {
        return botPokemonSpriteUrl;
    }

    public void setBotPokemonSpriteUrl(String botPokemonSpriteUrl) {
        this.botPokemonSpriteUrl = botPokemonSpriteUrl;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public boolean isTrickRoom() {
        return trickRoom;
    }

    public void setTrickRoom(boolean trickRoom) {
        this.trickRoom = trickRoom;
    }

    public boolean isPlayerSideStealthRock() {
        return playerSideStealthRock;
    }

    public void setPlayerSideStealthRock(boolean playerSideStealthRock) {
        this.playerSideStealthRock = playerSideStealthRock;
    }

    public boolean isPlayerSideSpikes() {
        return playerSideSpikes;
    }

    public void setPlayerSideSpikes(boolean playerSideSpikes) {
        this.playerSideSpikes = playerSideSpikes;
    }

    public boolean isPlayerSideToxicSpikes() {
        return playerSideToxicSpikes;
    }

    public void setPlayerSideToxicSpikes(boolean playerSideToxicSpikes) {
        this.playerSideToxicSpikes = playerSideToxicSpikes;
    }

    public boolean isPlayerSideStickyWeb() {
        return playerSideStickyWeb;
    }

    public void setPlayerSideStickyWeb(boolean playerSideStickyWeb) {
        this.playerSideStickyWeb = playerSideStickyWeb;
    }
}
