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

<div id="BackLink"><a href="/catalog">
	Return to Main Menu
</a></div>

<div id="Catalog">Please confirm the information below and then
press continue...

<table>
	<tr>
		<th align="center" colspan="2"><font size="4"><b>Order</b></font>
		<br />
		<font size="3"><b> <fmt:formatDate
			value="${sessionScope.order.orderDate}" pattern="yyyy/MM/dd hh:mm:ss" /></b></font>
		</th>
	</tr>

	<tr>
		<th colspan="2">Billing Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${sessionScope.order.billToFirstName}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${sessionScope.order.billToLastName}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${sessionScope.order.billAddress1}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${sessionScope.order.billAddress2}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${sessionScope.order.billAddress2}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${sessionScope.order.billState}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${sessionScope.order.billZip}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${sessionScope.order.billCountry}" /></td>
	</tr>
	<tr>
		<th colspan="2">Shipping Address</th>
	</tr>
	<tr>
		<td>First name:</td>
		<td><c:out value="${sessionScope.order.shipToFirstName}" /></td>
	</tr>
	<tr>
		<td>Last name:</td>
		<td><c:out value="${sessionScope.order.shipToLastName}" /></td>
	</tr>
	<tr>
		<td>Address 1:</td>
		<td><c:out value="${sessionScope.order.shipAddress1}" /></td>
	</tr>
	<tr>
		<td>Address 2:</td>
		<td><c:out value="${sessionScope.order.shipAddress2}" /></td>
	</tr>
	<tr>
		<td>City:</td>
		<td><c:out value="${sessionScope.order.shipCity}" /></td>
	</tr>
	<tr>
		<td>State:</td>
		<td><c:out value="${sessionScope.order.shipState}" /></td>
	</tr>
	<tr>
		<td>Zip:</td>
		<td><c:out value="${sessionScope.order.shipZip}" /></td>
	</tr>
	<tr>
		<td>Country:</td>
		<td><c:out value="${sessionScope.order.shipCountry}" /></td>
	</tr>

</table>
<form action="/order/newOrder?confirmed=true" method="post">
	<input type="hidden" name="csrf" value="${sessionScope.csrf_token}">
	<input type="submit" class="Button" value="Confirm">
</form>

</div>

<%@ include file="../common/IncludeBottom.jsp"%>





