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

![customer-req](https://user-images.githubusercontent.com/38008563/105272559-f4078b80-5bdc-11eb-81df-782b9cac1f28.png)
![customerhist-res](https://user-images.githubusercontent.com/38008563/105274431-83626e00-5be0-11eb-8391-b825f54d2082.png)

신규 고객 정보가 변경되면 고객정보 이력이 MOD 상태로 1건 추가로 생성된다 (비동기, pub/sub)

![saga](https://user-images.githubusercontent.com/38008563/105278153-3f736700-5be8-11eb-848a-0c038a28a95e.png)
![saga2](https://user-images.githubusercontent.com/38008563/105278062-1226b900-5be8-11eb-8979-0100e781963c.png)


고객정보 이력이 생성되면 고객이력의 changeHist에도 이력정보가 같이 생성된다. (CQRS)

![cqrs](https://user-images.githubusercontent.com/38008563/105280034-3a181b80-5bec-11eb-98d9-a4a526e7b5fa.png)


# 장애 격리

고객 정보 이력 서비스가 내려가더라도 고객정보 변경 처리는 가능하다.

![장애격리](https://user-images.githubusercontent.com/38008563/105280560-63857700-5bed-11eb-984f-ffd59d2b4c6b.png)

고객 정보 이력 서비스가 올라온 후 다시 고객정보 변경 처리를 진행하면 고객정보 이력이 생성된다.

![장애격리_조치후](https://user-images.githubusercontent.com/38008563/105280872-28d00e80-5bee-11eb-86c4-e9866013a6b9.png)


# Gateway

istio-ingressgateway를 통하여 외부 브라우저에서 접속이 가능

![istio-system-svc](https://user-images.githubusercontent.com/38008563/105280951-62087e80-5bee-11eb-9a2c-49ee51886f37.png)
![virtualservice](https://user-images.githubusercontent.com/38008563/105282966-d34a3080-5bf2-11eb-86f5-afcc6d7290cd.png)
![external-ip](https://user-images.githubusercontent.com/38008563/105282729-50c17100-5bf2-11eb-9f8a-cb031ec30647.png)

# Circuit Breaker

Destination Rule 을 통하여 istio connectionPool을 제한하여 Circuit Breaker를 구현

![istio-destinationrule](https://user-images.githubusercontent.com/38008563/105283110-22906100-5bf3-11eb-807a-3e7cdbd3e565.png)

customer의 req/res 에 대하여 customerhist가 3초간 대기하는 로직이 추가되어 있어 customer를 통한 기능 확인

![istio-connectionPool](https://user-images.githubusercontent.com/38008563/105240526-6f9f1380-5bb0-11eb-96d4-249cf1b6cbcb.png)


Siege 를 사용하여 10클라이언트로 15초간 부하를 발생
siege -c10 -t15S -v --content-type "application/json" 'http://customer:8080/customers POST {"name":"test", "phone":"010" , "address":"USA", "age":30 }'

일부 트래픽은 정상처리되고 있으나, 일부는 503 오류 Return 되고 있음을 확인

![cir1](https://user-images.githubusercontent.com/38008563/105284235-6c7a4680-5bf5-11eb-8010-b8732c07d4f5.png)

해당 내용은 Kiali에서도 확인이 가능
![kiali](https://user-images.githubusercontent.com/38008563/105283982-e3fba600-5bf4-11eb-8e26-4ba503e1c7d9.png)

# Autoscale(HPA)
autoscale 생성 및 siege 활용 부하 생성
부하발생 전 상황
![bf_hpa](https://user-images.githubusercontent.com/38008563/105285899-a6991780-5bf8-11eb-8e6f-5d8215adf6d6.png)

Deploy.yaml 파일 설정

![resource](https://user-images.githubusercontent.com/38008563/105284399-b105e200-5bf5-11eb-9539-1b7b8fd49ea0.png)


![hpa](https://user-images.githubusercontent.com/38008563/105285771-6c2f7a80-5bf8-11eb-814b-3ffa1e396431.png)

부하 생성으로 인한 Pod Scale-Out 확인

![hparesult](https://user-images.githubusercontent.com/41769626/105137145-2f9b4a80-5b36-11eb-8ddb-edc2b7b91381.PNG)
![hparesult2](https://user-images.githubusercontent.com/41769626/105137128-2ad69680-5b36-11eb-957d-c1a824e35522.PNG)
![hpa3](https://user-images.githubusercontent.com/38008563/105286025-df38f100-5bf8-11eb-828b-4174eb900023.png)


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
