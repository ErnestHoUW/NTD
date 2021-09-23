/*
 * GamePanel
 * This class draws all objects' pictures on to the screen.
 */

//imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.SpringLayout;

/**
 * The Class GamePanel.
 */
//GamePanel is a subclass of JPanel
public class ePanel extends JPanel {

	/** The health bar length. */
	private static int HEALTH_BAR_LENGTH = 40;// Constant for the size in px of the healthbars in the game

	/** The upgrade. */
	private UpgradePanel upgrade;// Another JPanel within gamePanel for the upgrade shop

	/** The player. */
	private Player player;// A Player object that interacts with the game

	/** The starting cash. */
	private int startingCash = 0;

	/** The starting cash Y. */
	private int startingCashY = 0;

	/** The platforms. */
	private ArrayList<Platform> platforms = new ArrayList<Platform>();// an Arraylist of Platforms that stores all the
																		// platforms in the game

	/** The shots. */
	private ArrayList<Projectile> shots = new ArrayList<Projectile>();// an Arraylist of Platforms that stores all projectiles on the screen

	/** The plat waves. */
	private int menu, platWaves;
	// Menu: 0 start screen, 1 playing

	/** The play sign. */
	// platWaves is the amount of platform spawns that have occured;
	private BufferedImage playerImg, homeSign, shopSign, playSign;// Buffered images for the player and the signs in the

	/** The back img. */
	// game;
	private Background backImg;// A Background object used to get the scrolling background image

