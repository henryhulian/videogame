package com.gaming.live.common.server;

import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.CookieImpl;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.FileResourceManager;
import io.undertow.server.handlers.resource.ResourceHandler;
import io.undertow.util.HttpString;
import java.io.File;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gaming.live.common.boundary.ApplicationRequest;
import com.gaming.live.common.boundary.ApplicationResponse;
import com.gaming.live.common.controller.core.Dispatcher;
import com.gaming.live.common.utils.TokenUtil;

public class ApplicationServer<RQ extends ApplicationRequest,RP extends ApplicationResponse>{
	
	private static final Logger logger = LoggerFactory.getLogger(ApplicationServer.class);
	
	private Class<RQ> requestClass;
	private Dispatcher<RQ,RP> dispatcher;
	private Undertow undertow;
	private SocketIOServer socketIOServer;
	private static ObjectMapper mapper ;
	static{
		mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public ApplicationServer( Properties properties ,Dispatcher<RQ,RP> dispatcher , Class<RQ> requestClass){
		this.requestClass=requestClass;
		this.dispatcher=dispatcher;
        setUpNettySocketIo(properties);
        setUpUndertow(properties);
	}
	
	//配置NettySocketIo服务
	private void setUpNettySocketIo(Properties properties) {
	    int ioThread = Integer.parseInt(
	    		properties.getProperty("io_thread_number",
				String.valueOf(Runtime.getRuntime().availableProcessors()+1)));
	    int workerThread = Integer.parseInt(
	    		properties.getProperty("work_thread_number", 
				String.valueOf(Runtime.getRuntime().availableProcessors()*200)));
	    String webSocketHost=properties.getProperty("websocket.host", "0.0.0.0");
	    int webSocketPort =  Integer.parseInt(properties.getProperty("websocket.port", "7079"));
	    String webSocketAptConextPath = properties.getProperty("server.namespace","/api");
        Configuration config = new Configuration();
        config.setHostname(webSocketHost);
        config.setPort(webSocketPort);
        config.setBossThreads(ioThread);
        config.setWorkerThreads(workerThread);
        socketIOServer = new SocketIOServer(config);
        SocketIONamespace socketIONamespace = socketIOServer.addNamespace(webSocketAptConextPath);
        socketIONamespace.addEventListener("event", requestClass, new DataListener<RQ>() {

			@Override
			public void onData(SocketIOClient client, RQ request,
					AckRequest ackSender) throws Exception {
				//获取TOKEN
				request.setToken((String)client.get(TokenUtil.TOKEN_COOKIE_NMAE));
				//派发事件
				RP response = dispatcher.dispatcher(request);
				//检查设置TOKEN
				if(response.getToken()!=null){
					client.set(TokenUtil.TOKEN_COOKIE_NMAE, response.getToken());
				}
				//返回结果
				ackSender.sendAckData(response);
			}
        	
		});
        logger.info("Setup SocketIOServer server "+webSocketHost+":"+webSocketPort+webSocketAptConextPath);
	}

	//配置Undertow服务
	private void setUpUndertow(Properties properties) {
	    int ioThread = Integer.parseInt(
	    		properties.getProperty("io_thread_number",
				String.valueOf(Runtime.getRuntime().availableProcessors()+1)));
	    int workerThread = Integer.parseInt(
	    		properties.getProperty("work_thread_number", 
				String.valueOf(Runtime.getRuntime().availableProcessors()*200)));
		String httpHost = properties.getProperty("http.host", "0.0.0.0");
        int httpPort=Integer.parseInt( properties.getProperty("http.port", "7080"));
        String httpApiContextPath=properties.getProperty("server.namespace","/api");
        String staticResourcePath = properties.getProperty("server.context.path.static","static");
        undertow = Undertow.builder()
                .addHttpListener(httpPort,httpHost)
                .setIoThreads(ioThread)
                .setWorkerThreads(workerThread)
                .setHandler(new PathHandler().addPrefixPath(httpApiContextPath , new HttpHandler() {
                    @Override
                    public void handleRequest(final HttpServerExchange exchange) throws Exception {
                    	if (exchange.isInIoThread()) {
                    	      exchange.dispatch(this);
                    	      return;
                    	 }
                    	exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Headers"), "origin, authorization, content-type, accept, referer, user-agent");
                    	exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Method"), "GET, POST, OPTIONS");
                    	exchange.getResponseHeaders().add(new HttpString("Access-Control-Allow-Origin"), "*");
                    	
                    	exchange.startBlocking();
                		//解析JSON
                    	RQ request = mapper.readValue(exchange.getInputStream(), requestClass);
                		//获取token
                    	if(exchange.getRequestCookies().get(TokenUtil.TOKEN_COOKIE_NMAE)!=null){
                    		request.setToken(exchange.getRequestCookies().get(TokenUtil.TOKEN_COOKIE_NMAE).getValue());
                    	}
                    	//派发事件
                    	RP response = dispatcher.dispatcher(request);
                    	//检查设置TOKEN
                    	if(response.getToken()!=null){
                    		exchange.getResponseCookies().put(TokenUtil.TOKEN_COOKIE_NMAE, new CookieImpl(TokenUtil.TOKEN_COOKIE_NMAE, response.getToken()));
        				}
                    	//返回结果
                    	exchange.getOutputStream().write(mapper.writeValueAsBytes(response));
                    	exchange.endExchange();
                    }
                }).addPrefixPath(properties.getProperty("server.context.path.static","static"), new ResourceHandler(new FileResourceManager(new File(properties.getProperty("server.context.path.static","static")), 10240)))).build();
        logger.info("Setup Undertow server "+httpHost+":"+httpPort+httpApiContextPath);
        logger.info("Setup Undertow server static resources at path:"+staticResourcePath+" at location:"+staticResourcePath);
	}

	public void start(){
		if(socketIOServer!=null){
			logger.info("Start SocketIOServer server......");
			socketIOServer.start();
		    //kill消息钩子函数，关闭系统使用的资源
	      	Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
	      			public void run() {
	      				logger.info("Stop SocketIOServer......");
	      				socketIOServer.stop();
	      				logger.info("Stop SocketIOServer OK!");
	      			}
	      	}));
			logger.info("Start SocketIOServer OK!");
		}
		if(undertow!=null){
			logger.info("Start Undertow server......");
			undertow.start();
			logger.info("Start Undertow OK!");
			 //kill消息钩子函数，关闭系统使用的资源
	      	Runtime.getRuntime().addShutdownHook(new Thread( new Runnable() {
	      			public void run() {
	      				logger.info("Stop Undertow......");
	      				undertow.stop();
	      				logger.info("Stop Undertow OK!");
	      			}
	      	}));
		}
	}
	
	public void stop(){
		if(socketIOServer!=null){
			logger.info("Stop SocketIOServer......");
			socketIOServer.stop();
			logger.info("Stop SocketIOServer OK!");
		}
		if(undertow!=null){
			logger.info("Stop Undertow......");
			undertow.stop();
			logger.info("Stop Undertow OK!");
		}
	}

}
