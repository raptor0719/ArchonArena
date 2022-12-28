package raptor.game.archonArena.unit.stats.values;

public class LeveledStatValue implements StatValue, Leveled {
	private final Number[] values;
	private int level;

	public LeveledStatValue(final Number[] values) {
		this.values = values;
		this.level = 0;
	}

	@Override
	public Number calculate() {
		return values[level];
	}
	@Override
	public int currentLevel() {
		return level;
	}

	@Override
	public int maxLevel() {
		return values.length;
	}

	@Override
	public void setLevel(final int level) {
		this.level = Math.min(level, maxLevel()-1);
	}

}
