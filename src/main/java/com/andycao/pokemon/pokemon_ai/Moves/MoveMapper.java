package com.andycao.pokemon.pokemon_ai.Moves;

import com.andycao.pokemon.pokemon_ai.Exceptions.InvalidIdentifierException;

import java.util.HashMap;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
//import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class MoveMapper {
	private Resource movesFilePath = new ClassPathResource("moves.txt");

    private HashMap<String, MoveDto> moveNameToAttributes;

    public MoveMapper() {
        moveNameToAttributes = new HashMap<String, MoveDto>();
        generateMapping();
    }

    private String extractMoveName(String moveText) {
        int startIndex = moveText.indexOf("[") + 1;
        int endIndex = moveText.indexOf("]");

        return moveText.substring(startIndex, endIndex);
    }

    private String extractMoveAttributes(String moveText, String startString) {
        String[] lines = moveText.split("\n");

        for (String line : lines) {
            if (!line.contains(startString)) {
                continue;
            }

            int startIndex = line.indexOf(startString) + startString.length();
            return line.substring(startIndex, line.length() - 1);
        }

        return "0"; // Only occurs if a priority/effect chance entry does not exist: return default priority/effect chance of 0
    }

    private String[] extractMoveFlags(String moveText) {
        String[] lines = moveText.split("\n");
        String flags = "";
        String searchText = "Flags = ";

        for (String line : lines) {
            if (!line.contains("Flags = ")) {
                continue;
            }

            flags = line.substring(line.indexOf(searchText) + searchText.length(), line.length() - 1);
        }

        return flags.split(",");
    }

    private void generateMapping() {
        TextReader textReader = new TextReader(movesFilePath);
        List<Document> moveDocs = textReader.get();
        String moveText = moveDocs.toString();
        String[] moves = moveText.split("#-------------------------------");

        for (int i = 1; i < moves.length; i++) {
            String moveName = extractMoveName(moves[i]);
            String displayName = extractMoveAttributes(moves[i], "Name = ");
            String type = extractMoveAttributes(moves[i], "Type = ");
            String category = extractMoveAttributes(moves[i], "Category = ");
            String power = extractMoveAttributes(moves[i], "Power = ");
            String accuracy = extractMoveAttributes(moves[i], "Accuracy = ");
            String totalPP = extractMoveAttributes(moves[i], "TotalPP = ");
            String target = extractMoveAttributes(moves[i], "Target = ");
            String priority = extractMoveAttributes(moves[i], "Priority = ");
            String functionCode = extractMoveAttributes(moves[i], "FunctionCode = ");
            String[] flags = extractMoveFlags(moves[i]);
            String effectChance = extractMoveAttributes(moves[i], "EffectChance = ");

            moveNameToAttributes.put(moveName, new MoveDto(moveName, displayName, type, category, power, accuracy, totalPP, target, priority, functionCode, flags, effectChance));
        }
        //System.out.println(moveNameToAttributes.toString());
    }

    public MoveDto getMoveAttributes(String moveName) throws InvalidIdentifierException {
        MoveDto attributes = moveNameToAttributes.get(moveName);

        if (attributes == null) {
            throw new InvalidIdentifierException(moveName + " isn't a valid existing move");
        }

        return attributes;
    }
}
