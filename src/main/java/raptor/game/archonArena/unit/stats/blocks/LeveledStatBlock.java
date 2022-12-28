package raptor.game.archonArena.unit.stats.blocks;

import java.util.Map;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;
import raptor.game.archonArena.unit.stats.values.Leveled;
import raptor.game.archonArena.unit.stats.values.LeveledStatValue;

public class LeveledStatBlock implements IStatBlock, Leveled {
	private final Map<Stat, LeveledStatValue<?>> values;
	private final Map<AbilityResource, Map<AbilityResourceStat, LeveledStatValue<?>>> resourceValues;

	private int level;

	public LeveledStatBlock(final Map<Stat, LeveledStatValue<?>> values, final Map<AbilityResource, Map<AbilityResourceStat, LeveledStatValue<?>>> resourceValues) {
		this.values = values;
		this.resourceValues = resourceValues;
	}

	@Override
	public Object statValue(final Stat stat) {
		return values.get(stat).calculate();
	}

	@Override
	public boolean usesResource(final AbilityResource resource) {
		return resourceValues.containsKey(resource);
	}

	@Override
	public Object resourceStatValue(final AbilityResource resource, final AbilityResourceStat stat) {
		return resourceValues.get(resource).get(stat).calculate();
	}

	@Override
	public int currentLevel() {
		return level;
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;

		for (final Stat stat : Stat.values())
			values.get(stat).setLevel(level);

		for (final AbilityResource resource : resourceValues.keySet())
			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				resourceValues.get(resource).get(stat).setLevel(level);
	}

	@Override
	public int maxLevel() {
		return values.get(Stat.MAX_HEALTH).maxLevel();
	}
}
