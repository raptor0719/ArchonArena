package raptor.game.archonArena.main;

import raptor.engine.display.render.IRenderer;
import raptor.engine.game.Game;
import raptor.engine.ui.input.IInputManager;
import raptor.engine.ui.input.IMousePositionPoll;
import raptor.game.archonArena.asset.AnimatedModelLibrary;
import raptor.game.archonArena.asset.SpriteLibrary;
import raptor.game.archonArena.asset.io.AnimatedModelLibraryReader;
import raptor.game.archonArena.asset.io.SpriteLibraryReader;
import raptor.game.archonArena.menu.MainMenu;

public class ArchonArena extends Game {
	private static final String MODEL_DIRECTORY_SUBPATH = "/art/model/";
	private static final String SPRITE_LIBRARY_SUBPATH = "/art/sprite/ArchonArena.sl";

	private static AnimatedModelLibrary modelLibrary = null;
	private static SpriteLibrary spriteLibrary = null;

	public ArchonArena(final String assetDirectoryPath, final IRenderer setRenderer, final IInputManager inputManager, final IMousePositionPoll mousePositionPoll) {
		super(new MainMenu(), setRenderer, inputManager, mousePositionPoll);

		ArchonArena.spriteLibrary = SpriteLibraryReader.read(assetDirectoryPath + SPRITE_LIBRARY_SUBPATH);
		ArchonArena.modelLibrary = AnimatedModelLibraryReader.read(assetDirectoryPath + MODEL_DIRECTORY_SUBPATH, ArchonArena.spriteLibrary);
	}

	public static AnimatedModelLibrary getModelLibrary() {
		return modelLibrary;
	}

	public static SpriteLibrary getSpriteLibrary() {
		return spriteLibrary;
	}
}
