package raptor.game.archonArena.model;

import raptor.engine.model.Sprite;

public class AnimatedModelDefinition {
	private final String name;
	private final Sprite sprite;
	private final int width;
	private final int height;

	public AnimatedModelDefinition(final String name, final Sprite sprite, final int width, final int height) {
		this.name = name;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public AnimatedModel getModelInstance() {
		return new AnimatedModel(sprite, width, height);
	}
}
