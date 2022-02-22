package raptor.game.archonArena.unit.selection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import raptor.engine.collision.geometry.CollisionRectangle;
import raptor.engine.display.render.BasicColor;
import raptor.engine.display.render.IColor;
import raptor.engine.display.render.IDrawable;
import raptor.engine.display.render.IGraphics;
import raptor.engine.game.Game;
import raptor.engine.game.entity.IEntity;
import raptor.engine.util.geometry.Point;
import raptor.engine.util.geometry.Rectangle;
import raptor.game.archonArena.unit.Unit;

public class SelectionManager implements IDrawable {
	private static final IColor SELECTION_BOX_COLOR = new BasicColor(0, 255, 0, 255);
	private static final int SELECTION_BOX_LINE_THICKNESS = 1;

	private final Set<Unit> currentSelected;

	private final Point start;
	private final Point end;

	private boolean selectionActive;

	private boolean append;
	private boolean remove;

	public SelectionManager() {
		this.currentSelected = new HashSet<Unit>();

		this.start = new Point(0, 0);
		this.end = new Point(0, 0);

		this.selectionActive = false;

		this.append = false;
		this.remove = false;
	}

	public Iterator<Unit> getSelected() {
		return currentSelected.iterator();
	}

	public void clearSelected() {
		currentSelected.clear();
	}

	public void startSelection(final int x, final int y) {
		selectionActive = true;

		start.setX(x);
		start.setY(y);

		end.setX(x);
		end.setY(y);
	}

	public void setSelectionEnd(final int x, final int y) {
		end.setX(x);
		end.setY(y);
	}

	public void select() {
		selectionActive = false;

		if (!append)
			clearSelected();

		final CollisionRectangle selectionBox = new CollisionRectangle();
		selectionBox.setCollision(new Rectangle(start, new Point(start.getX(), end.getY()), new Point(end.getX(), start.getY()), end));

		for (final IEntity entity : Game.getCurrentLevel().getAllEntities()) {
			if (!(entity instanceof Unit))
				continue;

			final Unit unit = (Unit)entity;

			if (isInSelectionBox(selectionBox, unit)) {
				if (remove)
					currentSelected.remove(unit);
				else
					currentSelected.add(unit);
			}
		}
	}

	public void cancelSelection() {
		selectionActive = false;
	}

	public void appendNewUnitsToSelection(final boolean append) {
		this.append = append;
	}

	public void removeUnitsFromSelection(final boolean remove) {
		this.remove = remove;
	}

	@Override
	public void draw(final IGraphics graphics) {
		for (final Unit unit : currentSelected)
			graphics.drawOval(unit.getX(), unit.getY(), 5, 5, false, SELECTION_BOX_COLOR);

		if (!selectionActive)
			return;

		graphics.drawLine(start.getX(), start.getY(), end.getX(), start.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(start.getX(), start.getY(), start.getX(), end.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(end.getX(), end.getY(), end.getX(), start.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(end.getX(), end.getY(), start.getX(), end.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
	}

	private boolean isInSelectionBox(final CollisionRectangle selectionBox, final Unit unit) {
		return selectionBox.collidesWithRectangle(unit.getDefinition().getSelectionBox());
	}
}
