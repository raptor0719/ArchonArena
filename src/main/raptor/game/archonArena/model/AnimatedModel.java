package raptor.game.archonArena.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.Model;
import raptor.engine.model.WireModel;

public class AnimatedModel extends Model {
	private final Map<String, Animation> animations;
	private final AnimationDefinition defaultAnimation;

	private Animation currentAnimation;
	private boolean deliveredActivationFrame;
	private int currentFrameIndex;
	private int currentFrameHolds;
	private int currentHold;
	private boolean loop;

	public AnimatedModel(final WireModel wireModel, final Map<String, DirectionalSprite> defaultVisuals, final List<AnimationDefinition> animations, final AnimationDefinition defaultAnimation) {
		super(wireModel, defaultVisuals);

		this.animations = new HashMap<>();
		for (final AnimationDefinition definition : animations) {
			final Animation instance = definition.getInstance();
			this.animations.put(instance.getName(), instance);
		}

		this.defaultAnimation = defaultAnimation;

		this.currentAnimation = getDefaultAnimation();
		this.deliveredActivationFrame = false;
		this.currentFrameIndex = 0;
		this.currentFrameHolds = this.currentAnimation.getHolds(this.currentFrameIndex);
		this.currentHold = 0;
		this.loop = false;
	}

	public void advanceFrame() {
		// TODO
	}

	public void playAnimation(final String animationName) {
		play(animationName, false);
	}

	public void loopAnimation(final String animationName) {
		play(animationName, true);
	}

	public boolean isActivationFrame() {
		final boolean isActivation = currentAnimation.getActivation(currentFrameIndex) && !deliveredActivationFrame;
		deliveredActivationFrame = true;
		return isActivation;
	}

	private void play(final String animationName, final boolean loopAnimation) {
		final Animation toPlay = animations.get(animationName);

		if (toPlay == null)
			return;

		currentAnimation = toPlay;
		deliveredActivationFrame = false;
		currentFrameIndex = 0;
		currentFrameHolds = currentAnimation.getHolds(currentFrameIndex);
		currentHold = 0;
		loop = loopAnimation;
	}

	private Animation getDefaultAnimation() {
		return animations.get(defaultAnimation.getName());
	}
}
