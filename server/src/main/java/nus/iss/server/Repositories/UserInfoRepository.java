package nus.iss.server.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import nus.iss.server.Model.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    Optional<UserInfo> findByName(String username);

}

