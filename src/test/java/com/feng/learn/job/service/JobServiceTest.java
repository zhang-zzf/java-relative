package com.feng.learn.job.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/05
 */
public class JobServiceTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    JobService jobService;

    @Test
    public void givenDefaultMethod_whenCallRealMethod_thenNoExceptionIsRaised() {
        Person p = new Person();
        p.setCurrentJobPosition(new JobPosition());

        when(jobService.findCurrentJobPosition(p))
            .thenReturn(Optional.of(p.getCurrentJobPosition()));

        doCallRealMethod().when(jobService)
            .assignJobPosition(
                ArgumentMatchers.argThat(person -> true),
                Mockito.any(JobPosition.class)
            );
        boolean b = jobService.assignJobPosition(p, p.getCurrentJobPosition());

        assertThat(b, is(false));
    }


}