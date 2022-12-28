package raptor.game.archonArena.unit.stats.values;

public class LeveledStatValue<T> implements StatValue<T>, Leveled {
	private final T[] values;
	private int level;

	public LeveledStatValue(final T[] values) {
		this.values = values;
		this.level = 0;
	}

	@Override
	public T calculate() {
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
