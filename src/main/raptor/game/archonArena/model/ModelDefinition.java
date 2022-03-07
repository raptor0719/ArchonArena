package raptor.game.archonArena.model;

import java.util.Map;

import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.Model;
import raptor.engine.model.WireModel;

public class ModelDefinition {
	private final WireModel wireModel;
	private final Map<String, DirectionalSprite> defaultVisuals;

	public ModelDefinition(final WireModel wireModel, final Map<String, DirectionalSprite> defaultVisuals) {
		this.wireModel = wireModel;
		this.defaultVisuals = defaultVisuals;
	}

	public Model getModelInstance() {
		return new Model(wireModel, defaultVisuals);
	}
}
