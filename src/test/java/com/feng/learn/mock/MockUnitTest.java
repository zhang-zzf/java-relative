package com.feng.learn.mock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.*;

import com.feng.learn.dao.UserDao;
import com.feng.learn.dao.model.User;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.verification.VerificationMode;

public class MockUnitTest {

    // 等价于 @Before方法中的MockitoAnnotations.initMocks(this);
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    UserDao userDaoMock;

    @Before
    public void initMock() {
        // MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMock() {
        // 被mock的方法返回默认值
        // 1. 对象 返回null
        // 2. byte / short / int / long 返回0
        // 3. char 返回 '\u0000'
        // 4. double / float 返回0.0
        // 5. boolean 返回false
        assertNull(userDaoMock.getById(5));
    }

    /**
     * 验证mock方法有没有被调用过
     */
    @Test
    public void testMethodInvoke() {
        userDaoMock.getById(5);
        // 等价于verify(userDaoMock, times(1)).getById(5);
        verify(userDaoMock).getById(5);

        userDaoMock.getById(4);
        userDaoMock.getById(4);
        // ==2次
        verify(userDaoMock, times(2)).getById(4);

        verify(userDaoMock, never()).getById(3);

        // 最多一次
        VerificationMode atMostOnce = atMostOnce();
        // 至少一次
        VerificationMode atLeastOnce = atLeastOnce();
        VerificationMode threeTimes = times(3);
        // 参考 org.mockito.Mockito 中的静态方法
    }

    /**
     * stub 打桩，让mock方法返回值
     */
    @Test
    public void testStub() {
        // stub
        // important 只针对当前测试有效，联合testStub2得出结论
        User user = User.builder().id(5).age(5).name("5").build();
        when(userDaoMock.getById(5)).thenReturn(user);
        assertEquals(user, userDaoMock.getById(5));
    }

    @Test
    public void testStub2() {
        assertNull(null, userDaoMock.getById(5));
    }

    // 返回多个值
    @Test
    public void testMoreThanOneReturnValue() {
        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Mockito").thenReturn("rocks");
        String result = i.next() + " " + i.next();
        // 断言
        assertEquals("Mockito rocks", result);
    }

    // 如何根据输入来返回值
    @Test
    public void testReturnValueDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo("Mockito")).thenReturn(1);
        // when(c.compareTo("real")).thenCallRealMethod();
        when(c.compareTo("Eclipse")).thenReturn(2);
        // 断言
        assertEquals(1, c.compareTo("Mockito"));
    }

    // 如何让返回值不依赖于输入
    @Test
    public void testReturnValueInDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo(anyInt())).thenReturn(-1);
        when(c.compareTo(anyString())).thenReturn(-2);
        // 断言
        assertEquals(-1, c.compareTo(9));
        assertEquals(-2, c.compareTo(""));
        assertEquals(-2, c.compareTo("Java"));
    }

    // 根据参数类型来返回值
    @Test
    public void testReturnValueDependentOnMethodType() {
        Comparable c = mock(Comparable.class);
        when(c.compareTo(CoreMatchers.isA(Integer.class))).thenReturn(0);
        // 断言
        assertEquals(0, c.compareTo(new Integer(1)));
        assertEquals(0, c.compareTo(new Integer(3)));
    }

    @Test(expected = IOException.class)
    public void testDoReturn() throws IOException {
        Closeable closeable = mock(Closeable.class);
        // stub
        doThrow(IOException.class).when(closeable).close();
        // invoke mock method
        closeable.close();
    }


    @Test
    public void testMockWhenAnswer() {
        List<String> mock = mock(List.class);
        when(mock.get(anyInt())).thenAnswer(invocation -> "Hello: " + invocation.getArgument(0));
        assertThat(mock.get(1), is(CoreMatchers.endsWith("1")));
        assertThat(mock.get(2), is(CoreMatchers.endsWith("2")));
    }

    /**
     * 用法总结
     * <p>1. ArgumentMatchers.* 可以用在Stubbing和verification</p>
     * <p>1. 有多个参数的方法，所有参数要么全是具体值，要么全是ArgumentMatcher</p>
     * <p>1. ArgumentMatchers.argThat() 可以配置java8 lambda 自定义ArgumentMatcher</p>
     * <p>In case of a method has more than one argument, it isn't possible to use ArgumentMatchers for only some of
     * the
     * arguments.
     * <p>Mockito requires you to provide all arguments either by matchers or by exact values.
     */
    @Test
    public void givenMockArgs_whenMethodInvoked_thenVerify() {
        final String STR = "str";
        List<String> mock = mock(List.class);

        // given
        given(mock.add(STR)).willReturn(true);

        boolean add = mock.add(STR);
        assertThat(add, is(true));
        then(mock).should().add(STR);
        then(mock).should().add(anyString());

        given(mock.contains(ArgumentMatchers.any(Integer.class))).willReturn(true);
        boolean contains = mock.contains(5);
        assertThat(contains, is(true));
        then(mock).should().contains(ArgumentMatchers.anyInt());
        // fail
        // then(mock).should().contains(ArgumentMatchers.anyString());

        mock.add(1, STR);
        then(mock).should().add(1, STR);

        //org.mockito.exceptions.misusing.InvalidUseOfMatchersException:
        //Invalid use of argument matchers!
        //2 matchers expected, 1 recorded:
        //then(mock).should().add(1, anyString());

        //do not care the second arg
        then(mock).should().add(eq(1), ArgumentMatchers.any());

    }


}
