//有 n 名工人。 给定两个数组 quality 和 wage ，其中，quality[i] 表示第 i 名工人的工作质量，其最低期望工资为 wage[i] 
//。 
//
// 现在我们想雇佣 k 名工人组成一个工资组。在雇佣 一组 k 名工人时，我们必须按照下述规则向他们支付工资： 
//
// 
// 对工资组中的每名工人，应当按其工作质量与同组其他工人的工作质量的比例来支付工资。 
// 工资组中的每名工人至少应当得到他们的最低期望工资。 
// 
//
// 给定整数 k ，返回 组成满足上述条件的付费群体所需的最小金额 。在实际答案的 10⁻⁵ 以内的答案将被接受。。 
//
// 
//
// 
// 
//
// 示例 1： 
//
// 
//输入： quality = [10,20,5], wage = [70,50,30], k = 2
//输出： 105.00000
//解释： 我们向 0 号工人支付 70，向 2 号工人支付 35。 
//
// 示例 2： 
//
// 
//输入： quality = [3,1,10,10,1], wage = [4,8,2,2,7], k = 3
//输出： 30.66667
//解释： 我们向 0 号工人支付 4，向 2 号和 3 号分别支付 13.33333。 
//
// 
//
// 提示： 
//
// 
// n == quality.length == wage.length 
// 1 <= k <= n <= 10⁴ 
// 1 <= quality[i], wage[i] <= 10⁴ 
// 
// Related Topics 贪心 数组 排序 堆（优先队列） 👍 135 👎 0


package com.shuzijun.leetcode.editor.cn;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.assertj.core.api.BDDAssertions.then;


public class MinimumCostToHireKWorkersTest {

    final Solution solution = new Solution();

    @Test
    void givenNormal_when_thenSuccess() {
        final double mincost = solution.mincostToHireWorkers(new int[]{10, 20, 5}, new int[]{70, 50, 30}, 2);
        then(mincost).isCloseTo(105, Offset.offset(0.1));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public double mincostToHireWorkers(int[] quality, int[] wage, int k) {
            Worker[] workers = new Worker[quality.length];
            for (int i = 0; i < quality.length; i++) {
                workers[i] = new Worker(quality[i], wage[i]);
            }
            // sort by wage/quality
            Arrays.sort(workers);
            PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
            int sumq = 0;
            double ans = Double.MAX_VALUE;
            for (Worker worker : workers) {
                pq.add(-worker.q);
                sumq += worker.q;
                if (pq.size() > k) {
                    sumq += pq.poll();
                }
                if (pq.size() == k) {
                    ans = Math.min(ans, worker.radio() * sumq);
                }
            }
            return ans;
        }

        class Worker implements Comparable<Worker> {

            int q;
            int w;

            public Worker(int quality, int wage) {
                q = quality;
                w = wage;
            }

            double radio() {
                return (double) w / q;
            }

            @Override
            public int compareTo(Worker o) {
                return Double.compare(radio(), o.radio());
            }

        }

    }

//leetcode submit region end(Prohibit modification and deletion)


}