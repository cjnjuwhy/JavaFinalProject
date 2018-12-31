package Being;
import java.util.ArrayList;

public class FightField {
	public FightField(){
		field = new WarriorInterface[Information.SIZE][Information.SIZE];
		battleLog = new ArrayList<String>();
	}

	
	public boolean isAlive(int x, int y) {
		if(hasWarrior(x,y)) {
			return field[x][y].isAlive();
		}
		else
			return false;
	}
	public boolean hasWarrior(int x, int y) {
		if(field[x][y] == null)
			return false;
		else
			return true;
	}
	public boolean inField(int x, int y) {
		if( x<0 || y<0 || x>=Information.SIZE || y>=Information.SIZE ) {
			//System.out.println("Out of bound.");
			return false;
		}
		return true;
	}
	
	public String showFields() {
		String result = new String();
		result += "***************************************************战场对阵情况****************************************************\n";
		for (int i=0; i<Information.SIZE; i++) {
			for (int j=0; j<Information.SIZE; j++) {
				if(field[i][j]==null || field[i][j].isAlive()==false) result += "---";
				else result += field[i][j];
				result += "\t";	
			}
			result += "\n";
		}
		result += "***************************************************战场对阵情况****************************************************\n";
		
		System.out.print(result);
		return result;
	}
	
	
	public WarriorInterface[][] field;
	public ArrayList<String> battleLog;
	
	public static int goodDead = 0;
	public static int badDead = 0;
	public boolean isDone() {
		if(goodDead==8 || badDead==8)
			return true;
		return false;
	}
	
	public void dead(int id) {
		if(id < 9)
			goodDead++;
		else
			badDead++;
			
	}
	
}
