package com.andycao.pokemon.pokemon_ai;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;
import com.andycao.pokemon.pokemon_ai.Pokemon.Pokemon;
import com.andycao.pokemon.pokemon_ai.Pokemon.PokemonStateDto;

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

    // public String pokemon1Name;
    // public int pokemon1CurrentHp;
    // public int pokemon1MaxHp;
    // public String pokemon1Status;
    // public String pokemon1Move1;
    // public String pokemon1Move2;
    // public String pokemon1Move3;
    // public String pokemon1Move4;

    // //ability and item

    // public String pokemon2Name;
    // public int pokemon2CurrentHp;
    // public int pokemon2MaxHp;
    // public String pokemon2Status;
    // public String pokemon2Move1;
    // public String pokemon2Move2;
    // public String pokemon2Move3;
    // public String pokemon2Move4;

    // public String pokemon3Name;
    // public int pokemon3CurrentHp;
    // public int pokemon3MaxHp;
    // public String pokemon3Status;
    // public String pokemon3Move1;
    // public String pokemon3Move2;
    // public String pokemon3Move3;
    // public String pokemon3Move4;

    // public String pokemon4Name;
    // public int pokemon4CurrentHp;
    // public int pokemon4MaxHp;
    // public String pokemon4Status;
    // public String pokemon4Move1;
    // public String pokemon4Move2;
    // public String pokemon4Move3;
    // public String pokemon4Move4;

    // public String pokemon5Name;
    // public int pokemon5CurrentHp;
    // public int pokemon5MaxHp;
    // public String pokemon5Status;
    // public String pokemon5Move1;
    // public String pokemon5Move2;
    // public String pokemon5Move3;
    // public String pokemon5Move4;

    // public String pokemon6Name;
    // public int pokemon6CurrentHp;
    // public int pokemon6MaxHp;
    // public String pokemon6Status;
    // public String pokemon6Move1;
    // public String pokemon6Move2;
    // public String pokemon6Move3;
    // public String pokemon6Move4;

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

        // Pokemon[] party = PlayerPartyManager.getInstance().getParty();

        // PokemonStateDto dto1 = party[0].generateStateDto();
        // PokemonStateDto dto2 = party[1].generateStateDto();
        // PokemonStateDto dto3 = party[2].generateStateDto();
        // PokemonStateDto dto4 = party[3].generateStateDto();
        // PokemonStateDto dto5 = party[4].generateStateDto();
        // PokemonStateDto dto6 = party[5].generateStateDto();

        // this.pokemon1Name = dto1.name;
        // this.pokemon1CurrentHp = dto1.currentHp;
        // this.pokemon1MaxHp = dto1.maxHp;
        // this.pokemon1Status = dto1.status;
        // this.pokemon1Move1 = dto1.move1;
        // this.pokemon1Move2 = dto1.move2;
        // this.pokemon1Move3 = dto1.move3;
        // this.pokemon1Move4 = dto1.move4;

        // this.pokemon2Name = dto2.name;
        // this.pokemon2CurrentHp = dto2.currentHp;
        // this.pokemon2MaxHp = dto2.maxHp;
        // this.pokemon2Status = dto2.status;
        // this.pokemon2Move1 = dto2.move1;
        // this.pokemon2Move2 = dto2.move2;
        // this.pokemon2Move3 = dto2.move3;
        // this.pokemon2Move4 = dto2.move4;

        // this.pokemon3Name = dto3.name;
        // this.pokemon3CurrentHp = dto3.currentHp;
        // this.pokemon3MaxHp = dto3.maxHp;
        // this.pokemon3Status = dto3.status;
        // this.pokemon3Move1 = dto3.move1;
        // this.pokemon3Move2 = dto3.move2;
        // this.pokemon3Move3 = dto3.move3;
        // this.pokemon3Move4 = dto3.move4;

        // this.pokemon4Name = dto4.name;
        // this.pokemon4CurrentHp = dto4.currentHp;
        // this.pokemon4MaxHp = dto4.maxHp;
        // this.pokemon4Status = dto4.status;
        // this.pokemon4Move1 = dto4.move1;
        // this.pokemon4Move2 = dto4.move2;
        // this.pokemon4Move3 = dto4.move3;
        // this.pokemon4Move4 = dto4.move4;

        // this.pokemon5Name = dto5.name;
        // this.pokemon5CurrentHp = dto5.currentHp;
        // this.pokemon5MaxHp = dto5.maxHp;
        // this.pokemon5Status = dto5.status;
        // this.pokemon5Move1 = dto5.move1;
        // this.pokemon5Move2 = dto5.move2;
        // this.pokemon5Move3 = dto5.move3;
        // this.pokemon5Move4 = dto5.move4;

        // this.pokemon6Name = dto6.name;
        // this.pokemon6CurrentHp = dto6.currentHp;
        // this.pokemon6MaxHp = dto6.maxHp;
        // this.pokemon6Status = dto6.status;
        // this.pokemon6Move1 = dto6.move1;
        // this.pokemon6Move2 = dto6.move2;
        // this.pokemon6Move3 = dto6.move3;
        // this.pokemon6Move4 = dto6.move4;
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

    // public String getPokemon1Name() {
    //     return pokemon1Name;
    // }
    // public void setPokemon1Name(String pokemon1Name) {
    //     this.pokemon1Name = pokemon1Name;
    // }
    // public int getPokemon1CurrentHp() {
    //     return pokemon1CurrentHp;
    // }
    // public void setPokemon1CurrentHp(int pokemon1CurrentHp) {
    //     this.pokemon1CurrentHp = pokemon1CurrentHp;
    // }
    // public int getPokemon1MaxHp() {
    //     return pokemon1MaxHp;
    // }
    // public void setPokemon1MaxHp(int pokemon1MaxHp) {
    //     this.pokemon1MaxHp = pokemon1MaxHp;
    // }
    // public String getPokemon1Status() {
    //     return pokemon1Status;
    // }
    // public void setPokemon1Status(String pokemon1Status) {
    //     this.pokemon1Status = pokemon1Status;
    // }
    // public String getPokemon1Move1() {
    //     return pokemon1Move1;
    // }
    // public void setPokemon1Move1(String pokemon1Move1) {
    //     this.pokemon1Move1 = pokemon1Move1;
    // }
    // public String getPokemon1Move2() {
    //     return pokemon1Move2;
    // }
    // public void setPokemon1Move2(String pokemon1Move2) {
    //     this.pokemon1Move2 = pokemon1Move2;
    // }
    // public String getPokemon1Move3() {
    //     return pokemon1Move3;
    // }
    // public void setPokemon1Move3(String pokemon1Move3) {
    //     this.pokemon1Move3 = pokemon1Move3;
    // }
    // public String getPokemon1Move4() {
    //     return pokemon1Move4;
    // }
    // public void setPokemon1Move4(String pokemon1Move4) {
    //     this.pokemon1Move4 = pokemon1Move4;
    // }
    // public String getPokemon2Name() {
    //     return pokemon2Name;
    // }
    // public void setPokemon2Name(String pokemon2Name) {
    //     this.pokemon2Name = pokemon2Name;
    // }
    // public int getPokemon2CurrentHp() {
    //     return pokemon2CurrentHp;
    // }
    // public void setPokemon2CurrentHp(int pokemon2CurrentHp) {
    //     this.pokemon2CurrentHp = pokemon2CurrentHp;
    // }
    // public int getPokemon2MaxHp() {
    //     return pokemon2MaxHp;
    // }
    // public void setPokemon2MaxHp(int pokemon2MaxHp) {
    //     this.pokemon2MaxHp = pokemon2MaxHp;
    // }
    // public String getPokemon2Status() {
    //     return pokemon2Status;
    // }
    // public void setPokemon2Status(String pokemon2Status) {
    //     this.pokemon2Status = pokemon2Status;
    // }
    // public String getPokemon2Move1() {
    //     return pokemon2Move1;
    // }
    // public void setPokemon2Move1(String pokemon2Move1) {
    //     this.pokemon2Move1 = pokemon2Move1;
    // }
    // public String getPokemon2Move2() {
    //     return pokemon2Move2;
    // }
    // public void setPokemon2Move2(String pokemon2Move2) {
    //     this.pokemon2Move2 = pokemon2Move2;
    // }
    // public String getPokemon2Move3() {
    //     return pokemon2Move3;
    // }
    // public void setPokemon2Move3(String pokemon2Move3) {
    //     this.pokemon2Move3 = pokemon2Move3;
    // }
    // public String getPokemon2Move4() {
    //     return pokemon2Move4;
    // }
    // public void setPokemon2Move4(String pokemon2Move4) {
    //     this.pokemon2Move4 = pokemon2Move4;
    // }
    // public String getPokemon3Name() {
    //     return pokemon3Name;
    // }
    // public void setPokemon3Name(String pokemon3Name) {
    //     this.pokemon3Name = pokemon3Name;
    // }
    // public int getPokemon3CurrentHp() {
    //     return pokemon3CurrentHp;
    // }
    // public void setPokemon3CurrentHp(int pokemon3CurrentHp) {
    //     this.pokemon3CurrentHp = pokemon3CurrentHp;
    // }
    // public int getPokemon3MaxHp() {
    //     return pokemon3MaxHp;
    // }
    // public void setPokemon3MaxHp(int pokemon3MaxHp) {
    //     this.pokemon3MaxHp = pokemon3MaxHp;
    // }
    // public String getPokemon3Status() {
    //     return pokemon3Status;
    // }
    // public void setPokemon3Status(String pokemon3Status) {
    //     this.pokemon3Status = pokemon3Status;
    // }
    // public String getPokemon3Move1() {
    //     return pokemon3Move1;
    // }
    // public void setPokemon3Move1(String pokemon3Move1) {
    //     this.pokemon3Move1 = pokemon3Move1;
    // }
    // public String getPokemon3Move2() {
    //     return pokemon3Move2;
    // }
    // public void setPokemon3Move2(String pokemon3Move2) {
    //     this.pokemon3Move2 = pokemon3Move2;
    // }
    // public String getPokemon3Move3() {
    //     return pokemon3Move3;
    // }
    // public void setPokemon3Move3(String pokemon3Move3) {
    //     this.pokemon3Move3 = pokemon3Move3;
    // }
    // public String getPokemon3Move4() {
    //     return pokemon3Move4;
    // }
    // public void setPokemon3Move4(String pokemon3Move4) {
    //     this.pokemon3Move4 = pokemon3Move4;
    // }
    // public String getPokemon4Name() {
    //     return pokemon4Name;
    // }
    // public void setPokemon4Name(String pokemon4Name) {
    //     this.pokemon4Name = pokemon4Name;
    // }
    // public int getPokemon4CurrentHp() {
    //     return pokemon4CurrentHp;
    // }
    // public void setPokemon4CurrentHp(int pokemon4CurrentHp) {
    //     this.pokemon4CurrentHp = pokemon4CurrentHp;
    // }
    // public int getPokemon4MaxHp() {
    //     return pokemon4MaxHp;
    // }
    // public void setPokemon4MaxHp(int pokemon4MaxHp) {
    //     this.pokemon4MaxHp = pokemon4MaxHp;
    // }
    // public String getPokemon4Status() {
    //     return pokemon4Status;
    // }
    // public void setPokemon4Status(String pokemon4Status) {
    //     this.pokemon4Status = pokemon4Status;
    // }
    // public String getPokemon4Move1() {
    //     return pokemon4Move1;
    // }
    // public void setPokemon4Move1(String pokemon4Move1) {
    //     this.pokemon4Move1 = pokemon4Move1;
    // }
    // public String getPokemon4Move2() {
    //     return pokemon4Move2;
    // }
    // public void setPokemon4Move2(String pokemon4Move2) {
    //     this.pokemon4Move2 = pokemon4Move2;
    // }
    // public String getPokemon4Move3() {
    //     return pokemon4Move3;
    // }
    // public void setPokemon4Move3(String pokemon4Move3) {
    //     this.pokemon4Move3 = pokemon4Move3;
    // }
    // public String getPokemon4Move4() {
    //     return pokemon4Move4;
    // }
    // public void setPokemon4Move4(String pokemon4Move4) {
    //     this.pokemon4Move4 = pokemon4Move4;
    // }
    // public String getPokemon5Name() {
    //     return pokemon5Name;
    // }
    // public void setPokemon5Name(String pokemon5Name) {
    //     this.pokemon5Name = pokemon5Name;
    // }
    // public int getPokemon5CurrentHp() {
    //     return pokemon5CurrentHp;
    // }
    // public void setPokemon5CurrentHp(int pokemon5CurrentHp) {
    //     this.pokemon5CurrentHp = pokemon5CurrentHp;
    // }
    // public int getPokemon5MaxHp() {
    //     return pokemon5MaxHp;
    // }
    // public void setPokemon5MaxHp(int pokemon5MaxHp) {
    //     this.pokemon5MaxHp = pokemon5MaxHp;
    // }
    // public String getPokemon5Status() {
    //     return pokemon5Status;
    // }
    // public void setPokemon5Status(String pokemon5Status) {
    //     this.pokemon5Status = pokemon5Status;
    // }
    // public String getPokemon5Move1() {
    //     return pokemon5Move1;
    // }
    // public void setPokemon5Move1(String pokemon5Move1) {
    //     this.pokemon5Move1 = pokemon5Move1;
    // }
    // public String getPokemon5Move2() {
    //     return pokemon5Move2;
    // }
    // public void setPokemon5Move2(String pokemon5Move2) {
    //     this.pokemon5Move2 = pokemon5Move2;
    // }
    // public String getPokemon5Move3() {
    //     return pokemon5Move3;
    // }
    // public void setPokemon5Move3(String pokemon5Move3) {
    //     this.pokemon5Move3 = pokemon5Move3;
    // }
    // public String getPokemon5Move4() {
    //     return pokemon5Move4;
    // }
    // public void setPokemon5Move4(String pokemon5Move4) {
    //     this.pokemon5Move4 = pokemon5Move4;
    // }
    // public String getPokemon6Name() {
    //     return pokemon6Name;
    // }
    // public void setPokemon6Name(String pokemon6Name) {
    //     this.pokemon6Name = pokemon6Name;
    // }
    // public int getPokemon6CurrentHp() {
    //     return pokemon6CurrentHp;
    // }
    // public void setPokemon6CurrentHp(int pokemon6CurrentHp) {
    //     this.pokemon6CurrentHp = pokemon6CurrentHp;
    // }
    // public int getPokemon6MaxHp() {
    //     return pokemon6MaxHp;
    // }
    // public void setPokemon6MaxHp(int pokemon6MaxHp) {
    //     this.pokemon6MaxHp = pokemon6MaxHp;
    // }
    // public String getPokemon6Status() {
    //     return pokemon6Status;
    // }
    // public void setPokemon6Status(String pokemon6Status) {
    //     this.pokemon6Status = pokemon6Status;
    // }
    // public String getPokemon6Move1() {
    //     return pokemon6Move1;
    // }
    // public void setPokemon6Move1(String pokemon6Move1) {
    //     this.pokemon6Move1 = pokemon6Move1;
    // }
    // public String getPokemon6Move2() {
    //     return pokemon6Move2;
    // }
    // public void setPokemon6Move2(String pokemon6Move2) {
    //     this.pokemon6Move2 = pokemon6Move2;
    // }
    // public String getPokemon6Move3() {
    //     return pokemon6Move3;
    // }
    // public void setPokemon6Move3(String pokemon6Move3) {
    //     this.pokemon6Move3 = pokemon6Move3;
    // }
    // public String getPokemon6Move4() {
    //     return pokemon6Move4;
    // }
    // public void setPokemon6Move4(String pokemon6Move4) {
    //     this.pokemon6Move4 = pokemon6Move4;
    // }
}
