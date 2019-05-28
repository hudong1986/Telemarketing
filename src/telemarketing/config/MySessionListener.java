package telemarketing.config;

import java.util.HashSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class MySessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        
        // ��application��Χ��һ��HashSet���������е�session
        @SuppressWarnings("unchecked")
		HashSet<HttpSession> sessions = (HashSet<HttpSession>) application.getAttribute("sessions");
        if (sessions == null) {
               sessions = new HashSet<HttpSession>();
               application.setAttribute("sessions", sessions);
        }
        
        // �´�����session����ӵ�HashSet����
        sessions.add(session);
        // �����ڱ𴦴�application��Χ��ȡ��sessions����
        // Ȼ��ʹ��sessions.size()��ȡ��ǰ���session������Ϊ������������
        
 }

 public void sessionDestroyed(HttpSessionEvent event) {
        HttpSession session = event.getSession();
        ServletContext application = session.getServletContext();
        HashSet<?> sessions = (HashSet<?>) application.getAttribute("sessions");
        
        // ���ٵ�session����HashSet�����Ƴ�
        sessions.remove(session);
 }
}
