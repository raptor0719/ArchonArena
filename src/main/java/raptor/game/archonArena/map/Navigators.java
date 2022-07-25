package raptor.game.archonArena.map;

public enum Navigators {
	GROUND(0, "ground");

	private final int id;
	private final String name;

	private Navigators(final int id, final String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
