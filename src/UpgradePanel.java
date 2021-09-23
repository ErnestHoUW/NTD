
/*
 * GamePanel
 * This class draws all objects' pictures on to the screen.
 */

import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.SpringLayout;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

import javax.swing.JSlider;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

/**
 * The Class UpgradePanel.
 */
class UpgradePanel extends JPanel {

	/** The player. */
	private final Player player;// Reference to the player

	/** The playfield. */
	private final ePanel playfield;// Reference to the main game panel

	/** The prices. */
	private final int[] prices = { 10, 20, 40, 80, 160, 250, 320, 450, 640 };// prices per upgrade

	/** The health upgrade amount. */
	// the increments by which each stat is increased by
	private final int HEALTH_UPGRADE_AMOUNT = 1;

	/** The damage upgrade amount. */
	private final int DAMAGE_UPGRADE_AMOUNT = 1;

	/** The firerate upgrade amount. */
	private final int FIRERATE_UPGRADE_AMOUNT = 5;

	/** The movespeed upgrade amount. */
	private final int MOVESPEED_UPGRADE_AMOUNT = 100;

	/** The gravity upgrade amount. */
	private final int GRAVITY_UPGRADE_AMOUNT = -250;

	/** The total money. */
	// the initial amount of money before anything was bought
	private final int totalMoney;

	/** The total cost label. */
	private final JLabel lblTotalCost;// the label for the total cost

	/** The shop back. */
	private BufferedImage shopBack;// the image of the shops background

	/**
	 * UpgradePanel This constructor creates a new UgradePanel that contains the
	 * games shop.
	 *
	 * @param playfield the playfield
	 */
	UpgradePanel(ePanel playfield) {
		this.player = playfield.getPlayer();// pass player inside the this class
		this.totalMoney = player.getMoney();// save the initial amount of money
		this.playfield = playfield;// pass the game panel
		setVisible(false);// set the panel to not be visible
		setBackground(new Color(0, 0, 0, 0));// set the background to transparent
		// setting the layout to spring
		SpringLayout springLayout = new SpringLayout();
		setLayout(springLayout);
		// removing focus
		this.setFocusable(false);
		// import the shops image;
		try {
			shopBack = new BufferedImage(360, 340, BufferedImage.TYPE_INT_RGB);
			shopBack = ImageIO.read(new File("./resources/shopBackground.png"));
		} catch (IOException e) {
		}

		// currentLv of eat stat is stored in the variable currentLv
		int currentLv = this.player.getHealth() / HEALTH_UPGRADE_AMOUNT;

		// the health label
		JLabel lblHealth = new JLabel("HP :" + currentLv);
		lblHealth.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.WEST, lblHealth, 30, SpringLayout.WEST, this);
		lblHealth.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(lblHealth);

