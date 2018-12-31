package Application;

import Being.FightField;
import Being.Formation;
import Being.Information;
import Being.Team;
import Being.Warrior;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Controller {
	 
    public Team goodMan;// = new Team("GoodMan");
	public Team badMan;// = new Team("BadMan");
	public FightField fightField;// = new FightField();
	private ExecutorService pool;// = Executors.newFixedThreadPool(20);
    public Text battleLogGUI = new Text(605, 290, "");
    public Text warriorLogGUI = new Text(605,20, "");
    public Text battleResultGUI = new Text(620, 520, "Who will WIN?");
    public static int count = 0;
	public Pane pane;
    private Stage primaryStage;
    
    int totalRound;

    public void initialize() {
    	count = 0;
    	goodMan = new Team("GoodMan");
    	badMan = new Team("BadMan");
    	fightField = new FightField();
    	pool = Executors.newFixedThreadPool(20);
    }
	
	public Controller() {
		initialize();
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("信息提示对话框");
		alert.setHeaderText(null);
		alert.setContentText("space:战斗开始，s:保存战斗场面，l:复现之前的战斗");
		alert.showAndWait();
		
		DropShadow ds = new DropShadow();
	    ds.setOffsetY(3.0);
	    ds.setColor(Color.color(0.4, 0.4, 0.4));
		battleResultGUI.setEffect(ds);
		battleResultGUI.setCache(true);
		battleResultGUI.setFill(Color.RED);
		battleResultGUI.setFont(Font.font(null, FontWeight.BOLD, 24));
	}
	
	public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        keyBoardListener();
    }
	/*
	 * 在运行过程中，更新GUI上的Text信息
	 */
	public synchronized void setTextGUI() {
		String temp = "";
		for(String s : fightField.battleLog) {
			temp += s + "\n";
		}
		battleLogGUI.setText(temp);
		
		String temp1 = "";
		for(Warrior w: goodMan.team) {
			temp1 = temp1 + w + " Life: " + w.getLife() + "\n";
		}
		for(Warrior w: badMan.team) {
			temp1 = temp1 + w + " Life: " + w.getLife() + "\n";
		}
		warriorLogGUI.setText(temp1);
	}
	
	public Pane initializeGUI(){
        pane = new Pane();
        pane.setPrefSize(800, 600);
        this.setBackground();
        return pane;
    }
	
	/*
	 * 设置背景信息，以及GUI界面相关内容
	 */
	public void setBackground() {
		TextField textBox = new TextField();
        textBox.setOpacity(0);
        pane.getChildren().add(textBox);
        Image backGroundImage = new Image(Information.BACKGROUND_PATH);
        ImageView backGroundImageView = new ImageView();
        backGroundImageView.setImage(backGroundImage);
        backGroundImageView.setFitWidth(600);
        backGroundImageView.setFitHeight(600);
        backGroundImageView.setOpacity(0.8);
        pane.getChildren().add(backGroundImageView);
        
        pane.getChildren().add(battleLogGUI);
        pane.getChildren().add(warriorLogGUI);
        pane.getChildren().add(battleResultGUI);
        
	}
	
	
	public void initializeGame(){
		System.out.println("=======show===========");
		loadWarrior();
		fightField.showFields();
		mapFieldToGUI();	
    }
	
	
	public void mapFieldToGUI() {
		
		for(int i=0; i<Information.SIZE; i++) {
			for(int j=0; j<Information.SIZE; j++) {
				
				while(true) {
				try {
					if(fightField.field[i][j] == null) {
						//continue;
					}
					else if(fightField.field[i][j].isAlive()==false) {
						ImageView creatureImageView = fightField.field[i][j].getImageView();
				        creatureImageView.setOpacity(0);
					}
					else {
						ImageView creatureImageView = fightField.field[i][j].getImageView();
				         creatureImageView.setX(j * 40);
				         creatureImageView.setY(i * 40);
				         if(count == 0) {
				         pane.getChildren().add(creatureImageView);
				         }
					}
					
					break;
				}
				catch(NullPointerException e) {
					
				}
				}
			}
		}
		count ++ ;
	}
	
	/*
	 * 监听键盘信息，如果获取到相应的键盘输入，则进入相应的函数
	 * space: 进入游戏运行程序
	 * l：进入回放模式
	 * s：进入保存模式
	 */
	public void keyBoardListener(){
	       pane.setOnKeyReleased((KeyEvent ke) -> {
	               char c = ke.getText().charAt(0);
	               switch (c) {
	                   case ' ':
	                	   System.out.println("you pressed space");
	                       startGame();
	                       break;
	                   case 'l':
	                	   System.out.println("you pressed l");
	                	   replayGame();
	                       break;
	                   case 's':
	                	   System.out.println("you pressed s");
	                	   saveLog();
	                       break;
	                   default:
	                       System.out.println(c);
	               }
	       });
    }
	/*
	 * 开始战斗。先清理GUI上的内容，然后将每个个体放入线程中，使其能够正常运行
	 */
	public void startGame() {
		resetGUI();
		for(int i=0; i<8; i++) {
			pool.execute(new Thread(goodMan.team.get(i)));
			pool.execute(new Thread(badMan.team.get(i)));
		}
		pool.execute(new Thread(new AutoRun(this)));
		pool.shutdown();
	}
	/*
	 * 先读取Log中的内容，然后在GUI上模拟战斗的继续进行
	 */
	public void replayGame() {
		resetGUI();
		Thread replayThread = new Thread(new AutoReplay(this));	
		replayThread.start();
	}
	/*
	 * 保存每个个体的移动信息
	 */
	public void saveLog() {
		String fileName = Information.LOG_DIR + "BsttleLog" + Information.DATE_FOR_FILENAME + ".txt";
		int max = 0;
		for(Warrior w: goodMan.team) {
			if(max < w.posLog.size())
				max = w.posLog.size();
		}
		
		try {
			File logFile = new File(fileName);
			if(!logFile.exists()){
				logFile.createNewFile();
			}
			FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write("" + max + "\n");
			
			for(Warrior w: goodMan.team) {
				for(int[] i: w.posLog) {
					bw.write("" + i[0] + " " + i[1] + " " + i[2] + " ");
				}
				bw.write("\n");
			}
			for(Warrior w: badMan.team) {
				for(int[] i: w.posLog) {
					bw.write("" + i[0] + " " + i[1] + " " + i[2] + " ");
				}
				bw.write("\n");
			}
			bw.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * 通过团队的信息确定战斗是否已经结束
	 */
	public boolean isBattleDone() {
		return goodMan.allDead() || badMan.allDead();
	}
	/*
	 * 登陆武将，对每个人的生命值，战斗力进行设定
	 */
	public void loadWarrior() {
		// String name, int id, String team, int attack, int life, FightField fightField
		goodMan.add(new Warrior("大娃", 1, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("二娃", 2, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("三娃", 3, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("四娃", 4, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("五娃", 5, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("六娃", 6, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("七娃", 7, "GoodMan", 50, 100, fightField));
		goodMan.add(new Warrior("老爷爷", 8, "GoodMan", 0, 100, fightField));
		
		badMan.add(new Warrior("蝎子精", 9, "BadMan", 50, 100, fightField));
		badMan.add(new Warrior("小兵1", 10, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("小兵2", 11, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("小兵3", 12, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("小兵4", 13, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("小兵5", 14, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("小兵6", 15, "BadMan", 20, 100, fightField));
		badMan.add(new Warrior("蛇精", 16, "BadMan", 50, 100, fightField));
		
		goodMan.goBattle(fightField.field, Formation.getForm(1, 1));
		badMan.goBattle(fightField.field, Formation.getForm(2, 2));
	}
	/*
	 * 在终端中显示每个武将的日志，其中详细记录着每条移动记录
	 */
	public void showWarriorLog() {
		for(Warrior w : goodMan.team) {
			System.out.println("==============");
			w.showMe();
			w.showPosLog();
		}
		for(Warrior w: badMan.team) {
			System.out.println("==============");
			w.showMe();
			w.showPosLog();
		}
		
		for(String s:fightField.battleLog) {
			System.out.println(s);
		}
	}
	/*
	 * 读取保存的战斗信息
	 */
	public void loadLog() {
		// UNDO
		String fileName = "/Users/huanyu_wang/BsttleLog_1231.txt";
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader bf = new BufferedReader(file);
			String str;
			totalRound = Integer.parseInt(bf.readLine());
			
			for(Warrior w: goodMan.team) {
				str = bf.readLine();
				w.loadLog(str);
			}
			for(Warrior w: badMan.team) {
				str = bf.readLine();
				w.loadLog(str);
			}
			bf.close();
			file.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * 将GUI上的文字提示以及图片信息进行更新
	 */
	public void resetGUI() {
		battleLogGUI.setText(null);
		warriorLogGUI.setText(null);
		battleResultGUI.setText(null);
		for(Warrior w: goodMan.team) {
			w.getImageView().setOpacity(1);
		}
		for(Warrior w: badMan.team) {
			w.getImageView().setOpacity(1);
		}
	}
	
}


/*
 * 在新开的一个进程中完成GUI界面的刷新
 */
class AutoRun implements Runnable{
	AutoRun(Controller control){
		this.control = control;
	}
	
	public void run() {
		while(!control.isBattleDone()) {
			control.setTextGUI();
			control.mapFieldToGUI();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		control.setTextGUI();
		control.mapFieldToGUI();
		System.out.println("Game Over");
		control.battleResultGUI.setText("Game Over\n" + "CalaBro\n" + "Win!");
		
		control.showWarriorLog();
	}
	
	private Controller control;
}

/*
 * 在一个单独的进程中完成Re-play的显示
 */
class AutoReplay implements Runnable{
	AutoReplay(Controller control){
		this.control = control;
	}
	public void run() {
		control.loadLog();
		for(int i=0; i<control.totalRound; i++) {
			String temp = "";
			for(Warrior w: control.goodMan.team) {
				if(i>=w.posLog.size()) {
					continue;
				}
				int[] posLog = w.posLog.get(i);
				if(posLog[2]>0) {
					temp += w + " life: " + posLog[2] + "\n";
					w.getImageView().setX(posLog[1] * 40);
					w.getImageView().setY(posLog[0] * 40);
				}
				else {
					w.getImageView().setOpacity(0);
				}
			}
			for(Warrior w: control.badMan.team) {
				if(i>=w.posLog.size())
					continue;
				int[] posLog = w.posLog.get(i);
				if(posLog[2]>0) {
					temp += w + "life: " + posLog[2] + "\n";
					w.getImageView().setX(posLog[1] * 40);
					w.getImageView().setY(posLog[0] * 40);
				}
				else {
					w.getImageView().setOpacity(0);
				}
			}
			control.warriorLogGUI.setText(temp);
			
			control.battleResultGUI.setText("Game Over\n" + "CalaBro\n" + "Win!");
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private Controller control;
}
