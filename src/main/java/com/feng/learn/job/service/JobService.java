package com.feng.learn.job.service;

import java.util.Optional;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/05
 */
public interface JobService {

    Optional<JobPosition> findCurrentJobPosition(Person person);

    default boolean assignJobPosition(Person person, JobPosition jobPosition) {
        if (!findCurrentJobPosition(person).isPresent()) {
            person.setCurrentJobPosition(jobPosition);
            return true;
        } else {
            return false;
        }
    }

}
