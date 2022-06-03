package raptor.game.archonArena.asset.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.Direction;
import raptor.engine.model.DirectionalSprite;
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
		final Map<String, DirectionalSprite> phases = new HashMap<>();

		for (final String phase : source.getPhases())
			phases.put(phase, convertDirectionalSprite(source.getSprite(phase)));

		return new SpriteCollection(source.getName(), phases);
	}

	private static DirectionalSprite convertDirectionalSprite(final raptor.modelMaker.spriteLibrary.DirectionalSprite source) {
		final Map<Direction, Sprite> sprites = new HashMap<>();

		for (final raptor.modelMaker.model.ViewDirection direction : raptor.modelMaker.model.ViewDirection.values())
			sprites.put(convertDirection(direction), convertSprite(source.getSprite(direction)));

		return new DirectionalSprite(sprites);
	}

	private static Sprite convertSprite(final raptor.modelMaker.spriteLibrary.Sprite source) {
		return new Sprite(source.getAttachmentPoint().getX(), source.getAttachmentPoint().getY(), source.getImage());
	}

	private static Direction convertDirection(final raptor.modelMaker.model.ViewDirection source) {
		return Direction.values()[source.ordinal()];
	}
}
