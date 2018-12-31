package Being;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Warrior implements WarriorInterface, Runnable{
	// should implements runnable interface
	public Warrior(String name, int id, String team, int attack, int life, FightField fightField) {
		this.name = name;
		this.id = id;
		this.team = team;
		this.attack = attack;
		this.life = life;
		this.fightField = fightField;
		this.alive = true;
		this.position = new int[2];
		position[0] = position[1] = -1;
		
		log = new ArrayList<String>();
		posLog = new ArrayList<int[]>();
		
		/*
		 * 如果输入的attack<0表示为测试模式
		 */
		if(attack >= 0) {
		Image image = new Image(Information.WARRIOR_PATH[id]);
        imageView = new ImageView();
        imageView.setImage(image);
        imageView.setFitWidth(40);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
		}
		
	}
	
	
	
	public String getTeam() {
		return team;
	}
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public int getAttack() {
		return attack;
	}
	public synchronized int getLife() {
		return life;
	}
	public int[] getPosition() {
		return this.position;
	}
	public boolean isAlive() {
		if(life>0) {
			this.alive = true;
		}
		else {
			this.alive = false;
		}
		return alive;
	}
	// only some attributes can be changed
	public synchronized void changeLife(int bonus) {
		this.life += bonus;
	}
	// useful for the video re-play
	public void changePosition(int x, int y) {
		this.position[0] = x;
		this.position[1] = y;
	}
	
	
	
	public int[] randomMove() {
		// no random move for dead one
		Random rand = new Random();
		int shift_x;
		int shift_y;
		do {
			shift_x = rand.nextInt(6)-3;
			shift_y = rand.nextInt(6)-3;
		}while(fightField.inField(this.position[0]+shift_x, this.position[1]+shift_y)==false ||
				fightField.isAlive(this.position[0]+shift_x,this.position[1]+shift_y)); 		// call a method, judge whether outofbound in it.
		
		fightField.field[position[0]][position[1]] = null;
		
		this.position[0] += shift_x;
		this.position[1] += shift_y;
		
		fightField.field[position[0]][position[1]] = this;
		
		return this.position;
	}
	
	public void attackOthers() {
		for(int i = -3; i<=3; i++) {
			for(int j = -3; j<=3; j++) {
				/* if [i][j]has warrior and in the other side, attack*/
				int newX = position[0]+i;
				int newY = position[1]+j;
				if(fightField.inField(newX, newY)
					&& fightField.isAlive(newX, newY) 
					&& this.team.compareTo(fightField.field[newX][newY].getTeam())!= 0 ) {
						fightField.field[newX][newY].changeLife(-attack);
						if(!fightField.field[newX][newY].isAlive()) {
							fightField.battleLog.add(this + " killed " + fightField.field[newX][newY]);
						}
				}
			}
		}
	}
	
	public String toString() {
		//String temp = String.format("Name: %s, Position:[%d,%d]", this.name, this.position[0], this.position[1]);
		//return temp;
		return this.name;
	}
	public String showMe() {
		String temp = String.format("Name: %s, Position:[%d,%d], Life: %d %n", name, position[0], position[1], life);
		System.out.println(temp);
		return temp;
	}
	
	public void run() {
		while(alive && !fightField.isDone()) {
		posLog.add(new int[] {position[0], position[1], life});
			int oldX = position[0];
			int oldY = position[1];
			randomMove();
			attackOthers();
			int newX = position[0];
			int newY = position[1];
			log.add( String.format("from [%d, %d] to [%d, %d]", oldX, oldY, newX, newY) );
		//fightField.showFields();
		// Log the information
		try {
		Thread.sleep(Information.DELAY);}
		catch(Exception e) {e.printStackTrace();}
		}
		posLog.add(new int[] {position[0], position[1], life});
		
		fightField.dead(id);
		
	}
	
	public void showPosLog() {
		int n = 1;
		for(int[] i: posLog) {
			System.out.printf("round %d: pos [%d, %d] life %d %n", n++, i[0], i[1], i[2]);
		}
	}
	
	public void loadLog(String logString) {
		posLog.clear();
		String []records = logString.split(" ");

		for(int i = 0; i<records.length; i++) {
			posLog.add(new int[] 
					{Integer.parseInt(records[i++]),
							Integer.parseInt(records[i++]),
									Integer.parseInt(records[i])});
		}
	}
	
	
	public ImageView imageView = null;
	
	public ImageView getImageView() {
		return imageView;
	}
	
	
	private String team;
	private String name;
	private int id;
	private int attack;
	private int life;
	private int[] position;
	boolean alive;
	
	public FightField fightField;
	public ArrayList<String> log;
	public ArrayList<int[]> posLog;

}
