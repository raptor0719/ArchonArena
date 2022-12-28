package raptor.game.archonArena.unit.stats;

import raptor.game.archonArena.unit.stats.blocks.CompositeStatBlock;
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

	}
}
