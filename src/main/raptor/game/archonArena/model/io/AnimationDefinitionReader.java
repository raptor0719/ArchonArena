package raptor.game.archonArena.model.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import raptor.engine.util.BinaryDataTools;
import raptor.game.archonArena.model.AnimationDefinition;

public class AnimationDefinitionReader {
	public static final byte[] MAGIC_NUMBER = new byte[] {'a', 'n', 'i', 'm', 'a', 't', 'i', 'o', 'n'};

	public static AnimationDefinition read(final InputStream istream) throws IOException {
		final DataInputStream dis = new DataInputStream(istream);

		final byte[] magicNumber = new byte[MAGIC_NUMBER.length];
		dis.readFully(magicNumber);

		if (!Arrays.equals(magicNumber, MAGIC_NUMBER))
			throw new IllegalArgumentException("Given stream was not an animation definition format.");

		final String name = BinaryDataTools.marshalString(dis);

		final int length = dis.readInt();

		final String[] frames = readFrames(length, dis);
		final int[] holds = readHolds(length, dis);
		final boolean[] activations = readActivations(length, dis);

		return new AnimationDefinition(name, frames, holds, activations);
	}

	private static String[] readFrames(final int length, final DataInputStream dis) throws IOException {
		final String[] frames = new String[length];

		for (int i = 0; i < length; i++)
			frames[i] = BinaryDataTools.marshalString(dis);

		return frames;
	}

	private static int[] readHolds(final int length, final DataInputStream dis) throws IOException {
		final int[] holds = new int[length];

		for (int i = 0; i < length; i++)
			holds[i] = dis.readInt();

		return holds;
	}

	private static boolean[] readActivations(final int length, final DataInputStream dis) throws IOException {
		final boolean[] activations = new boolean[length];

		for (int i = 0; i < length; i++)
			activations[i] = dis.readBoolean();

		return activations;
	}
}
