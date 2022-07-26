package raptor.game.archonArena.ui;

import raptor.engine.display.render.IGraphics;
import raptor.engine.display.render.IViewport;
import raptor.engine.game.Game;
import raptor.engine.ui.UserInterface;
import raptor.engine.ui.input.IInputManager;
import raptor.engine.ui.input.IMousePositionPoll;
import raptor.game.archonArena.unit.selection.SelectionManager;

public class ArchonArenaUserInterface extends UserInterface {
	private final float viewportMouseMoveFactor;
	private final SelectionManager selectionManager;

	public ArchonArenaUserInterface(final IInputManager inputManager, final IMousePositionPoll mousePositionPoll) {
		super(inputManager, mousePositionPoll);

		this.viewportMouseMoveFactor = 3;
		this.selectionManager = new SelectionManager();
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	@Override
	public void processActions() {
		super.processActions();

		selectionManager.setSelectionEnd(Game.getViewportToLocation().transformX(getMousePositionX()), Game.getViewportToLocation().transformY(getMousePositionY()));

		performEdgeScroll();
	}

	private void performEdgeScroll() {
		final IViewport viewport = Game.getRenderer().getViewport();

		final int mousePositionX = Game.getUserInterface().getMousePositionX();
		if (mousePositionX >= viewport.getWidth())
			viewport.setXPosition(viewport.getXPosition() + (int)(viewportMouseMoveFactor));
		else if (mousePositionX <= 0)
			viewport.setXPosition(viewport.getXPosition() - (int)(viewportMouseMoveFactor));

		final int mousePositionY = Game.getUserInterface().getMousePositionY();
		if (mousePositionY >= viewport.getHeight())
			viewport.setYPosition(viewport.getYPosition() + (int)(viewportMouseMoveFactor));
		else if (mousePositionY <= 0)
			viewport.setYPosition(viewport.getYPosition() - (int)(viewportMouseMoveFactor));
	}

	@Override
	public void draw(final IGraphics graphics) {
		selectionManager.draw(graphics);

		super.draw(graphics);
	}
}
