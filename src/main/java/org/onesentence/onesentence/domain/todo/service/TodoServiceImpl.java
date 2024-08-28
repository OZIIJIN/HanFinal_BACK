package org.onesentence.onesentence.domain.todo.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.chat.dto.ChatTypeMessage;
import org.onesentence.onesentence.domain.chat.dto.CoordinationMessage;
import org.onesentence.onesentence.domain.dijkstra.model.Graph;
import org.onesentence.onesentence.domain.dijkstra.model.Node;
import org.onesentence.onesentence.domain.dijkstra.service.DijkstraService;
import org.onesentence.onesentence.domain.fcm.dto.FCMSendDto;
import org.onesentence.onesentence.domain.fcm.service.FCMService;
import org.onesentence.onesentence.domain.fcm.service.SchedulerService;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.onesentence.onesentence.domain.todo.repository.TodoQuery;
import org.onesentence.onesentence.domain.user.entity.User;
import org.onesentence.onesentence.domain.user.repository.UserJpaRepository;
import org.onesentence.onesentence.global.WebSocketEventListener;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.quartz.SchedulerException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

	private final TodoJpaRepository todoJpaRepository;
	private final TodoQuery todoQuery;
	private final DijkstraService dijkstraService;
	private final UserJpaRepository userJpaRepository;
	private final SchedulerService schedulerService;
	private final SimpMessagingTemplate simpMessagingTemplate;
	private final ThreadPoolTaskScheduler taskScheduler;
	private final SimpUserRegistry simpUserRegistry;
	private final WebSocketEventListener webSocketEventListener;
	private final FCMService fcmService;

	private ScheduledFuture<?> futureMessageTask;

	private User findUserByUserId(Long userId) {
		return userJpaRepository.findById(userId)
			.orElseThrow(() -> new NotFoundException(ExceptionStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public Long createTodo(TodoRequest request, Long userId) throws SchedulerException {

		User user = findUserByUserId(userId);

		Todo todo = new Todo(request, user.getId());

		Todo savedTodo = todoJpaRepository.save(todo);

		schedulerService.setScheduler(savedTodo.getStart(), user.getFcmToken(),
			savedTodo.getTitle(), savedTodo.getId());

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long updateTodo(TodoRequest request, Long todoId, Long userId) {

		User user = findUserByUserId(userId);
		Todo todo = findById(todoId);

		if (!todo.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("TODO 작성자가 아닙니다.");
		}

		todo.updateTodo(request);

		return todo.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public Todo findById(Long todoId) {
		return todoJpaRepository.findById(todoId).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public void deleteTodo(Long todoId, Long userId) {

		User user = findUserByUserId(userId);
		Todo todo = findById(todoId);

		if (!todo.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("TODO 작성자가 아닙니다.");
		}

		todoJpaRepository.delete(todo);
	}

	@Override
	@Transactional
	public Long updateStatus(TodoStatusRequest request, Long todoId) {
		Todo todo = findById(todoId);
		if (request.getStatus().equals("진행중")) {
			todo.changeToInProgress();
		} else if (request.getStatus().equals("완료")) {
			todo.changeToDone();
		}

		return todo.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public TodoResponse getTodo(Long todoId) {
		Todo todo = findById(todoId);

		return TodoResponse.from(todo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByStatus(TodoStatus status, Long userId) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoQuery.findByStatus(status, userId);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByDate(LocalDate date, Long userId) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoQuery.findByDate(date, userId);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByCategory(String category, Long userId) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoQuery.findByCategory(category, userId);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodos(Long userId) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoQuery.findAll(userId);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Transactional(readOnly = true)
	@Override
	public List<TodoResponse> getPriorities(Long userId) {
		List<TodoPriority> todoPriorities = todoQuery.calculatePriority(userId);

		List<Node> nodes = new ArrayList<>();

		for (TodoPriority todoPriority : todoPriorities) {
			nodes.add(new Node(todoPriority.getTodoId(), todoPriority.getPriorityScore()));
		}
		Graph graph = new Graph(nodes);

		for (Node node : nodes) {
			for (Node neighbor : nodes) {
				if (!node.getTodoId().equals(neighbor.getTodoId())) {
					double weight = 1.0 / (neighbor.getPriorityScore() + 1.0); // 역수 가중치 계산
					graph.addEdge(node.getTodoId(), neighbor.getTodoId(), weight);
				}
			}
		}

		List<Long> optimalOrder = dijkstraService.getOptimalOrder(graph);
		log.info("Optimal Order: {}", optimalOrder);

		List<Todo> todos = todoQuery.getTodosByOptimalOrder(optimalOrder);

		Map<Long, Todo> todoMap = todos.stream()
			.collect(Collectors.toMap(Todo::getId, todo -> todo));

		List<TodoResponse> todoResponses = new ArrayList<>();
		for (Long todoId : optimalOrder) {
			Todo todo = todoMap.get(todoId);
			if (todo != null) {
				TodoResponse todoResponse = TodoResponse.from(todo);
				todoResponses.add(todoResponse);
			}
		}
		return todoResponses;
	}


	@Override
	@Transactional
	public Long createTodoByOneSentence(GPTCallTodoRequest gptCallTodoRequest, Long userId)
		throws IOException, FirebaseMessagingException {
		User user = findUserByUserId(userId);

		Todo todo = Todo.builder()
			.title(gptCallTodoRequest.getTitle())
			.start(gptCallTodoRequest.getStart())
			.category(gptCallTodoRequest.getCategory())
			.status(TodoStatus.TODO)
			.end(gptCallTodoRequest.getEnd())
			.location(gptCallTodoRequest.getLocation())
			.together(gptCallTodoRequest.getTogether())
			.userId(userId)
			.inputTime(gptCallTodoRequest.getInputTime())
			.build();
		Todo savedTodo = todoJpaRepository.save(todo);

		FCMSendDto fcmSendDto = FCMSendDto.builder()
			.token(user.getFcmToken())
			.title("한 문장 등록 완료")
			.todoId(savedTodo.getId())
			.build();

		fcmService.sendMessageTo(fcmSendDto);

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long setInputTime(Long todoId, TodoInputTimeRequest request, Long userId) {

		User user = findUserByUserId(userId);
		Todo todo = findById(todoId);

		if (!todo.getUserId().equals(user.getId())) {
			throw new IllegalArgumentException("TODO 작성자가 아닙니다.");
		}

		todo.setInputTime(request.getInputTime());

		return todo.getId();
	}

	@Override
	@Transactional(readOnly = true)
	public void coordinateTodo(Long todoId, Long userId) throws SchedulerException {

		User user = findUserByUserId(userId);

		Todo savedTodo = findById(todoId);

		schedulerService.setScheduler(savedTodo.getStart(), user.getFcmToken(),
			savedTodo.getTitle(), savedTodo.getId());

		CoordinationMessage messageDto = CoordinationMessage.builder()
			.label("yesorno")
			.todoId(savedTodo.getId())
			.message(user.getNickName() + "님이 아래 일정을 조율하고자 합니다.")
			.todoTitle(savedTodo.getTitle())
			.start(dateConvertToString(savedTodo.getStart()))
			.nickName(user.getNickName())
			.build();

		// 클라이언트가 연결되었는지 확인 후 메시지 전송
		futureMessageTask = taskScheduler.schedule(() -> sendMessageWhenClientConnected(messageDto),
			new Date(System.currentTimeMillis() + 1000)); // 5초 지연
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoDate> getTodoDatesByUserId(Long todoId) {
		Todo todo = findById(todoId);

		return todoQuery.getTodoDatesByUserId(todo.getUserId());
	}

	@Transactional(readOnly = true)
	public AvailableTimeSlots findAvailableTimeSlots(Long todoId) {

		Todo targetTodo = findById(todoId);

		LocalDate targetDate = targetTodo.getStart().toLocalDate();
		LocalDateTime startDate = targetDate.atTime(LocalTime.of(10, 0));
		LocalDateTime endDate = targetDate.plusDays(2).atTime(LocalTime.of(21, 0)); // 이틀 후 21시

		List<Todo> todos = todoJpaRepository.findByUserIdAndStartBetween(targetTodo.getUserId(),
			startDate, endDate);

		List<Todo> sortedTodos = todos.stream()
			.sorted(Comparator.comparing(Todo::getStart))
			.toList();

		List<String> availableTimeSlots = new ArrayList<>();

		int inputTimeMinutes = targetTodo.getInputTime() * 60;

		LocalDateTime lastEndTime = startDate;

		for (Todo todo : sortedTodos) {

			// 이전 일정의 종료 시간과 현재 일정의 시작 시간 사이의 간격이 inputTime 이상인지 확인
			while (lastEndTime.plusMinutes(inputTimeMinutes).isBefore(todo.getStart()) &&
				lastEndTime.getHour() < 21) {
				availableTimeSlots.add(dateConvertToString(lastEndTime));
				if (availableTimeSlots.size() == 3) {
					return new AvailableTimeSlots(availableTimeSlots);
				}
				lastEndTime = lastEndTime.plusMinutes(inputTimeMinutes); // 3시간 추가
				// 각 날짜의 끝 시간(21:00)으로 넘어가면 다음 날 10:00으로 설정
				if (lastEndTime.getHour() >= 21) {
					lastEndTime = lastEndTime.toLocalDate().plusDays(1).atTime(10, 0);
				}
			}

			// 마지막 일정의 종료 시간을 업데이트
			lastEndTime = todo.getEnd();
		}

		// 마지막 일정 후의 빈 시간대도 확인
		while (lastEndTime.plusMinutes(inputTimeMinutes).isBefore(endDate) &&
			lastEndTime.getHour() < 21) {
			availableTimeSlots.add(dateConvertToString(lastEndTime));
			if (availableTimeSlots.size() == 3) {
				return new AvailableTimeSlots(availableTimeSlots);
			}
			lastEndTime = lastEndTime.plusMinutes(inputTimeMinutes); // 3시간 추가
			// 각 날짜의 끝 시간(21:00)으로 넘어가면 다음 날 10:00으로 설정
			if (lastEndTime.getHour() >= 21) {
				lastEndTime = lastEndTime.toLocalDate().plusDays(1).atTime(10, 0);
			}
		}

		log.info("빈 시간대 추천");
		return new AvailableTimeSlots(availableTimeSlots);
	}

	@Override
	@Transactional
	public void checkTimeSlotsAndUpdateTodo(Long todoId, LocalDateTime date)
		throws IOException, FirebaseMessagingException {
		boolean isPossibleToUpdate = true;
		Todo todo = findById(todoId);
		User user = findUserByUserId(todo.getUserId());

		// 해당 시간대에 일정이 겹치는지 확인
		List<Todo> todos = todoQuery.checkTimeSlots(todo.getUserId(), date);

		LocalDateTime endTimeSlot = date.plusMinutes(todo.getInputTime() * 60);

		for (Todo t : todos) {
			if ((!date.isBefore(todo.getStart()) && !date.isAfter(todo.getEnd()))
				|| (!endTimeSlot.isBefore(todo.getStart()) && !endTimeSlot.isAfter(
				todo.getEnd()))) {

				isPossibleToUpdate = false;

				//일정이 겹치는 거기때문에
				ChatTypeMessage chatTypeMessageFalse = ChatTypeMessage.builder()
					.label("message")
					.message(user.getNickName() + "님의 일정이 이미 존재합니다. 다른 시간을 입력해주세요!")
					.build();

				log.info("다시 채팅 사용자 입력을 받아야함");
				simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal",
					chatTypeMessageFalse);
			}
		}

		if (isPossibleToUpdate) {
			todo.updateTodoDate(date, endTimeSlot);

			ChatTypeMessage chatTypeMessageTrue = ChatTypeMessage.builder()
				.label("message")
				.message(dateConvertToString(todo.getStart()) + " 으로 일정이 확정되었습니다.")
				.build();

			FCMSendDto fcmSendDto = FCMSendDto.builder()
				.token(user.getFcmToken())
				.title("한끝봇에 의해 일정이 조율되었습니다!")
				.body("[" + todo.getTitle() + "] " + dateConvertToString(todo.getStart())
					+ " 으로 일정이 변경되었습니다.")
				.todoId(todoId)
				.build();

			fcmService.sendMessageTo(fcmSendDto);

			log.info("채팅 사용자 입력 시간으로 일정 확정");
			simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", chatTypeMessageTrue);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void updateTodoDate(Long todoId, LocalDateTime start) {
		Todo todo = findById(todoId);

		LocalDateTime end = start.plusMinutes(todo.getInputTime() * 60);

		log.info(start + "/" + end + "일정 확정");

		todo.updateTodoDate(start, end);
	}

	public String dateConvertToString(LocalDateTime localDateTime) {
		// DateTimeFormatter 생성 (한국어 로케일 사용)
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 mm분",
			Locale.KOREAN);

		// LocalDateTime을 문자열로 포맷
		return localDateTime.format(formatter);
	}

	@Override
	@Transactional
	public void updateTodoByPush(TodoPushUpdateRequest request, Long todoId, Long userId) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분",
			Locale.KOREAN);

		LocalDateTime dateTime = LocalDateTime.parse(request.getDate(), formatter);

		updateTodoDate(todoId, dateTime);

	}

	private void sendMessageWhenClientConnected(CoordinationMessage messageDto) {
		// WebSocket에 연결된 사용자가 있는지 확인

		int connectedUsersCount = webSocketEventListener.getConnectedUserCount();
		log.info("Number of connected users: {}", simpUserRegistry.getUsers().size());

		if (connectedUsersCount != 0) {
			// WebSocket에 누군가 연결되어 있는 경우 메시지를 전송
			simpMessagingTemplate.convertAndSend("/sub/chatroom/hanfinal", messageDto);
		} else {
			// 아무도 연결되어 있지 않다면 일정 시간 후 다시 시도
			futureMessageTask = taskScheduler.schedule(
				() -> sendMessageWhenClientConnected(messageDto),
				new Date(System.currentTimeMillis() + 1000)
			);
		}
	}

	@Transactional(readOnly = true)
	public String findRecommendedTimeSlot(Todo targetTodo) {

		// 기본 추천 시간: 일정 시작 시간 기준 다음 날 같은 시간
		LocalDateTime recommendedStartTime = targetTodo.getStart().plusDays(1);
		LocalDateTime recommendedEndTime = recommendedStartTime.plusMinutes(
			targetTodo.getInputTime() * 60);

		// 시간대 탐색 범위 설정 (다음 날 오전 10시부터 이틀 후 오후 9시까지)
		LocalDate targetDate = recommendedStartTime.toLocalDate();
		LocalDateTime startDate = targetDate.atTime(LocalTime.of(10, 0));
		LocalDateTime endDate = targetDate.plusDays(2).atTime(LocalTime.of(21, 0));

		// 1. JPA 레포지토리에서 다음 날 같은 시간에 일정이 있는지 확인
		List<Todo> overlappingTodos = todoQuery.findOverlappingTodos(targetTodo.getUserId(),
			recommendedStartTime, recommendedEndTime);

		// 만약 일정이 겹치지 않으면 추가적으로 해당 시간대에서 소요 시간을 모두 사용할 수 있는지 확인
		if (overlappingTodos.isEmpty()) {
			// 해당 시간대 이후에 다른 일정이 있는지 확인
			List<Todo> nextTodos = todoQuery.findOverlappingTodos(targetTodo.getUserId(),
				recommendedEndTime, recommendedEndTime.plusMinutes(1));

			// 만약 그 이후에 겹치는 일정이 없다면 해당 시간 반환
			if (nextTodos.isEmpty()) {
				return dateConvertToString(recommendedStartTime);
			}
		}

		// 2. 만약 겹치는 일정이 있거나 소요 시간을 모두 사용할 수 없는 경우, 빈 시간대를 탐색
		LocalDateTime lastEndTime = startDate;

		List<Todo> todos = todoJpaRepository.findByUserIdAndStartBetween(targetTodo.getUserId(),
			startDate, endDate);
		List<Todo> sortedTodos = todos.stream()
			.sorted(Comparator.comparing(Todo::getStart))
			.toList();

		for (Todo todo : sortedTodos) {
			// 이전 일정의 종료 시간과 현재 일정의 시작 시간 사이에 빈 시간이 있는지 확인
			while (lastEndTime.plusMinutes(targetTodo.getInputTime() * 60).isBefore(todo.getStart())
				&& lastEndTime.getHour() < 21) {
				// 빈 시간이 존재하는 경우 해당 시간 반환
				return dateConvertToString(lastEndTime);
			}
			// 마지막 일정의 종료 시간을 업데이트
			lastEndTime = todo.getEnd();
		}

		// 마지막 일정 이후의 빈 시간대 탐색
		while (lastEndTime.plusMinutes(targetTodo.getInputTime() * 60).isBefore(endDate)
			&& lastEndTime.getHour() < 21) {
			return dateConvertToString(lastEndTime); // 첫 번째로 가능한 빈 시간을 반환
		}

		log.info("추천 가능한 빈 시간이 없습니다.");
		return null; // 만약 빈 시간이 없을 경우 null 반환 (예외 처리 가능)
	}

}
