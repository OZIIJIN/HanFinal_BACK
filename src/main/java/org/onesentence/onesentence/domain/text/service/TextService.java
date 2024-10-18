package org.onesentence.onesentence.domain.text.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import org.onesentence.onesentence.domain.text.dto.TextRequest;

public interface TextService {

	Long createTodoByOneSentence(TextRequest request, Long userId)
		throws IOException, FirebaseMessagingException;

}
