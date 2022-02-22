package raptor.game.archonArena.unit;

import raptor.engine.collision.geometry.CollisionRectangle;
import raptor.engine.model.Model;

public class UnitDefinition {
	private final String name;
	private final Model model;

	private final CollisionRectangle selectionBox;

	public UnitDefinition(final String name, final Model model, final CollisionRectangle selectionBox) {
		this.name = name;
		this.model = model;

		this.selectionBox = selectionBox;
	}

	public String getName() {
		return name;
	}

	public Model getModel() {
		return model;
	}

	public CollisionRectangle getSelectionBox() {
		return selectionBox;
	}
}
