package raptor.game.archonArena.unit.stats.values;

public class FlatStatValue implements StatValue {
	private final Number value;

	public FlatStatValue(final Number value) {
		this.value = value;
	}

	@Override
	public Number calculate() {
		return value;
	}
}
