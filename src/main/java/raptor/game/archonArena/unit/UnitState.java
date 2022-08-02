package raptor.game.archonArena.unit;

public enum UnitState {
	WAIT("idle1"),
	MOVE("walk1"),
	ATTACK("attack1");

	private final String defaultAnimationName;

	private UnitState(final String defaultAnimationName) {
		this.defaultAnimationName = defaultAnimationName;
	}

	public String getDefaultAnimationName() {
		return defaultAnimationName;
	}
}
