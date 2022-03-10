package raptor.game.archonArena.asset.io;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.WireModel;
import raptor.engine.model.WireModelReadWrite;
import raptor.engine.util.BinaryDataTools;
import raptor.game.archonArena.asset.AnimatedModelLibrary;
import raptor.game.archonArena.asset.SpriteLibrary;
import raptor.game.archonArena.model.AnimatedModelDefinition;
import raptor.game.archonArena.model.AnimationDefinition;
import raptor.game.archonArena.model.io.AnimationDefinitionReader;

public class AnimatedModelLibraryReader {
	public static final byte[] MAGIC_NUMBER = new byte[] {'m', 'o', 'd', 'e', 'l', 'l', 'i', 'b', 'r', 'a', 'r', 'y'};

	public static AnimatedModelLibrary read(final String filePath, final SpriteLibrary spriteLibrary) {
		final File file = new File(filePath);

		if (!file.exists() || file.isDirectory())
			throw new IllegalArgumentException(String.format("The file %s does not exist.", filePath));

		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(file);

			return read(fstream, spriteLibrary);
		} catch (final IOException e) {
			throw new RuntimeException("Failed reading AnimatedModelLibrary.", e);
		} finally {
			try {
				if (fstream != null)
					fstream.close();
			} catch (final IOException e) {
				// swallow
			}
		}
	}

	private static AnimatedModelLibrary read(final InputStream istream, final SpriteLibrary spriteLibrary) throws IOException {
		final DataInputStream dis = new DataInputStream(istream);

		final byte[] magicNumber = new byte[MAGIC_NUMBER.length];
		dis.readFully(magicNumber);

		if (!Arrays.equals(magicNumber, MAGIC_NUMBER))
			throw new IllegalArgumentException("Was not an AnimatedModelLibrary format.");

		final List<AnimatedModelDefinition> definitions = readDefinitions(spriteLibrary, dis);

		return new AnimatedModelLibrary(definitions);
	}

	private static List<AnimatedModelDefinition> readDefinitions(final SpriteLibrary spriteLibrary, final DataInputStream dis) throws IOException {
		final int definitionCount = dis.readInt();

		final List<AnimatedModelDefinition> definitions = new ArrayList<>();
		for (int i = 0; i < definitionCount; i++) {
			final String name = BinaryDataTools.marshalString(dis);
			final WireModel wireModel = WireModelReadWrite.read(dis);
			final Map<String, DirectionalSprite> defaultVisuals = readDefaultVisuals(spriteLibrary, dis);
			final List<AnimationDefinition> animations = readAnimations(dis);
			final String defaultAnimationName = BinaryDataTools.marshalString(dis);

			definitions.add(new AnimatedModelDefinition(name, wireModel, defaultVisuals, animations, defaultAnimationName));
		}

		return definitions;
	}

	private static Map<String, DirectionalSprite> readDefaultVisuals(final SpriteLibrary spriteLibrary, final DataInputStream dis) throws IOException {
		final int visualCount = dis.readInt();

		final Map<String, DirectionalSprite> defaultVisuals = new HashMap<>();
		for (int i = 0; i < visualCount; i++) {
			final String hardpointName = BinaryDataTools.marshalString(dis);
			final String spriteName = BinaryDataTools.marshalString(dis);

			final DirectionalSprite sprite = spriteLibrary.getSprite(spriteName);

			if (sprite == null)
				throw new IllegalArgumentException(String.format("The sprite '%s' does not exist in the sprite library", spriteName));

			defaultVisuals.put(hardpointName, sprite);
		}

		return defaultVisuals;
	}

	private static List<AnimationDefinition> readAnimations(final DataInputStream dis) throws IOException {
		final int animationCount = dis.readInt();

		final List<AnimationDefinition> animations = new ArrayList<>();
		for (int i = 0; i < animationCount; i++)
			animations.add(AnimationDefinitionReader.read(dis));

		return animations;
	}
}
