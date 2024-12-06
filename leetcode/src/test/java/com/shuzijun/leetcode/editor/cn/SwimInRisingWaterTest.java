package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import org.junit.jupiter.api.Test;


public class SwimInRisingWaterTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final int swimInWater = solution.swimInWater(new int[][]{
            new int[]{0, 2},
            new int[]{1, 3},
        });
        then(swimInWater).isEqualTo(3);
    }

    // leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        final int[][] arrows = new int[][]{
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{0, -1},
            new int[]{-1, 0},
        };

        public int swimInWater(int[][] grid) {
            // 值域 [0, 250]
            int max = 0;
            for (int[] row : grid) {
                for (int anInt : row) {
                    max = anInt > max ? anInt : max;
                }
            }
            int l = 0, r = max;
            while (l < r) {
                int m = l + ((r - l) >> 1);
                if (bfsTouchEnd(grid, new int[grid.length * grid.length], m)) {
                    r = m;
                }
                else {
                    l = m + 1;
                }
            }
            return l;
        }

        private boolean bfsTouchEnd(int[][] grid, int[] touched, int pivot) {
            if (grid[0][0] > pivot) {
                return false;
            }
            final int lng = grid.length;
            final int lastVal = grid[lng - 1][lng - 1];
            Queue<Item> queue = new LinkedList<>();
            queue.add(new Item(0, 0));
            touched[0] = 1;
            while (!queue.isEmpty()) {
                // 上下左右
                final Item item = queue.poll();
                if (grid[item.row][item.column] == lastVal) {
                    return true;
                }
                for (int[] arrow : arrows) {
                    int row = item.row + arrow[0];
                    int column = item.column + arrow[1];
                    if (row >= 0 && row < lng
                        && column >= 0 && column < lng
                        && touched[row * lng + column] == 0
                        && grid[row][column] <= pivot) {
                        // 别忘记置位
                        touched[row * lng + column] = 1;
                        queue.offer(new Item(row, column));
                    }
                }
            }
            return false;
        }

        class Item {

            int row;
            int column;

            public Item(int row, int column) {
                this.row = row;
                this.column = column;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                Item item = (Item) o;
                return row == item.row &&
                    column == item.column;
            }

            @Override
            public int hashCode() {
                return Objects.hash(row, column);
            }

        }

    }
    // leetcode submit region end(Prohibit modification and deletion)


}