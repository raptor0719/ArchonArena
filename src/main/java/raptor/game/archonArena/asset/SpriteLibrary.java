package raptor.game.archonArena.asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.SpriteCollection;

public class SpriteLibrary {
	private final Map<String, SpriteCollection> sprites;

	public SpriteLibrary(final List<SpriteCollection> sprites) {
		this.sprites = buildMap(sprites);
	}

	public SpriteCollection getSprite(final String name) {
		return sprites.get(name);
	}

	private Map<String, SpriteCollection> buildMap(final List<SpriteCollection> sprites) {
		final Map<String, SpriteCollection> spriteMap = new HashMap<>();

		for (final SpriteCollection s : sprites)
			spriteMap.put(s.getName(), s);

		return spriteMap;
	}
}
