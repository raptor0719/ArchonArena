package raptor.game.archonArena.unit.stats;

public class StatBlock {
	private int moveSpeed;

	private int visionRange;

	public StatBlock(final StatBlock copyOf) {
		this.moveSpeed = copyOf.getMoveSpeed();
		this.visionRange = copyOf.getVisionRange();
	}

	public StatBlock(final int moveSpeed, final int visionRange) {
		this.moveSpeed = moveSpeed;
		this.visionRange = visionRange;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public int getVisionRange() {
		return visionRange;
	}
}
