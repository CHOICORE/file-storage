# 테스트 방법

## 성공 케이스

- file.yml에 사용자 정의된 속성 값이 `FileProperties` 클래스에 정상적으로 바인딩 되는지 확인
- 업로드한 파일이 정상적으로 저장되는지 확인

---

## 실패 케이스

- `spring.servlet.multipart.max-file-size` 설정을 통해 업로드 파일의 최대 크기를 제한하고, 이를 초과하는 파일이 업로드 되지 않는지 확인
    - `spring.servlet.multipart.max-file-size` : 단일 파일의 최대 크기

## 알게된 점

**MockMvc로 테스트를 진행할 경우 `spring.servlet.multipart.max-request-size`는 테스트에 영향을 주지 못한다.**

(spring.servlet.multipart.* 하위 속성 값들은 `MultipartProperties`클래스에 정상적으로 바인딩 된다.)

그 이유는 `MockMvc`는 `MockHttpServletRequest`인터페이스를 사용하기 때문에, `MultipartConfigElement`클래스의 설정이 적용되지 않는다.

`MultipartConfigElement`의 설정을 사용하려면, `org.apache.catalina.connector.Request`클래스가 동작해야한다.

`HttpServletRequest` 인터페이스를 서브 클래싱한 `org.apache.catalina.connector.Request` 클래스에서 확인할 수 있다.

```java
import jakarta.servlet.MultipartConfigElement;

private void parseParts(boolean explicit) {
    // ...
    MultipartConfigElement mce = this.getWrapper().getMultipartConfigElement();
    // ...
}

```

`parseParts` 메서드 내부 로직을 살펴보면, `MultipartConfigElement`클래스의 설정을 사용하고 있음을 알 수 있다.

`RestAssured` 또는 `RestTemplate`를 사용해서 실제 서버를 기동시키고 테스트를 진행해야 되는건가 잘 모르겠다.

---
