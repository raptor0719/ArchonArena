package raptor.game.archonArena.unit.basicAttack;

public class BasicAttackDefinition {
	private final int range;
	private final int baseDamage;
	private final int physicalDamageCoefficient;

	public BasicAttackDefinition(final int range, final int baseDamage, final int physicalDamageCoefficient) {
		this.range = range;
		this.baseDamage = baseDamage;
		this.physicalDamageCoefficient = physicalDamageCoefficient;
	}

	public BasicAttack getInstance() {
		return new BasicAttack(getRange(), getBaseDamage(), getPhysicalDamageCoefficient());
	}

	public int getRange() {
		return range;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public int getPhysicalDamageCoefficient() {
		return physicalDamageCoefficient;
	}
}
