package raptor.game.archonArena.ui;

import raptor.engine.display.render.IGraphics;
import raptor.engine.ui.UserInterface;
import raptor.engine.ui.input.IInputManager;
import raptor.engine.ui.input.IMousePositionPoll;
import raptor.game.archonArena.unit.selection.SelectionManager;

public class ArchonArenaUserInterface extends UserInterface {

	private final SelectionManager selectionManager;

	public ArchonArenaUserInterface(final IInputManager inputManager, final IMousePositionPoll mousePositionPoll) {
		super(inputManager, mousePositionPoll);

		this.selectionManager = new SelectionManager();
	}

	public SelectionManager getSelectionManager() {
		return selectionManager;
	}

	@Override
	public void draw(final IGraphics graphics) {
		selectionManager.draw(graphics);

		super.draw(graphics);
	}
}
