
package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;


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

    //leetcode submit region begin(Prohibit modification and deletion)
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
                if (bfsTouchEnd(grid, m)) {
                    r = m;
                } else {
                    l = m + 1;
                }
            }
            return l;
        }

        private boolean bfsTouchEnd(int[][] grid, int pivot) {
            if (grid[0][0] > pivot) {
                return false;
            }
            final int lng = grid.length;
            final int lastVal = grid[lng - 1][lng - 1];
            Set<Item> touched = new HashSet<>(lng * lng);
            Queue<Item> queue = new LinkedList<>();
            final Item first = new Item(0, 0, grid[0][0]);
            queue.add(first);
            touched.add(first);
            while (!queue.isEmpty()) {
                // 上下左右
                final Item item = queue.poll();
                if (item.val == lastVal) {
                    return true;
                }
                for (int[] arrow : arrows) {
                    int row = item.row + arrow[0];
                    int column = item.column + arrow[1];
                    int val;
                    if (row >= 0 && row < lng
                            && column >= 0 && column < lng
                            && (val = grid[row][column]) <= pivot) {
                        final Item tmp = new Item(row, column, val);
                        if (touched.add(tmp)) {
                            queue.add(tmp);
                        }
                    }
                }
            }
            return false;
        }

        class Item {

            int row;
            int column;
            int val;

            public Item(int row, int column, int val) {
                this.row = row;
                this.column = column;
                this.val = val;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
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
//leetcode submit region end(Prohibit modification and deletion)


}