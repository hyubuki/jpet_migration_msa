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
			<th colspan=2>Payment Details</th>
		</tr>
		<tr>
			<td>Card Type:</td>
			<td><select name="order.cardType">
				<option value="Visa" ${sessionScope.order.cardType eq "Visa" ? "selected" : ""}>Visa</option>
				<option value="MasterCard" ${sessionScope.order.cardType eq "MasterCard" ? "selected" : ""}>MasterCard</option>
				<option value="American Express" ${sessionScope.order.cardType eq "American Express" ? "selected" : ""}>American Express</option>
			</select></td>
		</tr>
		<tr>
			<td>Card Number:</td>
			<td><input type="text" name="creditCard" value="${sessionScope.order.creditCard}"> * Use a fake
			number!</td>
		</tr>
		<tr>
			<td>Expiry Date (MM/YYYY):</td>
			<td><input type="text" name="expiryDate" value="${sessionScope.order.expiryDate}"></td>
		</tr>
		<tr>
			<th colspan=2>Billing Address</th>
		</tr>

		<tr>
			<td>First name:</td>
			<td><input type="text" name="billToFirstName" value="${sessionScope.order.billToFirstName}"></td>
		</tr>
		<tr>
			<td>Last name:</td>
			<td><input type="text" name="billToLastName" value="${sessionScope.order.billToLastName}"></td>
		</tr>
		<tr>
			<td>Address 1:</td>
			<td><input type="text" name="billAddress1" value="${sessionScope.order.billAddress1}"></td>
		</tr>
		<tr>
			<td>Address 2:</td>
			<td><input type="text" name="billAddress2" value="${sessionScope.order.billAddress2}"></td>
		</tr>
		<tr>
			<td>City:</td>
			<td><input type="text" name="billCity" value="${sessionScope.order.billCity}"></td>
		</tr>
		<tr>
			<td>State:</td>
			<td><input type="text" name="billState" value="${sessionScope.order.billState}"></td>
		</tr>
		<tr>
			<td>Zip:</td>
			<td><input type="text" name="billZip" value="${sessionScope.order.billZip}"></td>
		</tr>
		<tr>
			<td>Country:</td>
			<td><input type="text" name="billCountry" value="${sessionScope.order.billCountry}"></td>
		</tr>

		<tr>
			<td colspan=2><input type="checkbox" name="shippingAddressRequired">
			Ship to different address...</td>
		</tr>

	</table>
	<input type="submit" name="newOrder" value="Continue">

</form></div>

<%@ include file="../common/IncludeBottom.jsp"%>
