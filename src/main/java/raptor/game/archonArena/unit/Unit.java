package raptor.game.archonArena.unit;

import java.util.LinkedList;
import java.util.Queue;

import raptor.engine.collision.geometry.CollisionCircle;
import raptor.engine.display.render.BasicColor;
import raptor.engine.display.render.IColor;
import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Game;
import raptor.engine.nav.agent.DefaultNavAgent;
import raptor.engine.nav.api.INavAgent;
import raptor.engine.nav.api.INavigator;
import raptor.engine.util.geometry.Circle;
import raptor.engine.util.geometry.Point;
import raptor.engine.util.geometry.api.IPoint;
import raptor.game.archonArena.entity.AnimatedEntity;
import raptor.game.archonArena.main.ArchonArena;
import raptor.game.archonArena.unit.basicAttack.BasicAttack;
import raptor.game.archonArena.unit.order.AttackOrder;
import raptor.game.archonArena.unit.order.IOrder;
import raptor.game.archonArena.unit.order.MoveOrder;
import raptor.game.archonArena.unit.stats.StatBlock;

public class Unit extends AnimatedEntity {
	private static final int MAX_BUMP_TICK_COUNT = 5;
	private static final IColor HEALTH_BAR_COLOR = new BasicColor(0, 255, 0, 255);

	private final UnitDefinition definition;

	private final INavAgent navAgent;
	private final Queue<IOrder> orderQueue;

	private final StatBlock statBlock;

	private final BasicAttack basicAttack;

	private UnitState currentState;

	private IOrder currentOrder;
	private boolean isNewOrder;

	private int teamId;

	private int basicAttackCooldown;

	// Bump Control
	private int bumpTickCount;
	private int currentBumpLength;

	// Stuck detection
	private static final int STUCK_MAX_COUNTER = 34;
	private Point stuckPosition;
	private int stuckCounter;

	public Unit(final UnitDefinition definition, final INavigator navigator, final IPoint startPosition, final int teamId) {
		super(Game.getCurrentLevel().getEntityIdProvider().get(), definition.getName(), definition.getModelDefintion().getModelInstance(), definition.getSelectableWidth(), definition.getSelectableHeight());

		this.definition = definition;

		this.setCollision(0, new CollisionCircle(new Circle(this.getPosition(), definition.getUnitSizeRadius())));

		this.navAgent = new DefaultNavAgent(navigator);
		this.orderQueue = new LinkedList<IOrder>();

		this.statBlock = new StatBlock(definition.getBaseStatBlock());

		this.basicAttack = definition.getBasicAttackDefinition().getInstance();

		this.currentState = UnitState.WAIT;

		this.currentOrder = null;

		super.setX(startPosition.getX());
		super.setY(startPosition.getY());
		navAgent.setPosition(getX(), getY());

		this.teamId = teamId;

		this.basicAttackCooldown = 0;

		this.bumpTickCount = Math.min(MAX_BUMP_TICK_COUNT, statBlock.getAttackSpeed());
		this.currentBumpLength = 0;

		this.stuckPosition = new Point(this.getPosition().getX(), this.getPosition().getY());
		this.stuckCounter = 0;
	}

	@Override
	public void update(final double tickCount) {
		basicAttackCooldown -= tickCount;

		if (currentState == UnitState.MOVE) {
			if (this.getPosition().equals(stuckPosition)) {
				stuckCounter++;
			} else {
				stuckPosition.setX(this.getPosition().getX());
				stuckPosition.setY(this.getPosition().getY());
				stuckCounter = 0;
			}

			if (stuckCounter >= STUCK_MAX_COUNTER) {
				stopOrder();
				stuckCounter = 0;
			}
		}

		if (currentOrder == null && orderQueue.isEmpty()) {
			currentState = UnitState.WAIT;
			return;
		}

		if (currentOrder == null) {
			currentOrder = orderQueue.poll();
			isNewOrder = true;
		}

		if (currentOrder instanceof MoveOrder)
			handleMoveOrder(isNewOrder, tickCount);

		if (currentOrder instanceof AttackOrder)
			handleAttackOrder(tickCount);
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
	public void draw(final IGraphics graphics) {
		if (ArchonArena.getCurrentArchonArenaLevel().getVisionCalculator().hasVision(ArchonArena.getPlayer().getTeamId(), this.getId())) {
			super.draw(graphics);

			final float healthPercentage = statBlock.getCurrentHealthPercentage();
			final int totalWidth = definition.getSelectableWidth();
			final int healthBarWidth = (int)(totalWidth * healthPercentage);
			final int healthBarHeight = 5;

			graphics.drawRectangle(getX(), getY() - healthBarHeight, healthBarWidth, healthBarHeight, true, HEALTH_BAR_COLOR);
		}
	}

	public void damage(final int amount) {
		statBlock.applyDamage(amount);
	}

	public boolean isDead() {
		return statBlock.getCurrentHealth() <= 0;
	}

	public UnitState getState() {
		return currentState;
	}

	public void setState(final UnitState newState) {
		this.currentState = newState;
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

	public void attackOrder(final Unit target, final boolean queue) {
		final AttackOrder attackOrder = new AttackOrder(target);
		if (queue) {
			orderQueue.add(attackOrder);
			return;
		}

		orderQueue.clear();
		currentOrder = attackOrder;
		isNewOrder = true;
	}

	public void stopOrder() {
		orderQueue.clear();
		currentOrder = null;
		currentState = UnitState.WAIT;
	}

	public int getTeam() {
		return teamId;
	}

	public StatBlock getStatBlock() {
		return statBlock;
	}

	public UnitDefinition getDefinition() {
		return definition;
	}

	// INTERNALS

	private void handleMoveOrder(final boolean setup, final double tickCount) {
		if (setup) {
			final MoveOrder moveOrder = (MoveOrder)currentOrder;
			setupMove(moveOrder.getDestinationX(), moveOrder.getDestinationY());
			isNewOrder = false;
		}

		move(tickCount);

		if (navAgent.atDestination())
			finishOrder();
	}

	private void setupMove(final int destinationX, final int destinationY) {
		navAgent.setPosition(getX(), getY());
		navAgent.setDestination(destinationX, destinationY);
	}

	private void move(final double tickCount) {
		currentState = UnitState.MOVE;

		navAgent.move(statBlock.getMoveSpeed() * tickCount);

		super.setX(navAgent.getPositionX());
		super.setY(navAgent.getPositionY());
	}

	private void handleAttackOrder(final double tickCount) {
		final AttackOrder attackOrder = (AttackOrder)currentOrder;
		final Unit target = attackOrder.getTarget();

		if (!ArchonArena.getCurrentArchonArenaLevel().getVisionCalculator().hasVision(ArchonArena.getPlayer().getTeamId(), target.getId()) || target.isDead())
			finishOrder();

		if (Point.distanceTo(getX(), getY(), target.getX(), target.getY()) > basicAttack.getRange()) {
			setupMove(target.getX(), target.getY());
			move(tickCount);
			return;
		}

		currentState = UnitState.ATTACK;

		currentBumpLength += tickCount;

		if (currentBumpLength >= bumpTickCount) {
			getAnimatedModel().stopBump();
			currentBumpLength = 0;
		}

		if (basicAttackCooldown > 0)
			return;

		if (currentBumpLength == 0) {
			getAnimatedModel().bumpTowardPoint(target.getX(), target.getY());
			basicAttackCooldown = statBlock.getAttackSpeed();
			basicAttack.executeAttack(this, target);
		}
	}

	private void finishOrder() {
		currentOrder = null;
	}
}
