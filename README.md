# 실습을 위한 개발 환경 세팅
* https://github.com/slipp/web-application-server 프로젝트를 자신의 계정으로 Fork한다. Github 우측 상단의 Fork 버튼을 클릭하면 자신의 계정으로 Fork된다.
* Fork한 프로젝트를 eclipse 또는 터미널에서 clone 한다.
* Fork한 프로젝트를 eclipse로 import한 후에 Maven 빌드 도구를 활용해 eclipse 프로젝트로 변환한다.(mvn eclipse:clean eclipse:eclipse)
* 빌드가 성공하면 반드시 refresh(fn + f5)를 실행해야 한다.

# 웹 서버 시작 및 테스트
* webserver.WebServer 는 사용자의 요청을 받아 RequestHandler에 작업을 위임하는 클래스이다.
* 사용자 요청에 대한 모든 처리는 RequestHandler 클래스의 run() 메서드가 담당한다.
* WebServer를 실행한 후 브라우저에서 http://localhost:8080으로 접속해 "Hello World" 메시지가 출력되는지 확인한다.

# 각 요구사항별 학습 내용 정리
* 구현 단계에서는 각 요구사항을 구현하는데 집중한다. 
* 구현을 완료한 후 구현 과정에서 새롭게 알게된 내용, 궁금한 내용을 기록한다.
* 각 요구사항을 구현하는 것이 중요한 것이 아니라 구현 과정을 통해 학습한 내용을 인식하는 것이 배움에 중요하다. 

### 요구사항 1 - http://localhost:8080/index.html로 접속시 응답
*네트워크 요청 받아드리기*
1. java.net.ServerSocket(portNumber)를 생성하면 해당포트로 연결되는 네트워크 접속을 감지한다.
2. (무한루프를 통해서) 소켓접속이 감지되면, 해당 소켓을 분석하여서 적절한 로직을 수행한다.
3. 소켓의 inputStream에는 요청정보들이 담겨있고, outputStream에 응답정보를 작성할 수 있다.

### 요구사항 2 - get 방식으로 회원가입
* 요청 path에 queryString으로 정보들이 들어온다.

### 요구사항 3 - post 방식으로 회원가입
*HTTP 프로토콜*
- 요청
```
GET /path HTTP/1.1
Accept: application/json
Authorization: Bearer UExBMDFUMDRQV1MwMnzpdvtYYNWMSJ7CL8h0zM6q6a9ntw
... (`key: value` 형태의 헤더정보)
(빈줄)
...(바디정보)
```

- 응답
```
HTTP/1.1 200 OK
Date: Mon, 23 May 2005 22:38:34 GMT
Content-Type: text/html; charset=UTF-8
Content-Encoding: UTF-8
... (`key: value` 형태의 헤더정보)
(빈줄)
...(바디정보)
...
```

- (HTTP) 프로토콜이란, 결국 약속! 이다. *문자열*로 요청을 보내지만 클라이언트와 서버간에 *약속되어져 있는 형태*로 보내면 된다. 약속된 형태의 *문자열*을 클라이언트와 서버가 subString하여 파싱하고 정보를 추출하는 것이다.
    - 예를들어 헤더정보는 반드시 `key: value` 형태여야 한다. 콜론뒤에 빈칸도 포함되어있어야 한다!
    - 예를들어 순서는 반드시 `요청라인(첫줄)-헤더정보-빈줄-바디정보` 여야한다. 
    - 바디정보에 form 정보가 `key1=value1;key2=value2` 형태로 드러있다.

### 요구사항 4 - redirect 방식으로 이동
* HTTP 응답라인(첫줄)에 status 코드가 300번대라면, 클라이언트에게 리다이렉트를 하라는 프로토콜이다.
* 헤더정보에 `Location: /redirect-url` 형태로 보내면 어떤 주소로 리다이렉트 해야하는지 클라이언트에게 알려줄 수 있다.
* *HTTP는 프로토콜(약속)* 이고, 실제 형태는 문자열일뿐이다.

### 요구사항 5 - cookie
* 요청 헤더에 `Cookie: key1=value1;key2=value2` 형태로 클라이언트가 가지고 있는 쿠키 값을 서버에게 보낸다.
* 응답 헤더에 `Set-Cookie: key1=value1;key2=value2` 형태로 서버가 클라이언트에게 쿠키 값을 설정하라고 알려준다.
* *HTTP는 프로토콜(약속)* 이고, 실제 형태는 문자열일뿐이다.

### 요구사항 6 - stylesheet 적용
* 응답 헤더에 `Content-Type: text/css` 형태로 서버가 클라이언트에게 css 파일이라는 점을 알려준다.
* *HTTP는 프로토콜(약속)* 이고, 실제 형태는 문자열일뿐이다.

### 설계적 영감
- path를 매핑하는 작업에서 전략패턴을 적용하고 각 컨트롤러는 Constructor를 이용하여서 매핑해두었다 :thumbup:
- 컨트롤러들은 하나씩만 필요하여 싱글톤으로 만들었다.
- 스프링은 전략패턴과 싱글톤을 '쉽게' 구현하도록 도와준다. 그래서 개발자들이 이 패턴들을 사용하지 않아도 되는 편의가 생겼다.
- 싱글톤으로 분리하면, 이를 이용하면서 파생되는 DTO(Data,Domain..) 객체들이 *필연적으로* 만들어진다. 이를 잘 인지하고 분리하자! (HttpRuqest, HttpResponse)
- AOP같이 모든 작업들의 선작업 혹은 후작업이 필요하다면, 여기서 상속을 사용해보는 것을 고려해보자! (중복때문에 상속사용하는게 아니다. AOP를 구현하기 위함)

