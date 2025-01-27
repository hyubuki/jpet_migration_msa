# 기본 경로
- tomcat에서 배포할 때, Application Context를 "/order"로 설정

# Controller에서 제공할 인터페이스
## 기존 ActionBean
```
1. getOrderId()
2. setOrderId()
3. getOrder()
4. setOrder()
5. isShippingAddressRequired()
6. isConfirmed()
7. setConfirmed()
8. getCreditCardTypes()
9. getOrderList()
10. listOrders()
11. newOrderForm()
12. newOrder()
13. viewOrder()
14. clear()
```

## 변경 후,,
```
1. getOrderList()
2. listOrders()
3. newOrderForm()
4. newOrder()
5. viewOrder()
```
