## User API server

💡 회원가입 / 로그인 User API 서버

#### Requirements
- Java11
- Spring Boot 2.6.3
- MySQL 8.0
- Docker 20.10.7
- Docker compose 1.25

#### 기능

- [x] Spring 회원가입 Controller
- [x] Spring 로그인 Controller
- [x] MySQL DB 연동
- [x] Spring 서버 Docker 컨테이너화
- [x] DB 연동 Docker-compose 동기화

#### 사용방법
```sh
$ git clone https://github.com/Castock/user-api.git
$ .env 파일을 Project root 폴더에 생성
$ ./build.sh
$ ./run.sh
```

#### .env 파일
대괄호 안에 해당 Value 넣어서 사용
```sh
URL=jdbc:mysql://[DB name]:3306/test_db?autoReconnect=true
DB_ROOT_PW=[MySQL Root password]
DB_USER=[DB username]
DB_NAME=[DB name]
DB_PW=[DB password]
```
