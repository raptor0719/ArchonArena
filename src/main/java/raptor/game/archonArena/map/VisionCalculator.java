package raptor.game.archonArena.map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import raptor.engine.game.Game;
import raptor.engine.game.entity.IEntity;
import raptor.engine.util.geometry.LineSegment;
import raptor.game.archonArena.unit.Unit;

public class VisionCalculator {
	private final Map<Integer, Set<Long>> visibleToTeam;

	public VisionCalculator(final int teamCount) {
		this.visibleToTeam = new HashMap<>();

		for (int i = 0; i < teamCount; i++)
			visibleToTeam.put(i, new HashSet<>());
	}

	public void calculateVision() {
		for (final Set<Long> entitySet : visibleToTeam.values())
			entitySet.clear();

		for (final IEntity entity : Game.getCurrentLevel().getAllEntities()) {
			if (!(entity instanceof Unit))
				continue;

			final Unit unit = (Unit)entity;

			visibleToTeam.get(unit.getTeam()).add(unit.getId());

			for (final IEntity compare : Game.getCurrentLevel().getAllEntities()) {
				if (entity.getId() == compare.getId())
					continue;
				if (!(compare instanceof Unit))
					continue;

				final Unit compareUnit = (Unit)compare;

				if (unit.getTeam() == compareUnit.getTeam())
					continue;

				final LineSegment visionLine = new LineSegment(unit.getPosition(), compareUnit.getPosition());

				if (visionLine.getLength() > unit.getStatBlock().getVisionRange())
					continue;

				boolean hasVision = true;
				for (final LineSegment wall : Game.getCurrentLevel().getNavigator(Navigators.GROUND.getId()).getWalls()) {
					if (visionLine.intersectsWith(wall)) {
						hasVision = false;
						break;
					}
				}

				if (hasVision)
					visibleToTeam.get(unit.getTeam()).add(compareUnit.getId());
			}
		}
	}

	public boolean hasVision(final int teamId, final long entityId) {
		return visibleToTeam.get(teamId).contains(entityId);
	}
}
