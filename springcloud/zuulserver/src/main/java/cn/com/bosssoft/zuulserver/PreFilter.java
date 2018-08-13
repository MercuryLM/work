package cn.com.bosssoft.zuulserver;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

import static org.apache.coyote.http11.Constants.a;

public class PreFilter extends ZuulFilter {
    Logger logger = LoggerFactory.getLogger(PreFilter.class);

    // filterType 返回过滤器类型
    /*pre:请求执行之前filter
    route: 处理请求，进行路由
    post: 请求处理完成后执行的filter
    error:出现错误时执行的filter*/
    @Override
    public String filterType() {
        return "pre";
    }
    //filterOrder 返回过滤器的执行顺序
    @Override
    public int filterOrder() {
        return 1;
    }

    //shouldFilter 判断该过滤器是否需要被执行
    @Override
    public boolean shouldFilter() {
        return true;
    }
    //过滤器的具体逻辑
    @Override

    public Object run() throws ZuulException{

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        Object access = request.getParameter("access");
      /*  String token = request.getParameter("token");*/



       /* String pwd = request.getParameter("pwd");*/
        logger.info("send {} request to {}", request.getMethod(),request.getRequestURL().toString());
        if(access == null) {
            context.setSendZuulResponse(false);
            context.setResponseStatusCode(401);
        }

        return null;
    }
   /* public boolean ifRight(String a){
        if(a == "mercury")
            return true;
        return false;
    }*/
}
