
/*
 * Platform
 * This class creates and manages all of the platforms in the program.
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;

/**
 * The Class Platform.
 */
class Platform {

	/** The direction. */
	private int length, money = 1, direction = 1; // Direction: -1 Left, 1 Right.

	/** The move speed. */
	private double x, y, gravity, moveSpeed;

	/** The clock Y. */
	private Clock clockX, clockY;

	/** The enemy. */
	private Enemy enemy = null;

	/** The platform images. */
	private BufferedImage platHead, platBody, platButt;

	/** The whether or not it falls when a player touches it. */
	private boolean destructible = false;

	/**
	 * Platform This method creates a new platform.
	 *
	 * @param x       The desired X-Coordinate of the platform.
	 * @param y       The desired Y-Coordinate of the platform.
	 * @param length  An integer containing the desired length of the platform.
	 * @param gravity A double containing the force of gravity that is acting upon
	 *                the platform.
	 */
	Platform(int x, int y, int length, double gravity) {
		this.x = x;
		this.y = y;
		this.length = length;
		this.gravity = gravity;
		this.moveSpeed = 0;
		this.clockY = new Clock();
		if (gravity != 0 && RandInt.generate(1, 100) < 15) { // 15% chance for the platform to be a falling platform.
			destructible = true;
			// Import all the images for the falling platform.
			try {
				platHead = new BufferedImage(14, 32, BufferedImage.TYPE_INT_RGB);
				platHead = ImageIO.read(new File("./resources/rollHead.png"));
				platButt = new BufferedImage(30, 32, BufferedImage.TYPE_INT_RGB);
				platButt = ImageIO.read(new File("./resources/rollButt.png"));

				if (length <= 50) {
					platBody = new BufferedImage(50, 32, BufferedImage.TYPE_INT_RGB);
					platBody = ImageIO.read(new File("./resources/rollBody.png"));
					platBody = platBody.getSubimage(0, 0, length, 32);
				} else {
					platBody = new BufferedImage(50, 32, BufferedImage.TYPE_INT_RGB);
					platBody = ImageIO.read(new File("./resources/rollBody.png"));
					BufferedImage tempPlatBody1 = new BufferedImage(50, 32, BufferedImage.TYPE_INT_RGB);
					tempPlatBody1 = ImageIO.read(new File("./resources/rollBody.png"));
					for (int i = 0; i < (length / 50) - 1; i++) {
						platBody = joinImage(platBody, tempPlatBody1);
					}
					if (length % 50 != 0) {
						platBody = joinImage(platBody, tempPlatBody1.getSubimage(0, 0, length % 50, 32));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				// Import all the images for the platform.
				platHead = new BufferedImage(16, 24, BufferedImage.TYPE_INT_RGB);
				platHead = ImageIO.read(new File("./resources/salmonHead.png"));
				platButt = new BufferedImage(16, 24, BufferedImage.TYPE_INT_RGB);
				platButt = ImageIO.read(new File("./resources/salmonButt.png"));
				if (length <= 50) {
					platBody = new BufferedImage(50, 24, BufferedImage.TYPE_INT_RGB);
					platBody = ImageIO.read(new File("./resources/salmonBody.png"));
					platBody = platBody.getSubimage(0, 0, length, 24);
				} else {
					platBody = new BufferedImage(50, 24, BufferedImage.TYPE_INT_RGB);
					platBody = ImageIO.read(new File("./resources/salmonBody.png"));
					BufferedImage tempPlatBody1 = new BufferedImage(50, 24, BufferedImage.TYPE_INT_RGB);
					tempPlatBody1 = ImageIO.read(new File("./resources/salmonBody.png"));
					for (int i = 0; i < (length / 50) - 1; i++) {
						platBody = joinImage(platBody, tempPlatBody1);
					}
					if (length % 50 != 0) {
						platBody = joinImage(platBody, tempPlatBody1.getSubimage(0, 0, length % 50, 24));
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Platform This method creates a moving platform.
	 *
	 * @param x         The desired X-Coordinate of the moving platform.
	 * @param y         The desired Y-Coordinate of the moving platform.
	 * @param length    The desired length of the moving platform.
	 * @param gravity   The desired force of gravity that will act upon the moving
	 *                  platform.
	 * @param moveSpeed The desired movement speed of the moving platform.
	 */
	Platform(int x, int y, int length, double gravity, double moveSpeed) {
		this(x, y, length, gravity);
		this.moveSpeed = moveSpeed;
		this.clockY = new Clock();
		this.clockX = new Clock();
	}

	/**
	 * getPlatHead This method returns an image representing the head of the
	 * platform.
	 *
	 * @return BufferedImage, An image representing the head of the platform.
	 */
	BufferedImage getPlatHead() {
		return platHead;
	}

	/**
	 * getPlatBody This method returns an image representing the body of the
	 * platform.
	 *
	 * @return BufferedImage, An image representing the body of the platform.
	 */
	BufferedImage getPlatBody() {
		return platBody;
	}

	/**
	 * getPlatButt This method returns an image representing the butt of the
	 * platform.
	 *
	 * @return BufferedImage, An image representing the butt of the platform.
	 */
	BufferedImage getPlatButt() {
		return platButt;
	}

	/**
	 * getX This method returns the new X-Coordinate of a moving platform.
	 *
	 * @return Int, the new X-Coordinate of a moving platform.
	 */
	int getX() {
		if (moveSpeed != 0) {
			if (this.x + length + platButt.getWidth() >= 400) {
				direction = -1;
			} else if (this.x - platHead.getWidth() <= 0) {
				direction = 1;
			}
			this.x += moveSpeed * clockX.elapsedTime() * direction;
			clockX.updateTime();
		}
		if (enemy != null) {
			enemy.setX((int) Math.round(x) + (length - enemy.getWidth()) / 2);
		}
		return (int) Math.round(x);
	}

	/**
	 * getY This method returns the new Y-Coordinate of a platform.
	 *
	 * @return Int, the new Y-Coordinate of a moving platform.
	 */
	int getY() {
		this.y += gravity * clockY.elapsedTime();
		clockY.updateTime();
		if (enemy != null) {
			enemy.setY((int) Math.round(y) - enemy.getHeight());
		}
		return (int) Math.round(y);
	}

	/**
	 * getLength This method returns the length of the platform.
	 *
	 * @return Int, the length of the platform.
	 */
	int getLength() {
		return length;
	}

	/**
	 * getGravity This method returns the force of gravity acting upon the platform.
	 *
	 * @return Double, the force of gravity acting upon the platform.
	 */
	double getGravity() {
		return gravity;
	}

	/**
	 * setGravity This method changes the force of gravity acting upon the platform.
	 *
	 * @param g the new gravity
	 */
	void setGravity(double g) {
		this.gravity = g;
	}

	/**
	 * getMS This method returns the movement speed of the platform.
	 *
	 * @return Double, the movement speed of the platform.
	 */
	double getMS() {
		return moveSpeed * direction;
	}

	/**
	 * getDestructible This method returns whether or not the platform is a falling
	 * platform.
	 *
	 * @return Boolean, true if it is a falling platform, false if it is not a
	 *         falling platform.
	 */
	boolean getDestructible() {
		return destructible;
	}

	/**
	 * setDestructible This method changes a normal platform to a destructible one.
	 */
	void setDestructible() {
		this.destructible = !this.destructible;
	}

	/**
	 * addEnemy This method assigns a new enemy to the platform.
	 *
	 * @param health An integer containing the health of the enemy that is being
	 *               created.
	 */
	void addEnemy(int health) {
		this.enemy = new Enemy(getX(), getY(), health);
	}

	/**
	 * getEnemy This method returns the enemy that is currently residing on the
	 * platform.
	 *
	 * @return Enemy, the enemy residing on the platform.
	 */
	Enemy getEnemy() {
		return this.enemy;
	}

	/**
	 * removeEnemy This method removes the enemy that is residing on the platform.
	 */
	void removeEnemy() {
		this.enemy = null;
	}

	/**
	 * getMoney This method returns the amount of money on the platform, then
	 * changes the amount of money on the platform to 0.
	 *
	 * @return Int, the amount of money that was on the platform.
	 */
	int getMoney() {
		int amount = this.money;
		this.money = 0;
		return amount;
	}

	/**
	 * setMoney This method changes the amount of money that is on the platform.
	 *
	 * @param money An integer containing the amount of money that will be placed on
	 *              the platform.
	 */
	void setMoney(int money) {
		this.money = money;
	}

	/**
	 * joinImage This method joins two platform images together to create a longer
	 * platform image.
	 *
	 * @param img1 An image containing the first section of a platform.
	 * @param img2 An image containing the second section of a platform.
	 * @return BufferedImage, a new image that is the result of two images joining
	 *         together.
	 */
	private BufferedImage joinImage(BufferedImage img1, BufferedImage img2) {
		int width = img2.getWidth() + img1.getWidth();
		int height = img1.getHeight();
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = newImage.createGraphics();
		g2.drawImage(img1, null, 0, 0);
		g2.drawImage(img2, null, img1.getWidth(), 0);
		g2.dispose();
		return newImage;
	}
}
