package raptor.game.archonArena.asset.io;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import raptor.engine.model.Direction;
import raptor.engine.model.DirectionalSprite;
import raptor.engine.model.Sprite;
import raptor.engine.util.BinaryDataTools;
import raptor.game.archonArena.asset.SpriteLibrary;

public class SpriteLibraryReader {
	public static final byte[] MAGIC_NUMBER = new byte[] {'s', 'p', 'r', 'i', 't', 'e'};

	public static SpriteLibrary read(final String filePath) {
		try {
			final File file = new File(filePath);

			if (!file.exists() || file.isDirectory())
				throw new IllegalArgumentException(String.format("The file '%s' does not exist.", filePath));

			return read(file.getParentFile(), new FileInputStream(file));
		} catch (final IOException e) {
			throw new RuntimeException(String.format("Failed to read sprite library '%s'", filePath), e);
		}
	}

	private static SpriteLibrary read(final File spriteLibraryDirectory, final InputStream istream) throws IOException {
		final DataInputStream dis = new DataInputStream(istream);

		final byte[] magicNumber = new byte[MAGIC_NUMBER.length];
		dis.readFully(magicNumber);

		if (!Arrays.equals(magicNumber, MAGIC_NUMBER))
			throw new IOException("Given file was not a sprite library file.");

		// Read name, but drop it because we don't use it
		BinaryDataTools.marshalString(dis);

		final List<DirectionalSprite> spriteCollections = new ArrayList<DirectionalSprite>();
		final int spriteCollectionCount = dis.readInt();
		for (int i = 0; i < spriteCollectionCount; i++) {
			final String spriteCollectionName = BinaryDataTools.marshalString(dis);
			final Map<Direction, Sprite> sprites = new HashMap<Direction, Sprite>();

			for (final Direction viewDirection : Direction.values()) {
				final BufferedImage image = readImage(spriteLibraryDirectory, getSpriteImageFileName(spriteCollectionName, viewDirection));

				final int attachX = dis.readInt();
				final int attachY = dis.readInt();

				sprites.put(viewDirection, new Sprite(attachX, attachY, image));
			}

			spriteCollections.add(new DirectionalSprite(spriteCollectionName, sprites));
		}

		return new SpriteLibrary(spriteCollections);
	}

	private static BufferedImage readImage(final File spriteLibraryDirectory, final String fileNameWithoutExtension) throws IOException {
		final File[] files = spriteLibraryDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String fileName) {
				return fileNameWithoutExtension.equals(stripExtension(fileName));
			}
		});

		if (files.length < 1)
			throw new IllegalArgumentException(String.format("No file named %s (excluding extension) in directory.", fileNameWithoutExtension));

		return ImageIO.read(files[0]);
	}

	private static String getSpriteImageFileName(final String spriteCollectionName, final Direction viewDirection) {
		return String.format("%s_%s", spriteCollectionName, viewDirection.getAbbreviation());
	}

	private static String stripExtension(final String fileName) {
		final int dotLocation = fileName.lastIndexOf(".");
		return (dotLocation < 1) ? fileName : fileName.substring(0, dotLocation);
	}
}
