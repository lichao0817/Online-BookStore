package cn.itcast.dao;

import java.util.List;

import cn.itcast.domain.Orders;
import cn.itcast.domain.User;

public interface OrdersDao {
	//���涩����Ϣ�����ݿ���
	void addOrders(Orders orders, User user);
	//
	/**
	 * �����û���id��ѯ����
	 */
	List<Orders> findOrderByUsersId(String userId);
	Orders findOrdersById(String ordersId);
	/**
	 * ���ݶ���״̬��ѯ����
	 * @param i
	 * @return
	 */
	List<Orders> findOrdersByState(int i);
	void update(String ordersId);
}
