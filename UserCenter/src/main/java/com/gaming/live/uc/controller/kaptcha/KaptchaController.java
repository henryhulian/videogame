package com.gaming.live.uc.controller.kaptcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.gaming.live.common.constant.Code;
import com.gaming.live.common.utils.TokenUtil;
import com.gaming.live.uc.boundary.Kaptcha;
import com.gaming.live.uc.boundary.Request;
import com.gaming.live.uc.boundary.Response;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.google.common.cache.Cache;

public class KaptchaController{
	
	private  Cache<String, String> KaptchaCache ;
	private  DefaultKaptcha defaultKaptcha ;
	
	public KaptchaController( Cache<String, String> cache ) {
		
		KaptchaCache = cache;
		
		defaultKaptcha = new DefaultKaptcha();
		Properties properties = new Properties();
		properties.setProperty("kaptcha.image.width","90");
		properties.setProperty("kaptcha.image.height","50");
		properties.setProperty("kaptcha.textproducer.char.string","0123456789");
		properties.setProperty("kaptcha.textproducer.char.length","4");
		Config config = new Config(properties);
		defaultKaptcha.setConfig(config);
	}

	
	public void createKaptcha(Request data, Response response) {
		// 生成验证码
		String capText = defaultKaptcha.createText();
		BufferedImage bi = defaultKaptcha.createImage(capText);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "jpg", baos);
			baos.flush();
		} catch (IOException e) {
			return;
		}
		
		byte[] imageInByte = baos.toByteArray();

		// 把验证码值放入缓存
		String key = TokenUtil.createRandomToken();
		KaptchaCache.put(key, capText);
		
		response.setCode(Code.SUCCESS);
		response.setKaptcha(new Kaptcha(key,Base64.encodeBase64String(imageInByte)));
		
	}

}
