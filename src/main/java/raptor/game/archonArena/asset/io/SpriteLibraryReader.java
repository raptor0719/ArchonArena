package raptor.game.archonArena.asset.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.Sprite;
import raptor.engine.model.SpriteCollection;
import raptor.game.archonArena.asset.SpriteLibrary;

public class SpriteLibraryReader {
	public static SpriteLibrary read(final String filePath) {
		return convertSpriteLibrary(raptor.modelMaker.spriteLibrary.SpriteLibraryReader.read(filePath));
	}

	private static SpriteLibrary convertSpriteLibrary(final raptor.modelMaker.spriteLibrary.SpriteLibrary source) {
		final List<SpriteCollection> spriteCollections = new ArrayList<>();

		for (final raptor.modelMaker.spriteLibrary.SpriteCollection sc : source.getSpriteCollections())
			spriteCollections.add(convertSpriteCollection(sc));

		return new SpriteLibrary(spriteCollections);
	}

	private static SpriteCollection convertSpriteCollection(final raptor.modelMaker.spriteLibrary.SpriteCollection source) {
		final Map<String, Sprite> phases = new HashMap<>();

		for (final String phase : source.getPhases())
			phases.put(phase, convertSprite(source.getSprite(phase)));

		return new SpriteCollection(source.getName(), phases);
	}

	private static Sprite convertSprite(final raptor.modelMaker.spriteLibrary.Sprite source) {
		return new Sprite(source.getAttachmentPoint().getX(), source.getAttachmentPoint().getY(), source.getImage());
	}
}
