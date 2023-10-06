package com.example.cloudserviceapplication.repository;

import com.example.cloudserviceapplication.entity.UserEntity;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, String> {
}
