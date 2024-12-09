# ⭐️ 주요 프로젝트
## 귀찮음은 그만! LLM을 이용해 한 문장이면 끝나는 일정 관리 서비스, “한끝” 앱
- 2024.05. - 2024. 10.  / 2024 한이음 ICT 멘토링 공모전 금상(과학기술정보통신부 장관상) 수상
- 기존 일정 관리 서비스의 번거로움을 해결하기 위해 만든 앱으로 한이음 ICT 공모전에 출품하여 
장관상을 수상하였습니다.
- 🎥 [데모 영상](https://www.youtube.com/watch?v=lLT4Pt20u7c)
- 인원 : PM 1 / Frontend 1 / Backend 1
- 개발 기간 : 6개월
- 👉🏻 [레포지토리](https://github.com/OZIIJIN/HanFinal_BACK)
### 사용 기술
  - Java, Spring Boot, Spring JPA, MySQL, Spring Batch, Spring Actuator, QueryDSL, Firebase, WebSocket, Docker, Loki, Grafana, Prometheus, Quartz, OpenAI API, 공공데이터 API
### 핵심 기능
👉🏻 [한 문장으로 일정 등록](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/text/service/TextServiceImpl.java#L41)
![주요 기능 로직1](https://github.com/user-attachments/assets/57d4735e-b3d9-471d-9f90-fde12e100a4f)

👉🏻 [AI 일정 조율, 한끝봇](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/chat/service/ChatServiceImpl.java#L53)
![주요 기능 로직2](https://github.com/user-attachments/assets/31e3281b-3e1a-458c-b91a-081317ff2ccd)

👉🏻 [날씨 맞춤형 일정 관리](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/weather/service/WeatherService.java#L33)
![주요 기능 로직3](https://github.com/user-attachments/assets/051dfc64-d629-4b14-8e04-f5fe86a27e2f)

👉🏻 [일정 우선 순위 추천](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/todo/service/TodoServiceImpl.java#L195)

### 개발 주요 사항
  - Spring Boot 기반 RESTful API 서버 개발
  - OpenAI API를 활용하여 AI 챗봇 개발 및 일정 정보 추출 로직 개발
  - Websocket와 RabbitMQ를 활용하여 AI 챗봇과 유저의 실시간 채팅 기능 개발
  - Spring Batch를 활용하여 날씨 맞춤형 푸시 알림 기능 성능 최적화 
  - Grafana, Prometheus, Loki를 사용한 서버 성능 실시간 모니터링 시스템 구축
  - 비동기 처리와 멀티 스레드로 대량 요청 시 서버 성능 최적화
  - 지속적 코드 리팩토링 진행

### 트러블 슈팅
## 1. 한 문장 일정 등록 기능 서버 성능 개선 👉🏻 [소스 코드](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/text/service/TextServiceImpl.java#L41)
![트러블 슈팅1](https://github.com/user-attachments/assets/5e24f625-d6a6-47be-b297-d4455abc4194)
![트러블 슈팅1-1](https://github.com/user-attachments/assets/848cb5c1-7307-4342-b06f-9684302fb7c3)

## 2. 로그시스템 구축으로 버그 해결 👉🏻 [소스 코드](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/domain/gpt/service/GptServiceImpl.java#L43)
![트러블 슈팅2](https://github.com/user-attachments/assets/c27c5767-0b7a-498a-9b16-18711af5f590)

## 3. Spring Batch 도입으로 대량 데이터 처리 프로세스 개선 👉🏻 [소스 코드](https://github.com/OZIIJIN/HanFinal_BACK/blob/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/global/config/BatchConfig.java#L30)
![트러블 슈팅2-1](https://github.com/user-attachments/assets/d692e051-f389-439e-bb17-331714c3f72a)

### 예외 처리
👉🏻 [예외 처리](https://github.com/OZIIJIN/HanFinal_BACK/tree/e8f5e34d8ef08fc1addf15fff632c3d4cdf05b73/src/main/java/org/onesentence/onesentence/global/exception)

### 회고
- 기존 계층형 아키텍처로 설계하면서 하나의 도메인의 책임이 너무 비대해지는 것에 대한 고민이 생겼습니다. 이에 따라, 아키텍처를 설계할 때도 프로젝트의 성격이 중요하다는 것을 깨달았습니다.
