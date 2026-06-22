package com.getone21.cleanarchitecture.infrastructure.persistence.mapper;

import com.getone21.cleanarchitecture.domain.model.User;
import com.getone21.cleanarchitecture.infrastructure.persistence.entity.UserJpaEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserPersistenceMapper {

    UserJpaEntity toJpaEntity(User user);

    User toDomainModel(UserJpaEntity entity);
}
