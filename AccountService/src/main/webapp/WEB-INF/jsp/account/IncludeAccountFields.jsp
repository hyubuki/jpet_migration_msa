<%--

       Copyright 2010-2016 the original author or authors.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.

--%>
<h3>Account Information</h3>

<table>
	<c:if test="${sessionScope.account != null}">
		<tr>
			<td>First name:</td>
			<td><input type="text" name="firstName" value="${sessionScope.account.firstName}"></td>
		</tr>
		<tr>
			<td>Last name:</td>
			<td><input type="text" name="lastName" value="${sessionScope.account.lastName}"></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><input type="text" name="email" value="${sessionScope.account.email}"></td>
		</tr>
		<tr>
			<td>Phone:</td>
			<td><input type="text" name="phone" value="${sessionScope.account.phone}"></td>
		</tr>
		<tr>
			<td>Address 1:</td>
			<td><input type="text" name="address1" value="${sessionScope.account.address1}"></td>
		</tr>
		<tr>
			<td>Address 2:</td>
			<td><input type="text" name="address2" value="${sessionScope.account.address2}"></td>
		</tr>
		<tr>
			<td>City:</td>
			<td><input type="text" name="city" value="${sessionScope.account.city}"></td>
		</tr>
		<tr>
			<td>State:</td>
			<td><input type="text" name="state" value="${sessionScope.account.state}"></td>
		</tr>
		<tr>
			<td>Zip:</td>
			<td><input type="text" name="zip" value="${sessionScope.account.zip}"></td>
		</tr>
		<tr>
			<td>Country:</td>
			<td><input type="text" name="country" value="${sessionScope.account.country}"></td>
		</tr>
		</table>

		<h3>Profile Information</h3>

		<table>
		<tr>
			<td>Language Preference:</td>
			<td><select name="languagePreference">
				<option value="english" ${sessionScope.account.languagePreference eq 'english' ? 'selected' : ''}>english</option>
				<option value="japanese" ${sessionScope.account.languagePreference eq 'japanese' ? 'selected' : ''}>japanese</option>
			</select></td>
		</tr>
		<tr>
			<td>Favourite Category:</td>
			<td><select name="favouriteCategoryId">
				<option value="FISH" ${sessionScope.account.favouriteCategoryId eq 'FISH' ? 'selected' : ''}>FISH</option>
				<option value="DOGS" ${sessionScope.account.favouriteCategoryId eq 'DOGS' ? 'selected' : ''}>DOGS</option>
				<option value="REPTILES" ${sessionScope.account.favouriteCategoryId eq 'REPTILES' ? 'selected' : ''}>REPTILES</option>
				<option value="CATS" ${sessionScope.account.favouriteCategoryId eq 'CATS' ? 'selected' : ''}>CATS</option>
				<option value="BIRDS" ${sessionScope.account.favouriteCategoryId eq 'BIRDS' ? 'selected' : ''}>BIRDS</option>
			</select></td>
		</tr>
		<tr>
			<td>Enable MyList</td>
			<td><input type="checkbox" name="listOption" ${sessionScope.account.listOption ? 'checked' : ''}></td>
		</tr>
		<tr>
			<td>Enable MyBanner</td>
			<td><input type="checkbox" name="bannerOption" ${sessionScope.account.bannerOption ? 'checked' : ''}></td>
		</tr>
	</c:if>
	<c:if test="${account == null}">
		<tr>
			<td>First name:</td>
			<td><input type="text" name="firstName"></td>
		</tr>
		<tr>
			<td>Last name:</td>
			<td><input type="text" name="lastName"></td>
		</tr>
		<tr>
			<td>Email:</td>
			<td><input type="text" name="email"></td>
		</tr>
		<tr>
			<td>Phone:</td>
			<td><input type="text" name="phone"></td>
		</tr>
		<tr>
			<td>Address 1:</td>
			<td><input type="text" name="address1"></td>
		</tr>
		<tr>
			<td>Address 2:</td>
			<td><input type="text" name="address2"></td>
		</tr>
		<tr>
			<td>City:</td>
			<td><input type="text" name="city"></td>
		</tr>
		<tr>
			<td>State:</td>
			<td><input type="text" name="state"></td>
		</tr>
		<tr>
			<td>Zip:</td>
			<td><input type="text" name="zip"></td>
		</tr>
		<tr>
			<td>Country:</td>
			<td><input type="text" name="country"></td>
		</tr>
		</table>

		<h3>Profile Information</h3>

		<table>
		<tr>
			<td>Language Preference:</td>
			<td><select name="languagePreference">
				<option value="english">english</option>
				<option value="japanese">japanese</option>
			</select></td>
		</tr>
		<tr>
			<td>Favourite Category:</td>
			<td><select name="favouriteCategoryId">
				<option value="FISH">FISH</option>
				<option value="DOGS">DOGS</option>
				<option value="REPTILES">REPTILES</option>
				<option value="CATS">CATS</option>
				<option value="BIRDS">BIRDS</option>
			</select></td>
		</tr>
		<tr>
			<td>Enable MyList</td>
			<td><input type="checkbox" name="listOption"></td>
		</tr>
		<tr>
			<td>Enable MyBanner</td>
			<td><input type="checkbox" name="bannerOption"></td>
		</tr>
	</c:if>

</table>
