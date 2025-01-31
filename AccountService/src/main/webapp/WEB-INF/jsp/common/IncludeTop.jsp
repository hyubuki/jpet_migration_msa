<%@ page import="org.mybatis.jpetstore.domain.Account" %>
<%@ page import="org.mybatis.jpetstore.domain.Product" %>
<%@ page import="java.util.List" %><%--

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
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<link rel="StyleSheet" href="/account/css/jpetstore.css" type="text/css"
	media="screen" />

<meta name="generator"
	content="HTML Tidy for Linux/x86 (vers 1st November 2002), see www.w3.org" />
<title>JPetStore Demo</title>
<meta content="text/html; charset=windows-1252"
	http-equiv="Content-Type" />
<meta http-equiv="Cache-Control" content="max-age=0" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta http-equiv="Expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
<meta http-equiv="Pragma" content="no-cache" />
</head>

<body>

<div id="Header">

<div id="Logo">
<div id="LogoContent"><a href="/catalog">
	<img src="/account/images/logo-topbar.gif" />
</a></div>
</div>

<div id="Menu">
<div id="MenuContent"><a href="/cart/viewCart">
	<img align="middle" name="img_cart" src="/account/images/cart.gif" />
</a> <img align="middle" src="/account/images/separator.gif" /> <c:if
	test="${sessionScope.account == null}">
	<a href="/account/signonForm">
          Sign In
	    </a>
</c:if> <c:if test="${sessionScope.account != null}">
	<c:if test="${!sessionScope.isAuthenticated}">
		<a href="/account/signonForm">
            Sign In
	      </a>
	</c:if>
</c:if> <c:if test="${sessionScope.account != null}">
	<c:if test="${sessionScope.isAuthenticated}">
		<a href="/account/signoff">
            Sign Out
	      </a>
		<img align="middle" src="/account/images/separator.gif" />
		<a href="/account/editAccountForm">
            My Account
	      </a>
	</c:if>
</c:if> <img align="middle" src="/account/images/separator.gif" /> <a
	href="/account/html/help.html">?</a></div>
</div>

	<div id="Search">
		<div id="SearchContent">
			<!-- 검색 폼 -->
			<form action="/catalog/searchProducts" method="get">
				<input type="text" name="keywords" size="14" placeholder="Enter keyword" />
				<input type="submit" value="Search" />
			</form>
		</div>
	</div>

	<div id="QuickLinks">
		<a href="/catalog/category?categoryId=FISH">
			<img src="/account/images/sm_fish.gif" />
		</a> <img src="/account/images/separator.gif" /> <a href="/catalog/category?categoryId=DOGS">
		<img src="/account/images/sm_dogs.gif" />
	</a> <img src="/account/images/separator.gif" /> <a href="/catalog/category?categoryId=REPTILES">
		<img src="/account/images/sm_reptiles.gif" />
	</a> <img src="/account/images/separator.gif" /> <a href="/catalog/category?categoryId=CATS">
		<img src="/account/images/sm_cats.gif" />
	</a> <img src="/account/images/separator.gif" /> <a href="/catalog/category?categoryId=BIRDS">
		<img src="/account/images/sm_birds.gif" />
	</a></div>

</div>

<div id="Content"> <c:if test="${msg!=null}">${msg}</c:if>