package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        // Ваше решение
        List<Unit> suitableUnits = new ArrayList<>();

        // Если координаты Y в диапазоне 0-100, используем битовый массив
        // Для других диапазонов можно использовать BitSet
        int maxY = 100; // Максимальная координата Y

        // Проходим по всем рядам
        for (List<Unit> rowUnits : unitsByRow) {
            if (rowUnits.isEmpty()) {
                continue;
            }

            // Используем BitSet для отслеживания занятых позиций
            java.util.BitSet occupied = new java.util.BitSet(maxY + 1);

            // Заполняем BitSet
            for (Unit unit : rowUnits) {
                occupied.set(unit.getyCoordinate());
            }

            // Проверяем каждый юнит
            for (Unit unit : rowUnits) {
                int currentY = unit.getyCoordinate();

                if (isLeftArmyTarget) {
                    // Проверяем, свободна ли клетка слева
                    if (currentY - 1 < 0 || !occupied.get(currentY - 1)) {
                        suitableUnits.add(unit);
                    }
                } else {
                    // Проверяем, свободна ли клетка справа
                    if (currentY + 1 > maxY || !occupied.get(currentY + 1)) {
                        suitableUnits.add(unit);
                    }
                }
            }
        }

        return suitableUnits;
    }
}
