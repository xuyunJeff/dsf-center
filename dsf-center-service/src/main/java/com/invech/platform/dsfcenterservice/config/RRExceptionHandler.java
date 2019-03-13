package com.invech.platform.dsfcenterservice.config;

import com.invech.platform.dsfcenterdata.response.DsfInvokeException;
import com.invech.platform.dsfcenterdata.response.R;
import com.invech.platform.dsfcenterdata.response.R200Exception;
import com.invech.platform.dsfcenterdata.response.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());


	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(DsfInvokeException.class)
	public R DsfInvokeException(DsfInvokeException e){
		logger.info(e.getMessage());
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		r.put("dsfErrorCode", e.getDsfErrorCode());
		r.put("dsfErrorMsg", e.getMessage());
		return r;
	}


	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(R200Exception.class)
	public R handleR200Exception(R200Exception e){
		logger.info(e.getMsg());
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		return r;
	}

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public R handleRRException(RRException e){
		logger.error(e.getMessage(), e);
		R r = new R();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());
		return r;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public R handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return R.error("数据库中已存在该记录");
	}

//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	public R handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
//		logger.error("缺少请求参数", e);
//		return R.error(e.getMessage());
//	}
//
//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(HttpMessageNotReadableException.class)
//	public R handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//		logger.error("参数解析失败", e);
//		return R.error(e.getMessage());
//	}
//
//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public R handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
//		logger.error("参数验证失败", e);
//		BindingResult result = e.getBindingResult();
//		FieldError error = result.getFieldError();
//		String field = error.getField();
//		String code = error.getDefaultMessage();
//		String message = String.format("%s:%s", field, code);
//		return R.error(message);
//	}
//
//	/**
//	 * 400 - Bad Request
//	 */
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(BindException.class)
//	public R handleBindException(BindException e) {
//		logger.error("参数绑定失败", e);
//		BindingResult result = e.getBindingResult();
//		FieldError error = result.getFieldError();
//		String field = error.getField();
//		String code = error.getDefaultMessage();
//		String message = String.format("%s:%s", field, code);
//		return R.error(message);
//	}
//
//	@ResponseStatus(HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(MissingPathVariableException.class)
//	public R handleBindException(MissingPathVariableException e) {
//		logger.error("参数绑定失败", e);
//		String message = e.getVariableName()+"非法请求";
//		return R.error(message);
//	}


	@ExceptionHandler(Exception.class)
	public R handleException(Exception e){
		logger.error(e.getMessage(), e);
		return R.error(e.getMessage());
	}
}
