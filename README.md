
# Java Web框架学习

Java Web框架的学习分以下几个小demo:

1. web1-simple
2. web2-complex
3. web3-mvc

可以通过这几个项目逐渐深入的了解Java Web框架。

## web1-simple

HelloServlet.java 代码：


```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        request.setAttribute("currentTime", currentTime);
        request.getRequestDispatcher("/WEB-INF/jsp/hello.jsp").forward(request, response);
    }
}
```

运行：

```
mvn tomcat7:run
```

浏览器中打开：

http://localhost:8080/hello

## web2-complex

运行：

```
mvn tomcat7:run
```

浏览器中打开：

http://localhost:8080/city


## web3-mvc

项目依赖于 smart-framework 所以需要先运行 smart-framework 项目，打包

```
mvn install
```

再运行 web3-mvc 项目：

```
mvn tomcat7:run
```

浏览器中打开：

http://localhost:8080/city

## smart-framework

smart-framework 这是黄勇的开源框架，在 web3-mvc demo中有演示使用。

https://gitee.com/huangyong/smart-framework

这是非常好的学习框架，有利于了解Spring MVC这类框架的原理。

## smart-framework 框架特点

1.它是一款轻量级 Java Web 框架

* 内置 IOC、AOP、ORM、DAO、MVC 等特性
* 基于 Servlet 3.0 规范
* 使用 Java 注解取代 XML 配置

2.它使应用充分做到“前后端分离”

* 客户端可使用 HTML 或 JSP 作为视图模板
* 服务端可发布 REST 服务（使用 REST 插件）
* 客户端通过 AJAX 获取服务端数据并进行界面渲染

3.它可提高应用程序的开发效率

* 面向基于 Web 的中小规模的应用程序
* 新手能在较短时间内入门
* 核心具有良好的定制性且插件易于扩展

## smart-framework 框架的核心类

* DispatcherServlet （请求分发）
* HelperLoader （初始化加载器）
* ClassHelper  （类加载器）
* BeanHelper  （Bean 加载器）
* IocHelper  （Ioc 控制器）
* ControllerHelper （请求控制器）

## 主要流程

1. ClassHelper 加载所有 Bean 类（@Controller、@Service）
2. 加载 Bean 类名，与 Bean 实例的映射 Map
3. IocHelper 控制反转：
	- 3.1 通过反射获取 Bean 中所有成员变量，判断是否带注解 @Inject
	- 3.2 根据 Bean 类，获取 Bean 实例
	- 3.3 通过反射，来修改当前类成员变量的值，实例化成员变量
4. 处理请求与处理器的映射关系（@Action）
	- 4.1 获取所有 Controller 类
	- 4.2 遍历所有 Controller 类和其中定义的方法，判断是否带有 @Action 注解
	- 4.3 从注解中获取 URL 映射规则
	- 4.4 获取请求方法与请求路径，返回 Request 与 Handler

## DispatcherServlet(请求颁发，主流程)

1.在 DispatcherServlet 中主要用来初始化相关的 Helper 类。

```java
/**
 * 请求转发器
 * 编写一个Servlet，让它来处理所有请求。
 * 1.从HttpServletRequest对象中获取请求方法与请求路径，通过 ControllerHelper.getHandler 方法来获取 Handler对象。
 * 2.当拿到Handler对象后，可以方便地获取Controller的类，进而通过 BeanHelper.getBean 方法来获取Controller的实例对象。
 *
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
            // 写数据
            writer.write(json);
            // 刷新，表示完
            writer.flush();
            // 关闭
            writer.close();
        }
    }
}

```

### IocHelper（依赖注入，@Inject）

```java

/**
 * 依赖注入助手类（核心）：让 Bean 中的成员变量通过注解实例化。
 * 获取所有Bean Map，然后遍历这个映射关系，分别取出Bean 类与Bean 实例，
 * 进而通过反射获取类中所有的成员变量。遍历成员变量，判断是否带有Inject注解，
 * 如有，则根据Bean类取出的Bean实例，通过ReflectionUtil的setField方法来修改当前成员变量的值。
 *
 */
public final class IocHelper {

    static {
        // 获取所有的Bean类与 Bean 实例之间的映射关系（简称 Bean Map）
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            // 遍历 Bean Map
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                // 从 BeanMap 中获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类定义的所有成员变量（简称 Bean Field ）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历 Bean Field
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean Field 是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            // 在 Bean Map 中获取 Bean Field 对应的实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                // 通过反射初始化 BeanField 值
                                ReflectionUtil.setField(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
```

### ControllerHelper(请求处理，@Action)

```java
/**
 * 控制器助手类（核心）：让 Controller 中请求与处理器的绑定
 * 通过ClassHelper, 可以获取所有定义了Controller 注解的类，
 * 可以通过反射获取该类中所有带有Action 注解的方法（简称 Action 方法），获取 Action 注解中的请求表达式，
 * 进而获取请求方法与请求路径，封装一个请求对象（Request）与处理对象（Handler）,最后将Request与Handler建立一个映射关系，
 * 放入一个Action Map中，并提供一个可根据请求方法与请求路径获取处理对象的方法。
 *
 */
public final class ControllerHelper {

    /**
     * 用于存放请求与处理器的映射关系（简称 Action Map）
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        // 获取所有的Controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            // 遍历这些Controller类
            for (Class<?> controllerClass : controllerClassSet) {
                // 获取 Controller 类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    // 遍历这些 Controller 类中的方法
                    for (Method method : methods) {
                        // 判断当前方法是否带有 Action 注解
                        if (method.isAnnotationPresent(Action.class)) {
                            // 从Action 注解中获取 URL 映射规则
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            // 验证 URL 映射规则
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    // 获取请求方法与请求路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);
                                    // 初始化 Action Map
                                    ACTION_MAP.put(request, handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取 Handler
     */
    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);
    }
}
```
