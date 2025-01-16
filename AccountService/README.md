# 기본 경로
- tomcat에서 배포할 때, Application Context를 "/account"로 설정
# 세션에 담을 데이터
## 기존 ActionBean
```
기존 ActionBean은 Session에 저장되며, 세 가지의 상태를 저장하고 있습니다.
1. Account 객체: 로그인 성공 시에 사용자 정보를 담은 객체
2. MyList: 사용자가 즐겨찾기한 동물 카테고리의 동물 목록
3. isAuthenticated: 로그인 여부
```

## 변경 후,,,
```
Controller로 변경 시, Spring bean으로 관리되며 상태를 저장하지 않습니다.
ActionBean이 갖는 상태들은 각 모듈들 간에 공유되는 Session 공간으로 이동하게 됩니다.
기존의 웹 앱 형태를 최대한 보존할 수 있습니다.
모듈 간에 세션을 공유하기 위해 Redis를 선택하게 되었습니다.
```

### Redis의 장점
```
JPetStore6 웹 앱에서 사용자 정보가 Session에 담겨있기 때문에, 접근이 잦습니다.
만약 MySQL과 같은 RDB를 사용한다면 디스크 I/O 작업이 많아지기 때문에 작업이 느려지게 됩니다.
반면에 Redis의 경우 인메모리이기 때문에 RDB를 사용하는 것보다 작업 속도가 빠릅니다.
이러한 이유로 인해서 Redis를 사용했습니다.
```

## 결론: 세션에 저장될 데이터
```
기존 AccountActionBean에 저장되던 상태들은 모두 Session으로 옮기게 됩니다.
1. Account 객체
2. MyList
3. isAuthenticated
4. CATEGORY_LIST // 카테고리 종류 목록
5. LANGUAGE_LIST // 지원 언어 목록
```
이런 변경사항을 고려해 AccountActionBean의 새로운 형태인 Controller에서 제공하게 될 인터페이스를 추리면 아래와 같이 나타낼 수 있습니다.

# Controller에서 제공할 인터페이스
## 기존 ActionBean
```
1. getAccount(): 각 모듈에서 Account를 가져오는 것은 세션에 접근해서 가져오는 형태로 바뀔 예정, 필요없음
2. getUserName(): 세션에 저장된 Account에서 가져올 수 있는 정보, 필요없음
3. setUsername(): Stripes로 인해서 JSP -> ActionBean으로 정보가 전송될 때 필요한 메소드, 필요없음
4. getPassword(): getUserName()과 동일
5. setPassword(): setUserName()과 동일
6. getMyList(): 세션에 저장된 MyList를 가져오는 형태로 바뀔 예정, 필요 없음
7. setMyList(): 세션에 MyList를 담게 될 예정, 필요 없음
8. getCategories(): STATIC 상수 반환
9. newAccountForm(): 회원가입 페이지 반환
10. newAccount(): 실제 회원가입 로직
11. editAccountForm(): 회원정보수정 페이지 반환
12. editAccount(): 실제 회원정보수정 로직
13. signonForm(): 로그인 페이지 반환
14. signon(): 실제 로그인 로직
15. signoff(): 로그아웃 로직
16. isAuthenticated(): 세션에 저장된 Account를 확인하면 알 수 있음, 필요 없음
17. clear(): 세션 비우기
```

## 변경 후,,
```
1. newAccountForm(): 회원가입 페이지 반환
2. newAccount(): 실제 회원가입 로직
3. editAccountForm(): 회원정보수정 페이지 반환
4. editAccount(): 실제 회원정보수정 로직
5. signonForm(): 로그인 페이지 반환
6. signon(): 실제 로그인 로직
7. signoff(): 로그아웃 로직
8. clear(): 세션 비우기
```

# 필요한 외부 API
```
기존에 사용자가 즐겨찾기한 카테고리 ID를 가지고 해당 ID의 상품들을 MyList에 담는 로직이 존재합니다.
따라서 Catalog service에서 특정 ID에 맞는 상품을 가져오는 API를 사용해야 합니다.
해당 API는 다음과 같은 형태를 띄어야 합니다.

<Catalog service>
getProductList(categoryId): Json 형태로 카테고리 ID에 해당하는 Product 객체를 여러 개 받습니다.
searchProducts(keyword): Keyword로 상품을 검색한 결과 페이지로 이동되어야 합니다.
viewCategory(categoryId): 카테고리 ID에 해당하는 상품 목록을 볼 수 있는 페이지로 이동되어야 합니다.
viewMain(): 홈 화면으로 이동되어야 합니다.

<Cart service>
viewCart(): 장바구니 페이지로 이동되어야 합니다.

<Order service>
listOrders(): 주문 내역 페이지로 이동되어야 합니다.
```

# 제공할 API
```

```