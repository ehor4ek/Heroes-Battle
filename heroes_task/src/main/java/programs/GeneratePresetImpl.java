package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {
//
    static final byte MAX_AMOUNT = 11;

    //    @Override
//    public Army generate(List<Unit> unitList, int maxPoints) {
//        // Ваше решение
//        System.out.println(maxPoints);
//        for (Unit u : unitList) {
//            System.out.print("Тип юнита: " + u.getUnitType() + " Имя: " + u.getName() +
//                    "\nТип атаки: " + u.getAttackType() + " Атака: " + Integer.toString(u.getBaseAttack()) +
//                    "\nЦена: " + Integer.toString(u.getCost()) + " Здоровье: " + u.getHealth() +
//                    "\nКоординаты: " + u.getxCoordinate() + " ; " + u.getyCoordinate() + "\n\n"
//            );
//        }
//        return null;
//    }



//    public Army generateExact(List<Unit> unitList, int maxPoints) {
//        BestSolution best = new BestSolution();
//        backtrack(unitList, maxPoints, 0, new int[unitList.size()], best);
//        return buildArmy(unitList, best.counts);
//    }
//
//    class BestSolution {
//        int[] counts;
//        double attackEfficiency;
//        double healthEfficiency;
//
//        BestSolution() {
//            counts = new int[0];
//            attackEfficiency = 0;
//            healthEfficiency = 0;
//        }
//    }
//
//    private void backtrack(List<Unit> units, int pointsLeft, int index,
//                           int[] currentCounts, BestSolution best) {
//        // Базовый случай: все типы рассмотрены или закончились точки
//        if (index == units.size() || pointsLeft <= 0) {
//            evaluateSolution(units, currentCounts, pointsLeft, best);
//            return;
//        }
//
//        Unit unit = units.get(index);
//        int maxCount = Math.min(11, pointsLeft / unit.getCost());
//
//        // Перебираем все возможные количества для текущего типа
//        for (int count = maxCount; count >= 0; count--) {
//            currentCounts[index] = count;
//            int newPointsLeft = pointsLeft - count * unit.getCost();
//
//            // Отсечение по границе (bound)
//            if (canImprove(units, currentCounts, index + 1, newPointsLeft, best)) {
//                backtrack(units, newPointsLeft, index + 1, currentCounts, best);
//            }
//        }
//
//        currentCounts[index] = 0; // откат
//    }
//
//    private boolean canImprove(List<Unit> units, int[] counts, int nextIndex,
//                               int pointsLeft, BestSolution best) {
//        // Оптимистичная оценка: берем максимально эффективных
//        double maxPossibleEfficiency = calculateCurrentEfficiency(units, counts);
//
//        // Добавляем оценку для оставшихся типов
//        for (int i = nextIndex; i < units.size(); i++) {
//            Unit unit = units.get(i);
//            int maxCount = Math.min(11, pointsLeft / unit.getCost());
//            maxPossibleEfficiency += maxCount * unit.getBaseAttack() / (double) unit.getCost();
//            pointsLeft -= maxCount * unit.getCost();
//        }
//
//        return maxPossibleEfficiency > best.attackEfficiency ||
//                (Math.abs(maxPossibleEfficiency - best.attackEfficiency) < 1e-9 &&
//                        pointsLeft > 0); // есть шанс улучшить health efficiency
//    }

//    SECOND SOLUTION:

//    public Army generate(List<Unit> unitList, int maxPoints) {
//        int N = unitList.size(); // количество типов юнитов
//
//        // Динамическое программирование
//        // dp[points] = {totalAttack, totalHealth}
//        ArmyInfo[] dp = new ArmyInfo[maxPoints + 1];
//        for (int i = 0; i <= maxPoints; i++) {
//            dp[i] = new ArmyInfo();
//        }
//
//        // Перебор всех типов юнитов
//        for (int i = 0; i < N; i++) {
//            Unit unit = unitList.get(i);
//            int cost = unit.getCost();
//            int attack = unit.getBaseAttack();
//            int health = unit.getHealth();
//
//            // Обратный проход для избежания повторного использования
//            for (int points = maxPoints; points >= cost; points--) {
//                // Проверяем все возможные количества этого типа юнитов (от 1 до 11)
//                for (int count = 1; count <= MAX_AMOUNT; count++) {
//                    int totalCost = count * cost;
//                    if (totalCost > points) break;
//
//                    int prevPoints = points - totalCost;
//                    ArmyInfo prev = dp[prevPoints];
//
//                    // Вычисляем новые значения
//                    int newAttack = prev.totalAttack + count * attack;
//                    int newHealth = prev.totalHealth + count * health;
//
//                    // Проверяем, улучшили ли мы решение
//                    if (isBetter(newAttack, newHealth,
//                            dp[points].totalAttack, dp[points].totalHealth,
//                            totalCost, dp[points].totalCost) && ) {
//
//                        // Создаем новую конфигурацию
//                        ArmyInfo newInfo = new ArmyInfo();
//                        newInfo.totalAttack = newAttack;
//                        newInfo.totalHealth = newHealth;
//                        newInfo.totalCost = totalCost + prev.totalCost;
//                        newInfo.unitsCount = new HashMap<>(prev.unitsCount);
//                        newInfo.unitsCount.put(i,
//                                newInfo.unitsCount.getOrDefault(i, 0) + count);
//
//                        dp[points] = newInfo;
//                    }
//                }
//            }
//        }
//
//        // Находим лучшую конфигурацию
//        ArmyInfo bestConfig = findBestConfiguration(dp, maxPoints);
//
//        System.out.println(bestConfig.unitsCount);
//
//        // Создаем армию
//        return createArmyFromConfig(bestConfig, unitList);
//    }
//
//    // Вспомогательные классы
//    static class ArmyInfo {
//        int totalAttack = 0;
//        int totalHealth = 0;
//        int totalCost = 0;
//        Map<Integer, Integer> unitsCount = new HashMap<>(); // тип -> количество
//    }
//
//    private boolean isBetter(int newAttack, int newHealth,
//                             int oldAttack, int oldHealth,
//                             int newCost, int oldCost) {
//        // Сравниваем по attack/cost
//        double newEfficiency = (double) newAttack / newCost;
//        double oldEfficiency = (double) oldAttack / oldCost;
//
//        if (Math.abs(newEfficiency - oldEfficiency) > 1e-9) {
//            return newEfficiency > oldEfficiency;
//        }
//
//        // При равной эффективности атаки сравниваем по health/cost
//        double newHealthEff = (double) newHealth / newCost;
//        double oldHealthEff = (double) oldHealth / oldCost;
//
//        if (Math.abs(newHealthEff - oldHealthEff) <= 1e-9) {
//            return (Math.random() < 0.5);
//        }
//        return newHealthEff > oldHealthEff;
//    }
//
//    private ArmyInfo findBestConfiguration(ArmyInfo[] dp, int maxPoints) {
//        ArmyInfo best = dp[0];
//        for (int points = 1; points <= maxPoints; points++) {
//            if (isBetter(dp[points].totalAttack, dp[points].totalHealth,
//                    best.totalAttack, best.totalHealth,
//                    dp[points].totalCost, best.totalCost) && !(dp[points].unitsCount.isEmpty())) {
//                    best = dp[points];
//            }
//        }
//        return best;
//    }
//
//    private Army createArmyFromConfig(ArmyInfo config, List<Unit> unitList) {
//        Army army = new Army();
//        List<Unit> units_of_army = new ArrayList<Unit>();
//        byte[] x = {0,1,2};
//        for (int i = 0; i < 2; i++) {
//            byte a = (byte)(Math.random() * 3);
//            byte b = (byte)(Math.random() * 3);
//            byte c = x[a];
//            x[a] = x[b];
//            x[b] = c;
//        }
//        byte columns = 0;
//        byte y = 0;
//        byte delta = (byte)(Math.random() * 2 + 1);
//        for (Map.Entry<Integer, Integer> entry : config.unitsCount.entrySet()) {
//            int unitType = entry.getKey();
//            System.out.print(unitType + " ");
//            int count = entry.getValue();
//            System.out.println(count);
//            Unit unit = unitList.get(unitType);
//
//            for (int i = 0; i < count; i++) {
//
//                Unit army_unit = new Unit(
//                        unit.getName() + ' ' + Integer.toString(i),
//                    unit.getUnitType(),
//                    unit.getHealth(),
//                    unit.getBaseAttack(),
//                    unit.getCost(),
//                    unit.getAttackType(),
//                    unit.getAttackBonuses(),
//                    unit.getDefenceBonuses(),
//                    x[columns],
//                    y
//                );
//                y+=delta;
//                if (y > 20) {
//                    columns++;
//                    y = 0;
//                    delta=1;
//                }
//
//                units_of_army.add(army_unit);
//            }
//        }
//        army.setUnits(units_of_army);
//        army.setPoints(config.totalCost);
//        System.out.println(army.getUnits());
//        System.out.println(army.getPoints());
//        return army;
//    }

// FIRST SOLUTION:

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        // Сначала сортируем юнитов по эффективности
        List<Unit> sortedUnits = new ArrayList<>(unitList);

        // Сортировка по attack/cost (первичный критерий), затем health/cost (вторичный)
        sortedUnits.sort((u1, u2) -> {
            double eff1 = (double) u1.getBaseAttack() / u1.getCost();
            double eff2 = (double) u2.getBaseAttack() / u2.getCost();
            if (Math.abs(eff1 - eff2) > 1e-9) {
                return Double.compare(eff2, eff1); // по убыванию
            }
            // при равной атаке сравниваем по health/cost
            double healthEff1 = (double) u1.getHealth() / u1.getCost();
            double healthEff2 = (double) u2.getHealth() / u2.getCost();
            if (Math.abs(healthEff1 - healthEff2) <= 1e-9) {
                if (Math.random() < 0.5)
                    return -1;
                else
                    return 1;
            } else {
                return Double.compare(healthEff2, healthEff1);
            }
        });
        return greedyOptimized(sortedUnits, maxPoints);
    }

    private Army greedyOptimized(List<Unit> units, int maxPoints) {
        Army army = new Army();
        List<Unit> units_of_army = new ArrayList<Unit>();
        int remainingPoints = maxPoints;
        byte[] x = {0,1,2};
        for (int i = 0; i < 2; i++) {
            byte a = (byte)(Math.random() * 3);
            byte b = (byte)(Math.random() * 3);
            byte c = x[a];
            x[a] = x[b];
            x[b] = c;
        }
        byte columns = 0;
        byte y = 0;
        byte delta = (byte)(Math.random() * 2 + 1);

        // Жадный алгоритм с оптимизацией
        for (Unit unit : units) {
            int maxUnitsOfType = Math.min(MAX_AMOUNT, remainingPoints / unit.getCost());
            if (maxUnitsOfType <= 0) continue;

            // Добавляем юнитов
            for (int i = 0; i < maxUnitsOfType; i++) {
                Unit army_unit = new Unit(
                        unit.getName() + ' ' + Integer.toString(i),
                    unit.getUnitType(),
                    unit.getHealth(),
                    unit.getBaseAttack(),
                    unit.getCost(),
                    unit.getAttackType(),
                    unit.getAttackBonuses(),
                    unit.getDefenceBonuses(),
                    x[columns],
                    y
                );
                y+=delta;
                if (y > 20) {
                    columns++;
                    y = 0;
                    delta=1;
                }
                units_of_army.add(army_unit);
                remainingPoints -= unit.getCost();
            }
            if (remainingPoints <= 0) break;
        }
        army.setUnits(units_of_army);
        army.setPoints(maxPoints - remainingPoints);
//        System.out.println(army.getUnits());
//        System.out.println(army.getPoints());
        return army;
    }
}