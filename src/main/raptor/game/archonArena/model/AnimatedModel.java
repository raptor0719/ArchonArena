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
	private int currentFrameIndex;
	private int currentHold;
	private boolean deliveredActivationFrame;
	private boolean loop;

	public AnimatedModel(final WireModel wireModel, final Map<String, DirectionalSprite> defaultVisuals, final List<AnimationDefinition> animations, final AnimationDefinition defaultAnimation) {
		super(wireModel, defaultVisuals);

		if (!animations.contains(defaultAnimation))
			throw new IllegalArgumentException("The default animation must exist in the provided animations.");

		this.animations = new HashMap<>();
		for (final AnimationDefinition definition : animations) {
			final Animation instance = definition.getInstance();
			this.animations.put(instance.getName(), instance);
		}

		this.defaultAnimation = defaultAnimation;

		this.currentAnimation = getDefaultAnimation();
		this.deliveredActivationFrame = false;
		this.currentFrameIndex = 0;
		this.currentHold = 0;
		this.loop = false;

		setFrame();
	}

	public void advanceFrame() {
		currentHold++;

		if (currentHold < currentAnimation.getHolds(currentFrameIndex))
			return;

		currentFrameIndex++;
		deliveredActivationFrame = false;
		currentHold = 0;

		if (currentFrameIndex < currentAnimation.getLength()) {
			setFrame();
			return;
		}

		currentAnimation = (loop) ? currentAnimation : getDefaultAnimation();
		currentFrameIndex = 0;

		setFrame();
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

	private void setFrame() {
		super.setFrame(currentAnimation.getFrame(currentFrameIndex));
	}

	private void play(final String animationName, final boolean loopAnimation) {
		final Animation toPlay = animations.get(animationName);

		if (toPlay == null)
			return;

		currentAnimation = toPlay;
		deliveredActivationFrame = false;
		currentFrameIndex = 0;
		currentHold = 0;
		loop = loopAnimation;

		setFrame();
	}

	private Animation getDefaultAnimation() {
		return animations.get(defaultAnimation.getName());
	}
}
