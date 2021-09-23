
/*
 * Player
 * This class creates and manages the in-game character for the user.
 */

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Class Player.
 */
class Player {

	/** The shot clock. */
	private final Clock clockX, clockY, shotClock;

	/** The Constant PLAYER_HEIGHT. */
	static final int PLAYER_WIDTH = 40, PLAYER_HEIGHT = 40;

	/** The hitbox. */
	private final Rectangle hitbox;

	/** The move speed. */
	private int mouseX, mouseY, health, totalHealth, moving, moveSpeed;

	/** The money. */
	private int unlockedMSLv, damage, unlockedGRLv, fireRate, gravity, money;

	/** The jump speed. */
	private double x, y, xVelocity, yVelocity, platMS, jumpSpeed = 1500;

	/** The is shooting. */
	private boolean left = false, right = false, jumping = false, isShooting = false;

	/** The platform the player is on top of. */
	private Platform onPlat = null;

	/**
	 * Player This method creates a player.
	 *
	 * @param x The desired X-Coordinate for the player.
	 * @param y The desired Y-Coordinate for the player.
	 */
	Player(int x, int y) {
		importData();
		this.moving = 0;
		this.yVelocity = 0;
		this.xVelocity = 0;
		this.platMS = 0;
		this.x = x;
		this.y = y;
		hitbox = new Rectangle(x, y, PLAYER_WIDTH, PLAYER_HEIGHT);
		clockX = new Clock();
		clockY = new Clock();
		shotClock = new Clock();
	}

	/**
	 * keyPressed This method performs an action based on the key that is pressed.
	 * If A is pressed, the player moves left. If D is pressed, the player moves
	 * right. If W is pressed, the player jumps upwards.
	 *
	 * @param key The key that was pressed by the user.
	 */
	void keyPressed(String key) {
		if (key.equals("A") && !left) {
			this.moving = -1;
			this.left = true;
			clockX.updateTime();
		} else if (key.equals("D") && !right) {
			this.moving = 1;
			this.right = true;
			clockX.updateTime();
		} else if (key.equals("W")||key.equals("Space")) {
			jumping = true;
		}
	}

	/**
	 * keyReleased This method performs an action based on the key that is released
	 * after being held down. If A is released, the player stops moving right. If D
	 * is released, the player stops moving left. If W is released, the player stops
	 * jumping.
	 *
	 * @param key The key that was released after being held down by the user.
	 */
	void keyReleased(String key) {
		switch (key) {
		case "A":
			if (right) {
				this.moving = 1;
			} else {
				this.moving = 0;
			}
			clockX.updateTime();
			this.left = false;
			break;
		case "D":
			if (left) {
				this.moving = -1;
			} else {
				this.moving = 0;
			}
			clockX.updateTime();
			this.right = false;
			break;
		case "Space":
		case "W":
			jumping = false;
			break;
		}
	}

	/**
	 * mousePressed This method allows the user to start shooting based on where the
	 * mouse is pressed.
	 *
	 * @param mouseX The X-Coordinate of the mouse when it is pressed.
	 * @param mouseY The Y-Coordinated of the mouse when it is pressed.
	 */
	void mousePressed(int mouseX, int mouseY) {
		isShooting = true;
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	/**
	 * mouseReleased This method stops the player from shooting once the mouse is
	 * released after previously being held down.
	 */
	void mouseReleased() {
		isShooting = false;
	}

	/**
	 * mouseDragged This method tracks the coordinates of the mouse when it is
	 * dragged across the screen.
	 *
	 * @param mouseX The X-Coordinate of the mouse after it is dragged.
	 * @param mouseY The Y-Coordinate of the mouse after it is dragged.
	 */
	void mouseDragged(int mouseX, int mouseY) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
	}

	/**
	 * checkLocation This method checks to see if the player is touching a platform
	 * and changes the velocity of the player accordingly.
	 *
	 * @param platforms An ArrayList of rectangles that contains all of the
	 *                  platforms visible on the screen.
	 */
	private void checkLocation(ArrayList<Platform> platforms) {
		this.yVelocity -= gravity * clockY.elapsedTime();
		if (this.moving == 1) {
			this.xVelocity = moveSpeed;
		} else if (this.moving == -1) {
			this.xVelocity = -moveSpeed;
		} else {
			this.xVelocity = 0;
		}
		xVelocity += platMS;
		if ((this.x >= 360 && xVelocity > 0) || (this.x < 0 && xVelocity < 0)) {
			this.xVelocity = 0;
		}

		this.x += (xVelocity) * clockX.elapsedTime();
		clockX.updateTime();

		double newY = this.y - yVelocity * clockY.elapsedTime();

		checkCollision(platforms, newY);
		clockY.updateTime();
	}

