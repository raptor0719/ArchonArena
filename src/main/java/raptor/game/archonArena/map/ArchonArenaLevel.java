package raptor.game.archonArena.map;

import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Level;
import raptor.game.archonArena.main.ArchonArena;

public class ArchonArenaLevel extends Level {
	private final VisionCalculator visionCalculator;

	public ArchonArenaLevel() {
		this.visionCalculator = new VisionCalculator(ArchonArena.MAX_TEAM_COUNT);
	}

	@Override
	public void draw(final IGraphics graphics) {
		// no-op
	}

	public VisionCalculator getVisionCalculator() {
		return visionCalculator;
	}
}
