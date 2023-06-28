package com.github.learn.leetcode.domain.service.union_array;

import javax.validation.constraints.NotNull;

/**
 * @author zhanfeng.zhang
 * @date 2021/08/19
 */
public interface ArrayUnionService {

  /**
   * 两个数组的交集 II 给定两个数组，编写一个函数来计算它们的交集。
   * <p>
   *  
   * <p>
   * 示例 1：
   * <p>
   * 输入：nums1 = [1,2,2,1], nums2 = [2,2] 输出：[2,2] 示例 2:
   * <p>
   * 输入：nums1 = [4,9,5], nums2 = [9,4,9,8,4] 输出：[4,9]  
   * <p>
   * 说明：
   * <p>
   * 输出结果中每个元素出现的次数，应与元素在两个数组中出现次数的最小值一致。 我们可以不考虑输出结果的顺序。 进阶：
   * <p>
   * 如果给定的数组已经排好序呢？你将如何优化你的算法？ 如果 nums1 的大小比 nums2 小很多，哪种方法更优？
   * 如果 nums2 的元素存储在磁盘上，内存是有限的，并且你不能一次加载所有的元素到内存中，你该怎么办？
   * <p>
   * 作者：力扣 (LeetCode) 链接：https://leetcode-cn.com/leetbook/read/top-interview-questions-easy/x2y0c2/
   * 来源：力扣（LeetCode） 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
   */
  @NotNull int[] union(@NotNull int[] num1, @NotNull int[] num2);

}
