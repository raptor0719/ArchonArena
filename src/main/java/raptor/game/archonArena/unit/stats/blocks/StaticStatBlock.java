package raptor.game.archonArena.unit.stats.blocks;

import java.util.Map;
import java.util.Set;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;

public class StaticStatBlock implements IStatBlock {
	private final Map<Stat, Object> values;
	private final Map<AbilityResource, Map<AbilityResourceStat, Object>> resourceValues;

	public StaticStatBlock(final Map<Stat, Object> values, final Map<AbilityResource, Map<AbilityResourceStat, Object>> resourceValues) {
		this.values = values;
		this.resourceValues = resourceValues;
	}

	public Set<AbilityResource> supportedResources() {
		return resourceValues.keySet();
	}

	@Override
	public Object statValue(final Stat stat) {
		return values.get(stat);
	}

	@Override
	public boolean usesResource(final AbilityResource resource) {
		return resourceValues.containsKey(resource);
	}

	@Override
	public Object resourceStatValue(final AbilityResource resource, final AbilityResourceStat stat) {
		return resourceValues.get(resource).get(stat);
	}
}
