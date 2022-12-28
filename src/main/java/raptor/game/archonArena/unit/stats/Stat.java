package raptor.game.archonArena.unit.stats;

public enum Stat {
	MAX_HEALTH(StatType.INTEGER),
	PAIN_TOLERANCE(StatType.INTEGER),
	HEALTH_REGEN(StatType.DOUBLE),
	ATTACK_SPEED(StatType.DOUBLE),
	ATTACK_RANGE(StatType.INTEGER),
	VISION_RANGE(StatType.INTEGER),
	COOLDOWN_REDUCTION(StatType.DOUBLE),
	PHYSICAL_POWER(StatType.INTEGER),
	MAGICAL_POWER(StatType.INTEGER),
	ARMOR(StatType.DOUBLE),
	ANTI_MAGIC(StatType.DOUBLE),
	POISE(StatType.DOUBLE),
	TACTICS(StatType.DOUBLE),
	VAMPIRISM(StatType.DOUBLE),
	SOUL_STEAL(StatType.DOUBLE),
	CRITICAL_CHANCE(StatType.DOUBLE),
	ATTACK_CRITICAL_DAMAGE(StatType.DOUBLE),
	ABILITY_CRITICAL_DAMAGE(StatType.DOUBLE),
	PLATING(StatType.DOUBLE),
	HARDINESS(StatType.DOUBLE),
	AGILITY(StatType.DOUBLE);

	private StatType type;

	private Stat(final StatType type) {
		this.type = type;
	}

	public StatType getType() {
		return type;
	}
}
