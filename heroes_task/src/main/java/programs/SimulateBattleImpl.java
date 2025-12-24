package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.*;
import java.util.stream.Collectors;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog;

//    @Override
//    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
//        // Инициализация списков юнитов
//        List<Unit> playerUnits = new ArrayList<>(playerArmy.getUnits());
//        List<Unit> computerUnits = new ArrayList<>(computerArmy.getUnits());
//
//        // Сеты для определения принадлежности юнитов
//        Set<Unit> playerSet = new HashSet<>(playerUnits);
//
//        int round = 0;
//
//        // Основной цикл боя
//        while (!playerUnits.isEmpty() && !computerUnits.isEmpty()) {
//            round++;
//
//            // Создаем PriorityQueue для текущего раунда
//            PriorityQueue<Unit> initiativeQueue = new PriorityQueue<>(
//                    (u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack())
//            );
//
//            // Добавляем всех живых юнитов в очередь
//            addAliveUnitsToQueue(initiativeQueue, playerUnits);
//            addAliveUnitsToQueue(initiativeQueue, computerUnits);
//
//            // Обрабатываем ходы в порядке убывания атаки
//            while (!initiativeQueue.isEmpty()) {
//                Unit attacker = initiativeQueue.poll();
//
//                // Пропускаем мертвых юнитов
//                if (!attacker.isAlive()) {
//                    continue;
//                }
//
//                // Определяем вражескую армию
//                List<Unit> enemyList = playerSet.contains(attacker) ?
//                        computerUnits : playerUnits;
//
//                // Получаем цель атаки
//                Unit target = attacker.getProgram().attack();
//
//                // Проверяем валидность цели
//                if (target == null || !target.isAlive() || !enemyList.contains(target)) {
//                    // Ищем альтернативную цель
//                    target = findFirstAliveEnemy(enemyList);
//                }
//
//                // Выполняем атаку
//                if (target != null && target.isAlive()) {
//                    performAttack(attacker, target);
//
//                    // Если цель умерла, удаляем ее из очереди
//                    if (!target.isAlive()) {
//                        initiativeQueue.remove(target);
//                    }
//                } else {
//                    // Нет доступных целей
//                    printBattleLog.printBattleLog(attacker, null);
//                }
//            }
//
//            // Удаляем мертвых юнитов из списков
//            playerUnits = filterAliveUnits(playerUnits);
//            computerUnits = filterAliveUnits(computerUnits);
//        }
//    }

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> allUnits = new ArrayList<>();
        allUnits.addAll(playerArmy.getUnits());
        allUnits.addAll(computerArmy.getUnits());

        Set<Unit> playerSet = new HashSet<>(playerArmy.getUnits());
        Set<Unit> aliveSet = new HashSet<>(allUnits);

        int totalUnits = allUnits.size();
        int round = 0;

        // Главный цикл боя
        while (aliveSet.stream().anyMatch(u -> playerSet.contains(u) && u.isAlive()) &&
                aliveSet.stream().anyMatch(u -> !playerSet.contains(u) && u.isAlive())) {

            round++;

            // 1. СОРТИРОВКА: O(n log n)
            List<Unit> sortedUnits = aliveSet.stream()
                    .filter(Unit::isAlive)
                    .sorted((u1, u2) -> Integer.compare(u2.getBaseAttack(), u1.getBaseAttack()))
                    .toList();

            // 2. ОБРАБОТКА ХОДОВ: O(n) × O(1) для каждого юнита
            for (Unit attacker : sortedUnits) {
                if (!attacker.isAlive()) continue;

                // Определяем вражескую армию: O(1)
                Set<Unit> enemySet = playerSet.contains(attacker) ?
                        aliveSet.stream().filter(u -> !playerSet.contains(u)).collect(Collectors.toSet()) :
                        aliveSet.stream().filter(playerSet::contains).collect(Collectors.toSet());

                // Получаем цель: O(1) (по условию)
                Unit target = attacker.getProgram().attack();

                // Проверка цели: O(1) с использованием HashSet
                if (target == null || !target.isAlive() || !enemySet.contains(target)) {
                    // Быстрый поиск живой цели: O(1) в среднем
                    target = enemySet.stream()
                            .filter(Unit::isAlive)
                            .findFirst()
                            .orElse(null);
                }

                if (target != null && target.isAlive()) {
                    // Атака: O(1)
                    int damage = attacker.getBaseAttack();
                    target.setHealth(target.getHealth() - damage);

                    if (target.getHealth() <= 0) {
                        target.setAlive(false);
                        aliveSet.remove(target);  // O(1)
                    }

                    printBattleLog.printBattleLog(attacker, target);
                } else {
                    printBattleLog.printBattleLog(attacker, null);
                }
            }

            // Удаляем мертвых из основного набора: O(n)
            aliveSet.removeIf(unit -> !unit.isAlive());
        }
    }

    private void addAliveUnitsToQueue(PriorityQueue<Unit> queue, List<Unit> units) {
        for (Unit unit : units) {
            if (unit.isAlive()) {
                queue.add(unit);
            }
        }
    }

    private void performAttack(Unit attacker, Unit target) {
        // Простая логика урона
        int damage = attacker.getBaseAttack();
        target.setHealth(target.getHealth() - damage);

        if (target.getHealth() <= 0) {
            target.setAlive(false);
        }

        // Логирование
        printBattleLog.printBattleLog(attacker, target);
    }

    private Unit findFirstAliveEnemy(List<Unit> enemies) {
        for (Unit enemy : enemies) {
            if (enemy.isAlive()) {
                return enemy;
            }
        }
        return null;
    }

    private List<Unit> filterAliveUnits(List<Unit> units) {
        List<Unit> aliveUnits = new ArrayList<>();
        for (Unit unit : units) {
            if (unit.isAlive()) {
                aliveUnits.add(unit);
            }
        }
        return aliveUnits;
    }
}