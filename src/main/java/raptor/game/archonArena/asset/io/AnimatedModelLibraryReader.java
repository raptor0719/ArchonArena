package raptor.game.archonArena.asset.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import raptor.engine.model.Direction;
import raptor.engine.model.DirectionalWireModelFrame;
import raptor.engine.model.Hardpoint;
import raptor.engine.model.WireModel;
import raptor.engine.model.WireModelFrame;
import raptor.game.archonArena.asset.AnimatedModelLibrary;
import raptor.game.archonArena.asset.SpriteLibrary;
import raptor.game.archonArena.model.AnimatedModelDefinition;

public class AnimatedModelLibraryReader {
	public static AnimatedModelLibrary read(final String modelDirectoryPath, final SpriteLibrary spriteLibrary) {
		final File modelDirectory = new File(modelDirectoryPath);

		if (!modelDirectory.exists())
			throw new IllegalArgumentException(String.format("The directory %s does not exist.", modelDirectoryPath));
		if (!modelDirectory.isDirectory())
			throw new IllegalArgumentException(String.format("Expected a directory, but %s was a file.", modelDirectoryPath));

		final File[] files = modelDirectory.listFiles(new ModelMakerFileNameFilter());

		final List<AnimatedModelDefinition> modelDefinitions = new ArrayList<>();

		for (final File file : files) {
			final AnimatedModelDefinition definition = createDefinition(file);

			if (definition != null)
				modelDefinitions.add(definition);
		}

		return new AnimatedModelLibrary(modelDefinitions);
	}

	private static AnimatedModelDefinition createDefinition(final File file) {
		InputStream istream = null;

		try {
			istream = new FileInputStream(file);

			final raptor.modelMaker.model.Model model = raptor.modelMaker.model.io.ModelReader.read(istream);

			final WireModel wireModel = buildWireModel(model);

			// TODO: Finish implementing model maker model to archon arena model bridge
			// need SpriteModel and animation list

			return new AnimatedModelDefinition(model.getName(), wireModel, null, null, "idle1");
		} catch (final Throwable t) {
			System.err.println(String.format("Failed to read model from file: %s", file.getAbsolutePath()));
			t.printStackTrace(System.err);

			return null;
		} finally {
			try {
				if (istream != null)
					istream.close();
			} catch (final Exception e) {
				/* swallow */
			}
		}
	}

	private static WireModel buildWireModel(final raptor.modelMaker.model.Model model) {
		final List<DirectionalWireModelFrame> frames = new ArrayList<>();

		for (final raptor.modelMaker.model.Frame frame : model.getFrames())
			frames.add(buildDirectionalWireModelFrame(frame));

		return new WireModel(model.getName(), frames);
	}

	private static DirectionalWireModelFrame buildDirectionalWireModelFrame(final raptor.modelMaker.model.Frame frame) {
		final Map<Direction, WireModelFrame> framesMap = new HashMap<>();

		framesMap.put(Direction.LEFT, buildWireModelFrame(frame, true));
		framesMap.put(Direction.RIGHT, buildWireModelFrame(frame, false));

		return new DirectionalWireModelFrame(frame.getName(), framesMap);
	}

	private static WireModelFrame buildWireModelFrame(final raptor.modelMaker.model.Frame frame, final boolean isLeft) {
		final List<Hardpoint> hardpoints = new ArrayList<>();

		final int directionModifier = (isLeft) ? -1 : 1;

		for (final Map.Entry<String, raptor.modelMaker.model.Frame.SavedHardpointPosition> savedPosition : frame.getSavedPositions().entrySet()) {
			final raptor.modelMaker.model.Frame.SavedHardpointPosition position = savedPosition.getValue();
			hardpoints.add(new Hardpoint(savedPosition.getKey(), position.getX() * directionModifier, position.getY(), position.getRot() * directionModifier, position.getDepth(), position.getSpritePhase()));
		}

		return new WireModelFrame(hardpoints.toArray(new Hardpoint[hardpoints.size()]));
	}

	private static class ModelMakerFileNameFilter implements FilenameFilter {
		@Override
		public boolean accept(final File dir, final String name) {
			return name.endsWith(raptor.modelMaker.model.io.ModelWriter.MODEL_FILE_EXTENSION);
		}
	}
}
