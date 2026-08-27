package ch.bbw.m319.battleship;

import ch.bbw.m319.battleship.api.BattleshipArena;
import ch.bbw.m319.battleship.api.BattleshipField;
import ch.bbw.m319.battleship.api.BattleshipPlayer;
import ch.bbw.m319.battleship.api.ShipPosition;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;

public class TimPlayerStoobid implements BattleshipPlayer {

    public TimPlayerStoobid() {
        shotAtPos = new ArrayList<>();
    }

    public static void main(String[] args) {
		// let it play against itself
		BattleshipArena.playMultipleAndCount(new TimPlayerStoobid(), new ReallyStoobidPlayer(), 1000);
		IntSummaryStatistics summaryStats = averageMoveCounts.stream()
				.mapToInt(Integer::intValue)
				.summaryStatistics();
		System.out.println("Average moves so far: " + summaryStats.getAverage());
	}

	private static final int BOARD_WIDTH = 3;

	private final List<BattleshipField> shotAtPos;

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
		Random random = new Random();
		return BattleshipField.values()[random.nextInt(9)];
	}

	@Override
	public ShipPosition placeYourShip() {
        BattleshipField shipPos1 = BattleshipField.values()[getRandomPosition().ordinal()];
        BattleshipField shipPos2 = BattleshipField.values()[getRandomPosition().ordinal()];

		while(!isAdjacent(shipPos1, shipPos2)) {
			shipPos2 = BattleshipField.values()[getRandomPosition().ordinal()];
		}
		shotAtPos.clear();
        return new ShipPosition(shipPos1, shipPos2);
    }

	@Override
	public BattleshipField takeAim() {
		BattleshipField shot;
		do {
			shot = getRandomPosition();
		}
		while (shotAtPos.contains(shot));

		shotAtPos.add(shot);
		return shot;
	}

	@Override
	public void gameFinished(ShipPosition opponentShip, boolean youHaveWon) {
		averageMoveCounts.add(shotAtPos.size());
	}
}
