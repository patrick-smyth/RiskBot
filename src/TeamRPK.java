// put your code here

import java.util.ArrayList;
import java.util.Collections;

public class TeamRPK implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example
	
	private BoardAPI board;
	private PlayerAPI player;
	
	TeamRPK(BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		// put your code here
		return;
	}
	
	public String getName () {
		String command = "";
		// put your code here
		command = "BOT";
		return(command);
	}

	public String getReinforcement () {
		String command = "";
		// put your code here
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		// put your code here
		command = GameData.COUNTRY_NAMES[(int)(Math.random() * GameData.NUM_COUNTRIES)];
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	public String getCardExchange () {
		String command = "";
		// put your code here
		command = "skip";
		return(command);
	}

	public String getBattle () {
		String command = "";
		// put your code here
		command = "skip";
		return(command);
	}

	public String getDefence (int countryId) {
		String command = "";
		if(board.getNumUnits(countryId) >= 2) command = "2";
		else command = "1";
		return(command);
	}

	public String getMoveIn (int attackCountryId) {
		String command = "";
		// put your code here
		int numUnits = board.getNumUnits(attackCountryId)/2;
		command = Integer.toString(numUnits);
		return command;
	}

	public String getFortify () {
		String command = "";
		// put code here
		ArrayList<Integer> occupiedCountries = new ArrayList<>();

		for(int countryID = 0; countryID < 41; countryID++) {
			if(board.getOccupier(countryID) == 1) {
				occupiedCountries.add(countryID);
			}
		}

		int[][] maxUnitDiff = new int[occupiedCountries.size()][2];

		for(int i = 0; i < occupiedCountries.size(); i++) {
			int[] neighbours = GameData.ADJACENT[occupiedCountries.get(i)];
			ArrayList<Integer> unitDifferences = new ArrayList<>();
			ArrayList<Integer> friendlyNeighbours = new ArrayList<>();

			for(int neighbourID : neighbours){
				if(board.getOccupier(neighbourID) == 1) {
					friendlyNeighbours.add(neighbourID);
				} else {
					int unitDifference = board.getNumUnits(neighbourID) - board.getNumUnits(occupiedCountries.get(i));
					unitDifferences.add(unitDifference);
				}
			}

			boolean ableToFortify = false;
			if(!friendlyNeighbours.isEmpty() && !unitDifferences.isEmpty()) {
				for(int friendlyID : friendlyNeighbours) {
					if(board.getNumUnits(friendlyID) > 1) {
						ableToFortify = true;
						break;
					}
				}
			}

			maxUnitDiff[i][0] = occupiedCountries.get(i);

			if(ableToFortify) {
				maxUnitDiff[i][1] = Collections.max(unitDifferences);
			}else {
				maxUnitDiff[i][1] = Integer.MIN_VALUE;
			}

		}

		int[] maxUnitDifference = maxUnitDiff[0];

		// Find maximum difference of units between occupied countries and neighbours
		for(int i = 1; i < maxUnitDiff.length; i++) {
			int unitDifference = maxUnitDiff[i][1];
			if(unitDifference > maxUnitDifference[1]) maxUnitDifference = maxUnitDiff[i];
		}

		if(maxUnitDifference[1] == Integer.MIN_VALUE) {
			command = "skip";
		} else {
			ArrayList<Integer> friendlyNeighbours = new ArrayList<>();
			int[] neighbours = GameData.ADJACENT[maxUnitDifference[0]];
			for(int neighbourID : neighbours) {
				if(board.getOccupier(neighbourID) == 1) {
					friendlyNeighbours.add(neighbourID);
				}
			}

			int neighbourToDonate = friendlyNeighbours.get(0);
			int maxFriendlyUnits = board.getNumUnits(neighbourToDonate);

			if(friendlyNeighbours.size() > 1) {
				for(int i = 1 ; i < friendlyNeighbours.size(); i++) {
					int units = board.getNumUnits(friendlyNeighbours.get(i));
					if(units > maxFriendlyUnits) {
						maxFriendlyUnits = units;
						neighbourToDonate = friendlyNeighbours.get(i);
					}
				}
			}

			int unitsToDonate = maxFriendlyUnits / 2;
			command = GameData.COUNTRY_NAMES[neighbourToDonate].replaceAll("\\s","") + " " + GameData.COUNTRY_NAMES[maxUnitDifference[0]].replaceAll("\\s","") + " " + unitsToDonate;
		}
		return(command);
	}

	

}
