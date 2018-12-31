package Being;

import javafx.scene.image.ImageView;

public interface WarriorInterface {
	public boolean isAlive();
	public String getTeam();
	public void changeLife(int bonus);
	
	public ImageView getImageView();
	
	public int getId();
	public String getName();
	public int getAttack();
	public int[] getPosition();
	public void changePosition(int x, int y);
	public int[] randomMove();
	public void attackOthers();
	public void showPosLog();
	public void loadLog(String logString);
	
	
}