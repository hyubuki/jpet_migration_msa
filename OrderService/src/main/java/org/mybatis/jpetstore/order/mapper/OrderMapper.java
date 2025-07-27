/*
 *    Copyright 2010-2022 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore.order.mapper;

import org.apache.ibatis.annotations.*;
import org.mybatis.jpetstore.common.domain.Order;
import org.mybatis.jpetstore.order.domain.OrderRetryStatus;

import java.util.List;
import java.util.Optional;

/**
 * The Interface OrderMapper.
 *
 * @author Eduardo Macarron
 */
@Mapper
public interface OrderMapper {

  List<Order> getOrdersByUsername(String username);

  Order getOrder(int orderId);

  void insertOrder(Order order);

  void insertOrderStatus(Order order);

  void insertStatus(OrderRetryStatus status);

  void updateStatus(OrderRetryStatus status);

  void deleteStatus(int orderid);
  Optional<OrderRetryStatus> getStatus(int orderid);

}
