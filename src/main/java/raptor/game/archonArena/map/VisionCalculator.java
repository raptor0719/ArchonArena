package raptor.game.archonArena.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import raptor.engine.game.Game;
import raptor.engine.game.entity.IEntity;
import raptor.game.archonArena.unit.Unit;

public class VisionCalculator {
	private final Map<Integer, Set<Long>> visibleToTeam;

	public VisionCalculator(final int teamCount) {
		this.visibleToTeam = new HashMap<>();

		for (int i = 0; i < teamCount; i++)
			visibleToTeam.put(i, new HashSet<>());
	}

	// Calculate if a specific team has vision of a unit
	public void calculateVision() {
		for (final Set<Long> entitySet : visibleToTeam.values())
			entitySet.clear();

		// Go through each unit
		//  Mark the unit as visible to its own team
		for (final IEntity entity : Game.getCurrentLevel().getAllEntities()) {
			if (!(entity instanceof Unit))
				continue;

			final Unit unit = (Unit)entity;

			visibleToTeam.get(unit.getTeam()).add(unit.getId());

			for (final IEntity compare : Game.getCurrentLevel().getAllEntities()) {
				if (entity.getId() == compare.getId())
					continue;

				// Check if in vision range and is not blocked by terrain
			}
		}
	}

	public boolean hasVision(final int teamId, final long entityId) {
		return visibleToTeam.get(teamId).contains(entityId);
	}
}
