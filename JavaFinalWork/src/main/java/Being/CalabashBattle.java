package Being;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

/*
 * 终端版本的葫芦娃大战，同时也能够通过这个版本来完成测试过程
 */
public class CalabashBattle {
	public CalabashBattle() {
		loadWarrior(1);
	}
	public CalabashBattle(int i) {
		loadWarrior(i);
	}

	public static void main(String[] args) {
		CalabashBattle cb = new CalabashBattle();		
		
		System.out.println("=============================");
		System.out.println("====mode A        1==========");
		System.out.println("====mode B        2==========");
		System.out.println("====A: start a new battle====");
		System.out.println("====B: re-play the battle====");
		
		Scanner in = new Scanner(System.in);
		int temp = in.nextInt();
		if(temp == 1) {
			cb.modeA();
		}
		else{
			cb.modeB();
		}
		cb.showWarriorMove();
		cb.showWarriorLog();
		
		in.close();
	}
	
	/*
	 * 创建一场新的战斗，战斗结束的标志为一方全部死亡
	 */
	public void modeA() {
		fightField.showFields();
		createThreads();
		showBattle();
		saveLog();
		showWarriorLog();
	}
	
	public void modeB() {
		// load the record, and re-play the battle
		loadLog();
		
	}
	
	/*
	 * 将战斗信息保存到文件中
	 */
	public void saveLog() {
		String fileName = Information.LOG_DIR + "BsttleLog" + Information.DATE_FOR_FILENAME + ".txt";
		try {
			File logFile = new File(fileName);
			if(!logFile.exists()){
				logFile.createNewFile();
			}
			FileWriter fw = new FileWriter(logFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			bw.write(goodMan.team.get(0).posLog.size() + "6\n");
			
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
	 * 读取战斗信息
	 */
	public void loadLog() {
		String fileName = Information.FILE_TO_READ;
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader bf = new BufferedReader(file);
			String str;
			System.out.println(bf.readLine());
			
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
	 * 登陆武将
	 */
	public void loadWarrior(int i) {
		// String name, int id, String team, int attack, int life, FightField fightField
		goodMan.add(new Warrior("大娃", 1, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("二娃", 2, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("三娃", 3, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("四娃", 4, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("五娃", 5, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("六娃", 6, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("七娃", 7, "GoodMan", 50*i, 100, fightField));
		goodMan.add(new Warrior("老爷爷", 8, "GoodMan", 1*i, 100, fightField));
		
		badMan.add(new Warrior("蝎子精", 9, "BadMan", 50*i, 100, fightField));
		badMan.add(new Warrior("小兵1", 10, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("小兵2", 11, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("小兵3", 12, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("小兵4", 13, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("小兵5", 14, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("小兵6", 15, "BadMan", 20*i, 100, fightField));
		badMan.add(new Warrior("蛇精", 16, "BadMan", 50*i, 100, fightField));
		
		goodMan.goBattle(fightField.field, Formation.getForm(1, 1));
		badMan.goBattle(fightField.field, Formation.getForm(2, 2));
	}
	
	public Team goodMan = new Team("GoodMan");
	public Team badMan = new Team("BadMan");
	public FightField fightField = new FightField();
	private ExecutorService pool = Executors.newFixedThreadPool(16);
	
	
	public FightField getFightField() {
		fightField = new FightField();
		// loading the Warrior
		goodMan = new Team("GoodMan");
		badMan = new Team("BadMan");
		loadWarrior(1);
		return fightField;
	}
	
	/*
	 * 将所有的个体装入线程，然后将这些线程用线程池同一管理
	 */
	public void createThreads() {
		for(int i=0; i<8; i++) {
			pool.execute(new Thread(goodMan.team.get(i)));
			pool.execute(new Thread(badMan.team.get(i)));
		}
	}
	
	public void showBattle() {
		while(goodMan.allDead()==false && badMan.allDead()==false) {
			fightField.showFields();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
	}
	
	/*
	 * 展示每个个体的移动轨迹
	 */
	public void showWarriorMove() {
		for(Warrior w : goodMan.team) {
			System.out.println("==============");
			w.showMe();
			for(String s: w.log)
				System.out.println(s);
			}
		for(Warrior w: badMan.team) {
			System.out.println("==============");
			w.showMe();
			for(String s: w.log)
				System.out.println(s);
		}
	}
	
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


	public boolean isBattleDone() {
		return goodMan.allDead() || badMan.allDead();
	}
}

