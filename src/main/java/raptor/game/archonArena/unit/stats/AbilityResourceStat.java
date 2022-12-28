package raptor.game.archonArena.unit.stats;

public enum AbilityResourceStat {
	MAX_RESOURCE(StatType.DOUBLE),
	RESOURCE_REGEN(StatType.DOUBLE);

	private StatType type;

	private AbilityResourceStat(final StatType type) {
		this.type = type;
	}

	public StatType getType() {
		return type;
	}
}
