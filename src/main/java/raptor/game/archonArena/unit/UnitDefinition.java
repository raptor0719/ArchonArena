package raptor.game.archonArena.unit;

import raptor.game.archonArena.model.AnimatedModelDefinition;
import raptor.game.archonArena.unit.basicAttack.BasicAttackDefinition;
import raptor.game.archonArena.unit.stats.StatBlock;

public class UnitDefinition {
	private final String name;
	private final AnimatedModelDefinition modelDefinition;

	private final int unitSizeRadius;

	private final int selectableWidth;
	private final int selectableHeight;

	private final StatBlock baseStateBlock;

	private final BasicAttackDefinition basicAttackDefinition;

	public UnitDefinition(final String name, final AnimatedModelDefinition modelDefinition, final int unitSizeRadius, final StatBlock baseStateBlock, final BasicAttackDefinition basicAttackDefinition) {
		this.name = name;
		this.modelDefinition = modelDefinition;

		this.unitSizeRadius = unitSizeRadius;

		this.selectableWidth = modelDefinition.getWidth();
		this.selectableHeight = modelDefinition.getHeight();

		this.baseStateBlock = baseStateBlock;

		this.basicAttackDefinition = basicAttackDefinition;
	}

	public String getName() {
		return name;
	}

	public AnimatedModelDefinition getModelDefintion() {
		return modelDefinition;
	}

	public int getUnitSizeRadius() {
		return unitSizeRadius;
	}

	public int getSelectableWidth() {
		return selectableWidth;
	}

	public int getSelectableHeight() {
		return selectableHeight;
	}

	public StatBlock getBaseStatBlock() {
		return baseStateBlock;
	}

	public BasicAttackDefinition getBasicAttackDefinition() {
		return basicAttackDefinition;
	}
}
