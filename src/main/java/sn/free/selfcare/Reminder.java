package sn.free.selfcare;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Simple demo that uses java.util.Timer to schedule a task to execute once 5
 * seconds have passed.
 */

public class Reminder {
	Timer timer;
	public Reminder(int idUser,int seconds) {
		timer = new Timer();
		timer.schedule(new RemindTask(idUser), seconds*60*1000);
//		timer.schedule(new RemindTask(idUser), seconds*1000);
	}

	class RemindTask extends TimerTask {
		private int idUser;
		public RemindTask(int _idUser) {
			this.idUser=_idUser;
		}

		public void run() {
			//System.out.format("Time's up! for %d %n",this.idUser);
			System.out.format(" %d %n",this.idUser);
			timer.cancel(); // Terminate the timer thread
		}
	}

	public static void main(String args[]) {
		for (int i = 0; i <=8600; i++) {
			new Reminder(i,5);
		}
	
		System.out.format("Task scheduled.%n");
	}
}