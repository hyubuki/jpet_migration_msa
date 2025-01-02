# Microservices version of JPetStore6

## Abstract of architecture
![image](https://github.com/user-attachments/assets/3a4a1398-8dd3-46b1-a974-cc2a55b6e30a)


## To do...
### Common Changes
- [ ] Replace stripes with standard controller
- [ ] Remove stripes tags from JSP
- [ ] Apply redis for session clustering

### Account Service
1) 기존에는 AccountActionBean이 Session에서 공유되므로, Account 객체가 AccountActionBean의 필드로 담겨있는 형태였습니다. 이제 ActionBean이 필요하지 않고, SprinBean은 Stateless 해야 하므로 Account 객체를 세션 공간에 두고 모든 서비스에서 접근해서 사용하도록 할 예정입니다.
