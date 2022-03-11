package sn.free.selfcare;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Simple demo that uses java.util.Timer to schedule a task to execute once 5
 * seconds have passed.
 */

class Task implements Runnable {
	private final int rep;
	private final ScheduledExecutorService scheduler;
	private int idUser;

	public Task(int idUser,int rep, ScheduledExecutorService scheduler) {
		this.idUser =idUser ;
		this.rep = rep;
		this.scheduler = scheduler;
	}

	public void run() {
		// send an e-mail to user
		if (rep > 0) {
//			System.out.format("Sending an e-mail to user  %d !%n",idUser);
			System.out.format(" Sending sms %d %n",this.idUser);
			scheduler.schedule(new Task(idUser,rep - 1, scheduler), 1, MINUTES);
		}
			
	}
}

public class Example {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(50);

	public void sendEmails() {
		// foreach user
		for (int i = 0; i < 8600; i++) {
			scheduler.submit(new Task(i,3, scheduler));
		}
		scheduler.shutdown();
	}

	public static void main(String args[]) {
		new Example().sendEmails();
		System.out.format("Task scheduled.%n");
	}
}