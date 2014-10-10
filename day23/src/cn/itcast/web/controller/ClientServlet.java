package cn.itcast.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.domain.Book;
import cn.itcast.domain.Cart;
import cn.itcast.domain.CartItem;
import cn.itcast.domain.Category;
import cn.itcast.domain.Orders;
import cn.itcast.domain.OrdersItem;
import cn.itcast.domain.User;
import cn.itcast.service.BusinessService;
import cn.itcast.service.impl.BusinessServiceImpl;
import cn.itcast.util.Page;
import cn.itcast.util.WebUtil;

public class ClientServlet extends HttpServlet {
	private BusinessService s = new BusinessServiceImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if("showIndexCategory".equals(operation)){
			showIndexCategory(request,response);
		}
		if("showCategoryBooks".equals(operation)){
			showCategoryBooks(request,response);
		}
		if("buyBook".equals(operation)){
			buyBook(request,response);
		}
		if("regist".equals(operation)){
			regist(request,response);
		}
		if("login".equals(operation)){
			login(request,response);
		}
		if("genOrders".equals(operation)){
			genOrders(request,response);
		}
		if("showUsersOrders".equals(operation)){
			showUsersOrders(request,response);
		}
		if("showOrdersDetail".equals(operation)){
			showOrdersDetail(request,response);
		}
	}
	private void showOrdersDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ordersId = request.getParameter("ordersId");//ȡ�ö�����id
		Orders o = s.findOrdersById(ordersId);//��������ϸ��Ҫ�����;��ϸ�л�Ҫ��ѯ�������Ϣ
		request.setAttribute("o", o);
		request.getRequestDispatcher("/client/showOrdersDetail.jsp").forward(request, response);
	}
	//�����û���id��ѯ�����Լ��Ķ���
	private void showUsersOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//�ж��û��Ƿ��¼��1��û�е�¼��ת���¼ҳ��
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		if(user==null){
			request.setAttribute("message", "���ȵ�¼��2����Զ�ת���¼ҳ�档<meta http-equiv='Refresh' content='2;URL="+request.getContextPath()+"/client/login.jsp'>");
			request.getRequestDispatcher("/client/message.jsp").forward(request, response);
			return;
		}
		List<Orders> os = s.findOrdersByUsersId(user.getId());//��ѯĳ���û������ж���
		request.setAttribute("os", os);
		request.getRequestDispatcher("/client/listOrders.jsp").forward(request, response);
		
		
	}
	//���ɶ������Ѷ����Ͷ��������Ϣ�浽���ݿ���
	private void genOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//�ж��û��Ƿ��¼��1��û�е�¼��ת���¼ҳ��
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		if(user==null){
			request.setAttribute("message", "���ȵ�¼��2����Զ�ת���¼ҳ�档<meta http-equiv='Refresh' content='2;URL="+request.getContextPath()+"/client/login.jsp'>");
			request.getRequestDispatcher("/client/message.jsp").forward(request, response);
			return;
		}
		//ȡ�����ﳵ��Cart  Map<String,CartItem>  ���ģ��
		Cart cart = (Cart) session.getAttribute("cart");
		Orders orders = new Orders();
		orders.setNum(cart.getNum());
		orders.setPrice(cart.getPrice());
		//�㹺����
		List<OrdersItem> ordersItems = new ArrayList<OrdersItem>();
		for(Map.Entry<String,CartItem> item:cart.getItems().entrySet()){
			CartItem i = item.getValue();
			OrdersItem orderItem = new OrdersItem();
			orderItem.setNum(i.getNum());
			orderItem.setPrice(i.getPrice());
			orderItem.setBook(i.getBook());
			ordersItems.add(orderItem);
		}
		orders.setItems(ordersItems);
		s.addOrders(orders,user);
		request.setAttribute("message", "���ɶ����ɹ���");
		request.getRequestDispatcher("/client/message.jsp").forward(request, response);
	}
	private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		User user = s.login(username,password);
		if(user==null){
			request.setAttribute("message", "�û������������");
			request.getRequestDispatcher("/client/message.jsp").forward(request, response);
		}else{
			request.getSession().setAttribute("user", user);
			response.sendRedirect(request.getContextPath());
		}
		
	}
	//�û�ע��
	private void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		User user = WebUtil.fillBean(request, User.class);
		s.regist(user);
		request.setAttribute("message", "ע��ɹ���");
		request.getRequestDispatcher("/client/message.jsp").forward(request, response);
	}
	//�ѹ������Ʒ���빺�ﳵ
	private void buyBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		//��ȡ���id
		String bookId = request.getParameter("bookId");
		//��ȡҪ�������
		Book book = s.findBookById(bookId);
		//��HttpSession��ȡ�����ﳵ
		HttpSession session = request.getSession();
		Cart cart = (Cart)session.getAttribute("cart");
		//û�У��������ﳵ�����ŵ�HttpSession�У����ﳵCart��
		if(cart==null){
			cart = new Cart();
			session.setAttribute("cart", cart);
		}
		//����ŵ����ﳵ��
		cart.addBook(book);
		//��ʾ����ɹ�
		request.setAttribute("message", "����ɹ���");
		request.getRequestDispatcher("/client/message.jsp").forward(request, response);
	}
	//��ҳ�����շ�����з�ҳ�鼮��ѯ
	private void showCategoryBooks(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pagenum = request.getParameter("pagenum");
		String categoryId = request.getParameter("categoryId");
		Page page = s.findPageRecords(pagenum,categoryId);
		page.setUrl("/servlet/ClientServlet?operation=showCategoryBooks&categoryId="+categoryId);
		request.setAttribute("page", page);
		request.getRequestDispatcher("/client/welcome.jsp").forward(request, response);
	}
	//��ѯ���з��࣬��װ�󣬱���ǰ����ҳ����ʾ
	//��ѯ���е��鼮����Ҫ��ҳ
	private void showIndexCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> cs = s.findAllCategory();
		request.getSession().setAttribute("cs", cs);
		//��ѯ���е��鼮����Ҫ��ҳ
		String pagenum = request.getParameter("pagenum");
		Page page = s.findPageRecords(pagenum);
		page.setUrl("/servlet/ClientServlet?operation=showIndexCategory");
		request.setAttribute("page", page);
		request.getRequestDispatcher("/client/welcome.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
