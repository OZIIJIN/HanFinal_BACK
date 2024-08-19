package org.onesentence.onesentence.domain.user.repository;

import java.util.Optional;
import org.onesentence.onesentence.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

	boolean existsByNickName (String nickName);

	Optional<User> findByNickName (String nickName);

}
