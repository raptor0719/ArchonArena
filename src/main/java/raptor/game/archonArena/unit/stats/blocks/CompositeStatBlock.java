package raptor.game.archonArena.unit.stats.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;
import raptor.game.archonArena.unit.stats.StatType;
import raptor.game.archonArena.unit.stats.values.StatValue;

public class CompositeStatBlock implements IStatBlock {
	private final Map<Stat, StatSourceTracker> modifiers;
	private final Map<AbilityResource, Map<AbilityResourceStat, StatSourceTracker>> resourceModifiers;

	public CompositeStatBlock(final Set<AbilityResource> supportedResources) {
		this.modifiers = new HashMap<Stat, StatSourceTracker>();
		this.resourceModifiers = new HashMap<AbilityResource, Map<AbilityResourceStat, StatSourceTracker>>();

		for (final Stat stat : Stat.values())
			modifiers.put(stat, new StatSourceTracker());

		for (final AbilityResource resource : supportedResources) {
			final Map<AbilityResourceStat, StatSourceTracker> resourceStatMap = new HashMap<>();

			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				resourceStatMap.put(stat, new StatSourceTracker());

			resourceModifiers.put(resource, resourceStatMap);
		}
	}

	public void addModifier(final Stat stat, final String modifierName, final StatValue<?> value) {
		modifiers.get(stat).addStatSource(modifierName, value);
	}

	public void removeModifier(final Stat stat, final String modifierName) {
		modifiers.get(stat).removeStatSource(modifierName);
	}

	public void addResourceModifier(final AbilityResource resource, final AbilityResourceStat stat, final String modifierName, final StatValue<?> value) {
		resourceModifiers.get(resource).get(stat).addStatSource(modifierName, value);
	}

	public void removeResourceModifier(final AbilityResource resource, final AbilityResourceStat stat, final String modifierName) {
		resourceModifiers.get(resource).get(stat).removeStatSource(modifierName);
	}

	@Override
	public Object statValue(final Stat stat) {
		if (stat.getType() == StatType.INTEGER)
			return compositeInteger(modifiers.get(stat).getStatValues());
		else if (stat.getType() == StatType.DOUBLE)
			return compositeDouble(modifiers.get(stat).getStatValues());
		else
			throw new RuntimeException("Unexpected stat type.");
	}

	@Override
	public boolean usesResource(final AbilityResource resource) {
		return resourceModifiers.containsKey(resource);
	}

	@Override
	public Object resourceStatValue(final AbilityResource resource, final AbilityResourceStat stat) {
		if (stat.getType() == StatType.INTEGER)
			return compositeInteger(resourceModifiers.get(resource).get(stat).getStatValues());
		else if (stat.getType() == StatType.DOUBLE)
			return compositeDouble(resourceModifiers.get(resource).get(stat).getStatValues());
		else
			throw new RuntimeException("Unexpected stat type.");
	}

	private int compositeInteger(final Iterator<StatValue<?>> values) {
		int accumulator = 0;

		while (values.hasNext()) {
			final StatValue<?> value = values.next();

			accumulator += ((Integer)value.calculate());
		}

		return accumulator;
	}

	private double compositeDouble(final Iterator<StatValue<?>> values) {
		double accumulator = 0;

		while (values.hasNext()) {
			final StatValue<?> value = values.next();

			accumulator += ((Double)value.calculate());
		}

		return accumulator;
	}
}
