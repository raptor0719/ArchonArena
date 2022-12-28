package raptor.game.archonArena.unit.stats.blocks;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;

public interface IStatBlock {
	Object statValue(Stat stat);
	boolean usesResource(AbilityResource resource);
	Object resourceStatValue(AbilityResource resource, AbilityResourceStat stat);
}
