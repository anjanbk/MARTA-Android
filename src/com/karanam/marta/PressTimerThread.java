package com.karanam.marta;

/**
 * Timer thread that increments count by one every second.
 * @author AKaranam
 *
 */
public class PressTimerThread implements Runnable {
	int count;

	public PressTimerThread() {
		count = 0;
	}
	
	public void run() {
		try {
			Thread.sleep(1000);
			count++;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
	
	public int getCurrentCount() {
		return count;
	}
	
}
