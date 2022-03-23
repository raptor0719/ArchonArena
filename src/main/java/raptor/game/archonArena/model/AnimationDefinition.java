package raptor.game.archonArena.model;

public class AnimationDefinition {
	private final String name;
	private final String[] frames;
	private final int[] holds;
	private final boolean[] isActivation;

	public AnimationDefinition(final String name, final String[] frames, final int[] holds, final boolean[] isActivation) {
		this.name = name;
		this.frames = frames;
		this.holds = holds;
		this.isActivation = isActivation;

		validate();
	}

	public String getName() {
		return name;
	}

	public int getLength() {
		return frames.length;
	}

	public String getFrame(final int index) {
		return frames[index];
	}

	public int getHolds(final int index) {
		return holds[index];
	}

	public boolean getActivation(final int index) {
		return isActivation[index];
	}

	public Animation getInstance() {
		return new Animation(this);
	}

	private void validate() {
		if (frames.length != holds.length || frames.length != isActivation.length)
			throw new IllegalArgumentException("Invalid AnimationDefinition. Length of frames, holds, and activations must be equal.");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnimationDefinition other = (AnimationDefinition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
