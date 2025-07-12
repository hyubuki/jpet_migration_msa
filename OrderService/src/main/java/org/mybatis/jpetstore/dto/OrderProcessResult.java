package org.mybatis.jpetstore.dto;

/**
 * 주문 처리 결과를 표현하는 단순 DTO 입니다.
 */
public class OrderProcessResult {
    private final String viewName;
    private final String message;

    public OrderProcessResult(String viewName, String message) {
        this.viewName = viewName;
        this.message = message;
    }

    /**
     * 화면 이름을 반환합니다.
     *
     * @return 이동할 화면 이름
     */
    public String getViewName() {
        return viewName;
    }

    /**
     * 사용자에게 전달할 메시지를 반환합니다.
     *
     * @return 메시지(없으면 null)
     */
    public String getMessage() {
        return message;
    }
}
