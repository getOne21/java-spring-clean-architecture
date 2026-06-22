package com.getone21.cleanarchitecture.infrastructure.persistence.repository;

import com.getone21.cleanarchitecture.domain.model.User;
import com.getone21.cleanarchitecture.domain.port.out.UserRepositoryPort;
import com.getone21.cleanarchitecture.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final JpaUserRepository jpaUserRepository;
    private final UserPersistenceMapper mapper;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository, UserPersistenceMapper mapper) {
        this.jpaUserRepository = jpaUserRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        var entity = mapper.toJpaEntity(user);
        var savedEntity = jpaUserRepository.save(entity);
        return mapper.toDomainModel(savedEntity);
    }

    @Override
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id)
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email)
                .map(mapper::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll().stream()
                .map(mapper::toDomainModel)
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }
}
