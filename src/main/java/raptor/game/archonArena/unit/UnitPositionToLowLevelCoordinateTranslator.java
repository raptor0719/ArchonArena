package raptor.game.archonArena.unit;

public class UnitPositionToLowLevelCoordinateTranslator {
	public static int translatePositionX(final Unit unit) {
		return unit.getX() - unit.getDefinition().getSelectableWidth()/2;
	}

	public static int translatePositionY(final Unit unit) {
		return unit.getY() - unit.getDefinition().getSelectableHeight();
	}
}
