package raptor.game.archonArena.map;

import java.util.ArrayList;
import java.util.List;

import raptor.engine.display.render.BasicColor;
import raptor.engine.display.render.IColor;
import raptor.engine.display.render.IGraphics;
import raptor.engine.display.render.IViewport;
import raptor.engine.game.Game;
import raptor.engine.game.Level;
import raptor.engine.nav.mesh.NavMeshNavigator;
import raptor.engine.ui.UIAnchorPoint;
import raptor.engine.ui.UIState;
import raptor.engine.ui.UserInterface;
import raptor.engine.ui.elements.UIButton;
import raptor.engine.ui.input.IActionHandler;
import raptor.engine.ui.input.IInputMap;
import raptor.engine.ui.input.InputMap;
import raptor.engine.ui.input.KeyAction;
import raptor.engine.ui.input.PhysicalInput;
import raptor.engine.util.geometry.Point;
import raptor.engine.util.geometry.Polygon;
import raptor.game.archonArena.menu.MainMenu;

public class TestMap extends Level {
	private final UIState gameplayState;
	private UIButton exitLevelButton;

	boolean scrollLeft = false;

	public TestMap() {
		this.gameplayState = new UIState();
	}

	@Override
	public void init() {
		this.exitLevelButton = new ExitLevelButton(10, 10, UIAnchorPoint.TOP_LEFT, 50, 25, 0);

		gameplayState.addElement(exitLevelButton);

		final IInputMap inputMap = new InputMap();
		inputMap.mapInput(PhysicalInput.LEFT_MOUSE, KeyAction.RELEASED, UserInterface.ACTIVATE_BUTTON_ACTION);
		inputMap.mapInput(PhysicalInput.LEFT_ARROW, KeyAction.PRESSED, "VIEWPORT_LEFT_START");
		inputMap.mapInput(PhysicalInput.LEFT_ARROW, KeyAction.RELEASED, "VIEWPORT_LEFT_END");

		gameplayState.setInputMap(inputMap);

		gameplayState.addActionHandler("VIEWPORT_LEFT_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				scrollLeft = true;
			}
		});

		gameplayState.addActionHandler("VIEWPORT_LEFT_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				scrollLeft = false;
			}
		});

		gameplayState.addActionHandler("ACTIVATE", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				final IViewport viewport = Game.getRenderer().getViewport();

				viewport.setXPosition(gameMouseX - viewport.getWidth()/2);
				viewport.setYPosition(gameMouseY - viewport.getHeight()/2);
			}
		});

		addNavigator(0, getTestNavigator());

		Game.getUserInterface().setState(gameplayState);
	}

	@Override
	public void update() {
		if (scrollLeft) {
			final IViewport viewport = Game.getRenderer().getViewport();
			viewport.setXPosition(viewport.getXPosition() - 5);
		}
	}

	@Override
	public void draw(final IGraphics graphics) {
		final IColor color = new BasicColor(255, 255, 255, 255);

		graphics.drawLine(300, 1, 600, 200, 5, color);
		graphics.drawLine(600, 200, 500, 400, 5, color);
		graphics.drawLine(500, 400, 100, 400, 5, color);
		graphics.drawLine(100, 400, 1, 200, 5, color);
		graphics.drawLine(1, 200, 300, 1, 5, color);

		graphics.drawLine(300, 150, 300, 350, 2, color);
		graphics.drawLine(300, 350, 450, 200, 2, color);
		graphics.drawLine(450, 200, 300, 150, 2, color);
	}

	private NavMeshNavigator getTestNavigator() {
		final List<Point> parentPoints = new ArrayList<>();
		parentPoints.add(new Point(300, 0));
		parentPoints.add(new Point(600, 200));
		parentPoints.add(new Point(500, 400));
		parentPoints.add(new Point(100, 400));
		parentPoints.add(new Point(0, 200));

		final Polygon parent = new Polygon(parentPoints);

		final List<Point> holePoints = new ArrayList<>();
		holePoints.add(new Point(300, 150));
		holePoints.add(new Point(300, 350));
		holePoints.add(new Point(450, 200));

		final List<Polygon> holes = new ArrayList<>();
		holes.add(new Polygon(holePoints));

		return NavMeshNavigator.buildNavigator(parent, holes);
	}

	private static class ExitLevelButton extends UIButton {
		private final IColor backgroundColor = new BasicColor(255, 255, 255, 100);

		public ExitLevelButton(final int x, final int y, final UIAnchorPoint anchor, final int width, final int height, final int depth) {
			super(x, y, anchor, width, height, depth);
		}

		@Override
		public void draw(final IGraphics graphics) {
			graphics.drawRectangle(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight(), true, backgroundColor);
		}

		@Override
		public void activate() {
			Game.loadLevel(new MainMenu());
		}
	}
}
