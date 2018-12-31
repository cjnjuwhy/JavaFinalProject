package battleTest;

import static org.junit.Assert.*;
import org.junit.Test;

import Being.FightField;
import Being.Warrior;

public class WarriorTest {
	
	@Test
	public void lifeTest() {
		Warrior w = new Warrior("大娃", 1, "GoodMan", -1, 100, new FightField());
		
		w.changeLife(-50);
		System.out.println(w.getLife());
		assertEquals(w.getLife(), 50);
	}
	
	@Test
	public void randomMoveTest() {
		Warrior w = new Warrior("大娃", 1, "GoodMan", -1, 100, new FightField());
		w.changePosition(5, 5);
		w.randomMove();
		int [] newPos = w.getPosition();
		if(newPos[0] == 5 && newPos[1] == 5) {
			fail();
		}
	}
	
	@Test
	public void attackTest() {
		FightField fightField = new FightField();
		Warrior w1 = new Warrior("大娃", 1, "GoodMan", -50, 100, fightField);
		Warrior w2 = new Warrior("大娃", 1, "BadMan", -50, 100, fightField);
		w1.changePosition(5, 5);
		w2.changePosition(6, 6);
		fightField.field[5][5] = w1;
		fightField.field[6][6] = w2;
		w1.attackOthers();
		if(w2.getLife() == 100) {
			fail();
		}
		
	}
	
	/*
	 * 测试在run方法中是否会出现异常情况
	 */
	@Test
	public void runTest() {
		FightField fightField = new FightField();
		Warrior w1 = new Warrior("大娃", 1, "GoodMan", -50, 0, fightField);
		Warrior w2 = new Warrior("大娃", 1, "BadMan", -50, 100, fightField);
		w1.changePosition(5, 5);
		w2.changePosition(6, 6);
		fightField.field[5][5] = w1;
		fightField.field[6][6] = w2;
		w1.run();
	}

}
