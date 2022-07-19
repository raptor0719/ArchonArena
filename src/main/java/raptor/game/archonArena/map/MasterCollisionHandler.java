package raptor.game.archonArena.map;

import raptor.engine.collision.api.ICollisionPlaneHandler;
import raptor.engine.collision.geometry.CollisionCircle;
import raptor.engine.game.Terrain;
import raptor.engine.game.entity.IEntity;
import raptor.engine.util.geometry.DoubleVector;
import raptor.engine.util.geometry.Point;
import raptor.engine.util.geometry.Vector;
import raptor.engine.util.geometry.api.ICircle;
import raptor.engine.util.geometry.api.IPoint;
import raptor.game.archonArena.unit.Unit;
import raptor.game.archonArena.unit.UnitState;

public class MasterCollisionHandler implements ICollisionPlaneHandler {
	@Override
	public void handleEntityCollision(final long planeId, final IEntity a, final IEntity b) {
		final Unit unitA = (Unit)a;
		final Unit unitB = (Unit)b;

		final ICircle myCircle = ((CollisionCircle)unitA.getCollision(0)).getCollision();
		final ICircle entityCircle = ((CollisionCircle)unitB.getCollision(0)).getCollision();

		final IPoint myOrigin = myCircle.getOrigin();
		final IPoint entityOrigin = entityCircle.getOrigin();

		// Get the distance D I need to move back
		//  Get current distance from my origin to your origin (this is our current pos)
		//  Get sum of radii
		final double currentSpaceBetween = Point.distanceTo(myOrigin.getX(), myOrigin.getY(), entityOrigin.getX(), entityOrigin.getY());
		final int targetSpaceBetween = myCircle.getRadius() + entityCircle.getRadius();

		final double amountToMove = targetSpaceBetween - currentSpaceBetween;

		// Get the direction A I need to move back
		// Move back D units in direction A
		//  set x/y of unit
		//  set x/y of navagent
		final Vector vectorFromYoursToMine = new Vector(myOrigin.getX() - entityOrigin.getX(), myOrigin.getY() - entityOrigin.getY());
		final double currentDistanceMagnitude = vectorFromYoursToMine.getMagnitude();
		final double movementFactor = amountToMove/currentDistanceMagnitude;
		final double shareResolutionFactor = (shareResolution) ? 0.5 : 1;
		final DoubleVector movementVector = new DoubleVector(vectorFromYoursToMine.getX() * movementFactor * shareResolutionFactor, vectorFromYoursToMine.getY() * movementFactor * shareResolutionFactor);

		final int newX = (int)Math.round(myOrigin.getX() + movementVector.getX());
		final int newY = (int)Math.round(myOrigin.getY() + movementVector.getY());

		this.setX(newX);
		this.setY(newY);

		if (shareResolution) {
			final Vector vectorFromMineToYours = new Vector(entityOrigin.getX() - myOrigin.getX(), entityOrigin.getY() - myOrigin.getY());
			final DoubleVector yourMovementVector = new DoubleVector(vectorFromMineToYours.getX() * movementFactor * shareResolutionFactor, vectorFromMineToYours.getY() * movementFactor * shareResolutionFactor);

			final int yourNewX = (int)Math.round(entityOrigin.getX() + yourMovementVector.getX());
			final int yourNewY = (int)Math.round(entityOrigin.getY() + yourMovementVector.getY());

			entityUnit.setX(yourNewX);
			entityUnit.setY(yourNewY);
			entityUnit.setState(UnitState.WAIT);
		}
	}

	@Override
	public void handleTerrainCollision(final long planeId, final IEntity entity, final Terrain terrain) {
		// TODO
	}
}
