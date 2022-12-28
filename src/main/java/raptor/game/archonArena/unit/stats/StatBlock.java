package raptor.game.archonArena.unit.stats;

// FIXME: Remove once other stat system is complete
public class StatBlock {
	// Measured in units per second
	private int moveSpeed;
	private int visionRange;

	private int currentHealth;
	private int maxHealth;

	private int physicalPower;

	// Measured in attacks per second
	private double attackSpeed;

	public StatBlock(final StatBlock copyOf) {
		this.moveSpeed = copyOf.getMoveSpeed();
		this.visionRange = copyOf.getVisionRange();
		this.currentHealth = copyOf.getCurrentHealth();
		this.maxHealth = copyOf.getMaxHealth();
		this.physicalPower = copyOf.getPhysicalPower();
		this.attackSpeed = copyOf.getAttackSpeed();
	}

	public StatBlock(final int moveSpeed, final int visionRange, final int maxHealth, final int physicalPower, final double attackSpeed) {
		this.moveSpeed = moveSpeed;
		this.visionRange = visionRange;
		this.currentHealth = maxHealth;
		this.maxHealth = maxHealth;
		this.physicalPower = physicalPower;
		this.attackSpeed = attackSpeed;
	}

	// BEHAVIOURS

	public void applyDamage(final int amount) {
		currentHealth -= amount;
	}

	public float getCurrentHealthPercentage() {
		return (float)getCurrentHealth() / getMaxHealth();
	}

	// GETTERS and SETTERS

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public int getVisionRange() {
		return visionRange;
	}

	public int getCurrentHealth() {
		return currentHealth;
	}

	public int getMaxHealth() {
		return maxHealth;
	}

	public int getPhysicalPower() {
		return physicalPower;
	}

	public double getAttackSpeed() {
		return attackSpeed;
	}
}
