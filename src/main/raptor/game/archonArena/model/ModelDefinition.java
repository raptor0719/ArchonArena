package raptor.game.archonArena.model;

import java.util.Map;

import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.Model;
import raptor.engine.model.SpriteCollection;
import raptor.engine.model.SpriteModel;
import raptor.engine.model.WireModel;

public class ModelDefinition {
	private final WireModel wireModel;
	private final Map<String, DirectionalSprite> hardpointVisuals;

	public ModelDefinition(final WireModel wireModel, final Map<String, DirectionalSprite> hardpointVisuals) {
		this.wireModel = wireModel;
		this.hardpointVisuals = hardpointVisuals;
	}

	public Model getModelInstance() {
		final SpriteModel spriteModel = new SpriteModel();

		for (final Map.Entry<String, DirectionalSprite> entries : hardpointVisuals.entrySet()) {
			spriteModel.addMapping(entries.getKey());

			final SpriteCollection collection = spriteModel.getSpriteCollection(entries.getKey());
			collection.addCollectionOnTop(entries.getValue());
		}

		return new Model(wireModel, spriteModel);
	}
}
