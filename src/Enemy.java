
/*
 * Enemy
 * This is the enemy class that creates the enemies in-game.
 */
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;

/**
 * The Class Enemy.
 */
class Enemy {

	/** The damage of the enemy. */
	static int DAMAGE = 1;

	/** The clock for firerate */
	private final Clock clock = new Clock();

	/** The hitbox. */
	private final Rectangle hitbox;

	/** The stats of the enemy. */
	private int width, height, totalHealth, health, fireRate = 1, money;

	/** The if the enemy is facing right. */
	private boolean faceRight = false;

	/** The enemy image. */
	private BufferedImage enemyImg;

	/**
	 * Enemy This method creates an enemy using the given parameters.
	 * 
	 * @param x      The desired X-Coordinate of the enemy.
	 * @param y      The desired Y-Coordinate of the enemy.
	 * @param health The desired health of the enemy.
	 */
	Enemy(int x, int y, int health) {
		this.health = health;
		this.money = health;
		this.totalHealth = this.health;
		int enemyType = RandInt.generate(1, 3);
		if (enemyType != 1) {
			width = 43;
			height = 40;

		} else {
			width = 38;
			height = 64;
		}
		try {
			enemyImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			enemyImg = ImageIO.read(new File("./resources/e" + enemyType + ".png"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		if (enemyType == 1) {
			this.hitbox = new Rectangle(x, y, width, height - 12);
			this.height = 58;
		} else {
			this.hitbox = new Rectangle(x, y, width, height);
		}
	}

	/**
	 * getEnemyImg This method returns the image that is assigned to an enemy.
	 * 
	 * @return BufferedImage, the image that represents the enemy.
	 */
	BufferedImage getEnemyImg() {
		return enemyImg;
	}

	/**
	 * getWidth This method returns the width of an enemy.
	 * 
	 * @return Int, the width of the enemy.
	 */
	int getWidth() {
		return width;
	}

	/**
	 * getHeight This method returns the height of an enemy.
	 * 
	 * @return Int, the height of the enemy.
	 */
	int getHeight() {
		return height;
	}

	/**
	 * getX This method returns the X-Coordinate of an enemy.
	 * 
	 * @return Int, the X-Coordinate of the enemy.
	 */
	int getX() {
		return this.hitbox.x;
	}

	/**
	 * setX This method changes the X-Coordinate of the enemy.
	 * 
	 * @param x An X-Coordinate.
	 */
	void setX(int x) {
		this.hitbox.x = x;
	}

	/**
	 * getY This method returns the Y-Coordinate of an enemy.
	 * 
	 * @return Int, the Y-Coordinate of the enemy.
	 */
	int getY() {
		return this.hitbox.y;
	}

	/**
	 * setY This method changes the Y-Coordinate of an enemy.
	 * 
	 * @param y A Y-Coordinate.
	 */
	void setY(int y) {
		this.hitbox.y = y;
	}

	/**
	 * getHealth This method returns the current health of an enemy.
	 * 
	 * @return Int, the health that the enemy currently has.
	 */
	int getHealth() {
		return this.health;
	}

	/**
	 * getTotalHealth This method returns the total health of an enemy.
	 * 
	 * @return Int, the total health that the enemy has.
	 */
	int getTotalHealth() {
		return this.totalHealth;
	}

	/**
	 * loseHealth This method causes the health of an enemy to reduce by an amount
	 * equal to the damage dealt.
	 * 
	 * @param damage The damage dealt to the enemy.
	 */
	void loseHealth(int damage) {
		this.health -= damage;
	}

	/**
	 * getHitbox This method returns the hitbox of an enemy.
	 * 
	 * @return Rectangle, the hitbox of the enemy.
	 */
	Rectangle getHitbox() {
		return this.hitbox;
	}

	/**
	 * shoot This method returns whether or not an enemy can fire a projectile.
	 * 
	 * @return Boolean, true if the enemy can shoot, false if it cannot.
	 */
	boolean shoot() {
		if (clock.elapsedTime() >= fireRate) {
			clock.updateTime();
			return true;
		}
		return false;
	}

	/**
	 * faceRight This method determines whether or not the enemy should face right
	 * based on its position.
	 * 
	 * @param direction The current direction the enemy is facing.
	 */
	void faceRight(boolean direction) {
		if ((direction && !faceRight) || (!direction && faceRight)) {
			AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
			transform.translate(-enemyImg.getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			enemyImg = op.filter(enemyImg, null);
			faceRight = !faceRight;
		}
	}

	/**
	 * getMoney This method subtracts the amount of money that the enemy is holding
	 * and returns it to the player.
	 * 
	 * @return Int, the amount of money that is being sent to the player.
	 */
	int getMoney() {
		int amount = this.money;
		this.money = 0;
		return amount;
	}
}