package org.onesentence.onesentence.domain.user.repository;

import java.util.Optional;
import org.onesentence.onesentence.domain.user.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthJpaRepository extends JpaRepository<Auth, Long> {

	Optional<Auth> findByUserId (Long userId);

}
