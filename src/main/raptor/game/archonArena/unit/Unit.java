package raptor.game.archonArena.unit;

import raptor.engine.game.Game;
import raptor.engine.game.Terrain;
import raptor.engine.game.entity.Entity;
import raptor.engine.game.entity.IEntity;

public class Unit extends Entity {
	private final UnitDefinition definition;

	public Unit(final UnitDefinition definition) {
		super(Game.getCurrentLevel().getEntityIdProvider().get(), definition.getName(), definition.getModel());

		this.definition = definition;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleEntityCollision(long planeId, IEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTerrainCollision(long planeId, Terrain terrain) {
		// TODO Auto-generated method stub

	}

	public UnitDefinition getDefinition() {
		return definition;
	}
}
