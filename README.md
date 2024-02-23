## 헥사고날 아키텍처 기반 Respal

### 설계 방법
1. 도메인별 행동을 정의한다.
2. 행동을 기반으로 유스케이스를 정의한다.

### 아키텍처 규칙
- 도메인별 패키지 구분
- 행동 (기능) 단위로 분리
- Layer
  - 외부에서 내부로 (domain <- application <- adapter) 의존한다.
  - Adapter : 외부와 연결되는 역할
    - 포트 인터페이스를 통하지 않고서는 외부에서 접근을 하면 안됨 -> package-private(default)
    - in 
      - web 
    - out
      - persistence
  - Application 
    - service
      - UseCase들의 구현체, 실질적 비즈니스 로직 담당
      - incoming port를 통해서만 접근이 가능해야함 -> public x
    - port
      - in
        - UseCase : **비즈니스 규칙** 검증 담당
          - incoming Adapter로부터 입력을 받음. -> public
          - 입력 유효성 검증을 하지않고 비즈니스 로직을 검증해야함.
            - service가 아닌, service로 전달해주는 dto를 통해 입력 유효성을 검증해야함
            - 입력 유효성과 비즈니스 로직의 검증 방법 : 모델의 현재 상태 참조 유무에 따라 결정
              - 단순 DB 조회의 경우나 여의치 않은 경우 UseCase에서 검증한다.
              - 로직이 복잡해져서 도메인 모델에 로드해서 검증하는경우 도메인 내부에서 검증을 한다.
          - 읽기 전용 유스케이스 (단순히 DB로부터 도메인의 상태 조회만 필요한 경우)
            - 쿼리를 위한 인커밍 전용 포트 추가 후 쿼리 서비스에 구현
            - 쓰기가 가능한 유스케이스와 읽기 전용 쿼리를 분리 (CQS, CQRS)
          - process
            1. 입력을 받는다.
            2. 비즈니스 규칙을 검증한다. (비즈니스 규칙 검증하여 모델의 상태를 변경)
            3. 모델 상태를 조작한다. (영속성 어댑터를 통해 구현된 포트로 전달하여 데이터 저장)
            4. 출력을 반환한다. (해당 유스케이스를 호출한 어댑터로 반환할 출력 객체로 변환하여 반환)
               - 각 유스케이스에 맞게 구체적(최소화)으로 출력한다.
                 - 유스케이스들간 출력 모델을 공유하게되면, 유스케이스들끼리의 커플링이 올라감.
               - 호출자에게 꼭 필요한 데이터만 들고있어야 한다.
      - out
        - Persistence Adapter와 application 연결
  - Domain
    - 도메인 및 행동 정의
    - 풍부한 도메인 모델
      - ✅ 엔티티에서 가능한 많은 도메인 로직 구현 -> 객체지향적 접근을 위해 해당 모델 적용
    - 빈약한 도메인 모델
      - 엔티티에는 상태를 갖는 필드만 정의 후 유스케이스에서 구현
