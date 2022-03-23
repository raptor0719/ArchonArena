package raptor.game.archonArena.asset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.game.archonArena.model.AnimatedModelDefinition;

public class AnimatedModelLibrary {
	private final Map<String, AnimatedModelDefinition> models;

	public AnimatedModelLibrary(final List<AnimatedModelDefinition> definitions) {
		this.models = buildMap(definitions);
	}

	public AnimatedModelDefinition getDefinition(final String name) {
		return models.get(name);
	}

	private Map<String, AnimatedModelDefinition> buildMap(final List<AnimatedModelDefinition> definitions) {
		final Map<String, AnimatedModelDefinition> map = new HashMap<>();

		for (final AnimatedModelDefinition d : definitions)
			map.put(d.getName(), d);

		return map;
	}
}
