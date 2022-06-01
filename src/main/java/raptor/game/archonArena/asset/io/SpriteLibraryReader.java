package raptor.game.archonArena.asset.io;

import java.io.File;

import raptor.game.archonArena.asset.SpriteLibrary;

public class SpriteLibraryReader {
	public static SpriteLibrary read(final String filePath) {
		final File file = new File(filePath);

		if (!file.exists() || file.isDirectory())
			throw new IllegalArgumentException(String.format("The file '%s' does not exist.", filePath));

		// TODO Create model maker sprite library to archon arena sprite library bridge
		return null;
	}
}
