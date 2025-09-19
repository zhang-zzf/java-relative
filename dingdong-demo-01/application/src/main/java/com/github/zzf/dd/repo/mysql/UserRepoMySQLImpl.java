package com.github.zzf.dd.repo.mysql;



import static com.github.zzf.dd.utils.LogUtils.json;

import com.github.zzf.dd.repo.mysql.iot_card.entity.TbUser;
import com.github.zzf.dd.repo.mysql.iot_card.mapper.TbUserMapper;
import com.github.zzf.dd.user.model.User;
import com.github.zzf.dd.user.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;

@Repository
@Slf4j
@RequiredArgsConstructor
@Validated
public class UserRepoMySQLImpl implements UserRepo {

    final TbUserMapper tbUserMapper;

    final DomainMapper mapper = DomainMapper.INSTANCE;

    @Override
    public long tryCreateUser(User user) {
        log.info("tryCreateUser req -> {}", json(user));
        TbUser entity = mapper.toPO(user);
        int rows = tbUserMapper.insert(entity);// userName has unique key
        log.info("tryCreateUser resp -> {}", rows);
        return entity.getId();
    }

    @Override
    public User queryUserByUserNo(String userNo) {
        log.info("queryUserByUserNo req -> {}", userNo);
        TbUser dbData = tbUserMapper.selectByUserNo(userNo);
        User ret = mapper.toDomain(dbData);
        log.info("queryUserByUserNo resp -> {}", json(ret));
        return ret;
    }

    @Mapper
    public interface DomainMapper {
        DomainMapper INSTANCE = Mappers.getMapper(DomainMapper.class);

        TbUser toPO(User domain);

        User toDomain(TbUser po);

    }

}
