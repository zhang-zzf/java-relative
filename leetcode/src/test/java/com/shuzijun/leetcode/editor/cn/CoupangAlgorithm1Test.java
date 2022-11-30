package com.shuzijun.leetcode.editor.cn;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.BDDAssertions.then;

public class CoupangAlgorithm1Test {


    @Test
    void given_when_then() {
        Solution solution = new Solution();
        then(solution.calc("2+3*5-4/2")).isEqualTo(15);
        then(solution.calc("2+3*5*1-4/2")).isEqualTo(15);
        then(solution.calc("2+13*5-4/2")).isEqualTo(65);
        then(solution.calc("2+13*5*2-4/2")).isEqualTo(130);
        then(solution.calc("2+13*5*2-4/2/2")).isEqualTo(131);
        then(solution.calc("(2+3)*5-4/2")).isEqualTo(23);
        then(solution.calc("(2+(3+5)*2)*5-4/2")).isEqualTo(88);
    }

    class Solution {

        public int calc(String str) {
            char[] chars = str.replaceAll(" ", "").toCharArray();
            Map<Integer, Integer> opToPriority = new HashMap<Integer, Integer>() {{
                put((int) '(', 0);
                put((int) ')', 0);
                put((int) '*', 1);
                put((int) '/', 1);
                put((int) '+', 2);
                put((int) '-', 2);
            }};
            Queue<Integer> middleExpr = parseMiddleExpr(chars, opToPriority.keySet());
            // 后缀表达式
            Queue<Integer> suffix = convertMiddleExprToSuffixExpr(middleExpr, opToPriority);
            Stack<Integer> stack = new Stack<>();
            int x = 0, y = 0;
            while (!suffix.isEmpty()) {
                Integer poll = suffix.poll();
                switch (poll) {
                    case (int) '*':
                        stack.push(stack.pop() * stack.pop());
                        break;
                    case (int) '/':
                        y = stack.pop();
                        x = stack.pop();
                        stack.push(x / y);
                        break;
                    case (int) '+':
                        stack.push(stack.pop() + stack.pop());
                        break;
                    case (int) '-':
                        y = stack.pop();
                        x = stack.pop();
                        stack.push(x - y);
                        break;
                    default:
                        stack.push(poll);
                }
            }
            return stack.peek();
        }

        private Queue<Integer> convertMiddleExprToSuffixExpr(Queue<Integer> middleExpr,
                                                             Map<Integer, Integer> opToPriority) {
            Set<Integer> opSet = opToPriority.keySet();
            Queue<Integer> ans = new LinkedList<>();
            Stack<Integer> opStack = new Stack<>();
            while (!middleExpr.isEmpty()) {
                Integer poll = middleExpr.poll();
                if (!opSet.contains(poll)) {
                    ans.offer(poll);
                } else {
                    if ((int) ')' == poll) {
                        // ')'
                        Integer op;
                        while ((op = opStack.pop()) != (int) '(') {
                            ans.offer(op);
                        }
                    } else if ((int) '(' == poll) {
                        // '(' 直接压栈
                        opStack.push(poll);
                    } else {
                        while (true) {
                            Integer topOp = (opStack.empty() ? null : opStack.peek());
                            if (comparePriority(poll, topOp, opToPriority) > 0) {
                                // 优先级更高
                                opStack.push(poll);
                                break;
                            } else {
                                // 小于等于栈顶的优先级
                                ans.offer(opStack.pop());
                            }
                        }
                    }
                }
            }
            // 处理结尾数据
            while (!opStack.empty()) {
                ans.offer(opStack.pop());
            }
            return ans;
        }

        private int comparePriority(Integer poll, Integer topOp, Map<Integer, Integer> opToPriority) {
            if (topOp == null) {
                return 1;
            }
            if (topOp == (int) '(') {
                return 1;
            }
            if (opToPriority.get(poll) < opToPriority.get(topOp)) {
                return 1;
            }
            return -1;
        }

        private Queue<Integer> parseMiddleExpr(char[] chars, Set<Integer> opSet) {
            Queue<Integer> ans = new LinkedList<>();
            int num = 0;
            for (int aChar : chars) {
                if (opSet.contains(aChar)) {
                    if (num != 0) {
                        ans.offer(num);
                        num = 0;
                    }
                    ans.offer(aChar);
                } else {
                    num = num * 10 + (aChar - '0');
                }
            }
            // the last num
            if (num != 0) {
                ans.offer(num);
            }
            return ans;
        }

    }

}