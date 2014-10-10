package cn.itcast.service;

import java.util.List;

import cn.itcast.domain.Book;
import cn.itcast.domain.Cart;
import cn.itcast.domain.Category;
import cn.itcast.domain.Orders;
import cn.itcast.domain.User;
import cn.itcast.util.Page;

public interface BusinessService {
	void addCategory(Category c);
	List<Category> findAllCategory();
	void addBook(Book book);
	/**
	 * 后台查询图书使用
	 * @param pagenum
	 * @return
	 */
	Page findPageRecords(String pagenum);
	Category findCatetoryById(String categoryId);
	//按照分类查询分页数据
	Page findPageRecords(String pagenum, String categoryId);
	Book findBookById(String bookId);
	void regist(User user);
	User login(String username, String password);
	//生成订单
	void addOrders(Orders orders, User user);
	List<Orders> findOrdersByUsersId(String id);
	//订单的明细还要查出来;明细中还要查询出书的信息
	Orders findOrdersById(String ordersId);
	List<Orders> findOrdersByState(int i);
	void sureOrders(String ordersId);
	
}
