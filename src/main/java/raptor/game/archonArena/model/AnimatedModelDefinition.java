package raptor.game.archonArena.model;

import java.util.List;

import raptor.engine.model.SpriteModel;
import raptor.engine.model.WireModel;

public class AnimatedModelDefinition {
	private final String name;
	private final WireModel wireModel;
	private final SpriteModel spriteModel;
	private final List<AnimationDefinition> animations;
	private final AnimationDefinition defaultAnimation;
	private final int width;
	private final int height;

	public AnimatedModelDefinition(final String name, final WireModel wireModel, final SpriteModel spriteModel, final List<AnimationDefinition> animations, final String defaultAnimationName, final int width, final int height) {
		this.name = name;
		this.wireModel = wireModel;
		this.spriteModel = spriteModel;
		this.animations = animations;
		this.defaultAnimation = findDefinition(defaultAnimationName);

		if (defaultAnimation == null)
			throw new IllegalArgumentException("The default animation must exist in the provided animations.");

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
		return new AnimatedModel(wireModel, spriteModel, animations, defaultAnimation);
	}

	private AnimationDefinition findDefinition(final String name) {
		for (final AnimationDefinition d : animations)
			if (d.getName().equals(name))
				return d;
		return null;
	}
}
