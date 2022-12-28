package raptor.game.archonArena.unit.stats.blocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import raptor.game.archonArena.unit.stats.values.StatValue;

public class StatSourceTracker {
	private final List<String> statSourceNames;
	private final List<StatValue<?>> statValues;

	public StatSourceTracker() {
		this.statSourceNames = new ArrayList<String>();
		this.statValues = new ArrayList<StatValue<?>>();
	}

	public Iterator<StatValue<?>> getStatValues() {
		return statValues.iterator();
	}

	public void addStatSource(final String name, final StatValue<?> value) {
		statSourceNames.add(name);
		statValues.add(value);
	}

	public void removeStatSource(final String statSourceName) {
		for (int i = 0; i < statSourceNames.size(); i++) {
			final String name = statSourceNames.get(i);

			if (name.equals(statSourceName)) {
				statSourceNames.remove(i);
				statValues.remove(i);
				return;
			}
		}
	}
}
