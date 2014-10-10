<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/client/header.jsp"%>
	订单号：${o.ordernum}&nbsp;&nbsp;金额：￥${o.price}元，明细如下：<br/>
	<table border="1" width="880">
		<tr>
			<th>书名</th>
			<th>作者</th>
			<th>单价</th>
			<th>数量</th>
			<th>小计</th>
		</tr>
		<c:forEach items="${o.items}" var="i">
			<tr>
				<td>${i.book.name}</td>
				<td>${i.book.author }</td>
				<td>￥${i.book.price}元</td>
				<td>${i.num }</td>
				<td>￥${i.price}元</td>
			</tr>
		</c:forEach>
	</table>
    收货信息如下：<br/>
    地址：${sessionScope.user.address }<br/>
    电话：${sessionScope.user.cellphone }<br/>
  </body>
</html>
