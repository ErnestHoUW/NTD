import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * The Class Background.
 */
public class Background {

	/** The base image. */
	private BufferedImage base;

	/** The repeating image. */
	private BufferedImage repeating;

	/** The clock. */
	private Clock clock;

	/** The Constant rate of scroll. */
	private static final int SCROLL_RATE = 25;

	/** The y of the image. */
	private double y = 0;

	/** The whether or not it is scrolling. */
	private boolean scrolling = false;

	/**
	 * Instantiates a new background.
	 */
	public Background() {
		try {
			base = new BufferedImage(400, 1880, BufferedImage.TYPE_INT_RGB);
			base = ImageIO.read(new File("./resources/startBackground.png"));
			repeating = new BufferedImage(400, 2176, BufferedImage.TYPE_INT_RGB);
			repeating = ImageIO.read(new File("./resources/scrollBackground.png"));
		} catch (IOException e) {
		}
	}

	/**
	 * getImage
	 * Gets the image.
	 *
	 * @return the image
	 */
	public BufferedImage getImage() {
		if (scrolling) {//is scrolling
			BufferedImage newImg;
			y += clock.elapsedTime() * SCROLL_RATE;//update y
			clock.updateTime();
			int intY = (int) (Math.round(y));
			if (base.getHeight() - 800 - intY >= 0) {//if the base image is still scrolling
				newImg = base.getSubimage(0, base.getHeight() - 800 - intY, 400, 800);
			} else {//base image is gone
				if (repeating.getHeight() - 800 - (intY - base.getHeight() + 800) < 0) {//reset y verytime it makes a full loop
					intY = base.getHeight() - 800;
					y = intY;
				}
				newImg = repeating.getSubimage(0, repeating.getHeight() - 800 - (intY - base.getHeight() + 800), 400,//crop the image to fit the screen and move the image
						800);
			}
			return newImg;//the new image
		}
		return base.getSubimage(0, base.getHeight() - 800, 400, 800);//the base image without sroll
	}

	/**
	 * startScroll
	 * Start scroll.
	 */
	public void startScroll() {
		clock = new Clock();
		this.scrolling = true;
	}

	/**
	 * getY
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return (int) Math.round(this.y);
	}

}