	/**
	 * getLocation This method changes the position of the player in relation to the
	 * platforms on-screen.
	 *
	 * @param platforms An ArrayList of rectangles that contains all of the
	 *                  platforms visible on the screen.
	 * @return Point, the new location of the player on the screen.
	 */
	Point getLocation(ArrayList<Platform> platforms) {
		jump();
		checkLocation(platforms);
		if (y >= 800) {
			health = 0;
		}
		hitbox.x = (int) Math.round(this.x);
		hitbox.y = (int) Math.round(this.y);
		return new Point((int) Math.round(this.x), (int) Math.round(this.y));
	}

	/**
	 * getX This method returns the X-Coordinate of the player.
	 *
	 * @return Int, the X-Coordinate of the player.
	 */
	int getX() {
		return (int) Math.round(x);
	}

	/**
	 * setX This method sets the X-Coordinate of the player.
	 *
	 * @param x The new x value of the player.
	 */
	void setX(int x) {
		this.x = x;
	}

	/**
	 * getY This method returns the Y-Coordinate of the player.
	 *
	 * @return Int, the Y-Coordinate of the player.
	 */
	int getY() {
		return (int) Math.round(y);
	}

	/**
	 * jump This method makes the player jump upwards into the air.
	 */
	private void jump() {
		if (jumping && yVelocity == 0) {
			yVelocity = jumpSpeed;
			clockY.updateTime();
		}
	}

	/**
	 * shot This method allows the player to fire a projectile if the players fire
	 * rate allows it.
	 *
	 * @return Boolean, true if the player can fire a shot, false if the player
	 *         cannot.
	 */
	boolean shot() {
		if (isShooting && shotClock.elapsedTime() > 1 / (double) fireRate) {
			shotClock.updateTime();
			return true;
		}
		return false;
	}

	/**
	 * checkCollision This method checks to see if the player is colliding with any
	 * platforms.
	 *
	 * @param platforms An ArrayList of rectangles that contains all of the
	 *                  platforms visible on the screen.
	 * @param newY      An integer that contains the new Y-Coordinate of the player.
	 */
	private void checkCollision(ArrayList<Platform> platforms, double newY) {
		boolean landed = false;
		if (onPlat == null) {
			for (Platform platform : platforms) {
				if (this.x + PLAYER_WIDTH >= platform.getX() && this.x <= platform.getX() + platform.getLength()) {
					if (newY + PLAYER_HEIGHT >= platform.getY() && y + PLAYER_HEIGHT <= platform.getY()) {
						onPlat = platform;
						addMoney(onPlat.getMoney());
						landed = true;
						if (platform.getDestructible()) {
							platform.setGravity(platform.getGravity() + 500);
							platform.setDestructible();
						}
					}
				}
			}
		}
		if (onPlat != null) {
			landed = true;
			if (yVelocity <= 0) {
				this.yVelocity = 0;
				this.y = onPlat.getY() - PLAYER_HEIGHT;
				this.platMS = onPlat.getMS();
			}
			clockY.updateTime();
		}
		if (!landed
				|| (onPlat != null
						&& !(this.x + PLAYER_WIDTH >= onPlat.getX() && this.x <= onPlat.getX() + onPlat.getLength()))
				|| yVelocity > 0) {
			onPlat = null;
			this.y = newY;
			this.platMS = 0;
		}
	}

	/**
	 * importData This method imports the player data from a text file.
	 */
	void importData() {
		Scanner inFile = null;
		try {
			inFile = new Scanner(new File("./resources/gameSave.txt"));

		} catch (FileNotFoundException ignored) {

		}
		assert inFile != null;
		this.health = Integer.parseInt(inFile.nextLine());
		this.totalHealth = this.health;
		this.moveSpeed = Integer.parseInt(inFile.nextLine());
		this.unlockedMSLv = Integer.parseInt(inFile.nextLine());
		this.gravity = Integer.parseInt(inFile.nextLine());
		this.unlockedGRLv = Integer.parseInt(inFile.nextLine());
		this.damage = Integer.parseInt(inFile.nextLine());
		this.fireRate = Integer.parseInt(inFile.nextLine());
		this.money = Integer.parseInt(inFile.nextLine());
		inFile.close();
	}

	/**
	 * exportData This method exports data to a text file.
	 */
	void exportData() {
		PrintWriter outFile = null;
		try {
			outFile = new PrintWriter(new File("./resources/gameSave.txt"));

		} catch (FileNotFoundException ignored) {
		}

		assert outFile != null;
		outFile.println(this.health);
		outFile.println(this.moveSpeed);
		outFile.println(this.unlockedMSLv);
		outFile.println(this.gravity);
		outFile.println(this.unlockedGRLv);
		outFile.println(this.damage);
		outFile.println(this.fireRate);
		outFile.println(this.money);
		outFile.close();
	}

	/**
	 * getHealth This method returns the health of the player.
	 *
	 * @return Int, the health of the player.
	 */
	int getHealth() {
		return this.health;
	}

