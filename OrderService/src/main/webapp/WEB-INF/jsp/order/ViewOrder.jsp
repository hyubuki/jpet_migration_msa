<%--

       Copyright 2010-2022 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          https://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>
<%@ include file="../common/IncludeTop.jsp"%>

<div id="BackLink"><a href="/catalog">Return to Main Menu</a></div>

<div id="Catalog">

<table>
	<tr>
		<th align="center" colspan="2">Order #${order.getOrderId()}
		<fmt:formatDate value="${order.getOrderDate}"
			pattern="yyyy/MM/dd hh:mm:ss" /></th>
	</tr>
	<tr>
		<th colspan="2">Payment Details</th>
	</tr>
	<tr>
		<td>Card Type:</td>
		<td><c:out value="${order.getCardType()}" /></td>
	</tr>
	<tr>
		<td>Card Number:</td>
		<td><c:out value="${order.getCreditCard()}" /> * Fake
		number!</td>
	</tr>
	<tr>
		<td>Expiry Date (MM/YYYY):</td>
		<td><c:out value="${order.getExpiryDate()}" /></td>
	</tr>
	<tr>
		<th colspan="2">Billing Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${order.getBillToFirstName()}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${order.getBillToLastName()}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${order.getBillAddress1()}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${order.getBillAddress2()}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${order.getBillCity()}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${order.getBillState()}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${order.getBillZip()}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${order.getBillCountry()}" /></td>
	</tr>
	<tr>
		<th colspan="2">Shipping Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${order.getShipToFirstName()}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${order.getShipToLastName()}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${order.getShipAddress1()}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${order.getShipAddress2()}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${order.getShipCity()}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${order.getShipState()}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${order.getShipZip()}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${order.getShipCountry()}" /></td>
	</tr>
	<tr>
		<td>Courier:</td>
		<td><c:out value="${order.getCourier()}" /></td>
	</tr>
	<tr>
		<td colspan="2">Status: <c:out value="${order.status}" /></td>
	</tr>
	<tr>
		<td colspan="2">
		<table>
			<tr>
				<th>Item ID</th>
				<th>Description</th>
				<th>Quantity</th>
				<th>Price</th>
				<th>Total Cost</th>
			</tr>
			<c:forEach var="lineItem" items="${order.getLineItems()}">
				<tr>
					<td><a href="/catalog/item?itemId=${LineItem.getItem().getItemId()}">
							${LineItem.getItem().getItemId()}</a></td>
					<td><c:if test="${lineItem.getItem() != null}">
						${lineItem.getItem().getAttribute1()}
						${lineItem.item.attribute2}
						${lineItem.item.attribute3}
						${lineItem.item.attribute4}
						${lineItem.item.attribute5}
						${lineItem.item.product.name}
					</c:if> <c:if test="${lineItem.item == null}">
						<i>{description unavailable}</i>
					</c:if></td>

					<td>${lineItem.quantity}</td>
					<td>$<fmt:formatNumber value="${lineItem.unitPrice}"
						pattern="#,##0.00" /></td>
					<td>$<fmt:formatNumber value="${lineItem.total}"
						pattern="#,##0.00" /></td>
				</tr>
			</c:forEach>
			<tr>
				<th colspan="5">Total: $<fmt:formatNumber
					value="${actionBean.order.totalPrice}" pattern="#,##0.00" /></th>
			</tr>
		</table>
		</td>
	</tr>

</table>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>
