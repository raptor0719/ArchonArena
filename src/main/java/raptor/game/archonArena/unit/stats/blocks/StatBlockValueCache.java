package raptor.game.archonArena.unit.stats.blocks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;

public class StatBlockValueCache implements IStatBlock {
	private final Map<Stat, Number> values;
	private final Map<AbilityResource, Map<AbilityResourceStat, Number>> resourceValues;

	public StatBlockValueCache(final int defaultValue, final Set<AbilityResource> supportedResources) {
		this.values = new HashMap<Stat, Number>();
		this.resourceValues = new HashMap<AbilityResource, Map<AbilityResourceStat, Number>>();

		for (final Stat stat : Stat.values())
			values.put(stat, defaultValue);

		for (final AbilityResource resource : supportedResources) {
			final Map<AbilityResourceStat, Number> resourceMap = new HashMap<AbilityResourceStat, Number>();

			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				resourceMap.put(stat, defaultValue);

			resourceValues.put(resource, resourceMap);
		}
	}

	public void setStatValue(final Stat stat, final Number value) {
		values.put(stat, value);
	}

	public void setResourceStatValue(final AbilityResource resource, final AbilityResourceStat stat, final Number value) {
		resourceValues.get(resource).put(stat, value);
	}

	@Override
	public Number statValue(final Stat stat) {
		return values.get(stat);
	}

	@Override
	public boolean usesResource(final AbilityResource resource) {
		return resourceValues.containsKey(resource);
	}

	@Override
	public Number resourceStatValue(final AbilityResource resource, final AbilityResourceStat stat) {
		return resourceValues.get(resource).get(stat);
	}
}
