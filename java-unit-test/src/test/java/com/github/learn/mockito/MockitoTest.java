package com.github.learn.mockito;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.*;

import com.github.learn.AbstractJUnit4Mockito;
import com.github.learn.domain.model.User;
import com.github.learn.domain.repo.UserRepo;
import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.BDDAssertions;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.verification.VerificationMode;

/**
 * Mockito common usage
 *
 * @author zhang.zzf@alibaba-inc.com
 * @date 2021/07/04
 */
public class MockitoTest extends AbstractJUnit4Mockito {

    @Mock
    UserRepo userDaoMock;

    @Test
    public void givenNoMock_whenCallMethod_thenReturnDefaultValue() {
        // 被mock的方法返回默认值
        // 1. 对象 返回null
        // 1.1 Optional return Optional.empty()
        // 2. byte / short / int / long 返回0
        // 3. char 返回 '\u0000'
        // 4. double / float 返回0.0
        // 5. boolean 返回false
        BDDAssertions.then(userDaoMock.getById(5L)).isEmpty();
    }

    /**
     * 验证mock方法有没有被调用过
     * <p>参考 org.mockito.Mockito 中的静态方法</p>
     */
    @Test
    public void testMethodInvoke() {
        userDaoMock.getById(5L);
        // 等价于verify(userDaoMock, times(1)).getById(5);
        // 尽量不要使用 verify
        // verify(userDaoMock).getById(5L);
        BDDMockito.then(userDaoMock).should().getById(5L);
        userDaoMock.getById(4L);
        userDaoMock.getById(4L);
        // ==2次
        // verify(userDaoMock, times(2)).getById(4L);
        BDDMockito.then(userDaoMock).should().getById(5L);
        BDDMockito.then(userDaoMock).should(times(2)).getById(4L);
        // 最多一次
        VerificationMode atMostOnce = atMostOnce();
        // 至少一次
        VerificationMode atLeastOnce = atLeastOnce();
        VerificationMode threeTimes = times(3);
    }

    /**
     * stub 打桩，让mock方法返回值
     */
    @Test
    public void testStub() {
        // stub
        // important 只针对当前测试有效，联合testStub2得出结论
        // given
        User user = User.builder().id(5L).name("5").build();
        given(userDaoMock.getById(5L)).willReturn(Optional.of(user));
        // when
        final Optional<User> byId = userDaoMock.getById(5L);
        // then
        BDDAssertions.then(byId).isPresent().contains(user);
    }

    @Test
    public void testStub2() {
        BDDAssertions.then(userDaoMock.getById(5L)).isEmpty();
    }

    // 返回多个值
    @Test
    public void testMoreThanOneReturnValue() {
        Iterator i = mock(Iterator.class);
        given(i.next()).willReturn("Mockito").willReturn("rocks");
        String result = i.next() + " " + i.next();
        // 断言
        BDDAssertions.then(result).isEqualTo("Mockito rocks");
    }

    // 如何根据输入来返回值
    @Test
    public void testReturnValueDependentOnMethodParameter() {
        Comparable c = mock(Comparable.class);
        given(c.compareTo("Mockito")).willReturn(1);
        // when(c.compareTo("real")).thenCallRealMethod();
        given(c.compareTo("Eclipse")).willReturn(2);
        // 断言
        BDDAssertions.then(c.compareTo("Mockito")).isEqualTo(1);
        BDDAssertions.then(c.compareTo("Eclipse")).isEqualTo(2);
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
     * the arguments.
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
