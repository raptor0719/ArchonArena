package raptor.game.archonArena.unit;

import raptor.engine.model.Model;

public class UnitDefinition {
	private final String name;
	private final Model model;

	private final int moveSpeed;

	private final int width;
	private final int height;

	public UnitDefinition(final String name, final Model model, final int moveSpeed, final int width, final int height) {
		this.name = name;
		this.model = model;

		this.moveSpeed = moveSpeed;

		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public Model getModel() {
		return model;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
