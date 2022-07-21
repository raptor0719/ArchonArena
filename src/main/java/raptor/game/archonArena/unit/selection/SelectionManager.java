package raptor.game.archonArena.unit.selection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
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
import raptor.game.archonArena.unit.UnitPositionToLowLevelCoordinateTranslator;

public class SelectionManager implements IDrawable {
	private static final IColor SELECTION_BOX_COLOR = new BasicColor(0, 255, 0, 255);
	private static final int SELECTION_BOX_LINE_THICKNESS = 1;

	private static final IColor SELECTED_UNIT_BOX_COLOR = new BasicColor(0, 255, 0, 150);

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

		final CollisionRectangle selectionBox = new CollisionRectangle();
		selectionBox.setCollision(new Rectangle(start, new Point(start.getX(), end.getY()), new Point(end.getX(), start.getY()), end));

		final boolean onlySelectOne = selectionBox.getCollision().getArea() <= 1;

		boolean needToClear = !append;
		final List<Unit> actionableUnits = new ArrayList<Unit>();
		for (final IEntity entity : Game.getCurrentLevel().getAllEntities()) {
			if (!(entity instanceof Unit))
				continue;

			final Unit unit = (Unit)entity;

			if (isInSelectionBox(selectionBox, unit)) {
				if (needToClear) {
					clearSelected();
					needToClear = false;
				}

				if (onlySelectOne) {
					if (actionableUnits.isEmpty()) {
						actionableUnits.add(unit);
						continue;
					}

					final Unit currentProspect = actionableUnits.get(0);

					if (currentProspect.getPosition().getY() < unit.getPosition().getY()) {
						actionableUnits.remove(0);
						actionableUnits.add(unit);
					}
				} else {
					actionableUnits.add(unit);
				}
			}
		}

		for (final Unit unit : actionableUnits) {
			if (remove)
				currentSelected.remove(unit);
			else
				currentSelected.add(unit);
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

	public void moveOrder(final int pointX, final int pointY, final boolean queue) {
		for (final Unit unit : currentSelected)
			unit.moveOrder(pointX, pointY, queue);
	}

	@Override
	public void draw(final IGraphics graphics) {
		for (final Unit unit : currentSelected) {
			final int x = UnitPositionToLowLevelCoordinateTranslator.translatePositionX(unit);
			final int y = UnitPositionToLowLevelCoordinateTranslator.translatePositionY(unit);

			final int width = unit.getDefinition().getModelDefintion().getWidth();
			final int height = unit.getDefinition().getModelDefintion().getHeight();

			graphics.drawLine(x, y, x + width, y, SELECTION_BOX_LINE_THICKNESS, SELECTED_UNIT_BOX_COLOR);
			graphics.drawLine(x, y, x, y + height, SELECTION_BOX_LINE_THICKNESS, SELECTED_UNIT_BOX_COLOR);
			graphics.drawLine(x + width, y, x + width, y + height, SELECTION_BOX_LINE_THICKNESS, SELECTED_UNIT_BOX_COLOR);
			graphics.drawLine(x, y + height, x + width, y + height, SELECTION_BOX_LINE_THICKNESS, SELECTED_UNIT_BOX_COLOR);
		}

		if (!selectionActive)
			return;

		graphics.drawLine(start.getX(), start.getY(), end.getX(), start.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(start.getX(), start.getY(), start.getX(), end.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(end.getX(), end.getY(), end.getX(), start.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
		graphics.drawLine(end.getX(), end.getY(), start.getX(), end.getY(), SELECTION_BOX_LINE_THICKNESS, SELECTION_BOX_COLOR);
	}

	private boolean isInSelectionBox(final CollisionRectangle selectionBox, final Unit unit) {
		final CollisionRectangle unitSelectableBox = new CollisionRectangle();
		final int startX = UnitPositionToLowLevelCoordinateTranslator.translatePositionX(unit);
		final int startY = UnitPositionToLowLevelCoordinateTranslator.translatePositionY(unit);
		final int width = unit.getDefinition().getSelectableWidth();
		final int height = unit.getDefinition().getSelectableHeight();
		unitSelectableBox.setCollision(new Rectangle(startX, startY, startX + width, startY, startX, startY + height, startX + width, startY + height));

		return selectionBox.collidesWithRectangle(unitSelectableBox);
	}
}
