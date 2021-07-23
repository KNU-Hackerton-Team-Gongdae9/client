<h1 align="center">KNU 커뮤니티 서비스</h1>


## 제작 배경

    - COVID 19 장기화로 인해서 비대면 수업이 잦아짐에 따라 소통할 수 있는 기회가 적음


    - 대면으로 만날 기회가 적다보니 팀 프로젝트 진행이나 스터디를 진행함에 있어서 어려움을 겪고 있음

**이러한 이유로 컴퓨터학부 학생들과 소통을 할 수 있는 커뮤니티 서비스를 제작함**

> 우리 컴퓨터학부 학부생들이 코로나 시대의 힘든 비대면 상황을 슬기롭게 해결하고 극복해나가면서,
자신의 커리어를 향상시킬 수 있는 기회로 변모시킬 수 있도록 도움을 준다.

<br/>

## 프로젝트 개요

### Client

- `Kotlin`, `Android Studio` 사용


- `Retrofit2` 를 통해 서버와 API 통신


### Server   

- `Spring Boot`, `JPA(Java Persistence Assistance)`, `Spring Boot Security`, `Spring Boot Data JPA`, `Redis`, `Swagger API` 사용

   
- 클라우드, 오픈소스 기반의 다양한 서비스를 활용하였다. => `AWS EC2`, `AWS RDS`, `AWS S3`, `Docker`

   
- HTTP 프로토콜 위에서 동작하는 `토큰 기반의 인증 방식(JWT)`을 사용해 요청에 대한 보안을 강화


### System Architecture

![공대구호관팀구조](https://knu-moapp2.s3.ap-northeast-2.amazonaws.com/static/hack-architecture.png)


### Collaboration Tool

- **Server Github** : https://github.com/KNU-Hackerton-Team-Gongdae9/server


- **Client Github** : https://github.com/KNU-Hackerton-Team-Gongdae9/client


- **Server API Document** : `클라이언트`-`서버` 간 적극적으로 협업하기 위해 API 문서를 설계하여 실시간으로 관리

   
   ![일부 구현](https://user-images.githubusercontent.com/51476083/126737771-ad2c4307-7d68-48cf-acd1-3aa60e52494b.png)

[더 자세한 문서는 링크 참고](http://52.79.203.88:8080/swagger-ui.html#/)

### Youtube
[유튜브] (https://youtu.be/kNJewxJJT3w)
<br/>


## 프로젝트 대표 기능 설명

1. **회원가입 인증**

        회원가입의 경우 경북대학교 학생만을 가입할 수 있게 구현하였다. 
        이메일로 회원가입을 진행할때 knu.ac.kr의 메일 주소를 사용해야지만 가입이 가능하다. 입력한 이메일 형식을 Validation 한다. 
        이후 로그인도 가입 시 작성한 이메일을 통해서만 가능하다. 


2. **게시판 및 댓글 답글 기능**

        유저들은 카테고리 (자유게시판, 팀프로젝트 모집, 스터디모집, QnA 게시판)를 선택하여 
        게시글을 작성하고 댓글 및 답글을 게시할 수 있도록 하였다. 
        


3. **프로필 기능**

        유저들은 글 작성자의 프로필을 확인할 수 있다. 
        프로필에는 자신의 학년과 닉네임 및 다룰 줄 아는 언어가 나타나있으며 `Github Url`, `Velog`와 같은 개인 블로그 Url을 확인
        할 수 있도록 하여 자신과 비슷한 진로를 설계하고 준비해나가는 학우들을 컨택하기 쉽도록 하였다.


4. **쪽지 기능**

        글의 작성자나 댓글 작성자 및 프로필을 확인하는 유저들에게 쪽지를 보낼 수 있도록 구현하였다.
