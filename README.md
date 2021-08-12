# 간단한 URL 단축 서비스 만들기

2021.03.07 ~
## 3차시

### 요구사항

1. 화면으로 URL을 생성하고, 로그를 확인할 수 있도록 구성한다.
2. 원하는 클라우드(또는 서버 호스팅) 을 사용해, 배포를 해본다.

###

- 네이버 클라우드를 활용했습니다.
- Docker로 Front, Back-end 어플리케이션을 이미지로 빌드한 후 클라우드에서 받은 VM에서 Docker를 통해 실행했습니다.
 
참고 파일 링크
- [React + Nginx](./front/Dockerfile)
- [Spring Boot Application](./Dockerfile)
- [네이버 클라우드에서 docker-compose](./docker-compose.yml)

## 2차시

### 요구사항

1. 사용자는 어플리케이션을 통해 짧은 URL을 생성할 수 있다. 단, 같은 URL로 요청이 와도 매번 다른 짧은 URL을 생성해야 한다.
2. 사용자는 본 어플리케이션을 통해 짧은 URL로 요청시, 의도한 페이지로 이동할 수 있다.
3. 어플리케이션은, 자신을 통해 생성된 짧은 URL로 요청이 올 때 사용자의 정보를 수집 및 보관한다. 이 때, 저장할 정보는 IP는 필수, 그 외 값은 임의로 한다.
4. 사용자는 그렇게 보관된 정보를 조회할 수 있다.

- 이를 구현하는 과정에서의 라이브러리 및 DBMS등의 사용은 자신이 원하는데로 한다.

### API 정리

1. URL 생성

- POST [{host}/]
    ```json
    {
      "url" : "https://www.naver.com"
    }
    ```
- RESPONSE
    - STATUS:201
      ```json
      {
        "path": "R0MwTW04"
      }
      ```

2. 생성된 Short URL로 이동

- GET [{host}/{{path}}]
- RESPONSE
    - STATUS:303
- EXCEPTION
    - STATUS:404
      ```json
      {
        "notFoundResource": "NOT_EXISTS"
      }
      ```

3. 짧은 URL에 쌓인 로그 조회

- GET [{host}/shorts/{path}/logs]
- RESPONSE
    - STATUS:200
      ```json
        {
          "data": {
          "path": "NUdpN0JW",
          "requestCount": 1,
          "url": "https://www.naver.com",
          "logs": [
              {
                  "ip": "127.0.0.1",
                  "referer": null
              }
          ]
          },
          "transactionTime": "2021-03-31T16:24:46.459485",
          "message": "OK"
        }
      ```
- EXCEPTION
    - STATUS:404
      ```json
      {
        "notFoundResource": "NOT_EXISTS"
      }
      ```

---

## 1차시

goo.gl 이나, me2.kr 과 같은 서비스에서는 URL 단축 서비스를 제공한다. 예를들면, https://www.thisismyhostname.com/?wow-unbelivable 과 같이 다소 긴 URL을
해당 서비스가 제공하는 도메인과, path 형태로 줄여준다.
(예: https://me2.kr/H2ECSD)

주소를 줄이게 되면 여러 이득이 있다. 간단한 예를 들자면 아래의 경우다.

- 휴대폰 문자메시지로 URL을 보내야 하는데 너무 긴 경우
- URL을 종이(?) 같은 곳에 옮겨적고 싶은경우
- 특정 메신저 또는 게시글 발행서비스에서 최대 문자열 길이가 제한된 경우

이를 먼저 간단히 구현해보자.

### 요구사항

- URL 단축하기
- 단축된 URL은 중복되지 않아야함
- 단축된 URL와 원본 URL 관리하기(우선, 저장소 없이 메모리에서 관리 )
- 서버에서 관리되고 있는 URL을 확인할 수 있게 하기

### 정리

- URL 단축은 원본 URL을 SHA256으로 Hash후, Base64로 인코딩, 앞 글자 8개를 잘라서 생성했다.
- DB를 안쓰고 구현하는김에 심심해서 JPA와 비슷한 역할을 하는 Repository를 만들어봤다.
- POST /{url} 로 줄어든 url을 생성할 수 있다.
- GET /logs 로 저장된 원본 Url을 모두 조회할 수 있다.
- GET /{shortUrl} 로, 원래 페이지로 이동할 수 있다.