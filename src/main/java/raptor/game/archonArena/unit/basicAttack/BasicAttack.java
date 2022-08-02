package raptor.game.archonArena.unit.basicAttack;

import raptor.game.archonArena.unit.Unit;

public class BasicAttack {
	private int range;
	private int baseDamage;
	private int physicalDamageCoefficient;

	public BasicAttack(final int range, final int baseDamage, final int physicalDamageCoefficient) {
		this.range = range;
		this.baseDamage = baseDamage;
		this.physicalDamageCoefficient = physicalDamageCoefficient;
	}

	public void executeAttack(final Unit originatingUnit, final Unit target) {
		target.damage(getBaseDamage() + (physicalDamageCoefficient * originatingUnit.getStatBlock().getPhysicalPower()));
	}

	public int getRange() {
		return range;
	}

	public void setRange(final int range) {
		this.range = range;
	}

	public int getBaseDamage() {
		return baseDamage;
	}

	public void setBaseDamage(final int baseDamage) {
		this.baseDamage = baseDamage;
	}

	public int getPhysicalDamageCoefficient() {
		return physicalDamageCoefficient;
	}

	public void setPhysicalDamageCoefficient(final int physicalDamageCoefficient) {
		this.physicalDamageCoefficient = physicalDamageCoefficient;
	}
}
