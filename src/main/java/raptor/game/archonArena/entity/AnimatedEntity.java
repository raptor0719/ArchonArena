package raptor.game.archonArena.entity;

import raptor.engine.game.entity.Entity;
import raptor.game.archonArena.model.AnimatedModel;

public abstract class AnimatedEntity extends Entity {
	private final AnimatedModel animatedModel;

	public AnimatedEntity(long id, String name, AnimatedModel model, int width, int height) {
		super(id, name, model, width, height);
		this.animatedModel = model;
	}

	public AnimatedModel getAnimatedModel() {
		return animatedModel;
	}
}
