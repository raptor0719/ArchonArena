package raptor.game.archonArena.unit.stats.values;

public class FlatStatValue<T> implements StatValue<T> {
	private final T value;

	public FlatStatValue(final T value) {
		this.value = value;
	}

	@Override
	public T calculate() {
		return value;
	}
}
