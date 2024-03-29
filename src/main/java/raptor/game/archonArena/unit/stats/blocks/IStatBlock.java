package raptor.game.archonArena.unit.stats.blocks;

import raptor.game.archonArena.unit.stats.AbilityResource;
import raptor.game.archonArena.unit.stats.AbilityResourceStat;
import raptor.game.archonArena.unit.stats.Stat;

public interface IStatBlock {
	Number statValue(Stat stat);
	boolean usesResource(AbilityResource resource);
	Number resourceStatValue(AbilityResource resource, AbilityResourceStat stat);
}
