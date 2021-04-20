// put your code here

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TeamRPK implements Bot {
	// The public API of YourTeamName must not change
	// You cannot change any other classes
	// YourTeamName may not alter the state of the board or the player objects
	// It may only inspect the state of the board and the player objects
	// So you can use player.getNumUnits() but you can't use player.addUnits(10000), for example
	
	private BoardAPI board;
	private PlayerAPI player;

	private ArrayList<Integer> canAttack;
	private ArrayList<Integer> owned = new ArrayList<>();
	private ArrayList<Integer> otherOwned = new ArrayList<>();
	private ArrayList<Integer> attackingC;

	public
	
	TeamRPK(BoardAPI inBoard, PlayerAPI inPlayer) {
		board = inBoard;	
		player = inPlayer;
		// put your code here
		return;
	}
	
	public String getName () {
		String command = "";
		// put your code here
		command = "ChanceMyLuck";
		return(command);
	}

	public String getReinforcement () {
		String command = "";
		for(int[] c : GameData.CONTINENT_COUNTRIES ){
			for(int d : c){
				if(board.getOccupier(d) == player.getId()){
					owned.add(d);
				}
			}
		}

		ArrayList<Integer> occupiedCountries = new ArrayList<>();
		ArrayList<Integer> friendlyNeighbours = new ArrayList<>();
		ArrayList<Integer> enemyNeighbours = new ArrayList<>();


		for(int countryID = 0; countryID < 41; countryID++) {
			if(board.getOccupier(countryID) == player.getId()) {
				occupiedCountries.add(countryID);
			}
		}
		for(int i=0; i<42;i++) {
			for(int f: occupiedCountries){
				if(board.getOccupier(i) == player.getId() && board.isAdjacent(i,f)) {
					friendlyNeighbours.add(f);
					friendlyNeighbours.add(i);
				}
			}
		}

		int[] hasNeighbour = new int[occupiedCountries.size()];
		int[] hasEnemyNeighbour = new int[occupiedCountries.size()];
		for(int i=0;i< occupiedCountries.size();i++){
			hasNeighbour[i]=0;
			hasEnemyNeighbour[i]=0;
		}

		for(int i=0; i< occupiedCountries.size();i++){
			for(int j=0; j < friendlyNeighbours.size();j++){
				if(board.isAdjacent(occupiedCountries.get(i), friendlyNeighbours.get(j) )){
					hasNeighbour[i]+=1;
				}
			}
		}


		for(int a=0; a< occupiedCountries.size();a++){
			for(int i=0;i<42;i++) {
				if (board.isAdjacent(occupiedCountries.get(a),i) && board.getOccupier(i) != player.getId() &&
						board.getNumUnits(occupiedCountries.get(a)) > board.getNumUnits(i)){
					enemyNeighbours.add(i);

				}

			}
		}

		for(int i=0; i< occupiedCountries.size();i++){
			for(int j=0; j < friendlyNeighbours.size();j++){
				if(board.isAdjacent(occupiedCountries.get(i), enemyNeighbours.get(j) )){
					hasEnemyNeighbour[i]+=1;
				}
			}
		}




		int random = (int)(Math.random() * owned.size());
		command = GameData.COUNTRY_NAMES[owned.get(random)];
		command = command.replaceAll("\\s", "");
		command += " 1";
		return(command);
	}
	
	public String getPlacement (int forPlayer) {
		String command = "";
		for(int[] c : GameData.CONTINENT_COUNTRIES ){
			for(int d : c){
				if(board.getOccupier(d) == forPlayer){
					otherOwned.add(d);
				}
			}
		}
		System.out.println(otherOwned);
		System.out.println();
		int random = (int)(Math.random() * otherOwned.size());
		command = GameData.COUNTRY_NAMES[otherOwned.get(random)];
		command = command.replaceAll("\\s", "");
		return(command);
	}
	
	public String getCardExchange () {
		String command = "";
		command = bestExchange();
		if(command != "") return command;
		else {
			command = "skip";
		}
		return(command);
	}

	public String bestExchange(){
		ArrayList <Card> cards = player.getCards();
		int[] botCards = {0,0,0,0};
		for(Card c : cards){
			botCards[c.getInsigniaId()] += 1;
		}
		if(botCards[0] > 2) return "iii";
		if(botCards[1] > 2) return "ccc";
		if(botCards[2] > 2) return "aaa";
		if(botCards[0] != 0 && botCards[1] != 0 && botCards[2] != 0) return "ica";
		if(botCards[3] != 0 && botCards[0] > 1) return "iiw";
		if(botCards[3] != 0 && botCards[1] > 1) return "ccw";
		if(botCards[3] != 0 && botCards[2] > 1) return "aaw";
		if(botCards[3] != 0 && botCards[0]+botCards[1] > 1) return "icw";
		if(botCards[3] != 0 && botCards[0]+botCards[2] > 1) return "iaw";
		if(botCards[3] != 0 && botCards[1]+botCards[2] > 1) return "acw";
		if(botCards[3] > 1 && botCards[0] > 0) return "iww";
		if(botCards[3] > 1 && botCards[1] > 0) return "cww";
		if(botCards[3] > 1 && botCards[2] > 0) return "aww";
		if(botCards[3] > 2) return "www";
		return "";
	}

	public String getBattle () {
		String command = "";
		ArrayList<Integer> cAttack = new ArrayList<>();
		ArrayList<Integer> own = new ArrayList<>();
		ArrayList<Integer> attackC = new ArrayList<>();
		attackC.add(99);
		String Attacking = "";
		String Attackingfrom = "";
		for(int c = 0; c<GameData.COUNTRY_NAMES.length; c++){
			if(board.getOccupier(c) == player.getId()){
				own.add(c);
			}
		}
		owned = own;
		attackC.set(0,own.get(0));
		for(int a=0; a< own.size();a++){
			for(int i=0;i<42;i++) {
				if (board.isAdjacent(own.get(a),i) && board.getOccupier(i) != player.getId() &&
						board.getNumUnits(own.get(a)) > board.getNumUnits(i)){
					cAttack.add(i);
					attackC.set(0,own.get(a));
				}
//				if(board.isAdjacent(a,i) && board.getNumUnits(i) > board.getNumUnits(attackC.get(0)))
//					attackC.set(0,i);
			}
		}

		if(cAttack.size() == 0) return "skip";
		for(int i=0;i<42;i++){
			if(board.isAdjacent(attackC.get(0), i) && board.getOccupier(i) != player.getId()){
				String attackingfrom = GameData.COUNTRY_NAMES[attackC.get(0)].replaceAll("\\s", "");
				String attacking = GameData.COUNTRY_NAMES[cAttack.get(cAttack.size()-1)].replaceAll("\\s", "");
				int units = 1;
				if(board.getNumUnits(attackC.get(0)) > 3) units = 3;
				else units = board.getNumUnits(attackC.get(0))-1;
				command = attackingfrom + " " + attacking + " " + units;

			}
		}
		if(command == "") command = "skip";
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
		ArrayList<Integer> friendlyNeighbours = new ArrayList<>();

		for(int countryID = 0; countryID < 41; countryID++) {
			if(board.getOccupier(countryID) == player.getId()) {
				occupiedCountries.add(countryID);
			}
		}

		for(int i=0; i<42;i++) {
			for(int f: occupiedCountries){
				if(board.getOccupier(i) == player.getId() && board.isAdjacent(i,f)) {
					friendlyNeighbours.add(f);
					friendlyNeighbours.add(i);
				}
			}
		}

		int from = 0;
		int to = 0;

		boolean canFortify = false;
		int unitsToDonate = 0;
		for(int i=0; i<friendlyNeighbours.size()/2; i+=2){
			if(board.getNumUnits(friendlyNeighbours.get(i)) > board.getNumUnits(friendlyNeighbours.get(i+1))){
				if(board.getNumUnits(friendlyNeighbours.get(i)) > 2){
					unitsToDonate = board.getNumUnits(friendlyNeighbours.get(i))/2;
					from = friendlyNeighbours.get(i);
					to = friendlyNeighbours.get(i+1);

					canFortify = true;
				}
			}
		}
		if(canFortify){
			//command = "skip";
			command = GameData.COUNTRY_NAMES[from].replaceAll("\\s","") + " " + GameData.COUNTRY_NAMES[to].replaceAll("\\s","") + " " + unitsToDonate;
		}else{
			return "skip";
		}

//		int[][] maxUnitDiff = new int[occupiedCountries.size()][2];
//
//		for(int i = 0; i < occupiedCountries.size(); i++) {
//			int[] neighbours = GameData.ADJACENT[occupiedCountries.get(i)];
//			ArrayList<Integer> unitDifferences = new ArrayList<>();
//			ArrayList<Integer> friendlyNeighbours = new ArrayList<>();
//
//			for(int neighbourID : neighbours){
//				if(board.getOccupier(neighbourID) == 1) {
//					friendlyNeighbours.add(neighbourID);
//				} else {
//					int unitDifference = board.getNumUnits(neighbourID) - board.getNumUnits(occupiedCountries.get(i));
//					unitDifferences.add(unitDifference);
//				}
//			}
//
//			boolean ableToFortify = false;
//			if(!friendlyNeighbours.isEmpty() && !unitDifferences.isEmpty()) {
//				for(int friendlyID : friendlyNeighbours) {
//					if(board.getNumUnits(friendlyID) > 1) {
//						System.out.println(GameData.COUNTRY_NAMES[friendlyID]);
//						ableToFortify = true;
//						break;
//					}
//				}
//			}
//
//			maxUnitDiff[i][0] = occupiedCountries.get(i);
//
//			if(ableToFortify) {
//				maxUnitDiff[i][1] = Collections.max(unitDifferences);
//			}else {
//				maxUnitDiff[i][1] = Integer.MIN_VALUE;
//			}
//
//		}
//
//		int[] maxUnitDifference = maxUnitDiff[0];
//
//		// Find maximum difference of units between occupied countries and neighbours
//		for(int i = 1; i < maxUnitDiff.length; i++) {
//			int unitDifference = maxUnitDiff[i][1];
//			if(unitDifference > maxUnitDifference[1]) maxUnitDifference = maxUnitDiff[i];
//		}
//
//		if(maxUnitDifference[1] == Integer.MIN_VALUE) {
//			command = "skip";
//		} else {
//			ArrayList<Integer> friendlyNeighbours = new ArrayList<>();
//			int[] neighbours = GameData.ADJACENT[maxUnitDifference[0]];
//			for(int neighbourID : neighbours) {
//				if(board.getOccupier(neighbourID) == 1) {
//					friendlyNeighbours.add(neighbourID);
//				}
//			}
//
//			int neighbourToDonate = friendlyNeighbours.get(0);
//			int maxFriendlyUnits = board.getNumUnits(neighbourToDonate);
//
//			if(friendlyNeighbours.size() > 1) {
//				for(int i = 1 ; i < friendlyNeighbours.size(); i++) {
//					int units = board.getNumUnits(friendlyNeighbours.get(i));
//					if(units > maxFriendlyUnits) {
//						maxFriendlyUnits = units;
//						neighbourToDonate = friendlyNeighbours.get(i);
//					}
//				}
//			}
//
//			int unitsToDonate = maxFriendlyUnits / 2;
			//command = GameData.COUNTRY_NAMES[neighbourToDonate].replaceAll("\\s","") + " " + GameData.COUNTRY_NAMES[maxUnitDifference[0]].replaceAll("\\s","") + " " + unitsToDonate;
//		}
		return(command);
	}

	

}
