<%--

       Copyright 2010-2023 the original author or authors.

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

<div id="Catalog"><form action="/order/newOrder" method="post">

	<table>
		<tr>
			<th colspan=2>Shipping Address</th>
		</tr>

		<tr>
			<td>First name:</td>
			<td><input type="text" name="shipToFirstName" value="${sessionScope.order.shipToFirstName}"></td>
		</tr>
		<tr>
			<td>Last name:</td>
			<td><input type="text" name="shipToLastName" value="${sessionScope.order.shipToLastName}"></td>
		</tr>
		<tr>
			<td>Address 1:</td>
			<td><input type="text" name="shipAddress1" value="${sessionScope.order.shipAddress1}"></td>
		</tr>
		<tr>
			<td>Address 2:</td>
			<td><input type="text" name="shipAddress2" value="${sessionScope.order.shipAddress2}"></td>
		</tr>
		<tr>
			<td>City:</td>
			<td><input type="text" name="shipCity" value="${sessionScope.order.shipCity}"></td>
		</tr>
		<tr>
			<td>State:</td>
			<td><input type="text" name="shipState" value="${sessionScope.order.shipState}"></td>
		</tr>
		<tr>
			<td>Zip:</td>
			<td><input type="text" name="shipZip" value="${sessionScope.order.shipZip}"></td>
		</tr>
		<tr>
			<td>Country:</td>
			<td><input type="text" name="shipCountry" value="${sessionScope.order.shipCountry}"></td>
		</tr>


	</table>
	<input type="hidden" name="csrf" value="${sessionScope.csrf_token}">
	<input type="submit" name="newOrder?changeShipInfo=true" value="Continue">

	</form></div>

<%@ include file="../common/IncludeBottom.jsp"%>
