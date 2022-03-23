package raptor.game.archonArena.unit;

import raptor.game.archonArena.model.AnimatedModelDefinition;

public class UnitDefinition {
	private final String name;
	private final AnimatedModelDefinition modelDefinition;

	private final int moveSpeed;

	private final int width;
	private final int height;

	public UnitDefinition(final String name, final AnimatedModelDefinition modelDefinition, final int moveSpeed, final int width, final int height) {
		this.name = name;
		this.modelDefinition = modelDefinition;

		this.moveSpeed = moveSpeed;

		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public AnimatedModelDefinition getModelDefintion() {
		return modelDefinition;
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
