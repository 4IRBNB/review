![26조4irbnb커버이미지(사이즈X)](https://github.com/user-attachments/assets/968d5342-1dc6-4022-820d-2e6766d5174d)
# 1. About Project
- 프로젝트명 : 4irbnb
- 프로젝트 소개 : 대규모 트래픽 처리와 서비스 확장을 고려하여 MSA 아키텍처, 멀티 레포 방식, 다중 DB 구조를 적용한 숙소 예약 플랫폼
- 프로젝트 목표
  - 데이터 일관성 및 트랜잭션 관리
  - 대규모 트래픽 처리와 서비스 확장을 고려한 설계
- 프로젝트 기간 : 25.04 ~ 25.05

# 2. 팀 4irbnb
| 오혜민 | 김훈 | 우정욱 | 신예나 | 
|:------:|:------:|:------:|:------:|
| Lead | BE | Tech | BE |
| [GitHub](https://github.com/oh-min)) | [GitHub](https://github.com/Hooni-i) | [GitHub](https://github.com/wookssss) | [GitHub](https://github.com/Yea-Na) |

# 3. 작업 및 역할 분담

### 김훈
- **사용자 인증 및 권한 관리**
    - 권한별 회원가입 기능
    - 사용자 인증 및 역할 기반 권한 관리
    - API 요청 시 JWT를 통한 인증 처리
    - 사용자별 접근 제어 적용
 
### 신예나
- **숙소**
    - 전체·호스트별 목록 조회(페이징) 기능
    - 권한이 있는 경우 객실 생성 및 삭제 기능
- **쿠폰**
    - 쿠폰 CRUD와 발급·소프트삭제 기능
    - 권한이 있는 경우 해당 쿠폰 생성, 삭제 기능
      
### 우정욱
- **예약**
    - 예약 생성 시 동시성 제어
    - Feign Client 통신으로 예약과 결제 동시 관리
    - Kafka 비동기 메시징 시스템으로 예약과 결제 연동
- **결제**
    - 예약 생성을 위한 결제 처리
    - Kafka 비동기 메시징을 이용한 결제 생성 및 취소 응답

### 오혜민
- **리뷰**
    - 사용완료된 예약이 존재하는 경우 리뷰 작성 기능
    - 권한이 있는 경우 해당 리뷰 수정 및 삭제 기능
    - 리뷰의 별점별 개수와 평균 별점을 계산하는 통계 기능
- **알림**
    - 예약확정이 되면 해당 예약 정보를 저장하여 Slack 알림 발송 기능
 
# 4. 적용 기술
- 🖥️ **Backend**
    - 언어 & 프레임워크: Java 17, Spring Boot 3.x
    - 아키텍처: MSA (Spring Cloud: Eureka, GateWay)
    - ORM & DB: JPA, JPQL, QueryDSL, PostgreSQL, Redis
    - 비동기 처리: Kafka
- **🔐 인증 & 보안**
    - JWT
    - Spring Security
- **⚙️ Infra & 배포**
    - AWS EC2, RDS, Docker, ECR, ElastiCache
    - GitHub (형상 관리)
- **📊 Monitoring & Test**
    - Spring Boot Actuator, Prometheus, Grafana
    - JMeter (부하 테스트)
- **🔗 외부 연동 API**
    - Slack API (알림)
- **🧑‍💻 협업툴**
    - Github
    - Discord
    - Notion
      
# 3. ERD
![ERD(수정)](https://github.com/user-attachments/assets/5673cc83-53a6-45bd-8e2c-f4242e95971a)

# 4. 인프라 설계도
<img width="735" alt="인프라설계도(실제)" src="https://github.com/user-attachments/assets/d6546297-4fd0-4b46-a266-a29f74eae95c" /> 
