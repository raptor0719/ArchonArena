package raptor.game.archonArena.unit;

import raptor.engine.model.Model;

public class UnitDefinition {
	private final String name;
	private final Model model;

	public UnitDefinition(final String name, final Model model) {
		this.name = name;
		this.model = model;
	}

	public String getName() {
		return name;
	}

	public Model getModel() {
		return model;
	}
}