	/**
	 * setHealth This method changes the health of the player.
	 *
	 * @param health An integer containing the new health of the player.
	 */
	void setHealth(int health) {
		this.health = health;
	}

	/**
	 * getTotalHealth This method returns the totalHealth of the player.
	 *
	 * @return Int, the total health of the player.
	 */
	int getTotalHealth() {
		return totalHealth;
	}

	/**
	 * setTotalHealth This method changes the total health of the player.
	 *
	 * @param totalHealth An integer containing the new total health of the player
	 */
	void setTotalHealth(int totalHealth) {
		this.totalHealth = totalHealth;
	}

	/**
	 * getMoveSpeed This method returns the movespeed of the player.
	 *
	 * @return Int, the movespeed of the player.
	 */
	int getMoveSpeed() {
		return moveSpeed;
	}

	/**
	 * setMoveSpeed This method changes the movespeed of the player.
	 *
	 * @param moveSpeed An integer containing the new movespeed of the player.
	 */
	void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	/**
	 * getDamage This method returns the damage that the player deals per
	 * projectile.
	 *
	 * @return Int, the damage that the player deals per projectile.
	 */
	int getDamage() {
		return damage;
	}

	/**
	 * setDamage This method changes the damage that the player deals per
	 * projectile.
	 *
	 * @param damage An integer containing the new amount of damage the player will
	 *               deal per projectile.
	 */
	void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * getFireRate This method returns the rate of fire of the player.
	 *
	 * @return Int, the rate of fire of the player.
	 */
	int getFireRate() {
		return fireRate;
	}

	/**
	 * setFireRate This method changes the rate of fire of the player.
	 *
	 * @param fireRate An integer containing the new rate of fire of the player.
	 */
	void setFireRate(int fireRate) {
		this.fireRate = fireRate;
	}

	/**
	 * getGravity This method returns the force of gravity acting upon the player.
	 *
	 * @return Int, the force of gravity acting upon the player.
	 */
	int getGravity() {
		return gravity;
	}

	/**
	 * setGravity This method changes the force of gravity acting upon the player.
	 *
	 * @param gravity An integer containing the new force of gravity that will act
	 *                upon the player.
	 */
	void setGravity(int gravity) {
		this.gravity = gravity;
	}

	/**
	 * getMoney This method returns the amount of money that the player has.
	 *
	 * @return Int, the amount of money that the player has.
	 */
	int getMoney() {
		return this.money;
	}

	/**
	 * addMoney This method adds to the amount of money that the player has.
	 *
	 * @param adding An integer containing the amount that the players money will
	 *               increase by.
	 */
	void addMoney(int adding) {
		this.money += adding;
	}

	/**
	 * getUnlockedMSLv This method returns the level of movespeed that has been
	 * enhanced by shop upgrades.
	 *
	 * @return Int, the level of movespeed that has been enhanced by shop upgrades.
	 */
	int getUnlockedMSLv() {
		return unlockedMSLv;
	}

	/**
	 * setUnlockedMSLv This method changes the level of enhanced movespeed that is
	 * in use.
	 *
	 * @param unlockedMSLv An integer containing the desired level of enhanced
	 *                     movespeed.
	 */
	void setUnlockedMSLv(int unlockedMSLv) {
		this.unlockedMSLv = unlockedMSLv;
	}

	/**
	 * getUnlockedGRLv This method returns the level of gravity that has been
	 * weakened by shop upgrades.
	 *
	 * @return Int, the level of gravity that has been weakened by shop upgrades.
	 */
	int getUnlockedGRLv() {
		return unlockedGRLv;
	}

	/**
	 * setUnlockedGRLv This metho changes the level of weakened gravity that is in
	 * use.
	 *
	 * @param unlockedGRLv An integer containing the desired level of weakened
	 *                     gravity.
	 */
	void setUnlockedGRLv(int unlockedGRLv) {
		this.unlockedGRLv = unlockedGRLv;
	}

	/**
	 * getMouseX This method returns the X-Coordinate of the mouse.
	 *
	 * @return Int, the X-Coordinate of the mouse.
	 */
	int getMouseX() {
		return mouseX;
	}

	/**
	 * getMouseY This method returns the Y-Coordinate of the mouse.
	 *
	 * @return Int, the Y-Coordinate of the mouse.
	 */
	int getMouseY() {
		return mouseY;
	}

	/**
	 * getHitbox This method returns the hitbox of the player.
	 *
	 * @return Rectangle, the hitbox of the player.
	 */
	Rectangle getHitbox() {
		return this.hitbox;
	}

	/**
	 * buy This method decreases the amount of money that the player has based on
	 * the cost of what they purchased.
	 *
	 * @param cost An integer containing the amount of money that the player is
	 *             spending.
	 * @return Boolean, true if the purchase was successful, false if it was
	 *         unsuccessful.
	 */
	boolean buy(int cost) {
		if (cost > money) {
			return false;
		}
		money -= cost;
		return true;
	}
}
