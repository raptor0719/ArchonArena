package raptor.game.archonArena.unit.stats;

import raptor.game.archonArena.unit.stats.blocks.CompositeStatBlock;
import raptor.game.archonArena.unit.stats.blocks.IStatBlock;
import raptor.game.archonArena.unit.stats.blocks.StatBlockValueCache;
import raptor.game.archonArena.unit.stats.blocks.StaticStatBlock;

public class UnitStats {
	private final StaticStatBlock base;
	private final CompositeStatBlock baseFactor;
	private final CompositeStatBlock bonus;
	private final CompositeStatBlock bonusFactor;
	private final CompositeStatBlock calculatedFactor;
	private final CompositeStatBlock extra;

	private final StatBlockValueCache baseFactorCache;
	private final StatBlockValueCache baseCalculatedCache;
	private final StatBlockValueCache bonusCache;
	private final StatBlockValueCache bonusFactorCache;
	private final StatBlockValueCache bonusCalculatedCache;
	private final StatBlockValueCache calculatedCache;
	private final StatBlockValueCache calculatedFactorCache;
	private final StatBlockValueCache totalCache;
	private final StatBlockValueCache extraCache;

	private final StatBlockValueCache finalCache;

	public UnitStats(final StaticStatBlock base) {
		this.base = base;

		this.baseFactor = new CompositeStatBlock(base.supportedResources());
		this.bonus = new CompositeStatBlock(base.supportedResources());
		this.bonusFactor = new CompositeStatBlock(base.supportedResources());
		this.calculatedFactor = new CompositeStatBlock(base.supportedResources());
		this.extra = new CompositeStatBlock(base.supportedResources());

		this.baseFactorCache = new StatBlockValueCache(1, base.supportedResources());
		this.baseCalculatedCache = new StatBlockValueCache(0, base.supportedResources());
		this.bonusCache = new StatBlockValueCache(0, base.supportedResources());
		this.bonusFactorCache = new StatBlockValueCache(1, base.supportedResources());
		this.bonusCalculatedCache = new StatBlockValueCache(0, base.supportedResources());
		this.calculatedCache = new StatBlockValueCache(0, base.supportedResources());
		this.calculatedFactorCache = new StatBlockValueCache(1, base.supportedResources());
		this.totalCache = new StatBlockValueCache(1, base.supportedResources());
		this.extraCache = new StatBlockValueCache(1, base.supportedResources());

		this.finalCache = new StatBlockValueCache(1, base.supportedResources());
	}

	public void calculate() {
		calculateCompositeResult(baseFactor, baseFactorCache);
		multiplyStatBlocks(base, baseFactorCache, baseCalculatedCache);

		calculateCompositeResult(bonus, bonusCache);
		calculateCompositeResult(bonusFactor, bonusFactorCache);
		multiplyStatBlocks(bonusCache, bonusFactorCache, bonusCalculatedCache);

		addStatBlocks(baseCalculatedCache, bonusCalculatedCache, calculatedCache);

		calculateCompositeResult(calculatedFactor, calculatedFactorCache);
		multiplyStatBlocks(calculatedCache, calculatedFactorCache, totalCache);

		calculateCompositeResult(extra, extraCache);

		addStatBlocks(totalCache, extraCache, finalCache);
	}

	/* CALCULATE HELPER METHODS */

	private void calculateCompositeResult(final CompositeStatBlock composite, final StatBlockValueCache cache) {
		for (final Stat stat : Stat.values())
			cache.setStatValue(stat, composite.statValue(stat));

		for (final AbilityResource resource : base.supportedResources())
			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				cache.setResourceStatValue(resource, stat, composite.resourceStatValue(resource, stat));
	}

	private void addStatBlocks(final IStatBlock a, final IStatBlock b, final StatBlockValueCache cache) {
		for (final Stat stat : Stat.values())
			cache.setStatValue(stat, addStat(stat.getType(), a.statValue(stat), b.statValue(stat)));

		for (final AbilityResource resource : base.supportedResources())
			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				cache.setResourceStatValue(resource, stat, addStat(stat.getType(), a.resourceStatValue(resource, stat), b.resourceStatValue(resource, stat)));
	}

	private void multiplyStatBlocks(final IStatBlock a, final IStatBlock b, final StatBlockValueCache cache) {
		for (final Stat stat : Stat.values())
			cache.setStatValue(stat, multiplyStat(stat.getType(), a.statValue(stat), b.statValue(stat)));

		for (final AbilityResource resource : base.supportedResources())
			for (final AbilityResourceStat stat : AbilityResourceStat.values())
				cache.setResourceStatValue(resource, stat, multiplyStat(stat.getType(), a.resourceStatValue(resource, stat), b.resourceStatValue(resource, stat)));
	}

	private Number addStat(final StatType type, final Number a, final Number b) {
		if (type == StatType.INTEGER)
			return addInteger(a, b);
		else if (type == StatType.DOUBLE)
			return addDouble(a, b);
		else
			throw new RuntimeException("Unexpected stat type");
	}

	private Number multiplyStat(final StatType type, final Number a, final Number b) {
		if (type == StatType.INTEGER)
			return multiplyInteger(a, b);
		else if (type == StatType.DOUBLE)
			return multiplyDouble(a, b);
		else
			throw new RuntimeException("Unexpected stat type");
	}

	private Number addInteger(final Number a, final Number b) {
		return a.intValue() + b.intValue();
	}

	private Number multiplyInteger(final Number a, final Number b) {
		return a.intValue() * b.intValue();
	}

	private Number addDouble(final Number a, final Number b) {
		return a.doubleValue() + b.doubleValue();
	}

	private Number multiplyDouble(final Number a, final Number b) {
		return a.doubleValue() * b.doubleValue();
	}
}
