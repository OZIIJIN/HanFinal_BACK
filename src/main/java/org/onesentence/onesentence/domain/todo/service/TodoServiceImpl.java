package org.onesentence.onesentence.domain.todo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.onesentence.onesentence.domain.dijkstra.model.Graph;
import org.onesentence.onesentence.domain.dijkstra.model.Node;
import org.onesentence.onesentence.domain.dijkstra.service.DijkstraService;
import org.onesentence.onesentence.domain.gpt.dto.GPTCallTodoRequest;
import org.onesentence.onesentence.domain.todo.dto.*;
import org.onesentence.onesentence.domain.todo.entity.Todo;
import org.onesentence.onesentence.domain.todo.entity.TodoStatus;
import org.onesentence.onesentence.domain.todo.repository.TodoJpaRepository;
import org.onesentence.onesentence.domain.todo.repository.TodoQuery;
import org.onesentence.onesentence.domain.todo.repository.TodoQueryImpl;
import org.onesentence.onesentence.global.exception.ExceptionStatus;
import org.onesentence.onesentence.global.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService{

	private final TodoJpaRepository todoJpaRepository;
	private final TodoQuery todoQuery;
	private final DijkstraService dijkstraService;
	private final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);


	@Override
	@Transactional
	public Long createTodo(TodoRequest request) {

		Todo todo = new Todo(request);
		Todo savedTodo = todoJpaRepository.save(todo);

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long updateTodo(TodoRequest request, Long todoId) {

		Todo todo = findById(todoId);
		todo.updateTodo(request);

		return todo.getId();
	}

	public Todo findById(Long todoId) {
		return todoJpaRepository.findById(todoId).orElseThrow(() -> new NotFoundException(
			ExceptionStatus.NOT_FOUND));
	}

	@Override
	@Transactional
	public void deleteTodo(Long todoId) {
		Todo todo = findById(todoId);
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
	public List<TodoResponse> getTodosByStatus(TodoStatus status) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findByStatus(status);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByDate(LocalDate date) {
		List<TodoResponse> todoResponses = new ArrayList<>();
		LocalDateTime dayStart = date.atStartOfDay();
		LocalDateTime dayEnd = date.atTime(LocalTime.MAX);

		List<Todo> todos = todoJpaRepository.findByStartBetween(dayStart, dayEnd);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodosByCategory(String category) {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findByCategory(category);

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TodoResponse> getTodos() {
		List<TodoResponse> todoResponses = new ArrayList<>();

		List<Todo> todos = todoJpaRepository.findAll();

		for (Todo todo : todos) {
			todoResponses.add(TodoResponse.from(todo));
		}

		return todoResponses;
	}

	@Transactional(readOnly = true)
	@Override
	public List<TodoResponse> getPriorities() {
		List<TodoPriority> todoPriorities = todoQuery.calculatePriority();

		List<Node> nodes = new ArrayList<>();

		for(TodoPriority todoPriority : todoPriorities) {
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
		logger.info("Optimal Order: {}", optimalOrder);

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
	public Long createTodoByOneSentence(GPTCallTodoRequest gptCallTodoRequest) {

		Todo todo = Todo.builder()
			.title(gptCallTodoRequest.getTitle())
			.start(gptCallTodoRequest.getStart())
			.category(gptCallTodoRequest.getCategory())
			.status(TodoStatus.TODO)
			.end(gptCallTodoRequest.getEnd())
			.location(gptCallTodoRequest.getLocation())
			.together(gptCallTodoRequest.getTogether())
			.build();
		Todo savedTodo = todoJpaRepository.save(todo);

		return savedTodo.getId();
	}

	@Override
	@Transactional
	public Long setInputTime(Long todoId, TodoInputTimeRequest request) {

		Todo todo = findById(todoId);
		todo.setInputTime(request.getInputTime());

		return todo.getId();
	}
}
