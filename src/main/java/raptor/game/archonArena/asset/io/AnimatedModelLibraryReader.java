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
import raptor.engine.model.SpriteCollection;
import raptor.engine.model.SpriteModel;
import raptor.engine.model.WireModel;
import raptor.engine.model.WireModelFrame;
import raptor.game.archonArena.asset.AnimatedModelLibrary;
import raptor.game.archonArena.asset.SpriteLibrary;
import raptor.game.archonArena.model.AnimatedModelDefinition;
import raptor.game.archonArena.model.AnimationDefinition;

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
			final AnimatedModelDefinition definition = createDefinition(file, spriteLibrary);

			if (definition != null)
				modelDefinitions.add(definition);
		}

		return new AnimatedModelLibrary(modelDefinitions);
	}

	private static AnimatedModelDefinition createDefinition(final File file, final SpriteLibrary spriteLibrary) {
		InputStream istream = null;

		try {
			istream = new FileInputStream(file);

			final raptor.modelMaker.model.Model model = raptor.modelMaker.model.io.ModelReader.read(istream);

			final WireModel wireModel = buildWireModel(model);
			final SpriteModel spriteModel = buildSpriteModel(model, wireModel, spriteLibrary);
			final List<AnimationDefinition> animations = buildAnimationDefinitions(model);

			return new AnimatedModelDefinition(model.getName(), wireModel, spriteModel, animations, "idle1");
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

	private static SpriteModel buildSpriteModel(final raptor.modelMaker.model.Model source, final WireModel wireModel, final SpriteLibrary spriteLibrary) {
		final Map<String, SpriteCollection> spriteCollections = new HashMap<>();

		for (final raptor.modelMaker.model.Hardpoint sourceHardpoint : source.getHardpoints())
			spriteCollections.put(sourceHardpoint.getName(), spriteLibrary.getSprite(sourceHardpoint.getSpriteCollectionName()));

		return new SpriteModel(spriteCollections);
	}

	private static List<AnimationDefinition> buildAnimationDefinitions(final raptor.modelMaker.model.Model source) {
		final List<AnimationDefinition> animationDefinitions = new ArrayList<>();

		for (final raptor.modelMaker.model.Animation sourceAnimation : source.getAnimations()) {
			final String[] frameNames = new String[sourceAnimation.size()];
			for (int i = 0; i < frameNames.length; i++)
				frameNames[i] = sourceAnimation.getFrame(i);

			final int[] holds = new int[sourceAnimation.size()];
			for (int i = 0; i < holds.length; i++)
				holds[i] = sourceAnimation.getHolds(i);

			final boolean[] activations = new boolean[sourceAnimation.size()];
			for (int i = 0; i < activations.length; i++)
				activations[i] = sourceAnimation.isActivation(i);

			animationDefinitions.add(new AnimationDefinition(sourceAnimation.getName(), frameNames, holds, activations));
		}

		return animationDefinitions;
	}

	/* HELPER CLASSES */

	private static class ModelMakerFileNameFilter implements FilenameFilter {
		@Override
		public boolean accept(final File dir, final String name) {
			return name.endsWith(raptor.modelMaker.model.io.ModelWriter.MODEL_FILE_EXTENSION);
		}
	}
}
