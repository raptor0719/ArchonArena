package raptor.game.archonArena.unit.stats.values;

public interface Leveled {
	int currentLevel();
	int maxLevel();
	void setLevel(int level);
}
