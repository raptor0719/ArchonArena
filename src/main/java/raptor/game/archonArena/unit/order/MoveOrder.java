package raptor.game.archonArena.unit.order;

public class MoveOrder implements IOrder {
	private final int destinationX;
	private final int destinationY;

	public MoveOrder(final int destinationX, final int destinationY) {
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}

	public int getDestinationX() {
		return destinationX;
	}

	public int getDestinationY() {
		return destinationY;
	}
}
