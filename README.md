# **카카오페이 과제3 Source code build guide**

## 개발환경
* STS4 ([STS4](https://spring.io/tools) 설치)
* OpenJDK 11
* Spring Boot 2.2.5
* Lombok ([Lombok](https://projectlombok.org/download) 설치)
* MySQL Community Server 8.0.19

---

## Build Guide
* `Github`에서 코드 다운로드
* `STS` 실행 -> `STS Import` -> `Maven` -> `Existing Maven Projects` 메뉴에서 소스 코드 경로 선택

## jar 파일 생성
* 빌드할 프로젝트 선택 후 우클릭 -> `Run As` -> `Maven build` -> `Goals:`에 `package`입력 -> `Run` 선택
* `target` 폴더에 `project명-0.0.1-SNAPSHOT.jar` 결과물 생성

## 웹서버 실행
* 서버를 실행할 프로젝트 선택 후 우클릭 -> `Run As` -> `Spring Boot App` 선택

## JUnit 테스트 실행
* 테스트할 프로젝트 선택 후 우클릭 -> `Run As` -> `JUnit Test` 선택

---

## 가입 API
* URI : /signup
* Method : POST
* Param : {userid : "admin", "password" : 1234}

## 로그인 API
* URI : /signin
* Method : POST
* Param : {userid : "admin", "password" : 1234}

## 토큰 갱신 API
* URI : /refresh
* Method : POST
* Header : {"Authorization" : "Bearer token"}

## 파일 등록 API
* URI : /finance/save
* Method : POST
* Header : {"Authorization" : "Bearer token"}
* 파일 경로는 "C:/사전과제3.csv" 위치하고 있어야 함

## 주택금융 공급 금융기관(은행) 목록을 출력하는 API
* URI : /finance/banklist
* Method : GET
* Header : {"Authorization" : "Bearer token"}

## 년도별 각 금융기관의 지원금액 합계를 출력하는 API
* URI : /finance/house/state
* Method : GET
* Header : {"Authorization" : "Bearer token"}

## 각 년도별 각 기관의 전체 지원금액 중에서 가장 큰 금액의 기관명을 출력하는 API
* URI : /finance/minmax
* Method : GET
* Header : {"Authorization" : "Bearer token"}

## 특정 은행의 특정 달에 대해서 2018년도 해당 달에 금융지원 금액을 예측하는 API
* URI : /finance/predict
* Method : GET
* Header : {"Authorization" : "Bearer token"}
* Param : {"bank" : "국민은행", "month" : 2}
