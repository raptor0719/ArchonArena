package raptor.game.archonArena.model;

public class Animation {
	private final AnimationDefinition definition;
	private final int[] holds;

	private int totalFrames;
	private int current;

	public Animation(final AnimationDefinition definition) {
		this.definition = definition;
		this.holds = compileHolds(definition);

		this.totalFrames = calculateTotalFrames();
		this.current = 0;
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

	public void setFrameCount(final int frameCount) {
		if (frameCount == totalFrames)
			return;

		final int difference = frameCount - totalFrames;
		if (difference > 0)
			increaseFrames(difference);
		else
			reduceFrames(Math.abs(difference));
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

	private void increaseFrames(final int difference) {
		for (int i = 0; i < difference; i++) {
			holds[current] += 1;
			current = (current + 1) % holds.length;
		}
	}

	private void reduceFrames(final int difference) {
		for (int i = 0; i < difference; i++) {
			holds[current] -= 1;

			current -= 1;

			if (current < 0)
				current = holds.length - 1;
		}
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