	/**
	 * GamePanel This constructor creates a new GamePanel that runs the game.
	 */
	public ePanel() {
		start(new Player(180, 0), false);// start the game falling from the middle top of the screen without shop
		// setDoubleBuffered(true);// double buffering prevents image shuttering
		// Initializing all the buffered Images;
		playerImg = new BufferedImage(Player.PLAYER_WIDTH, Player.PLAYER_HEIGHT, BufferedImage.TYPE_INT_RGB);
		homeSign = new BufferedImage(68, 96, BufferedImage.TYPE_INT_RGB);
		shopSign = new BufferedImage(68, 96, BufferedImage.TYPE_INT_RGB);
		playSign = new BufferedImage(68, 96, BufferedImage.TYPE_INT_RGB);
		try {
			playerImg = ImageIO.read(new File("./resources/ernest.png"));
			homeSign = ImageIO.read(new File("./resources/home_sign.png"));
			shopSign = ImageIO.read(new File("./resources/shop_sign.png"));
			playSign = ImageIO.read(new File("./resources/play_sign.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * paintComponent This method changes the positions of all objects in the
	 * program and repaints the window to show visual changes.
	 *
	 * @param g A Graphics component that is used to create the graphics.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		Point p;// a point object used to get and store coordinates for the player and
				// projectile
		g2d.drawImage(backImg.getImage(), null, 0, 0);// draw the background image
		if (menu == 0) {// if on the start screen
			if (upgrade.isVisible()) {// if the shop is visible
				upgrade.paintChildren(g);// paint the shop under everything but the background
				// draw the start of the startingPlatform
				g2d.drawImage(platforms.get(0).getPlatBody(), null,
						platforms.get(0).getX() + platforms.get(0).getPlatHead().getWidth(), platforms.get(0).getY());
				g2d.drawImage(platforms.get(0).getPlatHead(), null, platforms.get(0).getX(), platforms.get(0).getY());
			} else {
				// draw the end of the platform
				g2d.drawImage(platforms.get(0).getPlatBody(), null, platforms.get(0).getX(), platforms.get(0).getY());
				g2d.drawImage(platforms.get(0).getPlatButt(), null,
						platforms.get(0).getX() + platforms.get(0).getLength(), platforms.get(0).getY());
			}
			p = player.getLocation(platforms);// get the player location and save it in p
			if (p.x >= 360 && !upgrade.isVisible()) {// if on the start screen and going right
				menu = 1;// start the game
				platforms.get(0).setGravity(9);// set the starting platform to fall
				backImg.startScroll();// start the background scrolling
				startingCash = player.getMoney();
			} else if (p.x >= 360 && upgrade.isVisible()) {// if walking right shop
				start(player, false);
				;// set set shop to invisible
				player.setX(1);// move the player to the other side of the screen
				player.importData();
			} else if (p.x <= 0 && !upgrade.isVisible()) {// if walking left to the from starting screen
				start(player, true);
				player.setX(359);// move the player to the other side of the screen
			}
		} else if (menu == 1) {// if the game has started
			// if the player has no more health
			if (player.getHealth() <= 0) {
				player.setHealth(player.getTotalHealth());// give them their health again
				player.exportData();// save their data
				start(new Player(player.getX(), 0), false);// restart the game from where they were;
			} else {
				platformGen();// start the generation of platforms

				// checking and drawing platforms
				for (int i = 0; i < platforms.size(); i++) {// go through all the platforms and enemies
					Platform currentPlatform = platforms.get(i);// save the reference for the current platform that is
																// being
																// checked
					if (currentPlatform.getY() > 870) {// if the platform too far past the bottom of the screen
						platforms.remove(i);// remove the platform
					} else {
						// draw the platform head middle and end
						g2d.drawImage(currentPlatform.getPlatHead(), null,
								currentPlatform.getX() - currentPlatform.getPlatHead().getWidth(),
								currentPlatform.getY());
						g2d.drawImage(currentPlatform.getPlatBody(), null, currentPlatform.getX(),
								currentPlatform.getY());
						g2d.drawImage(currentPlatform.getPlatButt(), null,
								currentPlatform.getX() + currentPlatform.getLength(), currentPlatform.getY());
						if (currentPlatform.getEnemy() != null) {// if the platform has a enemy
							// draw the its healthBar;
							g.drawRect(
									currentPlatform.getEnemy().getX()
											+ (HEALTH_BAR_LENGTH - currentPlatform.getEnemy().getWidth()) / 2,
									currentPlatform.getEnemy().getY() - 10, HEALTH_BAR_LENGTH, 5);
							g.setColor(Color.GREEN);
							g.fillRect(
									currentPlatform.getEnemy().getX()
											+ (HEALTH_BAR_LENGTH - currentPlatform.getEnemy().getWidth()) / 2 + 1,
									currentPlatform.getEnemy().getY() - 9,
									(int) (HEALTH_BAR_LENGTH * ((double) currentPlatform.getEnemy().getHealth()
											/ (double) currentPlatform.getEnemy().getTotalHealth())) - 1,
									4);
							g.setColor(Color.BLACK);
							// change the enemies facing direction based on player location;
							if (currentPlatform.getEnemy().getX() < player.getX()) {
								currentPlatform.getEnemy().faceRight(true);
							} else {
								currentPlatform.getEnemy().faceRight(false);
							}
							// draw the enemy
							g2d.drawImage(currentPlatform.getEnemy().getEnemyImg(), null,
									currentPlatform.getEnemy().getX(), currentPlatform.getEnemy().getY());
							// if the enemy is shooting
							if (currentPlatform.getEnemy().shoot()) {
								// create a new projectile aiming at the player
								// shots.add(new Projectile(1,10,0,10,false));
								player.getLocation(platforms);
								shots.add(new Projectile(
										currentPlatform.getEnemy().getX() + currentPlatform.getEnemy().getWidth() / 2,
										currentPlatform.getEnemy().getY() + currentPlatform.getEnemy().getHeight() / 2,
										player.getX() + Player.PLAYER_WIDTH / 2,
										player.getY() + Player.PLAYER_HEIGHT / 2, false));
							}
							if (currentPlatform.getEnemy().getHitbox().intersects(player.getHitbox())) {//if the player touches the enemy
								player.setHealth(player.getHealth()-3);
								currentPlatform.removeEnemy();
							}
						}
					}
				}
			} // end of checking and drawing platforms
			NumberFormat formatter = new DecimalFormat("0000");
			g2d.setFont(new Font("Bauhaus 93", Font.PLAIN, 50));
			g2d.setColor(new Color(255, 255, 255, 200));

			if (platforms.get(0).getLength() == 400 - 16 && platforms.get(0).getY() <= 775) {
				startingCashY = backImg.getY();
			}
			g2d.drawString(formatter.format(player.getMoney() - startingCash), 150, startingCashY);
			g.setColor(Color.BLACK);
		}
		// drawing the signs
		if (platforms.get(0).getLength() == 400 - 16) {// if the starting platform still exists
			if (!upgrade.isVisible()) {// if at the startscreen
				// draw the shop sign
				g2d.drawImage(shopSign, null, platforms.get(0).getX() + 16,
						platforms.get(0).getY() - shopSign.getHeight());
				// draw the playSign
				g2d.drawImage(playSign, null,
						platforms.get(0).getX() + platforms.get(0).getLength() - playSign.getWidth(),
						platforms.get(0).getY() - playSign.getHeight());
			} else {// at the shop
				// draw the home sign
				g2d.drawImage(homeSign, null,
						platforms.get(0).getX() + platforms.get(0).getLength() - homeSign.getWidth(),
						platforms.get(0).getY() - homeSign.getHeight());
			}
		}
		// checking and drawing projectiles
		for (int i = 0; i < shots.size(); i++) {// goes through all the projectiles
			boolean removed = false;// a flag for is the projectile has been removed or not
			p = shots.get(i).getLocation();// get the location of the current shot
			if (!removed && (p.y > 800 || p.y < 0 || p.x < 0 || p.x > 400)) {// if the projectile touches the borders of
																				// the screen
				if ((p.x < 0 || p.x > 400) && !shots.get(i).switchX()) {// if the projectile touches the left or right
																		// side of the screen and they cant bounce
					shots.remove(i);// remove the projectile
					removed = true;// flag is raised
				}
			}
			// if the projectile was fired by a player
			if (!removed && shots.get(i).isPlayerProjectile()) {
				// check all platforms for enemies
				for (int j = 0; j < platforms.size(); j++) {
					if (!removed) {
						if (platforms.get(j).getEnemy() != null) {// if the platform has enemy
							if (shots.get(i).getHitbox().intersects(platforms.get(j).getEnemy().getHitbox())) {// if the
																												// projectile
																												// hits
																												// the
																												// enemy
								platforms.get(j).getEnemy().loseHealth(player.getDamage());// the enemy takes damage
								if (platforms.get(j).getEnemy().getHealth() <= 0) {// if the enemy has no more health
									player.addMoney(platforms.get(j).getEnemy().getMoney());// the player gets the
																							// enemies money
									platforms.get(j).removeEnemy();// remove the enemy from the platform
								}
								shots.remove(i);// remove the projectile
								removed = true;// raise the flag
							}
						}
					}
				}
			}
			// if the projectile was fired by a enemy
			if (!removed && !shots.get(i).isPlayerProjectile()) {
				if (shots.get(i).getHitbox().intersects(player.getHitbox())) {// if the projectile hits the player
					player.setHealth(player.getHealth() - Enemy.DAMAGE);// the player takes damage;
					shots.remove(i);// remove the projectile
					removed = true;// raise the flag
				} else {
					shots.get(i).setGravity(platWaves * 5 + 100);
				}
			}
			if (!removed) {// if the projectile still exist
				// create a new object to rotate the projectile
				AffineTransform transformation = new AffineTransform();
				transformation.translate(p.x, p.y);// set the drawing origin to the fireball location
				transformation.rotate(shots.get(i).getRotation());// rotate the fireball
				transformation.translate(-Projectile.WIDTH / 2, -Projectile.WIDTH / 2);// center the fireball
				g2d.drawImage(shots.get(i).getImage(), transformation, this);// draw the fireball with the
																				// transformations
			}
		}

		p = player.getLocation(platforms);// save the player location to p
		if (player.shot()) {// if the player has shot
			// create a new projectile aiming from the player to the mouse;
			shots.add(new Projectile(p.x + Player.PLAYER_WIDTH / 2, p.y + Player.PLAYER_HEIGHT / 2, player.getMouseX(),
					player.getMouseY(), true));
		}

		// draw the player health bare
		g.drawRect(p.x, p.y - 10, HEALTH_BAR_LENGTH, 5);
		g.setColor(Color.GREEN);
		g.fillRect(p.x + 1, p.y - 9,
				(int) (HEALTH_BAR_LENGTH * ((double) player.getHealth() / (double) player.getTotalHealth())) - 1, 4);
		g.setColor(Color.BLACK);
		// draw the player
		g2d.drawImage(playerImg, null, p.x, p.y);

		// draw the player's money;
		g2d.setFont(new Font("Bauhaus 93", Font.PLAIN, 26));
		g2d.setColor(new Color(255, 255, 255, 200));
		g2d.drawString(" $" + Integer.toString(player.getMoney()), 0, 795);
		g.setColor(Color.BLACK);

		// draw the player's score;

	}

	/**
	 * getPlayer This method returns the player.
	 *
	 * @return Player, the player.d
	 */
	public Player getPlayer() {
		return this.player;
	}

	/**
	 * platformGen This method generates all of the platforms that are visible in
	 * the game window.
	 */
	public void platformGen() {
		if (platforms.get(platforms.size() - 1).getY() >= 150) {// if the last platform has moved 150 px
			platWaves++;// change the amount of waves spawned by one
			int gravity = platWaves * 5 + 100;// the gravity of the platforms are defined by platwaves for game scaling
			int amount = RandInt.generate(0, 2);// amount of platforms in a single wave
			int y = -40;// initial y value;
			if (amount == 2) {// if spawning 2
				int length = RandInt.generate(40, 100);// get length of the platform
				int x = RandInt.generate(16, 200 - (length + 16));// get the platforms x within the first half of the
																	// screen
				platforms.add(new Platform(x, y, length, gravity));// create the platform
				length = RandInt.generate(40, 100);// get another length for the platform
				x = RandInt.generate(284, 400 - (length + 16));// get the plaform's x winthin the second half of the
																// screen
				platforms.add(new Platform(x, y, length, gravity));// create the platform
			} else {
				int length = RandInt.generate(40, 250);// get the length of the platform
				int x = RandInt.generate(16, 400 - (length + 16));// get the x for the platform
				int movingPlatform = RandInt.generate(1, 2);// 50% chance that it is moving
				if (movingPlatform == 1) {// if it is moving
					platforms.add(new Platform(x, y, length, gravity, platWaves * 5 + 50));// create a moving platform
				} else {
					platforms.add(new Platform(x, y, length, gravity));// create a normal platform
				}
			}
			if (platWaves % 3 == 0) {// every 3 waves
				if (amount == 2) {// if amount added was 2
					// randomly select one of the platforms spawned to have a enemy
					platforms.get(platforms.size() - RandInt.generate(1, 2)).addEnemy(platWaves / 6 + 2);
				} else {
					// add a enemy to the last platform
					platforms.get(platforms.size() - 1).addEnemy(platWaves / 6 + 2);
				}
			}
		}
	}

	/**
	 * start This method starts and restarts the game.
	 *
	 * @param player      the player
	 * @param shopVisible whether or not the the shop is visible on restart.
	 */
	public void start(Player player, boolean shopVisible) {
		this.player = player;// create a new player
		this.startingCash = 0;
		this.backImg = new Background();// create a new background
		if (platforms.size() != 1) {// remove all other platforms
			platforms.clear();
			this.platforms.add(new Platform(0, 750, 400 - 16, 0));// add the starting platform
			this.platforms.get(0).setMoney(0);// set the starting platform money to 0;
		}
		this.shots.clear();
		this.menu = 0;// 0 start screen 1 playing
		this.platWaves = 0;// reset the wave counter
		if (upgrade != null) {
			this.remove(upgrade);// remove upgrade
		}
		// place upgrade panel
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		upgrade = new UpgradePanel(this);
		springLayout.putConstraint(SpringLayout.NORTH, upgrade, 390, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, upgrade, 0, SpringLayout.WEST, this);
		springLayout.putConstraint(SpringLayout.SOUTH, upgrade, 690, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, upgrade, 0, SpringLayout.EAST, this);
		this.add(upgrade);
		upgrade.setVisible(shopVisible);
	}
}
