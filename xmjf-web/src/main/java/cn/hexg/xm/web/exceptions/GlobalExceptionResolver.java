package cn.hexg.xm.web.exceptions;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import cn.hexg.xm.constant.P2pConstant;
import cn.hexg.xm.exceptions.LoginException;
import cn.hexg.xm.exceptions.ParamsExcetion;
import cn.hexg.xm.model.ResultInfo;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;


@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView=getDefautlModelAndView();	
		/**
		 * 1.view
		 * 2.json
		 */
		if(handler instanceof HandlerMethod){
			/**
			 * 处理未登录情况
			 */
			if (ex instanceof LoginException){
				ModelAndView mv=new ModelAndView();
				mv.addObject("ctx",request.getContextPath());
				mv.setViewName("login");
				return mv;
			}
			HandlerMethod handlerMethod=(HandlerMethod) handler;
			Method method= handlerMethod.getMethod();
			ResponseBody responseBody= method.getAnnotation(ResponseBody.class);
			if(null !=responseBody){
				ResultInfo messageModel=new ResultInfo();
				messageModel.setCode(P2pConstant.OP_FAILED_CODE);
				messageModel.setMsg(P2pConstant.OP_FAILED_MSG);
				if(ex instanceof ParamsExcetion){
					ParamsExcetion pe=(ParamsExcetion) ex;
					messageModel.setCode(pe.getErrorCode());
					messageModel.setMsg(pe.getErrorMsg());
				}
				response.setCharacterEncoding("utf-8");
				response.setContentType("application/json;charset=utf-8");
				PrintWriter pw= null;
				try {
					pw=response.getWriter();
					
				} catch (IOException e) {
					e.printStackTrace();
					messageModel.setCode(P2pConstant.OP_FAILED_CODE);
					messageModel.setMsg(P2pConstant.OP_FAILED_MSG);
				}finally{
					if(null!=pw){
						pw.write(JSON.toJSONString(messageModel));
						pw.flush();
						pw.close();
					}
				}
				return null;
			}else{
				if(ex instanceof ParamsExcetion){
					ParamsExcetion pe=(ParamsExcetion) ex;
					modelAndView.addObject("uri", request.getRequestURI());
					modelAndView.addObject("errorMsg", pe.getErrorMsg());
					modelAndView.addObject("code", pe.getErrorCode());
				}
				return modelAndView;
			}
			//return modelAndView;
		}else{
			return modelAndView;
		}
	}

	private ModelAndView getDefautlModelAndView() {
		ModelAndView mv=new ModelAndView();
		mv.setViewName("error");
		mv.addObject("msg", P2pConstant.OP_FAILED_CODE);
		return mv;
	}

}
