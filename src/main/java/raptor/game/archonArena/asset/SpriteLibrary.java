package raptor.game.archonArena.asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.DirectionalSprite;

public class SpriteLibrary {
	private final Map<String, DirectionalSprite> sprites;

	public SpriteLibrary(final List<DirectionalSprite> sprites) {
		this.sprites = buildMap(sprites);
	}

	public DirectionalSprite getSprite(final String name) {
		return sprites.get(name);
	}

	private Map<String, DirectionalSprite> buildMap(final List<DirectionalSprite> sprites) {
		final Map<String, DirectionalSprite> spriteMap = new HashMap<>();

		for (final DirectionalSprite s : sprites)
			spriteMap.put(s.getName(), s);

		return spriteMap;
	}
}
