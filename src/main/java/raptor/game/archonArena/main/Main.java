package raptor.game.archonArena.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import raptor.engine.display.render.JavaAwtRenderer;
import raptor.engine.ui.input.JavaAwtInputManager;

public class Main {
	public static void main(final String[] args) throws AWTException, InterruptedException {
		final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();
		final GraphicsDevice usedGraphicsDevice = graphicsDevices[0];

		final JFrame jframe = new JFrame(usedGraphicsDevice.getDefaultConfiguration());
		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setUndecorated(true);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		final JPanel displayPanel = new JPanel();
		displayPanel.setPreferredSize(new Dimension(jframe.getWidth(), jframe.getHeight()));
		jframe.add(displayPanel);
		displayPanel.setVisible(true);
		displayPanel.setBackground(Color.BLACK);
		displayPanel.setCursor(getInvisibleCursor());

		Thread.sleep(250);

		final JavaAwtRenderer renderer = new JavaAwtRenderer((Graphics2D)displayPanel.getGraphics(), displayPanel.getWidth(), displayPanel.getHeight(), usedGraphicsDevice);
		final JavaAwtInputManager inputManager = new JavaAwtInputManager(usedGraphicsDevice, renderer.getViewport());

		displayPanel.addMouseListener(inputManager);
		displayPanel.addKeyListener(inputManager);
		displayPanel.addMouseMotionListener(inputManager);

		displayPanel.requestFocusInWindow();

		ArchonArena game = null;
		try {
			game = new ArchonArena("src/main/resources", renderer, inputManager, inputManager);
			game.start();
		} catch (final Throwable t) {
			t.printStackTrace();
		}

		jframe.dispose();
	}

	private static Cursor getInvisibleCursor() {
		return Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB), new java.awt.Point(0, 0), "spooky");
	}
}
