
/*
 * Projectile
 * This class creates and manages all projectiles.
 */

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * The Class Projectile.
 */
class Projectile {

	/** The Constant ROTATION_RATE. */
	private static final int ENEMY_SPEED = 200, PLAYER_SPEED = 500, MAX_AMOUNT_OF_BOUNCES = 10, ROTATION_RATE = 1;

	/** The Constant WIDTH. */
	static final int WIDTH = 16;

	/** The rotation clock. */
	private Clock velocityXClock, velocityYClock, rotationClock;

	/** The hitbox. */
	private final Rectangle hitbox;

	/** The player projectile. */
	private final boolean playerProjectile;

	/** The rotation. */
	private double x, y, xVelocity, yVelocity, rotation = 0;

	/** The amount bounced. */
	private int amountBounced = 0;

	/** The shot img. */
	private BufferedImage shotImg;

	/** The gravity. */
	private int gravity = 0;

	/**
	 * Projectile This method creates a projectile.
	 *
	 * @param x                The desired X-Coordinate of the projectile.
	 * @param y                The desired Y-Coordinate of the projectile.
	 * @param x2               The X-Coordinate of the destination of the
	 *                         projectile.
	 * @param y2               The Y-Coordinate of the destination of the
	 *                         projectile.
	 * @param playerProjectile Whether or not the desired projectile is player-made.
	 */
	Projectile(int x, int y, int x2, int y2, boolean playerProjectile) {
		this.x = x;
		this.y = y;
		try {
			shotImg = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
			if (playerProjectile) {// new
				shotImg = ImageIO.read(new File("./resources/fireball2.png"));
			} else {
				shotImg = ImageIO.read(new File("./resources/fireball1.png"));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		double direction = Math.atan2((y2 - y), (x2 - x));
		this.playerProjectile = playerProjectile;
		if (playerProjectile) {
			this.xVelocity = Math.cos(direction) * PLAYER_SPEED;
			this.yVelocity = Math.sin(direction) * PLAYER_SPEED;
		} else {
			this.xVelocity = Math.cos(direction) * ENEMY_SPEED;
			this.yVelocity = Math.sin(direction) * ENEMY_SPEED;
		}

		this.velocityXClock = new Clock();
		this.velocityYClock = new Clock();
		this.rotationClock = new Clock();
		this.hitbox = new Rectangle(x - WIDTH / 2, y - WIDTH / 2, WIDTH, WIDTH);
	}

	/**
	 * getLocation This method returns a new location for a projectile.
	 *
	 * @return Point, the new location for the projectile.
	 */
	Point getLocation() {
		this.x += xVelocity * velocityXClock.elapsedTime();
		velocityXClock.updateTime();
		this.y += (yVelocity + gravity) * velocityYClock.elapsedTime();
		velocityYClock.updateTime();
		this.hitbox.x = (int) Math.round(this.x);
		this.hitbox.y = (int) Math.round(this.y);

		return new Point((int) Math.round(this.x), (int) Math.round(this.y));
	}

	/**
	 * getHitbox This method returns the hitbox of a projectile.
	 *
	 * @return Rectangle, the hitbox of the projectile.
	 */
	Rectangle getHitbox() {
		return hitbox;
	}

	/**
	 * getImage This method returns the image that is assigned to a projectile.
	 *
	 * @return BufferedImage, the image that is assigned to the projectile.
	 */
	BufferedImage getImage() {
		return shotImg;
	}

	/**
	 * getRotation This method returns the amount that a projectile should be
	 * rotated by.
	 *
	 * @return Double, the amount the the projectile needs to be rotated.
	 */
	double getRotation() {
		if (rotation > Math.PI * 2) {
			rotation = 0;
		}
		rotation += rotationClock.elapsedTime() * (Math.PI * 2 * ROTATION_RATE);
		rotationClock.updateTime();
		return rotation;
	}

	/**
	 * switchX This method determines whether or not a projectile will bounce.
	 *
	 * @return Boolean, true if the projectile will bounce, false if it will not.
	 */
	boolean switchX() {
		if (amountBounced < MAX_AMOUNT_OF_BOUNCES) {
			amountBounced++;
			this.xVelocity = -this.xVelocity;
			if (this.x < 0) {
				this.x = 0;
			} else if (this.x > 400) {
				this.x = 400;
			}
			return true;
		}
		return false;
	}

	/**
	 * Sets the gravity.
	 *
	 * @param gravity the new gravity
	 */
	void setGravity(int gravity) {
		this.gravity = gravity;
	}

	/**
	 * isPlayerProjectile This method returns true if the projectile is a
	 * player-made projectile, and false if it is not.
	 *
	 * @return Boolean, true if the projectile is a player-made projectile, false if
	 *         it is not.
	 */
	boolean isPlayerProjectile() {
		return this.playerProjectile;
	}
}
