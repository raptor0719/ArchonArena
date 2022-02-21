package raptor.game.archonArena.main;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import raptor.engine.display.render.JavaAwtRenderer;
import raptor.engine.display.render.ViewportToLocationTransformer;
import raptor.engine.game.Game;
import raptor.engine.ui.input.JavaAwtInputManager;
import raptor.game.archonArena.menu.MainMenu;

public class Main {
	public static void main(final String[] args) throws AWTException, InterruptedException {
		final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		final GraphicsDevice[] graphicsDevices = graphicsEnvironment.getScreenDevices();

		final JFrame jframe = new JFrame(graphicsDevices[1].getDefaultConfiguration());
		jframe.setExtendedState(JFrame.MAXIMIZED_BOTH);
		jframe.setUndecorated(true);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		jframe.setLayout(new GridBagLayout());

		final JPanel displayPanel = new JPanel();
		displayPanel.setPreferredSize(new Dimension(jframe.getWidth(), jframe.getHeight()));
		jframe.add(displayPanel);
		displayPanel.setVisible(true);
		displayPanel.setBackground(Color.BLACK);

		Thread.sleep(250);

		final JavaAwtRenderer renderer = new JavaAwtRenderer((Graphics2D)displayPanel.getGraphics(), displayPanel.getWidth(), displayPanel.getHeight());
		final ViewportToLocationTransformer viewportToLocationTransformer = new ViewportToLocationTransformer(renderer.getViewport());
		final JavaAwtInputManager inputManager = new JavaAwtInputManager(viewportToLocationTransformer);

		displayPanel.addMouseListener(inputManager);
		displayPanel.addKeyListener(inputManager);

		displayPanel.requestFocusInWindow();

		final Game game = new Game(new MainMenu(), renderer, inputManager, inputManager);

		game.start();

		jframe.dispose();
	}
}
