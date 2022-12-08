package raptor.game.archonArena.asset;

import java.util.Map;

import raptor.engine.model.Sprite;

public class SpriteLibrary {
	private final Map<String, Sprite> sprites;

	public SpriteLibrary(final Map<String, Sprite> sprites) {
		this.sprites = sprites;
	}

	public Sprite getSprite(final String name) {
		return sprites.get(name);
	}
}
