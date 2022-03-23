package raptor.game.archonArena.unit;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;

import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Game;
import raptor.engine.game.Terrain;
import raptor.engine.game.entity.Entity;
import raptor.engine.game.entity.IEntity;
import raptor.engine.nav.agent.DefaultNavAgent;
import raptor.engine.nav.api.INavAgent;
import raptor.engine.nav.api.INavigator;
import raptor.engine.util.geometry.DoubleVector;
import raptor.game.archonArena.unit.order.IOrder;
import raptor.game.archonArena.unit.order.MoveOrder;

public class Unit extends Entity {
	private static final DoubleVector NORTH = new DoubleVector(0, -100);

	private final UnitDefinition definition;

	private final INavAgent navAgent;
	private final Queue<IOrder> orderQueue;

	private final BufferedImage _TEMP_sprite;

	private IOrder currentOrder;
	private boolean isNewOrder;

	public Unit(final UnitDefinition definition, final INavigator navigator) {
		super(Game.getCurrentLevel().getEntityIdProvider().get(), definition.getName(), definition.getModelDefintion().getModelInstance());

		this.definition = definition;

		this.navAgent = new DefaultNavAgent(navigator);
		this.orderQueue = new LinkedList<IOrder>();

		try {
			this._TEMP_sprite = ImageIO.read(new File("test-unit-sprite.png"));
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}

		this.currentOrder = null;
	}

	@Override
	public void update() {
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
	public void draw(final IGraphics graphics) {
		graphics.drawImage(_TEMP_rotateImage(_TEMP_sprite, getFacingInDegrees()),
				UnitPositionToLowLevelCoordinateTranslator.translatePositionX(this),
				UnitPositionToLowLevelCoordinateTranslator.translatePositionY(this));
	}

	private BufferedImage _TEMP_rotateImage(final BufferedImage image, final int degrees) {
		final int width = image.getWidth();
		final int height = image.getHeight();

		final double radians = Math.toRadians(degrees);

		final BufferedImage rotated = new BufferedImage(width, height, image.getType());

		final Graphics2D g = rotated.createGraphics();

		g.rotate(radians, width/2, height/2);
		g.drawImage(image, null, 0, 0);

		return rotated;
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
	}

	private int calculateFacingInDegrees(final DoubleVector facingVector) {
		return (int) (Math.toDegrees(facingVector.getAngleBetween(NORTH)) * ((facingVector.getX() < 0) ? -1 : 1));
	}
}
