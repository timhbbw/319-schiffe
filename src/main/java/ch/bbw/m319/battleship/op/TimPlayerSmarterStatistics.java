package ch.bbw.m319.battleship.op;

import ch.bbw.m319.battleship.RicoPlayer;
import ch.bbw.m319.battleship.api.BattleshipArena;
import ch.bbw.m319.battleship.api.BattleshipField;
import ch.bbw.m319.battleship.api.BattleshipPlayer;
import ch.bbw.m319.battleship.api.ShipPosition;

import java.util.*;

public class TimPlayerSmarterStatistics implements BattleshipPlayer {

    public TimPlayerSmarterStatistics() {
        shotAtPos = new ArrayList<>();
    }

    public static void main(String[] args) {
		BattleshipArena.playMultipleAndCount(new TimPlayerSmarterStatistics(), new RicoPlayer(), 10000);
				IntSummaryStatistics summaryStats = averageMoveCounts.stream()
				.mapToInt(Integer::intValue)
				.summaryStatistics();
		System.out.println("Average moves per game: " + summaryStats.getAverage());
	}

	private static final int BOARD_WIDTH = 3;

	private final List<BattleshipField> shotAtPos;

	private final Random random = new Random();

	private List<BattleshipField> nextFieldsToBeHit;

	private  final List<BattleshipField> optimalFieldsToHit = List.of(BattleshipField.A2, BattleshipField.B1, BattleshipField.B3, BattleshipField.C2);

	private static final List<Integer> averageMoveCounts = new ArrayList<>();

	private boolean isAdjacent(BattleshipField a, BattleshipField b) {
		int diff = Math.abs(a.ordinal() - b.ordinal());
		if (diff == BOARD_WIDTH) return true;
		if (diff == 1) {
			return a.ordinal() / BOARD_WIDTH == b.ordinal() / BOARD_WIDTH;
		}
		return false;
	}

    private BattleshipField getRandomPosition() {
		return BattleshipField.values()[random.nextInt(9)];
	}

	@Override
	public ShipPosition placeYourShip() {
        BattleshipField shipPos1 = BattleshipField.values()[getRandomPosition().ordinal()];
        BattleshipField shipPos2 = BattleshipField.values()[getRandomPosition().ordinal()];

		while(!isAdjacent(shipPos1, shipPos2)) {
			shipPos2 = BattleshipField.values()[getRandomPosition().ordinal()];
		}
		if (nextFieldsToBeHit != null) {
			nextFieldsToBeHit = null;
		}
		shotAtPos.clear();
        return new ShipPosition(shipPos1, shipPos2);
    }

	@Override
	public BattleshipField takeAim() {
		BattleshipField shot;
		do {
			if (nextFieldsToBeHit != null && !new HashSet<>(shotAtPos).containsAll(nextFieldsToBeHit)) {
				shot = getRandomFieldFromList(nextFieldsToBeHit);
			} else {
				shot = getRandomFieldFromList(optimalFieldsToHit);
			}
		}
		while (shotAtPos.contains(shot));

		shotAtPos.add(shot);
		return shot;
	}

	@Override
	public void outcomeOfYourTurn(BattleshipField targetedField, boolean isHit) {
		if (isHit) {
			nextFieldsToBeHit = getSurroundingFields(targetedField);
		}
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
	public void gameFinished(ShipPosition opponentShip, boolean youHaveWon) {
		averageMoveCounts.add(shotAtPos.size());
	}

	private BattleshipField getRandomFieldFromList(List<BattleshipField> targetedFields) {
		return targetedFields.get(random.nextInt(targetedFields.size()));
	}
}
