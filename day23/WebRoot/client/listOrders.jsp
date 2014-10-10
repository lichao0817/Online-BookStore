<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/client/header.jsp"%>
	<h1>亲！以下是您近期的订单哦</h1>
    <table border="1" width="880">
    	<tr>
    		<th>订单号</th>
    		<th>金额</th>
    		<th>订单状态</th>
    		<th>明细</th>
    	</tr>
    	<c:forEach items="${os}" var="o">
    		<tr>
	    		<th>${o.ordernum}</th>
	    		<th>￥${o.price}元</th>
	    		<th>${o.state==0?'未发货':'已发货'}</th>
	    		<th>
	    			<a href="${pageContext.request.contextPath}/servlet/ClientServlet?operation=showOrdersDetail&ordersId=${o.id}">查看明细</a>
	    		</th>
	    	</tr>
    	</c:forEach>
    </table>
  </body>
</html>
