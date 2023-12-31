> ## 프로젝트 목표

- 파일 업로드, 다운로드, 삭제, 조회 및 관리하는 시스템입니다.
- 테스트 코드를 통해 동작을 확인하고, 점진적으로 개선해나가는 방식으로 진행합니다.
- 웹 환경은 [Spring Web MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html)로 동작하지만 추 후 [Spring WebFlux](https://docs.spring.io/spring-framework/reference/web/webflux.html)를 사용하여, 비동기 방식으로 전환해볼 예정입니다.

> ## 애플리케이션 요구사항
**이 API는 다음과 같은 기능을 제공합니다.**

### 사용자 기능

1. **파일 업로드**
    - 사용자는 파일을 업로드할 수 있는 기능을 제공받습니다.
    - 업로드 가능한 파일의 최대 용량에는 제한이 있습니다.
    - 허용되는 파일 확장자에 대한 제한이 있습니다.
    - 업로드된 파일은 서버에 저장되며, 원본 파일의 메타정보를 보존합니다.
    - 각 업로드된 파일은 고유 식별자로 관리됩니다.
    - 다중 파일 업로드는 지원되지 않으며, 여러 파일 업로드가 필요한 경우 클라이언트 측에서 비동기 방식으로 개별 업로드를 수행합니다.

2. **파일 다운로드**
    - 사용자는 업로드한 파일을 다운로드할 수 있습니다.
    - 파일을 다운로드할 때는 업로드 시 부여된 고유 식별자를 이용합니다.
    - 단일 파일은 원본 형태로 다운로드되며, 여러 파일을 선택한 경우 압축하여 다운로드합니다.

3. **파일 삭제**
    - 사용자는 자신이 업로드한 파일을 삭제할 수 있는 기능을 이용할 수 있습니다.
    - 삭제된 파일은 임시 디렉토리(휴지통)로 이동되어 30일 동안 복구할 수 있습니다.
    - 휴지통의 파일은 복구할 수 있으며, 복구된 파일은 원래의 경로로 이동합니다.
    - 30일이 지난 파일은 자동으로 삭제되어 복구할 수 없습니다.

4. **파일 목록 조회**
    - 사용자는 자신이 업로드한 파일들의 목록을 확인할 수 있습니다.
    - 파일 목록은 페이징과 슬라이싱을 제공합니다.
    - 파일은 고유 아이디, 파일명, 파일 크기, 업로드 일자를 제공합니다, 또한 상세 조회를 위한 링크를 제공합니다.
    - 파일 목록은 전체, 파일 유형(사진,비디오,문서)을 기준으로 카테고리를 나누어 조회할 수 있습니다.
        - 전체 목록의 경우 업로드 일자를 기준으로 내림차순 정렬합니다.
        - 카테고리 별 목록인 경우(사진,비디오,문서) 와 같은 파일 유형으로 그룹화 하고 업로드 일자를 기준으로 내림차순 정렬합니다.

5. **파일 상세 조회**
    - 사용자는 특정 파일의 상세 정보를 조회할 수 있습니다.

---

### 기능 구현

1. 파일 업로드
   - API Path: /v1/files
   - Http Method: PUT
   - Content-Type: multipart/form-data
   - Request Body: 파일

   > PUT /v1/files HTTP/1.1
   >
   > Host: localhost:8080
   >
   > Content-Type: multipart/form-data;

    - 적합한 Http Method 를 결정하고 Multipart/form-data 형식으로 파일을 업로드 합니다.
        - POST, PUT 방식 중 논의를 통해 결정합니다.
    - 파일 업로드 시, 파일의 효율적인 탐색을 위해 메타 정보를 데이터베이스에 함께 저장합니다.
        - 원본 파일명 (원본 데이터 저장 용)
        - 저장된 파일명 (중복 방지)
        - 파일 확장자 (파일 타입 확인 용)
        - 파일 크기 (파일 용량 확인 용)
        - 파일 타입 (파일 타입 확인 용)
        - 파일 생성일
        - 파일 수정일
        - 파일 업로드 일
        - 파일 업로드 사용자