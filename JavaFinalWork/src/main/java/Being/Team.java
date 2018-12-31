package Being;


import java.util.ArrayList;
import java.util.Iterator;

interface TeamInterface{
	public void showTeam();
	public String getSide();
	public void add(Warrior temp);
	public void goBattle(WarriorInterface [][] fields, int[][] place);
	public boolean allDead();
}

public class Team implements TeamInterface{

	public ArrayList<Warrior> team;
	
	
	private String side;
	public Team(String side){
		team = new ArrayList<Warrior>();
		this.side = side;
	}
	public void showTeam() {
		Iterator<Warrior> it = team.iterator();
		while(it.hasNext()) {
			it.next().showMe();
		}
	}
	public String getSide() {
		return side;
	}
	public void add(Warrior temp) {
		team.add(temp);
	}
	
	public void goBattle(WarriorInterface [][] fields, int[][] place) {
		for(int i=0; i<team.size(); i++) {
			int m = place[i][0];
			int n = place[i][1];
			team.get(i).changePosition(m, n);
			fields[m][n] = team.get(i);
		}
	}
	
	public String toString() {
		String s = new String();
		Iterator<Warrior> it = team.iterator();
		while(it.hasNext()) {
			s += it.next();
			s += "\n";
		}
		return s;
		
	}
	
	public boolean allDead() {
		for(Warrior w: team) {
			if(w.isAlive())
				return false;
		}
		return true;
	}
	
}