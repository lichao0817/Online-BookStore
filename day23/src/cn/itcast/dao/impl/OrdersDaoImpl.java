package cn.itcast.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.dao.OrdersDao;
import cn.itcast.domain.Book;
import cn.itcast.domain.Orders;
import cn.itcast.domain.OrdersItem;
import cn.itcast.domain.User;
import cn.itcast.exception.DaoException;
import cn.itcast.util.DBCPUtil;

public class OrdersDaoImpl implements OrdersDao {
	private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());
	//���涩���Ļ�����Ϣ
	//�����еĶ�����ҲҪ����
	public void addOrders(Orders orders, User user) {
		try{
			qr.update("insert into orders (id,ordernum,num,price,user_id) values(?,?,?,?,?)", orders.getId(),orders.getOrdernum(),orders.getNum(),orders.getPrice(),user.getId());
			//�����еĶ�����
			List<OrdersItem> items = orders.getItems();
			if(items!=null&&items.size()>0){
				String sql = "insert into ordersitem (id,num,price,orders_id,book_id) values(?,?,?,?,?)";
				Object pps[][] = new Object[items.size()][];
				for(int i=0;i<items.size();i++){
					OrdersItem item = items.get(i);
					pps[i] = new Object[]{item.getId(),item.getNum(),item.getPrice(),orders.getId(),item.getBook().getId()};
				}
				qr.batch(sql, pps);
			}
		}catch(Exception e){
			throw new DaoException(e);
		}
	}
	public List<Orders> findOrderByUsersId(String userId) {
		try {
			return qr.query("select * from orders where user_id=? order by ordernum desc", new BeanListHandler<Orders>(Orders.class), userId);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	//��������ϸ��Ҫ�����;��ϸ�л�Ҫ��ѯ�������Ϣ����ѯ���û�����
	public Orders findOrdersById(String ordersId) {
		try {
			Orders o = qr.query("select * from orders where id=?", new BeanHandler<Orders>(Orders.class), ordersId);
			
			
			if(o!=null){
				//��ѯ�û���Ϣ:�ٵ�һ����������û������Ĭ�϶��ǲ������
				User user = qr.query("select * from user where id=(select user_id from orders where id=?)", new BeanHandler<User>(User.class), ordersId);
				o.setUser(user);
				//������ϸ
				List<OrdersItem> items = qr.query("select * from ordersitem where orders_id=?", new BeanListHandler<OrdersItem>(OrdersItem.class), ordersId);
//				��ѯ�������Ϣ
				if(items!=null&&items.size()>0){
					for(OrdersItem item:items){
						Book b = qr.query("select * from book where id=(select book_id from ordersitem where id=?)", new BeanHandler<Book>(Book.class),item.getId());
						item.setBook(b);
					}
				}
				o.setItems(items);
			}
			return o;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	//��Ҫ��ѯ�û�
	public List<Orders> findOrdersByState(int i) {
		try {
			List<Orders> os = qr.query("select * from orders where state=? order by ordernum desc", new BeanListHandler<Orders>(Orders.class), i);
			if(os!=null&&os.size()>0){
				for(Orders o:os){
					User user = qr.query("select * from user where id=(select user_id from orders where id=?)", new BeanHandler<User>(User.class), o.getId());
					o.setUser(user);
				}
			}
			return os;
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}
	public void update(String ordersId) {
		try {
			qr.update("update orders set state=? where id=?", 1,ordersId);
		} catch (SQLException e) {
			throw new DaoException(e);
		}
	}

}