		// the health cost label
		JLabel lblHpCost = new JLabel();
		lblHpCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblHpCost, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblHpCost, -70, SpringLayout.EAST, this);
		springLayout.putConstraint(SpringLayout.NORTH, lblHealth, 0, SpringLayout.NORTH, lblHpCost);
		if (currentLv < 10) {
			lblHpCost.setText("- $" + prices[currentLv - 1]);
		} else {
			lblHpCost.setVisible(false);
		}
		lblHpCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHpCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblHpCost);

		// the health upgrade button
		JButton buttonHealth = new JButton("+1");
		buttonHealth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonHealth.setFont(new Font("Bauhaus 93", Font.PLAIN, 15));
		buttonHealth.setForeground(Color.WHITE);
		buttonHealth.setBackground(new Color(0,0,0,0));
		springLayout.putConstraint(SpringLayout.NORTH, buttonHealth, 10, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.WEST, buttonHealth, 90, SpringLayout.EAST, lblHealth);
		if (currentLv == 10) {
			buttonHealth.setVisible(false);
		}
		buttonHealth.setFocusable(false);
		buttonHealth.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int currentLv = player.getHealth() / HEALTH_UPGRADE_AMOUNT;
				if (currentLv < 10 && player.buy(prices[currentLv - 1])) {
					player.setHealth(player.getHealth() + HEALTH_UPGRADE_AMOUNT);
					player.setTotalHealth(player.getHealth());
					currentLv++;
					lblHealth.setText("HP: " + currentLv);
				}
				if (currentLv < 10) {
					lblHpCost.setText("- $" + prices[currentLv - 1]);
				} else {
					lblHpCost.setVisible(false);
					buttonHealth.setVisible(false);
				}
				updateTotalCost();
			}
		});
		add(buttonHealth);

		// the damages current lv
		currentLv = player.getDamage() / DAMAGE_UPGRADE_AMOUNT;

		// the damage label
		JLabel lblDamage = new JLabel("Dmg: " + currentLv);
		lblDamage.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblDamage, 6, SpringLayout.SOUTH, lblHealth);
		springLayout.putConstraint(SpringLayout.WEST, lblDamage, 0, SpringLayout.WEST, lblHealth);
		lblDamage.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(lblDamage);

		// the damage cost label
		JLabel lblDmgCost = new JLabel();
		lblDmgCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblDmgCost, 48, SpringLayout.NORTH, this);
		if (currentLv < 10) {
			lblDmgCost.setText("- $" + prices[currentLv - 1]);
		} else {
			lblDmgCost.setVisible(false);
		}
		springLayout.putConstraint(SpringLayout.EAST, lblDmgCost, 0, SpringLayout.EAST, lblHpCost);
		lblDmgCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDmgCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblDmgCost);

		// the damage button
		JButton buttonDamage = new JButton("+1");
		buttonDamage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonDamage.setFont(new Font("Bauhaus 93", Font.PLAIN, 15));
		buttonDamage.setForeground(Color.WHITE);
		buttonDamage.setBackground(new Color(0,0,0,0));
		springLayout.putConstraint(SpringLayout.NORTH, buttonDamage, 0, SpringLayout.NORTH, lblDamage);
		springLayout.putConstraint(SpringLayout.EAST, buttonDamage, 0, SpringLayout.EAST, buttonHealth);
		if (currentLv == 10) {
			buttonDamage.setVisible(false);
		}
		buttonDamage.setFocusable(false);
		buttonDamage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int currentLv = player.getDamage() / DAMAGE_UPGRADE_AMOUNT;
				if (currentLv < 10 && player.buy(prices[currentLv - 1])) {
					player.setDamage(player.getDamage() + DAMAGE_UPGRADE_AMOUNT);
					currentLv++;
					lblDamage.setText("Dmg: " + currentLv);
				}
				if (currentLv < 10) {
					lblDmgCost.setText("- $" + prices[currentLv - 1]);
				} else {
					lblDmgCost.setVisible(false);
					buttonDamage.setVisible(false);
				}
				updateTotalCost();
			}
		});
		buttonDamage.setFocusable(false);
		add(buttonDamage);

		// the current level of fire rate
		currentLv = (player.getFireRate() - 1) / FIRERATE_UPGRADE_AMOUNT + 1;

		// the fire rate label
		JLabel lblFireRate = new JLabel("Fire Rate: " + currentLv);
		lblFireRate.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblFireRate, 5, SpringLayout.SOUTH, lblDamage);
		springLayout.putConstraint(SpringLayout.WEST, lblFireRate, 0, SpringLayout.WEST, lblHealth);
		lblFireRate.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(lblFireRate);

		// the fire rate cost label
		JLabel lblFRCost = new JLabel();
		lblFRCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblFRCost, 83, SpringLayout.NORTH, this);
		if (currentLv < 10) {
			lblFRCost.setText("- $" + prices[currentLv - 1]);
		} else {
			lblFRCost.setVisible(false);
		}
		springLayout.putConstraint(SpringLayout.EAST, lblFRCost, 0, SpringLayout.EAST, lblHpCost);
		lblFRCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFRCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblFRCost);

		// the fire rate upgrade button
		JButton buttonFirerate = new JButton("+" + FIRERATE_UPGRADE_AMOUNT + "/S");
		buttonFirerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		buttonFirerate.setFont(new Font("Bauhaus 93", Font.PLAIN, 15));
		buttonFirerate.setForeground(Color.WHITE);
		buttonFirerate.setBackground(new Color(0,0,0,0));
		springLayout.putConstraint(SpringLayout.NORTH, buttonFirerate, 0, SpringLayout.NORTH, lblFireRate);
		springLayout.putConstraint(SpringLayout.EAST, buttonFirerate, 0, SpringLayout.EAST, buttonDamage);
		if (currentLv == 10) {
			buttonFirerate.setVisible(false);
		}
		buttonFirerate.setFocusable(false);
		buttonFirerate.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int currentLv = (player.getFireRate() - 1) / FIRERATE_UPGRADE_AMOUNT + 1;
				if (currentLv < 10 && player.buy(prices[currentLv - 1])) {
					player.setFireRate(player.getFireRate() + FIRERATE_UPGRADE_AMOUNT);
					currentLv++;
					lblFireRate.setText("Fire Rate: " + currentLv);
				}
				if (currentLv < 10) {
					lblFRCost.setText("- $" + prices[currentLv - 1]);
				} else {
					lblFRCost.setVisible(false);
					buttonFirerate.setVisible(false);
				}
				updateTotalCost();
			}
		});
		buttonFirerate.setFocusable(false);
		add(buttonFirerate);

		// the current level of player movement speed
		currentLv = (player.getMoveSpeed() - 400) / MOVESPEED_UPGRADE_AMOUNT;

		// the movement speed label
		JLabel lableMS = new JLabel("Move Speed: " + currentLv);
		lableMS.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.WEST, lableMS, 0, SpringLayout.WEST, lblHealth);
		lableMS.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(lableMS);

		// the movement speed cost label
		JLabel lblMSCost = new JLabel();
		lblMSCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblMSCost, 10, SpringLayout.NORTH, lableMS);
		springLayout.putConstraint(SpringLayout.EAST, lblMSCost, 0, SpringLayout.EAST, lblHpCost);
		lblMSCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMSCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblMSCost);

		// the movement speed slider
		JSlider msSlider = new JSlider();
		msSlider.setForeground(new Color(205, 133, 63));
		springLayout.putConstraint(SpringLayout.SOUTH, msSlider, -124, SpringLayout.SOUTH, this);
		springLayout.putConstraint(SpringLayout.SOUTH, lableMS, -6, SpringLayout.NORTH, msSlider);
		springLayout.putConstraint(SpringLayout.WEST, msSlider, 0, SpringLayout.WEST, lblHealth);
		msSlider.setBackground(new Color(0, 0, 0, 0));
		msSlider.setValue(currentLv);
		msSlider.addChangeListener(e -> {
			if (player.getUnlockedMSLv() < 10) {
				lblMSCost.setText("- $" + prices[player.getUnlockedMSLv() - 1]);
			} else {
				lblMSCost.setVisible(false);
			}
			if (msSlider.getValueIsAdjusting()) {
				if (msSlider.getValue() <= player.getUnlockedMSLv()) {
					player.setMoveSpeed(MOVESPEED_UPGRADE_AMOUNT * msSlider.getValue()+400);
					lableMS.setText("Move Speed: " + msSlider.getValue());
				} else if (player.buy(prices[msSlider.getValue() - 2])) {
					player.setMoveSpeed(MOVESPEED_UPGRADE_AMOUNT * msSlider.getValue()+400);
					lableMS.setText("Move Speed: " + msSlider.getValue());
					player.setUnlockedMSLv(msSlider.getValue());
				} else {
					msSlider.setValue(player.getUnlockedMSLv());
					lableMS.setText("Move Speed: " + msSlider.getValue());
				}
				updateTotalCost();
			}
		});
		msSlider.setFocusable(false);
		msSlider.setToolTipText("It's called gravity");
		msSlider.setMinimum(1);
		msSlider.setMaximum(10);
		add(msSlider);

		// the current level of gravity
		currentLv = -(player.getGravity() - 5000) / GRAVITY_UPGRADE_AMOUNT - 1;

		// the gravity label
		JLabel gravityLabel = new JLabel("Gravity: " + currentLv);
		gravityLabel.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, gravityLabel, 6, SpringLayout.SOUTH, msSlider);
		springLayout.putConstraint(SpringLayout.WEST, gravityLabel, 0, SpringLayout.WEST, lblHealth);
		gravityLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(gravityLabel);

		// the gravity label cost
		JLabel lblGCost = new JLabel();
		lblGCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblGCost, 182, SpringLayout.NORTH, this);
		springLayout.putConstraint(SpringLayout.EAST, lblGCost, 0, SpringLayout.EAST, lblHpCost);
		lblGCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblGCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblGCost);

		// the gravity label slider
		JSlider gravitySlider = new JSlider();
		gravitySlider.setForeground(new Color(205, 133, 63));
		springLayout.putConstraint(SpringLayout.EAST, gravitySlider, 0, SpringLayout.EAST, msSlider);
		gravitySlider.setBackground(new Color(0, 0, 0, 0));
		gravitySlider.setValue(10 + currentLv + 1);
		gravitySlider.setFocusable(false);
		gravitySlider.addChangeListener(e -> {
			if (10 - player.getUnlockedGRLv() < 9) {
				lblGCost.setText("- $" + prices[10 - player.getUnlockedGRLv()]);
			} else {
				lblGCost.setVisible(false);
			}
			if (gravitySlider.getValueIsAdjusting()) {
				if (gravitySlider.getValue() >= player.getUnlockedGRLv()) {
					player.setGravity(5000 + GRAVITY_UPGRADE_AMOUNT * (10 - gravitySlider.getValue()));
					gravityLabel.setText("Gravity: " + (gravitySlider.getValue() - 11));
				} else if (player.buy(prices[9 - gravitySlider.getValue()])) {
					player.setGravity(5000 + GRAVITY_UPGRADE_AMOUNT * (10 - gravitySlider.getValue()));
					gravityLabel.setText("Gravity: " + (gravitySlider.getValue() - 11));
					player.setUnlockedGRLv(gravitySlider.getValue());
				} else {
					gravitySlider.setValue(player.getUnlockedGRLv());
					gravityLabel.setText("Gravity: " + (gravitySlider.getValue() - 11));
				}
				updateTotalCost();
			}
		});
		add(gravitySlider);
		gravitySlider.setToolTipText("It's called gravity");
		gravitySlider.setMaximum(10);
		gravitySlider.setMinimum(1);

		// the confirmation label
		JLabel lblConfirm = new JLabel("Confirm");
		springLayout.putConstraint(SpringLayout.NORTH, lblConfirm, 80, SpringLayout.NORTH, gravityLabel);
		springLayout.putConstraint(SpringLayout.WEST, lblConfirm, 0, SpringLayout.WEST, lblHealth);
		lblConfirm.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.SOUTH, gravitySlider, -6, SpringLayout.NORTH, lblConfirm);
		lblConfirm.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));
		add(lblConfirm);

		// the total cost label
		lblTotalCost = new JLabel("");
		springLayout.putConstraint(SpringLayout.EAST, lblTotalCost, -100, SpringLayout.EAST, this);
		lblTotalCost.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, lblTotalCost, 6, SpringLayout.NORTH, lblConfirm);
		updateTotalCost();
		lblTotalCost.setHorizontalAlignment(SwingConstants.TRAILING);
		lblTotalCost.setFont(new Font("Tahoma", Font.PLAIN, 18));
		add(lblTotalCost);

		// the yes button
		JButton btnYes = new JButton("YES");
		btnYes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnYes.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));
		btnYes.setBackground(new Color(0,0,0,0));
		btnYes.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, btnYes, 6, SpringLayout.SOUTH, gravitySlider);
		btnYes.setFocusable(false);
		btnYes.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				player.exportData();
				setVisible(false);
				exitShop();
			}
		});
		btnYes.setFocusable(false);
		add(btnYes);

		// the no button
		JButton btnNo = new JButton("NO");
		btnNo.setFont(new Font("Bauhaus 93", Font.PLAIN, 13));
		btnNo.setBackground(new Color(0,0,0,0));
		springLayout.putConstraint(SpringLayout.SOUTH, btnYes, 0, SpringLayout.SOUTH, btnNo);
		springLayout.putConstraint(SpringLayout.EAST, btnYes, -6, SpringLayout.WEST, btnNo);
		btnNo.setForeground(Color.WHITE);
		springLayout.putConstraint(SpringLayout.NORTH, btnNo, 6, SpringLayout.SOUTH, gravitySlider);
		springLayout.putConstraint(SpringLayout.EAST, btnNo, 0, SpringLayout.EAST, msSlider);
		btnNo.setFocusable(false);
		btnNo.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setVisible(false);
				exitShop();
			}
		});
		btnNo.setFocusable(false);
		add(btnNo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintChildren(java.awt.Graphics)
	 */
	@Override
	protected void paintChildren(Graphics g) {// allows for the panel to be painted before the game panels objects
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(shopBack, null, 0, 390);
		g.translate(0, 390);
		super.paintChildren(g);
		g.translate(0, -390);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {// overrides the the original paint method so it only paints when needed
	}

	/**
	 * updateTotalCost
	 * 
	 * Update total cost.
	 */
	private void updateTotalCost() {
		lblTotalCost.setText("- $" + (totalMoney - player.getMoney()));// calculates the total money spent and sets the
																		// total cost label
	}

	/**
	 * exitShop
	 * 
	 * Exit the shop.
	 */
	private void exitShop() {
		playfield.start(player, true);// restarts the game with but the player location stays the same
	}
}
