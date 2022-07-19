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
	private enum CollisionType {
		CAME_TO(0),
		WENT_TO(1),
		SHARED(0.5);

		public final double resolutionShare;

		private CollisionType(final double resolutionShare) {
			this.resolutionShare = resolutionShare;
		}
	}

	@Override
	public void handleEntityCollision(final long planeId, final IEntity a, final IEntity b) {
		final Unit unitA = (Unit)a;
		final Unit unitB = (Unit)b;

		final ICircle circleA = ((CollisionCircle)unitA.getCollision(0)).getCollision();
		final ICircle circleB = ((CollisionCircle)unitB.getCollision(0)).getCollision();

		final IPoint originA = circleA.getOrigin();
		final IPoint originB = circleB.getOrigin();

		final CollisionType collisionTypeA = resolveCollisionType(unitA.getState(), unitB.getState());
		final CollisionType collisionTypeB = resolveCollisionType(unitB.getState(), unitA.getState());

		final double currentSpaceBetween = Point.distanceTo(originA.getX(), originA.getY(), originB.getX(), originB.getY());
		final int targetSpaceBetween = circleA.getRadius() + circleB.getRadius();

		final double amountToMove = targetSpaceBetween - currentSpaceBetween;

		final double resolutionShareA = collisionTypeA.resolutionShare;
		final double resolutionShareB = collisionTypeB.resolutionShare;

		if (collisionTypeA != CollisionType.CAME_TO)
			resolveCollision(unitA, resolutionShareA, amountToMove, unitB);

		if (collisionTypeB != CollisionType.CAME_TO)
			resolveCollision(unitB, resolutionShareB, amountToMove, unitA);
	}

	private CollisionType resolveCollisionType(final UnitState a, final UnitState b) {
		if (a == UnitState.MOVE && b != UnitState.MOVE)
			return CollisionType.WENT_TO;
		else if (a != UnitState.MOVE && b == UnitState.MOVE)
			return CollisionType.CAME_TO;
		else
			return CollisionType.SHARED;
	}

	private void resolveCollision(final Unit subject, final double resolutionShare, final double amountToMove, final Unit other) {
		final Unit unitSubject = subject;
		final Unit unitOther = other;

		final ICircle subjectCircle = ((CollisionCircle)unitSubject.getCollision(0)).getCollision();
		final ICircle otherCircle = ((CollisionCircle)unitOther.getCollision(0)).getCollision();

		final IPoint subjectOrigin = subjectCircle.getOrigin();
		final IPoint otherOrigin = otherCircle.getOrigin();

		final Vector vectorFromOtherToSubject = new Vector(subjectOrigin.getX() - otherOrigin.getX(), subjectOrigin.getY() - otherOrigin.getY());
		final double currentDistanceMagnitude = vectorFromOtherToSubject.getMagnitude();
		final double movementFactor = amountToMove/currentDistanceMagnitude;
		final DoubleVector movementVector = new DoubleVector(vectorFromOtherToSubject.getX() * movementFactor * resolutionShare, vectorFromOtherToSubject.getY() * movementFactor * resolutionShare);

		final int newX = (int)Math.round(subjectOrigin.getX() + movementVector.getX());
		final int newY = (int)Math.round(subjectOrigin.getY() + movementVector.getY());

		subject.setX(newX);
		subject.setY(newY);
	}

	@Override
	public void handleTerrainCollision(final long planeId, final IEntity entity, final Terrain terrain) {
		// TODO
	}
}
