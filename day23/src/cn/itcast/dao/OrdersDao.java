package cn.itcast.dao;

import java.util.List;

import cn.itcast.domain.Orders;
import cn.itcast.domain.User;

public interface OrdersDao {
	//保存订单信息到数据库中
	void addOrders(Orders orders, User user);
	//
	/**
	 * 根据用户的id查询订单
	 */
	List<Orders> findOrderByUsersId(String userId);
	Orders findOrdersById(String ordersId);
	/**
	 * 根据订单状态查询订单
	 * @param i
	 * @return
	 */
	List<Orders> findOrdersByState(int i);
	void update(String ordersId);
}
