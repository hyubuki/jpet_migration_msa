--
--    Copyright 2010-2013 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

create table orders (
    orderid int not null,
    userid varchar(80) not null,
    orderdate date not null,
    shipaddr1 varchar(80) not null,
    shipaddr2 varchar(80) null,
    shipcity varchar(80) not null,
    shipstate varchar(80) not null,
    shipzip varchar(20) not null,
    shipcountry varchar(20) not null,
    billaddr1 varchar(80) not null,
    billaddr2 varchar(80)  null,
    billcity varchar(80) not null,
    billstate varchar(80) not null,
    billzip varchar(20) not null,
    billcountry varchar(20) not null,
    courier varchar(80) not null,
    totalprice decimal(10,2) not null,
    billtofirstname varchar(80) not null,
    billtolastname varchar(80) not null,
    shiptofirstname varchar(80) not null,
    shiptolastname varchar(80) not null,
    creditcard varchar(80) not null,
    exprdate varchar(7) not null,
    cardtype varchar(80) not null,
    locale varchar(80) not null,
    constraint pk_orders primary key (orderid)
);

create table orderstatus (
    orderid int not null,
    linenum int not null,
    timestamp date not null,
    status varchar(2) not null,
    constraint pk_orderstatus primary key (orderid, linenum)
);

create table lineitem (
    orderid int not null,
    linenum int not null,
    itemid varchar(10) not null,
    quantity int not null,
    unitprice decimal(10,2) not null,
    constraint pk_lineitem primary key (orderid, linenum)
);

CREATE TABLE sequence
(
    name               varchar(30)  not null,
    nextid             int          not null,
    constraint pk_sequence primary key (name)
);

create table OrderRetryStatus (
    orderid             int not null,
    status              varchar(30 ),
    constraint pk_status primary key (orderid)
);