package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    private static final int[][] DIRECTIONS = {
            {0, 1}, {1, 0}, {0, -1}, {-1, 0},
            {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
    };

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        if (attackUnit == null || targetUnit == null) {
            return Collections.emptyList();
        }

        int startX = attackUnit.getxCoordinate();
        int startY = attackUnit.getyCoordinate();
        int goalX = targetUnit.getxCoordinate();
        int goalY = targetUnit.getyCoordinate();

        // Создаем сет препятствий
        Set<String> obstacles = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit != attackUnit && unit != targetUnit && unit.isAlive()) {
                obstacles.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        // BFS для поиска пути
        return bfsSearch(startX, startY, goalX, goalY, obstacles);
    }

    private List<Edge> bfsSearch(int startX, int startY, int goalX, int goalY, Set<String> obstacles) {
        // Очередь для BFS
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});

        // Мапа для восстановления пути
        Map<String, String> cameFrom = new HashMap<>();
        cameFrom.put(startX + "," + startY, null);

        // Посещенные точки
        Set<String> visited = new HashSet<>();
        visited.add(startX + "," + startY);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];

            // Если достигли цели
            if (x == goalX && y == goalY) {
                return reconstructPathBFS(cameFrom, goalX, goalY);
            }

            // Проверяем соседей
            for (int[] dir : DIRECTIONS) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                // Проверка границ
                if (nx < 0 || nx >= WIDTH || ny < 0 || ny >= HEIGHT) {
                    continue;
                }

                String key = nx + "," + ny;

                // Пропускаем препятствия и уже посещенные
                if (obstacles.contains(key) || visited.contains(key)) {
                    continue;
                }

                // Добавляем в очередь
                queue.add(new int[]{nx, ny});
                visited.add(key);
                cameFrom.put(key, x + "," + y);
            }
        }

        // Путь не найден
        return Collections.emptyList();
    }

    private List<Edge> reconstructPathBFS(Map<String, String> cameFrom, int goalX, int goalY) {
        List<Edge> path = new ArrayList<>();
        String current = goalX + "," + goalY;

        // Восстанавливаем путь от цели к началу
        List<String> points = new ArrayList<>();
        while (current != null) {
            points.add(current);
            current = cameFrom.get(current);
        }

        // Разворачиваем и конвертируем в Edge
        for (int i = points.size() - 1; i >= 0; i--) {
            String[] coords = points.get(i).split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);
            path.add(new Edge(x, y));
        }

        return path;
    }

}
