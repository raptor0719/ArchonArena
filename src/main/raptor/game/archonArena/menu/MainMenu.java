package raptor.game.archonArena.menu;

import raptor.engine.display.render.BasicColor;
import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Game;
import raptor.engine.game.Level;
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

public class MainMenu extends Level {
	private final UIState mainMenuState;
	private UIButton button;

	public MainMenu() {
		this.mainMenuState = new UIState();
	}

	@Override
	public void init() {
		mainMenuState.addActionHandler("EXIT_GAME", new IActionHandler() {
			@Override
			public void handleAction(final int gameMouseX, final int gameMouseY) {
				Game.exitGame();
			}
		});

		final int x = Game.getRenderer().getViewport().getWidth()/2;
		final int y = Game.getRenderer().getViewport().getHeight()/2;

		button = new UIButton(x, y, UIAnchorPoint.CENTER, 100, 50, 0) {
			@Override
			public void draw(final IGraphics graphics) {
				graphics.drawRectangle(getAbsoluteX(), getAbsoluteY(), getWidth(), getHeight(), true, new BasicColor(255, 255, 255, 100));
			}

			@Override
			public void activate() {
				Game.exitGame();
			}
		};

		mainMenuState.addElement(button);

		final IInputMap inputMap = new InputMap();
		inputMap.mapInput(PhysicalInput.LEFT_MOUSE, KeyAction.RELEASED, UserInterface.ACTIVATE_BUTTON_ACTION);
		inputMap.mapInput(PhysicalInput.F_KEY, KeyAction.RELEASED, "EXIT_GAME");

		mainMenuState.setInputMap(inputMap);

		Game.getUserInterface().setState(mainMenuState);
	}

	@Override
	public void update() {
		final Point p = Game.getUserInterface().getMousePosition();
		button.setX(p.getX());
		button.setY(p.getY());
	}

	@Override
	public void draw(final IGraphics graphics) {
		final int viewportWidth = Game.getRenderer().getViewport().getWidth();
		final int viewportHeight = Game.getRenderer().getViewport().getHeight();

		final BasicColor coolPurpleColorMan = new BasicColor(50, 0, 150, 200);
		final int barWidth = 100;

		graphics.drawRectangle(0, 0, viewportWidth, barWidth, true, coolPurpleColorMan);
		graphics.drawRectangle(0, 0, barWidth, viewportHeight, true, coolPurpleColorMan);
		graphics.drawRectangle(viewportWidth - barWidth, 0, barWidth, viewportHeight, true, coolPurpleColorMan);
		graphics.drawRectangle(0, viewportHeight - barWidth, viewportWidth, barWidth, true, coolPurpleColorMan);
	}
}
