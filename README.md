<h1 align="center">KNU 커뮤니티 서비스</h1>


## 제작 배경

    - COVID 19 장기화로 인해서 비대면 수업이 잦아짐에 따라 소통할 수 있는 기회가 적음


    - 대면으로 만날 기회가 적다보니 팀 프로젝트 진행이나 스터디를 진행함에 있어서 어려움을 겪고 있음

이러한 이유로 컴퓨터학부 학생들과 소통을 할 수 있는 커뮤니티 서비스를 제작함

<br/>

## 프로젝트 개발 언어

    - Client의 경우 Kotlin을 이용하여 Android 애플리케이션을 제작

    - Server는 Spring Boot를 이용하여 제작하였으며, 데이터베이스의 경우 ORM 기반의 JPA를 이용하여 구축하였으며 Mysql을 사용하였다.

Server Github : https://github.com/KNU-Hackerton-Team-Gongdae9/server

<br/>


## 프로젝트 대표 기능 설명

1. 회원가입 인증
  
        회원가입의 경우 경북대학교 학생만을 가입할 수 있게 구현하였다. 
        이메일로 회원가입을 진행할때 knu.ac.kr의 메일 주소를 사용 해야지만 가입이 가능하다. 
        또한 회원가입을 완료하면 자신이 설정했던 아이디로 인증메일이 전송이되고 그것을 클릭해야 정상적으로 사용이 가능하다.

2. 게시판 및 댓글 답글 기능
   
        유저들은 카테고리 (자유게시판,팀프로젝트 모집,스터디모집,QnA 게시판)를 선택하여 
        게시글을 작성하고 댓글 및 답글을 게시할 수 있도록 하였다. 
        물론 서비스의 유저만 게시판 기능을 사용할 수 있도록 하였다.


3. 프로필 기능
 
        유저들은 글 작성자의 프로필을 확인할 수 있다. 
        프로필에는 자신의 학년과 닉네임 및 다룰 줄 아는 언어가 나타나있으며 
        Github Url, velog와 같은 개인블로그 Url을 확인할 수 있도록 하였다.


4. 쪽지 기능

        글의 작성자나 댓글 작성자 및 프로필을 확인하는 유저들에게 쪽지를 보낼 수 있도록 구현하였다.
