package ch.bbw.m319.battleship;

import ch.bbw.m319.battleship.api.BattleshipArena;
import ch.bbw.m319.battleship.api.BattleshipField;
import ch.bbw.m319.battleship.api.BattleshipPlayer;
import ch.bbw.m319.battleship.api.ShipPosition;

import java.util.*;

public class RicoPlayer implements BattleshipPlayer {

    public RicoPlayer() {
        shots = new ArrayList<>();
    }

    public static void main(String[] args) {
        BattleshipArena.playMultipleAndCount(new RicoPlayer(), new TimPlayer(), 100000);
          }

    private final List<BattleshipField> shots;

    private static List<BattleshipField> nextFieldsToBeHit;

    private BattleshipField randomField() {
        int randomNum = (int)(Math.random() * 3);
        int randomNumLetter = (int)(Math.random() * 3);
        String randomLetter = "A";

        randomNum++;
        if (randomNumLetter == 1) {
            randomLetter = "B";
        } else if (randomNumLetter == 2) {
            randomLetter = "C";
        }
        return BattleshipField.valueOf(randomLetter + randomNum);
    }


    private BattleshipField checkShip(BattleshipField fieldOne) {
        String[] validFieldTwo;
        if (fieldOne.name().equals("A1")) {
            validFieldTwo = new String[]{"A2", "B1"};
        } else if (fieldOne.name().equals("A2")) {
            validFieldTwo = new String[]{"A1", "A3", "B2"};
        } else if (fieldOne.name().equals("A3")) {
            validFieldTwo = new String[]{"A2", "B3"};
        } else if (fieldOne.name().equals("B1")) {
            validFieldTwo = new String[]{"A1", "B2", "C1"};
        } else if (fieldOne.name().equals("B2")) {
            validFieldTwo = new String[]{"A2", "B1", "B3", "C2"};
        } else if (fieldOne.name().equals("B3")) {
            validFieldTwo = new String[]{"A3", "B2", "C3"};
        } else if (fieldOne.name().equals("C1")) {
            validFieldTwo = new String[]{"B1", "C2"};
        } else if (fieldOne.name().equals("C2")) {
            validFieldTwo = new String[]{"B2", "C1", "C3"};
        } else {
            validFieldTwo = new String[]{"B3", "C2"};
        }
        int arrayLength = validFieldTwo.length;
        return BattleshipField.valueOf(validFieldTwo[(int)(Math.random() * arrayLength)]);
    }

    private List<BattleshipField> getSurroundingFields(BattleshipField targetedField) {
        return switch (targetedField) {
            case A1 -> List.of(BattleshipField.A2, BattleshipField.B1);
            case A2 -> List.of(BattleshipField.A1, BattleshipField.A3, BattleshipField.B2);
            case A3 -> List.of(BattleshipField.A2, BattleshipField.B3);
            case B1 -> List.of(BattleshipField.A1, BattleshipField.C1, BattleshipField.B2);
            case B2 -> List.of(BattleshipField.A2, BattleshipField.C2, BattleshipField.B1, BattleshipField.B3);
            case B3 -> List.of(BattleshipField.A3, BattleshipField.C3, BattleshipField.B2);
            case C1 -> List.of(BattleshipField.B1, BattleshipField.C2);
            case C2 -> List.of(BattleshipField.C1, BattleshipField.C3, BattleshipField.B2);
            default -> List.of(BattleshipField.C2, BattleshipField.B3);
        };
    }

    @Override
    public ShipPosition placeYourShip() {
        BattleshipField fieldOne = randomField();

        BattleshipField fieldTwo = checkShip(fieldOne);

        shots.clear();
        return new ShipPosition(fieldOne, fieldTwo);

    }

    public BattleshipField checkAim() {
        BattleshipField shot;
        do {
            if (nextFieldsToBeHit != null && !new HashSet<>(shots).containsAll(nextFieldsToBeHit)) {
                shot = getRandomFieldFromList(nextFieldsToBeHit);
            } else {
                shot = randomField();
            }
        } while (shots.contains(shot));

        shots.add(shot);

        return shot;
    }

    @Override
    public BattleshipField takeAim() {
        return checkAim();
    }

    @Override
    public void outcomeOfYourTurn(BattleshipField targetedField, boolean isHit) {
        if (isHit) {
            nextFieldsToBeHit = getSurroundingFields(targetedField);
        }
    }

    public BattleshipField getRandomFieldFromList(List<BattleshipField> targetedFields) {
        Random random = new Random(); //TODO: Math.Random
        return targetedFields.get(random.nextInt(targetedFields.size()));
    }
}


