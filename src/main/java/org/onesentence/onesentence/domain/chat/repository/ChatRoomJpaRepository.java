package org.onesentence.onesentence.domain.chat.repository;

import org.onesentence.onesentence.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoom, Long> {

}