### ec2 서버에 배포 후
*EC2 접속하기*
1. chmod 400 jwp-book.pem
    - 키파일 퍼미션을 400으로 두어야 ssh가 작동한다. (라고 aws에서 안내해줌)
2. ssh -i "jwp-book.pem" [ubuntu@ec2-13-124-248-99.ap-northeast-2.compute.amazonaws.com](mailto:ubuntu@ec2-13-124-248-99.ap-northeast-2.compute.amazonaws.com)
    - ssh로 키파일을 이용하여서 ec2에 접속
    - ubuntu@ 는 계정명을 말하는것 같다. 뒤에 주소는 ec2에서 명시하는 퍼블릭IP
        - 그래서 아마존 리눅스로 다시 만드니까 유저명이 ec2-user였고 ec2-user@로 해야 됐다!

*한글 로케일 설정하기*
1. sudo locale-gen ko_KR.EUC-KR ko_KR.UTF-8
2. sudo dpkg-reconfigure locales
3. .bash_profile 파일에 설정추가
   ```
   LANG="ko_KR.UTF-8"
   LANGUAGE="ko_KR:ko:en_US:en" 
   ```
4. source .bash_profile

*JDK와 메이븐 설치하기*
1. wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" [https://download.java.net/openjdk/jdk8u41/ri/openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz](https://download.java.net/openjdk/jdk8u41/ri/openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz)
    - jdk 다운로드
2. gunzip openjdk-8u41-b04-linux-x64-14_jan_2020.tar.gz
    - 압축풀기 gz
3. tar -xvf openjdk-8u41-b04-linux-x64-14_jan_2020.tar
    - 압축풀기 tar (따로따로 풀어야하네)
4. ln -s java-se-8u41-ri/ java
    - 압축푼 디렉토리 'java'로 심볼릭 링크 만들기
5. 프로필 파일에 path 추가 (로케일 설정시에 사용했던 .bash_profile에다가 했다)
   ```
   bash
   export JAVA_HOME=~/java
   export PATH=$PATH:$JAVA_HOME/bin
   ```
    - JAVA_HOME에 (심볼릭 설정한) java 폴더 설정하고
    - PATH설정 추가!
6. source .bash_profile
    - 설정 반영
7. java -version
    - path설정이 잘 되었나 확인
8. wget [http://apache.mirror.cdnetworks.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz](http://apache.mirror.cdnetworks.com/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz)
    - 메이븐 다운로드
9. gunzip apache-maven-3.6.3-bin.tar.gz
    - 압축풀기
10. tar -xvf apache-maven-3.6.3-bin.tar
    - 압축풀기
11. 프로필 파일에 메이븐 path 추가
    ```
    bash
    export JAVA_HOME=~/java
    export MAVEN_HOME=~/maven
    export PATH=$PATH:$JAVA_HOME/bin:$MAVEN_HOME/bin
    ```
12. source .bash_profile
13. env
    - 환경설정 확인
14. mvn -version
    - path 설정 잘 됐나 확인
    
*JDK, 메이븐 설치하기 (apt-get으로)*
- 이렇게 하니까 메이븐 에러가 안남.. 아니면 우분투 버전을 16버전으로 하니까 안나는 걸 수 도있어
- sudo apt-get install openjdk-8-jdk
- sudo apt-get install maven

*Git 설치하고 프로젝트 받기*
1. (sudo apt-get install git)
    - 보통 설치가 되어있음
2. git —version
    - git 설치되었는지 확인
3. git clone [https://github.com/siyoon210/web-application-server](https://github.com/siyoon210/web-application-server)
    - 배포할 레포 git clone
4. mvn clean package
    - 클론해온 레포 디렉토리 안에서 실행
    - 메이븐으로 삭제(clean) 후 빌드(package)
    - 결과물은 target 디렉토리 안에 생김
5. git clone [https://github.com/siyoon210/web-application-server](https://github.com/siyoon210/web-application-server)
    - 배포할 레포 git clone
6. mvn clean package
    - 클론해온 레포 디렉토리 안에서 실행
    - 메이븐으로 삭제(clean) 후 빌드(package)
    - 결과물은 target 디렉토리 안에 생김

*ec2 포트열기*
- ec2 정보에서 `보안그룹`을 선택한다. (보통 launch-wizard-4 이런식에 이름으로 되어있음)
- `사용자 지정 TCP` 8080(원하는 번호) 포트를 공개한다. (혹은 HTTP 80을 오픈해도 됨)
    - 소스가 0.0.0.0/0, 이랑 ::/0 두개가 생기는데 뭔지는 잘 모르겠음
    
*빌드 배포하기*
- mvn clean package
- java -cp target/classes:target/dependecy/* webserver.Webserver 8080
- 재배포하기 위해서 프로세스 끄기
    - ps -ef | grep webserver
        - 실행한 프로세스 아이디(PID)를 찾고
    - kill -9 $PID
        - 웹서버 종료
    - 위에 배포코드실행