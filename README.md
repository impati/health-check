# health-checking

---

클라우드 환경에 서버를 배포하고 난 후 알 수 없는 이유로 왕왕 서버가 내려가 있었다.

이를 해결하기위한 방법으로는 
- 직접 접속하여 커맨드를 통해 작동하고 있는지 확인한다.
- 클라우드 서비스 중 헬스체크 기능을 사용한다.
- 직접 서비스에 접속하여 확인한다.
- 직접 헬스체크 서버를  구현한다.


직접 서버 헬스 체크 기능을 구현하여 서버가 다운시 사용자의 이메일로 알림을 보내어 

이 문제를 해결해보자


# 기능

---
## Checking Queue

하나의 쓰레드가 **1분마다** 우선순위 큐를 체크합니다.

- 큐에 있는 element 가 현재 시간보다 늦은 경우 검사를 실시합니다.
- 검사는 특정 앤드포인트로 요청을 보내는 것입니다. (동기  , 비동기)
- 검사를 실시하기전에 큐에서 꺼내어 값을 업데이트 한 후 다시 큐에 삽입합니다.


## Synchronization Queue

**30분** 마다 데이터베이스 변경 여부 공유변수를 확인하고 동기화를 진행합니다.

- 공유 변수가 false 라면 패스합니다. 
- 공유 변수가 true 라면 큐에 존재하는 비활성화 요소들을 제거하고 새로운 활성화 요소를 큐에 삽입합니다.

## Server Checking

- 약속된 서버에 요청을 보내고 정상 응답인지를 확인합니다.
- 정상 응답이라면 정상응답을 저장하고 종료합니다.
- 정상 응답이 아니라면 3번 연속시도 후 메일로 알람을 보내고 실패응답을 저장하고 종료합니다.


## Send Mail
- Server Checking 실패 후 사용자 이메일로 실패 이메일을 보낸다.
- 연속 두번 실패시 더이상 보내지 않으며 비활성화 상태로 전환합니다.