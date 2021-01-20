# CNA Azure 1차수 (5조_든든한 아침식사)

# 서비스 시나리오

## 든든한 아침식사 (Good Morning Food Delivery) ~ 고객정보이력 추가

기능적 요구사항

1. 고객이 가입을 하면 신규 고객정보 이력을 생성한다. (res/req)
1. 고객이 기본정보 변경을 하면 고객정보 변경 이력을 생성한다. (pub/sub)
1. 고객 정보가 삭제되면 고객정보 삭제 이력을 생성한다. (pub/sub)

비기능적 요구사항

1. 트랜잭션
    : 신규 고객정보 이력 생성 후 3초간 Delay 후에 가입 완료 처리를 한다. (타 시스템 연계 또는 추가 로직이 존재한다고 가정)
1. 장애격리
    : 신규 고객정보의 등록 시 최대 동시 처리량을 제한하여 고객정보 이력 생성에 대한 부하를 차단한다.  Circuit breaker, fallback
1. 성능
    : 고객 정보 변경 이력을 확인 할 수 있어야 한다. CQRS


# Event Storming 모델
 ![event-storming](https://user-images.githubusercontent.com/38008563/105215660-e1696400-5b94-11eb-9951-642ed55ad159.jpg)

## 구현 점검

# SAGA , CQRS , REQ/RES, CORREALTION-KEY

신규 고객이 가입하면 고객정보 이력이 NEW 상태로 생성된다 (동기, req/res)

![reqres-2](https://user-images.githubusercontent.com/41769626/105133623-6a9a7f80-5b30-11eb-8d83-1db0f6fb22c7.PNG)
![reqres](https://user-images.githubusercontent.com/41769626/105133651-77b76e80-5b30-11eb-9adf-2edfc8feac2c.PNG)

신규 고객 정보가 변경되면 고객정보 이력이 MOD 상태로 1건 추가로 생성된다 (비동기, pub/sub)

![saga](https://user-images.githubusercontent.com/41769626/105133790-b3eacf00-5b30-11eb-8c5e-ab4008c590ed.PNG)
![saga2](https://user-images.githubusercontent.com/41769626/105133796-b51bfc00-5b30-11eb-8205-c5f4fd3e1208.PNG)


고객정보 이력이 생성되면 고객이력의 changeHist에도 이력정보가 같이 생성된다. (CQRS)

![cqrs](https://user-images.githubusercontent.com/41769626/105133840-c6650880-5b30-11eb-8921-38b7d063c2a5.PNG)


# 장애 격리

고객 정보 이력 서비스가 내려가더라도 고객 서비스는 정상 동작한다.

![장애차단](https://user-images.githubusercontent.com/41769626/105134333-9c601600-5b31-11eb-915e-68831709ba6f.PNG)

고객 정보 이력 서비스가 올라온 후 다시 고객정보 변경 처리를 진행하면 고객정보 이력이 생성된다.

![장애차단2](https://user-images.githubusercontent.com/41769626/105134460-d03b3b80-5b31-11eb-8e5a-3f08b6686ec5.PNG)


# Gateway

istio-gateway를 통하여 외부 브라우저에서 접속이 가능

![gateway](https://user-images.githubusercontent.com/41769626/105133937-ec8aa880-5b30-11eb-954e-181ca496ffc5.PNG)

# Circuit Breaker

istio connectionPool을 사용한 Circuit Breaker

![istio-connectionPool](https://user-images.githubusercontent.com/38008563/105240526-6f9f1380-5bb0-11eb-96d4-249cf1b6cbcb.png)


Siege 를 사용하여 100클라이언트로 20초간 부하를 발생시킨다.
siege -c100 -t60S -r10 -v --content-type "application/json" 'http://payment:8080/pays POST {"payId":1}'

부하가 발생된 요청은 500으로 빠지며 Availability 가 감소함을 확인한다.

![cir1](https://user-images.githubusercontent.com/41769626/105141802-0c27ce00-5b3d-11eb-8d8f-03df20d32367.PNG)
![cir](https://user-images.githubusercontent.com/41769626/105141805-0d58fb00-5b3d-11eb-9a67-fc6b6291febf.PNG)

# Autoscale(HPA)
autoscale 생성 및 siege 활용 부하 생성

Deploy.yaml 파일 설정

![resource](https://user-images.githubusercontent.com/38008563/105245032-aecf6380-5bb4-11eb-9f28-a284d4700e3c.png)


![hpa1](https://user-images.githubusercontent.com/41769626/105137057-1397a900-5b36-11eb-9119-014b2580510f.PNG)

부하 생성으로 인한 Pod Scale-Out 확인

![hpa](https://user-images.githubusercontent.com/41769626/105137145-2f9b4a80-5b36-11eb-8ddb-edc2b7b91381.PNG)
![hpa2](https://user-images.githubusercontent.com/41769626/105137128-2ad69680-5b36-11eb-957d-c1a824e35522.PNG)
![hpa3](https://user-images.githubusercontent.com/41769626/105137131-2c07c380-5b36-11eb-963f-f95fc524c331.PNG)


# Readiness Probe

Deploy.yaml 에 설정 적용 후 이미지 교체와 동시에 siege 테스트 수행

kubectl set image deployment order order=final05crg.azurecr.io/mileages:v10C
siege -c100 -t20S -v 'http://mileage:8080/mileages'

![readi2](https://user-images.githubusercontent.com/41769626/105137674-0929df00-5b37-11eb-83a4-d1eec543d47f.PNG)

수행 결과 Avaliability 100%

![readi](https://user-images.githubusercontent.com/41769626/105137803-442c1280-5b37-11eb-8cda-4dd716c0ea75.PNG)

# ConfigMap/Presistence Volume
ConfigMap으로 logback 설정 정보를 입력하고 해당 정보를 PVC를 통하여 Pod에 Mount하여 Pod 로깅 포맷을 구성

-- ConfigMap 적용

![ConfigMap](https://user-images.githubusercontent.com/38008563/105241495-1b486380-5bb1-11eb-8176-6c9c6f68ee6d.png)

-- ConfigMap 및 PVC 설정

![deployment](https://user-images.githubusercontent.com/38008563/105245260-f524c280-5bb4-11eb-9c1b-709106d43ffb.png)

-- PVC 사용하여 Pod 접근 후 Mount 된 Volume 확인

![pvc](https://user-images.githubusercontent.com/41769626/105125453-bbee4300-5b1f-11eb-9be6-53d64068771a.PNG)

# Self-Healing (Liveness Probe)

아래 조건으로 Deploy

![liveness](https://user-images.githubusercontent.com/41769626/105143130-c8ce5f00-5b3e-11eb-93a2-11abceea70bd.PNG)

루트 아래 iamalive 가 없으므로 계속 restart함 / pod에 접근하여 iamalive 더미 파일을 생성

![liveness3](https://user-images.githubusercontent.com/41769626/105143493-472b0100-5b3f-11eb-992d-e1a1cfc43ca4.PNG)

생성 후 조건을 만족하여 더 이상 restart 되지 않음

![liveness2](https://user-images.githubusercontent.com/41769626/105143524-4eeaa580-5b3f-11eb-9baf-a87c6ea7ada3.PNG)
