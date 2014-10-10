package cn.itcast.web.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import cn.itcast.domain.Book;
import cn.itcast.domain.Category;
import cn.itcast.domain.Orders;
import cn.itcast.service.BusinessService;
import cn.itcast.service.impl.BusinessServiceImpl;
import cn.itcast.util.Page;
import cn.itcast.util.WebUtil;

public class ManagerServlet extends HttpServlet {
	private BusinessService s = new BusinessServiceImpl();
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String operation = request.getParameter("operation");
		if("addCategory".equals(operation)){
			addCategory(request,response);
		}
		if("showAllCatetory".equals(operation)){
			showAllCatetory(request,response);
		}
		if("showAllCatetoryUI".equals(operation)){
			showAllCatetoryUI(request,response);
		}
		if("addBook".equals(operation)){
			addBook(request,response);
		}
		if("showAllBook".equals(operation)){
			showAllBook(request,response);
		}
		if("showAllOrders0".equals(operation)){
			showAllOrders0(request,response);
		}
		if("showOrdersDetail".equals(operation)){
			showOrdersDetail(request,response);
		}
		if("sureOrders".equals(operation)){
			sureOrders(request,response);
		}
		if("showAllOrders1".equals(operation)){
			showAllOrders1(request,response);
		}
	}
	private void showAllOrders1(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Orders> os = s.findOrdersByState(1);
		request.setAttribute("os", os);
		request.getRequestDispatcher("/manager/showOrders.jsp").forward(request, response);
		
	}
	//��������
	private void sureOrders(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ordersId = request.getParameter("ordersId");//ȡ�ö�����id
		s.sureOrders(ordersId);
		request.setAttribute("message", "�����ɹ�");
		request.getRequestDispatcher("/message.jsp").forward(request, response);
	}
	//�鿴������ϸ����ǰ̨����ͬ
	private void showOrdersDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String ordersId = request.getParameter("ordersId");//ȡ�ö�����id
		Orders o = s.findOrdersById(ordersId);//��������ϸ��Ҫ�����;��ϸ�л�Ҫ��ѯ�������Ϣ;�ĸ��û���ҲҪ��ѯ����
		request.setAttribute("o", o);
		request.getRequestDispatcher("/manager/showOrdersDetail.jsp").forward(request, response);
	}
	//��ʾ����δ�����Ķ���,��Ҫ��ѯ�����ĸ��û���
	private void showAllOrders0(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Orders> os = s.findOrdersByState(0);
		request.setAttribute("os", os);
		request.getRequestDispatcher("/manager/showOrders.jsp").forward(request, response);
	}
	//��̨��ѯ����ͼ���ҳ
	private void showAllBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String pagenum = request.getParameter("pagenum");
		Page page = s.findPageRecords(pagenum);
		page.setUrl("/servlet/ManagerServlet?operation=showAllBook");
		request.setAttribute("page", page);
		request.getRequestDispatcher("/manager/listBooks.jsp").forward(request, response);
	}
	//����鼮�����ݿ���
	private void addBook(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String resultPath = "";
		String storePath = getServletContext().getRealPath("/files");
		try {
			Book book = new Book();
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//��������
			List<FileItem> items = upload.parseRequest(request);
			for(FileItem item:items){
				if(item.isFormField()){
					//��װ���ݵ�JavaBean��
					String fieldName = item.getFieldName();//�ֶ�������javabean��������������ͼƬ
					String fieldValue = item.getString(request.getCharacterEncoding());
					BeanUtils.setProperty(book, fieldName, fieldValue);//����ͼƬ·�����������ݶ�����
				}else{
					//�����ļ��ϴ�
					InputStream in = item.getInputStream();
					String fileName = item.getName();//   c:\dsf\a.jpg
					fileName = UUID.randomUUID()+fileName.substring(fileName.lastIndexOf("\\")+1);//a.jpg 
					//���ô�ȡ��ͼƬ�ļ���
					book.setImage(fileName);
					OutputStream out = new FileOutputStream(storePath+"\\"+fileName);
					byte b[] = new byte[1024];
					int len = -1;
					while((len=in.read(b))!=-1){
						out.write(b, 0, len);
					}
					out.close();
					in.close();
					item.delete();//ɾ����ʱ�ļ�
				}
			}
			s.addBook(book);
			//��ѯ����
			List<Category> cs = s.findAllCategory();
			request.setAttribute("cs", cs);
			resultPath = "/manager/addBook.jsp";
			request.setAttribute("message", "<script type='text/javascript'>alert('��ӳɹ�')</script>");
		} catch (Exception e) {
			e.printStackTrace();
			resultPath = "/message.jsp";
			request.setAttribute("message", "������æ");
		}
		request.getRequestDispatcher(resultPath).forward(request, response);
	}
	//��ѯ���з��࣬��������鼮ʱ����ʾ
	private void showAllCatetoryUI(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> cs = s.findAllCategory();
		request.setAttribute("cs", cs);
		request.getRequestDispatcher("/manager/addBook.jsp").forward(request, response);
	}
	private void showAllCatetory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		List<Category> cs = s.findAllCategory();
		request.setAttribute("cs", cs);
		request.getRequestDispatcher("/manager/listCategory.jsp").forward(request, response);
	}
	//��ӷ��ൽ���ݿ���
	private void addCategory(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		Category c = WebUtil.fillBean(request, Category.class);
		s.addCategory(c);
		request.setAttribute("message", "<script type='text/javascript'>alert('��ӳɹ�');</script>");
		request.getRequestDispatcher("/manager/addCategory.jsp").forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

}
