package raptor.game.archonArena.model;

import raptor.engine.model.Model;
import raptor.engine.model.Sprite;
import raptor.engine.util.geometry.DoubleVector;
import raptor.engine.util.geometry.OffsetPoint;
import raptor.engine.util.geometry.api.IPoint;

public class AnimatedModel extends Model {
	private static final double BUMP_OFFSET_FRACTION = 0.25;

	private final int width;
	private final int height;

	private OffsetPoint bumpOffsetPoint;

	public AnimatedModel(final Sprite sprite, final int width, final int height) {
		super(sprite);

		this.width = width;
		this.height = height;

		this.bumpOffsetPoint = null;
	}

	@Override
	public void setPosition(final IPoint reference) {
		this.bumpOffsetPoint = new OffsetPoint(reference);

		super.setPosition(bumpOffsetPoint);
	}

	public void bumpTowardPoint(final int x, final int y) {
		final IPoint origin = bumpOffsetPoint.getOrigin();

		final DoubleVector v = DoubleVector.unitVectorTowardPoint(origin.getX(), origin.getY(), x, y);

		final double xScaled = width * BUMP_OFFSET_FRACTION * v.getX();
		final double yScaled = height * BUMP_OFFSET_FRACTION * v.getY();

		bumpOffsetPoint.setOffsetX((int)xScaled);
		bumpOffsetPoint.setOffsetY((int)yScaled);
	}

	public void stopBump() {
		bumpOffsetPoint.setOffsetX(0);
		bumpOffsetPoint.setOffsetY(0);
	}
}
