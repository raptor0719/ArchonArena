package raptor.game.archonArena.model;

import java.util.List;
import java.util.Map;

import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.WireModel;

public class AnimatedModelDefinition {
	private final String name;
	private final WireModel wireModel;
	private final Map<String, DirectionalSprite> defaultVisuals;
	private final List<AnimationDefinition> animations;
	private final AnimationDefinition defaultAnimation;

	public AnimatedModelDefinition(final String name, final WireModel wireModel, final Map<String, DirectionalSprite> defaultVisuals, final List<AnimationDefinition> animations, final String defaultAnimationName) {
		this.name = name;
		this.wireModel = wireModel;
		this.defaultVisuals = defaultVisuals;
		this.animations = animations;
		this.defaultAnimation = findDefinition(defaultAnimationName);

		if (defaultAnimation == null)
			throw new IllegalArgumentException("The default animation must exist in the provided animations.");
	}

	public String getName() {
		return name;
	}

	public AnimatedModel getModelInstance() {
		return new AnimatedModel(wireModel, defaultVisuals, animations, defaultAnimation);
	}

	private AnimationDefinition findDefinition(final String name) {
		for (final AnimationDefinition d : animations)
			if (d.getName().equals(name))
				return d;
		return null;
	}
}
