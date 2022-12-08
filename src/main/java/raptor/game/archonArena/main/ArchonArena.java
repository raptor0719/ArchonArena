package raptor.game.archonArena.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import raptor.engine.display.render.IRenderer;
import raptor.engine.game.Game;
import raptor.engine.model.Sprite;
import raptor.engine.ui.UserInterface;
import raptor.engine.ui.input.IInputManager;
import raptor.engine.ui.input.IMousePositionPoll;
import raptor.game.archonArena.asset.AnimatedModelLibrary;
import raptor.game.archonArena.asset.SpriteLibrary;
import raptor.game.archonArena.map.ArchonArenaLevel;
import raptor.game.archonArena.menu.MainMenu;
import raptor.game.archonArena.model.AnimatedModelDefinition;
import raptor.game.archonArena.ui.ArchonArenaUserInterface;

public class ArchonArena extends Game {
	public static final int MAX_TEAM_COUNT = 8;

	private static final String SPRITE_LIBRARY_SUBPATH = "/art/sprite/";

	private static AnimatedModelLibrary modelLibrary = null;
	private static SpriteLibrary spriteLibrary = null;
	private static ArchonArenaUserInterface archonArenaUserInterface = null;
	private static Player player;

	public ArchonArena(final String assetDirectoryPath, final IRenderer setRenderer, final IInputManager inputManager, final IMousePositionPoll mousePositionPoll) {
		super(new MainMenu(), setRenderer, inputManager, mousePositionPoll);

		ArchonArena.spriteLibrary = buildSpriteLibrary(assetDirectoryPath + SPRITE_LIBRARY_SUBPATH);
		ArchonArena.modelLibrary = buildAnimationModelLibrary(ArchonArena.spriteLibrary);
		ArchonArena.player = new Player();
	}

	@Override
	protected UserInterface buildUserInterface(final IInputManager inputManager, final IMousePositionPoll mousePositionPoll) {
		ArchonArena.archonArenaUserInterface = new ArchonArenaUserInterface(inputManager, mousePositionPoll);
		return ArchonArena.archonArenaUserInterface;
	}

	public static ArchonArenaUserInterface getArchonArenaUserInterface() {
		return ArchonArena.archonArenaUserInterface;
	}

	public static AnimatedModelLibrary getModelLibrary() {
		return modelLibrary;
	}

	public static SpriteLibrary getSpriteLibrary() {
		return spriteLibrary;
	}

	public static Player getPlayer() {
		return player;
	}

	public static ArchonArenaLevel getCurrentArchonArenaLevel() {
		return (ArchonArenaLevel)Game.getCurrentLevel();
	}

	private static AnimatedModelLibrary buildAnimationModelLibrary(final SpriteLibrary spriteLibrary) {
		final List<AnimatedModelDefinition> definitions = new ArrayList<AnimatedModelDefinition>();

		definitions.add(new AnimatedModelDefinition("Torin", spriteLibrary.getSprite("Torin"), 40, 40));

		return new AnimatedModelLibrary(definitions);
	}

	private static SpriteLibrary buildSpriteLibrary(final String directoryPath) {
		final File spriteDirectory = new File(directoryPath);

		if (!spriteDirectory.isDirectory())
			throw new IllegalArgumentException(String.format("Given directory path `%s' was not a directory.", directoryPath));

		final List<SpriteDefinition> definitions = new ArrayList<SpriteDefinition>();
		definitions.add(new SpriteDefinition("Torin", 10, 10));

		final Map<String, Sprite> spriteMap = new HashMap<String, Sprite>();

		final FilePrefixFilter filter = new FilePrefixFilter();
		for (final SpriteDefinition definition : definitions) {
			filter.setPrefix(definition.getName());
			final File[] files = spriteDirectory.listFiles(filter);

			if (files == null || files.length < 1)
				throw new IllegalArgumentException(String.format("Was looking for image file to match `%s.*' but didn't find one.", definition.getName()));

			if (files.length > 1)
				throw new IllegalArgumentException(String.format("Was looking for image file to match `%s.*' but found multiple.", definition.getName()));

			try {
				final BufferedImage image = ImageIO.read(files[0]);

				spriteMap.put(definition.getName(), new Sprite(definition.getxDraw(), definition.getyDraw(), image));
			} catch (final Exception e) {
				throw new RuntimeException("Error creating sprite library entry.", e);
			}
		}

		return new SpriteLibrary(spriteMap);
	}

	private static class SpriteDefinition {
		private final String name;
		private final int xDraw;
		private final int yDraw;

		public SpriteDefinition(final String name, final int xDraw, final int yDraw) {
			this.name = name;
			this.xDraw = xDraw;
			this.yDraw = yDraw;
		}

		public String getName() {
			return name;
		}

		public int getxDraw() {
			return xDraw;
		}

		public int getyDraw() {
			return yDraw;
		}
	}

	private static class FilePrefixFilter implements FilenameFilter {
		private String prefix;

		public FilePrefixFilter() {
			this.prefix = "";
		}

		@Override
		public boolean accept(final File dir, final String name) {
			final String suffix = name.substring(prefix.length(), name.length());

			return name.startsWith(prefix) && suffix.startsWith(".");
		}

		public void setPrefix(final String prefix) {
			this.prefix = prefix;
		}
	}

	private static String buildSpriteName(final String fileName) {
		if (fileName == null || fileName.trim().isEmpty())
			throw new IllegalArgumentException(String.format("Sprite file with name `%s' is illegal.", fileName));

		final int suffixIndex = fileName.indexOf(".");

		final String spriteName = (suffixIndex < 0) ? fileName : fileName.substring(0, suffixIndex);

		if (spriteName == null || spriteName.trim().isEmpty())
			throw new IllegalArgumentException(String.format("Sprite file with name `%s' is illegal.", fileName));

		return spriteName;
	}
}
