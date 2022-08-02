package raptor.game.archonArena.unit.order;

import raptor.game.archonArena.unit.Unit;

public class AttackOrder implements IOrder {
	private final Unit target;

	public AttackOrder(final Unit target) {
		this.target = target;
	}

	public Unit getTarget() {
		return target;
	}
}
