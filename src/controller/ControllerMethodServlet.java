package controller;

import action.AllAction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

@WebServlet(name = "ControllerMethodServlet", urlPatterns = {"*.do"}, initParams = {@WebInitParam(name = "properties", value = "method.properties")})
public class ControllerMethodServlet extends HttpServlet {
    private Properties pr = new Properties();
    private AllAction action = new AllAction();
    private Class[] paramType = new Class[]{HttpServletRequest.class, HttpServletResponse.class};

    @Override
    public void init(ServletConfig config) {
        String props = config.getInitParameter("properties");
        FileInputStream f;
        try {
            f = new FileInputStream(config.getServletContext().getRealPath("/") + "WEB-INF/" + props);
            pr.load(f);
            f.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        Object[] paramObjs = new Object[]{request, response};
        String forward;
        String command;

        try {
            command = request.getRequestURI().substring(request.getContextPath().length());
            String methodName = pr.getProperty(command);
            Method method = AllAction.class.getMethod(methodName, paramType);
            //noinspection JavaReflectionInvocation
            forward = (String) method.invoke(action, paramObjs);
        } catch (Exception e) {
            throw new ServletException(e);
        }

        if (forward == null) {
            forward = command.replace(".do", ".jsp");
        }

        RequestDispatcher disp = request.getRequestDispatcher(forward);
        disp.forward(request, response);
    }
}
