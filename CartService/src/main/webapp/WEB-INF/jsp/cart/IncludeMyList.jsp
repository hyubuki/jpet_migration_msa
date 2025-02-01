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
<c:if test="${!empty sessionScope.myList}">
	<p>Pet Favorites <br />
	Shop for more of your favorite pets here.</p>
	<ul>
<%--		Todo accountBean --%>
		<c:forEach var="product" items="${sessionScope.myList}">
			<li><a href="/catalog/product?productId=${proudct.productId}">${product.name}</a> (${product.productId})</li>
		</c:forEach>
	</ul>
</c:if>
