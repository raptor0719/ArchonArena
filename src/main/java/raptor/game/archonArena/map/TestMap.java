package raptor.game.archonArena.map;

import java.util.ArrayList;
import java.util.List;

import raptor.engine.collision.CollisionPlane;
import raptor.engine.display.render.BasicColor;
import raptor.engine.display.render.IColor;
import raptor.engine.display.render.IGraphics;
import raptor.engine.display.render.IViewport;
import raptor.engine.display.render.ViewportToLocationTransformer;
import raptor.engine.game.Game;
import raptor.engine.nav.api.INavigator;
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
import raptor.game.archonArena.main.ArchonArena;
import raptor.game.archonArena.menu.MainMenu;
import raptor.game.archonArena.unit.Unit;
import raptor.game.archonArena.unit.UnitDefinition;
import raptor.game.archonArena.unit.selection.SelectionManager;
import raptor.game.archonArena.unit.stats.StatBlock;

public class TestMap extends ArchonArenaLevel {
	private final UIState gameplayState;
	private UIButton exitLevelButton;

	private final int viewportMoveFactor;
	private final int viewportMouseMoveFactor;
	int viewportMoveX = 0;
	int viewportMoveY = 0;

	Unit testUnit;

	public TestMap() {
		super();
		this.gameplayState = new UIState();

		this.viewportMoveFactor = 10;
		this.viewportMouseMoveFactor = 10;
	}

	@Override
	public void init() {
		this.exitLevelButton = new ExitLevelButton(10, 10, UIAnchorPoint.TOP_LEFT, 50, 25, 0);

		gameplayState.addElement(exitLevelButton);

		final IInputMap inputMap = new InputMap();
		inputMap.mapInput(PhysicalInput.LEFT_MOUSE, KeyAction.RELEASED, UserInterface.ACTIVATE_BUTTON_ACTION);

		inputMap.mapInput(PhysicalInput.LEFT_ARROW, KeyAction.PRESSED, "VIEWPORT_LEFT_START");
		inputMap.mapInput(PhysicalInput.LEFT_ARROW, KeyAction.RELEASED, "VIEWPORT_LEFT_END");
		inputMap.mapInput(PhysicalInput.RIGHT_ARROW, KeyAction.PRESSED, "VIEWPORT_RIGHT_START");
		inputMap.mapInput(PhysicalInput.RIGHT_ARROW, KeyAction.RELEASED, "VIEWPORT_RIGHT_END");
		inputMap.mapInput(PhysicalInput.UP_ARROW, KeyAction.PRESSED, "VIEWPORT_UP_START");
		inputMap.mapInput(PhysicalInput.UP_ARROW, KeyAction.RELEASED, "VIEWPORT_UP_END");
		inputMap.mapInput(PhysicalInput.DOWN_ARROW, KeyAction.PRESSED, "VIEWPORT_DOWN_START");
		inputMap.mapInput(PhysicalInput.DOWN_ARROW, KeyAction.RELEASED, "VIEWPORT_DOWN_END");

		inputMap.mapInput(PhysicalInput.LEFT_MOUSE, KeyAction.PRESSED, "SELECTION_START");
		inputMap.mapInput(PhysicalInput.LEFT_MOUSE, KeyAction.RELEASED, "SELECTION_END");

		inputMap.mapInput(PhysicalInput.RIGHT_MOUSE, KeyAction.RELEASED, "ORDER_TARGET");

		gameplayState.setInputMap(inputMap);

		gameplayState.addActionHandler("VIEWPORT_LEFT_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveX = -1;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_LEFT_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveX = 0;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_RIGHT_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveX = 1;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_RIGHT_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveX = 0;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_UP_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveY = -1;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_UP_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveY = 0;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_DOWN_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveY = 1;
			}
		});
		gameplayState.addActionHandler("VIEWPORT_DOWN_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				viewportMoveY = 0;
			}
		});
		gameplayState.addActionHandler("SELECTION_START", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				ArchonArena.getArchonArenaUserInterface().getSelectionManager().startSelection(gameMouseX, gameMouseY);
			}
		});
		gameplayState.addActionHandler("SELECTION_END", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				ArchonArena.getArchonArenaUserInterface().getSelectionManager().select();
			}
		});
		gameplayState.addActionHandler("ORDER_TARGET", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				ArchonArena.getArchonArenaUserInterface().getSelectionManager().moveOrder(gameMouseX, gameMouseY, false);
			}
		});

		final INavigator navigator = getTestNavigator();
		addNavigator(0, navigator);

		Game.getUserInterface().setState(gameplayState);

		final UnitDefinition testUnitDefinition = new UnitDefinition("test unit", ArchonArena.getModelLibrary().getDefinition("Torin"), 20, new StatBlock(5, 100));
		testUnit = new Unit(testUnitDefinition, navigator, new Point(200, 200), 0);
		this.addEntity(testUnit);

		final Unit testUnit2 = new Unit(testUnitDefinition, navigator, new Point(250, 250), 0);
		this.addEntity(testUnit2);

		final Unit testUnit3 = new Unit(testUnitDefinition, navigator, new Point(250, 300), 1);
		this.addEntity(testUnit3);

		this.addCollisionPlane(Navigators.GROUND.getId(), Navigators.GROUND.getName(), new MasterCollisionHandler());
		final CollisionPlane plane = this.getCollisionPlane(0);
		plane.registerEntity(testUnit);
		plane.registerEntity(testUnit2);
		plane.registerEntity(testUnit3);

		ArchonArena.getArchonArenaUserInterface().getSelectionManager().setRender(true);
	}

	@Override
	public void cleanup() {
		final SelectionManager selectionManager = ArchonArena.getArchonArenaUserInterface().getSelectionManager();

		selectionManager.cancelSelection();
		selectionManager.clearSelected();
		selectionManager.setRender(false);
	}

	@Override
	public void update() {
		this.getVisionCalculator().calculateVision();

		final IViewport viewport = Game.getRenderer().getViewport();

		viewport.setXPosition(viewport.getXPosition() + viewportMoveX*viewportMoveFactor);
		viewport.setYPosition(viewport.getYPosition() + viewportMoveY*viewportMoveFactor);

		final int mousePositionX = Game.getUserInterface().getMousePositionX();
		final int mousePositionY = Game.getUserInterface().getMousePositionY();

		if (mousePositionX >= viewport.getWidth())
			viewport.setXPosition(viewport.getXPosition() + viewportMouseMoveFactor);
		else if (mousePositionX <= 0)
			viewport.setXPosition(viewport.getXPosition() - viewportMouseMoveFactor);
		else if (mousePositionY >= viewport.getHeight())
			viewport.setYPosition(viewport.getYPosition() + viewportMouseMoveFactor);
		else if (mousePositionY <= 0)
			viewport.setYPosition(viewport.getYPosition() - viewportMouseMoveFactor);

		final ViewportToLocationTransformer t = Game.getViewportToLocation();
		ArchonArena.getArchonArenaUserInterface().getSelectionManager().setSelectionEnd(t.transformX(mousePositionX), t.transformY(mousePositionY));
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
