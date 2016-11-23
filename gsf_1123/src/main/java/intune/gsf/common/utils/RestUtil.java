package intune.gsf.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import intune.gsf.model.RestParam;

public class RestUtil {

	private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);

	public static <T> Object call(RestParam req, Class<T> responseType) throws Exception {

		RestTemplate restTemplate = new RestTemplate();

		SimpleClientHttpRequestFactory rf = (SimpleClientHttpRequestFactory) restTemplate

				.getRequestFactory();

		rf.setConnectTimeout(3000);//3초

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<String, Object>();

		// 헤더 세팅

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		if (req.getHeader() != null) {

			for (String key : req.getHeader().keySet()) {

				headers.add(key, req.getHeader().get(key));

			}

		}

		// 파라메터 세팅

		if (req.getParameter() != null) {

			for (String key : req.getParameter().keySet()) {

				body.add(key, req.getParameter().get(key));

			}

		}

		HttpEntity<?> requestEntity = new HttpEntity(body, headers);

		logger.debug("-----------------------------------------------------------------");

		logger.debug("-- API Call                                         ---");

		logger.debug("-----------------------------------------------------------------");

		logger.debug("   URL : " + req.getUrl());

		logger.debug("-----------------------------------------------------------------");

		for (String key : headers.keySet()) {

			logger.debug("   Header : " + key + " = " + headers.get(key));

		}

		logger.debug("-----------------------------------------------------------------");

		for (String key : body.keySet()) {

			logger.debug("   Param : " + key + " = " + body.get(key));

		}

		logger.debug("-----------------------------------------------------------------");

		Object responseBody = responseType.newInstance();

		try {

			ResponseEntity<T> res = restTemplate.exchange(req.getUrl(), HttpMethod.GET, requestEntity,

					responseType);

			responseBody = res.getBody();

			logger.debug("-----------------------------------------------------------------");

			logger.debug("-- Response Success                                           ---");

			logger.debug("-----------------------------------------------------------------");

			logger.debug("   body : " + responseBody);

			logger.debug("-----------------------------------------------------------------");

		} catch (Exception e) {

			logger.debug("-----------------------------------------------------------------");

			logger.debug("-- Response Fail                                              ---");

			logger.debug("-----------------------------------------------------------------");

			logger.debug("   Message  : " + e.getMessage());

			logger.debug("-----------------------------------------------------------------");

		}

		return responseBody;

	}


}
