package raptor.game.archonArena.model;

public class Animation {
	private final AnimationDefinition definition;
	private final int[] holds;
	private final int totalFrames;

	private double ticksPerFrame;

	public Animation(final AnimationDefinition definition) {
		this.definition = definition;
		this.holds = compileHolds(definition);
		this.totalFrames = calculateTotalFrames();

		this.ticksPerFrame = 1;
	}

	public String getName() {
		return definition.getName();
	}

	public int getLength() {
		return definition.getLength();
	}

	public int getHolds(final int index) {
		return holds[index];
	}

	public String getFrame(final int index) {
		return definition.getFrame(index);
	}

	public boolean getActivation(final int index) {
		return definition.getActivation(index);
	}

	public int getTotalFrameCount() {
		return totalFrames;
	}

	public double getTicksPerFrame() {
		return ticksPerFrame;
	}

	public void setTicksPerFrame(final double ticksPerFrame) {
		this.ticksPerFrame = ticksPerFrame;
	}

	public AnimationDefinition getDefinition() {
		return definition;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((definition == null) ? 0 : definition.hashCode());
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
		Animation other = (Animation) obj;
		if (definition == null) {
			if (other.definition != null)
				return false;
		} else if (!definition.equals(other.definition))
			return false;
		return true;
	}

	private int[] compileHolds(final AnimationDefinition definition) {
		final int[] holds = new int[definition.getLength()];

		for (int i = 0; i < definition.getLength(); i++)
			holds[i] = definition.getHolds(i);

		return holds;
	}

	private int calculateTotalFrames() {
		int total = 0;

		for (final int i : holds)
			total += i;

		return total;
	}
}
