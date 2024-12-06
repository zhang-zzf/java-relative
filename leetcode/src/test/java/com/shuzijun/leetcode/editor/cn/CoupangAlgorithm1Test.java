package com.shuzijun.leetcode.editor.cn;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.Deque;
import java.util.LinkedList;
import org.junit.jupiter.api.Test;

public class CoupangAlgorithm1Test {


    @Test
    void given_when_then() {
        Solution solution = new Solution();
        then(solution.calc("2")).isEqualTo(2);
        then(solution.calc("2+3*5-4/2")).isEqualTo(15);
        then(solution.calc("2+3*5*1-4/2")).isEqualTo(15);
        then(solution.calc("2+13*5-4/2")).isEqualTo(65);
        then(solution.calc("2+13*5*2-4/2")).isEqualTo(130);
        then(solution.calc("2+13*5*2-4/2/2")).isEqualTo(131);
        // then(solution.calc("(2+3)*5-4/2")).isEqualTo(23);
        // then(solution.calc("(2+(3+5)*2)*5-4/2")).isEqualTo(88);
    }

    class Solution {

        /**
         * 仅计算 + - * /
         */
        public int calc(String str) {
            Deque<Integer> deque = parseMiddleExpr(str);
            Deque<Integer> stack = new LinkedList<>();
            while (!deque.isEmpty()) {
                Integer i = deque.poll();
                if (i == '*') {
                    stack.offer(stack.pollLast() * deque.poll());
                }
                else if (i == '/') {
                    stack.offer(stack.pollLast() / deque.poll());
                }
                else {
                    stack.offer(i);
                }
            }
            while (!stack.isEmpty()) {
                Integer i = stack.poll();
                if (i == '+') {
                    deque.offer(deque.pollLast() + stack.poll());
                }
                else if (i == '-') {
                    deque.offer(deque.pollLast() - stack.poll());
                }
                else {
                    deque.offer(i);
                }
            }
            return deque.peek();
        }

        private Deque<Integer> parseMiddleExpr(String str) {
            Deque<Integer> ans = new LinkedList<>();
            int left = 0, right = 0;
            while (right < str.length()) {
                char c = str.charAt(right);
                if (c == '+' || c == '-' || c == '*' || c == '/') {
                    if (left < right) {
                        ans.offer(Integer.valueOf(str.substring(left, right)));
                    }
                    ans.offer((int) c);
                    left = right + 1;
                }
                right += 1;
            }
            if (left < right) {
                ans.offer(Integer.valueOf(str.substring(left, right)));
            }
            return ans;
        }

    }

}