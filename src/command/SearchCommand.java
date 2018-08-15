package command;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

import domain.MemberBean;
import enums.Domain;
import proxy.*;
import service.MemberServiceImpl;

public class SearchCommand extends Command{
	public SearchCommand(HttpServletRequest request) {
		setRequest(request);
		setDomain(request.getServletPath().substring(1,  
				request.getServletPath().indexOf(".")));
		setAction(request.getParameter("action"));
		setPage(request.getParameter("page"));
		execute(); 
	}
	@Override
	public void execute() {
		if(request.getSession().getAttribute("option") == null) {
			request.getSession().setAttribute("option", "none");
			request.getSession().setAttribute("count", MemberServiceImpl.getInstance().count());
		}
		
		if(request.getParameter("option") != null) {
			request.getSession().setAttribute("option", request.getParameter("option"));
			request.getSession().setAttribute("word", request.getParameter("word"));
			request.getSession().setAttribute("count", 
					(request.getSession().getAttribute("option").equals("none"))?
					MemberServiceImpl.getInstance().count():
					MemberServiceImpl.getInstance().count(
							request.getSession().getAttribute("option")+"/"
							+request.getSession().getAttribute("word")
					)
			);
		}	
		
		String pageNum = request.getParameter("pageNum");
		PageProxy pxy = new PageProxy();
		pxy.carryOut(
				((pageNum==null)?
						"1/"
						:pageNum+"/")
				+request.getSession().getAttribute("count"));
		Pagination page = pxy.getPagination();
		
		String keys,values;
		if(!(((String)request.getSession().getAttribute("option")).equals("none"))) {
			keys = "domain/beginRow/endRow/column/value";
			values = 
				Domain.MEMBER.toString()+"/"
				+String.valueOf(page.getBeginRow())+"/"
				+String.valueOf(page.getEndRow())+"/"
				+(String) request.getSession().getAttribute("option")+"/"
				+(String) request.getSession().getAttribute("word");
		}else {
			keys = "domain/beginRow/endRow";
			values = 
				Domain.MEMBER.toString()+"/"
				+String.valueOf(page.getBeginRow())+"/"
				+String.valueOf(page.getEndRow());
		}
		
		String[] arr1 = keys.split("/"), 
				 arr2 = values.split("/");
		Map<String,Object>paramMap = new HashMap<>();
		for(int i=0;i<arr1.length;i++) {
			paramMap.put(arr1[i], arr2[i]);
		}
		
		request.setAttribute("page", page);
		List<MemberBean> members = MemberServiceImpl.getInstance().search(paramMap);
		request.setAttribute("list", members);
		super.execute();
	}
}
