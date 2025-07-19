package org.mybatis.jpetstore.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@MockBean
public @interface MockitoBean {
    Class<?>[] value() default {};
}
