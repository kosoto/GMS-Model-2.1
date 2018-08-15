package command;

import javax.servlet.http.HttpServletRequest;

import domain.MemberBean;
import service.MemberServiceImpl;

public class RemoveCommand extends Command {
	public RemoveCommand(HttpServletRequest request) {
		setRequest(request);
		setAction(request.getParameter("action"));
		setDomain(request.getServletPath().substring(1, 
				request.getServletPath().indexOf(".")));
		execute();
	}
	@Override
	public void execute() {
		System.out.println(request.getParameter("password"));
		System.out.println(((MemberBean)(request.getSession().getAttribute("user"))).getPassword());
		if(request.getParameter("password").equals(((MemberBean)(request.getSession().getAttribute("user"))).getPassword())) {
			MemberServiceImpl.getInstance()
			.remove((MemberBean) request.getSession().getAttribute("user"));
			request.getSession().invalidate();
		}else {
			request.setAttribute("match", "FALSE");
		}
	}
}
