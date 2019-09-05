
/**
 * The Class Clock.
 */
/*
 * Clock.java This class keeps track of how long the game has gone on for.
 */
class Clock {

	/** The end time. */
	private long currentTime;

	/**
	 * Clock This method creates a new clock.
	 */
	Clock() {
		updateTime();
	}

	/**
	 * updateTime This method updates the clocks time.
	 */
	void updateTime() {
		currentTime = System.nanoTime();
	}

	/**
	 * elapsedTime This method returns the amount of time that has elapsed since the
	 * clock was initialized.
	 * 
	 * @return Double, the amount of time that has elapsed since the clock was
	 *         initialized.
	 */
	double elapsedTime() {
		return (System.nanoTime() - currentTime) / 1.0E9;
	}
}
