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

<%--TODO session, account service--%>
<div id="Welcome">
<div id="WelcomeContent"><c:if
	test="${account != null }">
	<c:if test="${isAuthenticated}">
        Welcome ${account.getFirstName()}!
      </c:if>
</c:if></div>
</div>

<div id="Main">
<div id="Sidebar">
<div id="SidebarContent">
	<img src="../images/fish_icon.gif" /> <br />
Saltwater, Freshwater <br />
	<a href="${pageContext.request.contextPath}/catalog/category?categoryId=DOGS">
		<img src="/catalog/images/dogs_icon.gif" />
	</a> <br />
Various Breeds <br />
	<a href="${pageContext.request.contextPath}/catalog/category?categoryId=CATS">
		<img src="/catalog/images/cats_icon.gif" />
	</a> <br />
Various Breeds, Exotic Varieties <br />
	<a href="${pageContext.request.contextPath}/catalog/category?categoryId=REPTILES">
		<img src="/catalog/images/reptiles_icon.gif" />
	</a> <br />
Lizards, Turtles, Snakes <br />
	<a href="${pageContext.request.contextPath}/catalog/category?categoryId=BIRDS">
		<img src="/catalog/images/birds_icon.gif" />
	</a> <br />
Exotic Varieties</div>
</div>

<div id="MainImage">
<div id="MainImageContent">
  <map name="estoremap">
	<area alt="Birds" coords="72,2,280,250"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=BIRDS" shape="RECT" />
	<area alt="Fish" coords="2,180,72,250"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=FISH" shape="RECT" />
	<area alt="Dogs" coords="60,250,130,320"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=DOGS" shape="RECT" />
	<area alt="Reptiles" coords="140,270,210,340"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=Reptiles" shape="RECT" />
	<area alt="Cats" coords="225,240,295,310"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=Cats" shape="RECT" />
	<area alt="Birds" coords="280,180,350,250"
		href="${pageContext.request.contextPath}/catalog/category?categoryId=Birds" shape="RECT" />
  </map>
  <img height="355" src="/catalog/images/splash.gif" align="middle"
	usemap="#estoremap" width="350" /></div>
</div>

<div id="Separator">&nbsp;</div>
</div>

<%@ include file="../common/IncludeBottom.jsp"%>