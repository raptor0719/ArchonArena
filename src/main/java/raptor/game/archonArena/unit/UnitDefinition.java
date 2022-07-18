package raptor.game.archonArena.unit;

import raptor.game.archonArena.model.AnimatedModelDefinition;

public class UnitDefinition {
	private final String name;
	private final AnimatedModelDefinition modelDefinition;

	private final int moveSpeed;

	private final int selectableWidth;
	private final int selectableHeight;

	public UnitDefinition(final String name, final AnimatedModelDefinition modelDefinition, final int moveSpeed) {
		this.name = name;
		this.modelDefinition = modelDefinition;

		this.moveSpeed = moveSpeed;

		this.selectableWidth = modelDefinition.getWidth();
		this.selectableHeight = modelDefinition.getHeight();
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

	public int getSelectableWidth() {
		return selectableWidth;
	}

	public int getSelectableHeight() {
		return selectableHeight;
	}
}
