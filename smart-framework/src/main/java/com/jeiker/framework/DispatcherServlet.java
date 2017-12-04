package com.jeiker.framework;

import com.jeiker.framework.bean.Data;
import com.jeiker.framework.bean.Handler;
import com.jeiker.framework.bean.Param;
import com.jeiker.framework.bean.View;
import com.jeiker.framework.helper.*;
import com.jeiker.framework.util.JsonUtil;
import com.jeiker.framework.util.ReflectionUtil;
import com.jeiker.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 请求转发器
 * 编写一个Servlet，让它来处理所有请求。
 * 1.从HttpServletRequest对象中获取请求方法与请求路径，通过 ControllerHelper.getHandler 方法来获取 Handler对象。
 * 2.当拿到Handler对象后，可以方便地获取Controller的类，进而通过 BeanHelper.getBean 方法来获取Controller的实例对象。
 *
 * @author huangyong
 * @since 1.0.0
 */
@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        // 初始化相关的 Helper 类
        HelperLoader.init();
        // 获取 ServletContext 对象（用于注册 Servlet ）
        ServletContext servletContext = servletConfig.getServletContext();
        // 注册处理 Servlet
        registerServlet(servletContext);

        UploadHelper.init(servletContext);
    }

    private void registerServlet(ServletContext servletContext) {
        // 注册处理 JSP 的 Servlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        // 注册处理静态资源的默认 Servlet
        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletHelper.init(request, response);
        try {
            // 获取请求方法与请求路径
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();
            // 获取 Action 处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                // 获取 Controller 类及其 Bean 实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                // 创建请求参数
                Param param;
                if (UploadHelper.isMultipart(request)) {
                    param = UploadHelper.createParam(request);
                } else {
                    param = RequestHelper.createParam(request);
                }

                Object result;
                // 调用 Action 方法
                Method actionMethod = handler.getActionMethod();
                if (param.isEmpty()) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                }
                // 处理 Action 方法返回值
                if (result instanceof View) {
                    // 返回 JSP 页面
                    handleViewResult((View) result, request, response);
                } else if (result instanceof Data) {
                    // 返回 Json 数据
                    handleDataResult((Data) result, response);
                }
            }
        } finally {
            ServletHelper.destroy();
        }
    }

    /**
     * 返回 JSP 页面
     * @param view
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void handleViewResult(View view, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String path = view.getPath();
        if (StringUtil.isNotEmpty(path)) {
            if (path.startsWith("/")) {
                response.sendRedirect(request.getContextPath() + path);
            } else {
                Map<String, Object> model = view.getModel();
                for (Map.Entry<String, Object> entry : model.entrySet()) {
                    request.setAttribute(entry.getKey(), entry.getValue());
                }
                request.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(request, response);
            }
        }
    }

    /**
     * 返回 Json 数据
     * @param data
     * @param response
     * @throws IOException
     */
    private void handleDataResult(Data data, HttpServletResponse response) throws IOException {
        Object model = data.getModel();
        if (model != null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            String json = JsonUtil.toJson(model);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
