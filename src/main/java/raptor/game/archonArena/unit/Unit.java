package raptor.game.archonArena.unit;

import java.util.LinkedList;
import java.util.Queue;

import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Game;
import raptor.engine.game.Terrain;
import raptor.engine.game.entity.IEntity;
import raptor.engine.nav.agent.DefaultNavAgent;
import raptor.engine.nav.api.INavAgent;
import raptor.engine.nav.api.INavigator;
import raptor.engine.util.geometry.DoubleVector;
import raptor.game.archonArena.entity.AnimatedEntity;
import raptor.game.archonArena.unit.order.IOrder;
import raptor.game.archonArena.unit.order.MoveOrder;

public class Unit extends AnimatedEntity {
	private static final DoubleVector NORTH = new DoubleVector(0, -100);

	private final UnitDefinition definition;

	private final INavAgent navAgent;
	private final Queue<IOrder> orderQueue;

	private IOrder currentOrder;
	private boolean isNewOrder;

	public Unit(final UnitDefinition definition, final INavigator navigator) {
		super(Game.getCurrentLevel().getEntityIdProvider().get(), definition.getName(), definition.getModelDefintion().getModelInstance(), definition.getWidth(), definition.getHeight());

		this.definition = definition;

		this.navAgent = new DefaultNavAgent(navigator);
		this.orderQueue = new LinkedList<IOrder>();

		this.currentOrder = null;

		getAnimatedModel().loopAnimation("idle1");
	}

	@Override
	public void update() {
		super.update();

		if (currentOrder == null && orderQueue.isEmpty())
			return;

		if (currentOrder == null) {
			currentOrder = orderQueue.poll();
			isNewOrder = true;
		}

		if (currentOrder instanceof MoveOrder)
			move(isNewOrder);
	}

	@Override
	public void handleEntityCollision(long planeId, IEntity entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleTerrainCollision(long planeId, Terrain terrain) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setX(final int x) {
		super.setX(x);
		navAgent.setPosition(x, getY());
	}

	@Override
	public void setY(final int y) {
		super.setY(y);
		navAgent.setPosition(getX(), y);
	}

	@Override
	protected void _draw(final IGraphics graphics) {
//		graphics.drawRectangle(getX(), getY(), definition.getWidth(), definition.getHeight(), false, new BasicColor(0, 255, 0, 100));
	}

	public void moveOrder(final int pointX, final int pointY, final boolean queue) {
		final MoveOrder moveOrder = new MoveOrder(pointX, pointY);
		if (queue) {
			orderQueue.add(moveOrder);
			return;
		}

		orderQueue.clear();
		currentOrder = moveOrder;
		isNewOrder = true;
	}

	public void stopOrder() {
		orderQueue.clear();
		currentOrder = null;
	}

	public UnitDefinition getDefinition() {
		return definition;
	}

	// INTERNALS

	private void move(final boolean setup) {
		if (setup) {
			final MoveOrder moveOrder = (MoveOrder)currentOrder;
			navAgent.setPosition(getX(), getY());
			navAgent.setDestination(moveOrder.getDestinationX(), moveOrder.getDestinationY());
			isNewOrder = false;
			getAnimatedModel().loopAnimation("walk1");
		}

		navAgent.move(definition.getMoveSpeed());

		super.setX(navAgent.getPositionX());
		super.setY(navAgent.getPositionY());
		setFacingInDegrees(calculateFacingInDegrees(navAgent.getFaceVector()));

		if (navAgent.atDestination())
			finishOrder();
	}

	private void finishOrder() {
		currentOrder = null;
		getAnimatedModel().loopAnimation("idle1");
	}

	private int calculateFacingInDegrees(final DoubleVector facingVector) {
		return (int) (Math.toDegrees(facingVector.getAngleBetween(NORTH)) * ((facingVector.getX() < 0) ? -1 : 1));
	}
}
