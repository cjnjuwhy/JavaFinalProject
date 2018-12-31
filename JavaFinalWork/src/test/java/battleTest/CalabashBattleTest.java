package battleTest;

import static org.junit.Assert.*;
import org.junit.Test;

import Being.CalabashBattle;
import Being.*;

public class CalabashBattleTest {
	
	@Test
	public void loadLogTest() {
		
		CalabashBattle cb = new CalabashBattle(-1);
		cb.loadLog();
		
		for(Warrior w: cb.goodMan.team) {
			if(w.getPosition()[0] <0 || w.getPosition()[1]<0)
				fail();
		}
		for(Warrior w: cb.badMan.team) {
			if(w.getPosition()[0] <0 || w.getPosition()[1]<0)
				fail();
		}
	}
	
	@Test
	public void loadWarriorTest() {
		CalabashBattle cb = new CalabashBattle(-1);
		cb.showWarriorLog();
	}
	
	@Test
	public void isBattleDoneTest() {
		CalabashBattle cb = new CalabashBattle(-1);
		for(Warrior w: cb.badMan.team) {
			w.changeLife(-100);
		}
		if(!cb.isBattleDone())
			fail();
		
	}

}
