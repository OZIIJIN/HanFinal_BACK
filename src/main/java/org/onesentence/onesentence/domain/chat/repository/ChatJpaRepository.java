package org.onesentence.onesentence.domain.chat.repository;

import org.onesentence.onesentence.domain.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatJpaRepository extends JpaRepository<Chat, Long> {

}
