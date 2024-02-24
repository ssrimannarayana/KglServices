package com.kgl.KglServices.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.DocumentException;
import com.kgl.KglServices.exceptions.KglExceptions;
import com.kgl.KglServices.model.CibilObj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

@Service
public class KglCibilServices {
	private static final Logger logger = LoggerFactory.getLogger(KglCibilServices.class);
	@Value("${first_cibil_api}")
	private String firstCibilApi;
	@Value("${2W_CIBIL_API}")
	private String appsheet_update_cibil_info_url_2W;
	@Value("${CV_CIBIL_API}")
	private String appsheet_update_cibil_info_url_CV;
	@Value("${second_api}")
	private String secondApi;
	@Value("${token_cred}")
	private String tokenCred;
	@Value("${token_api}")
	private String tokenApi;
	@Value("${serverfilePath}")
	private String sfilePath;
	@Value("${serverUrl}")
	private String serverUrl;
	@Value("${AP_USERID}")
	private String ap_userId;
	@Value("${PC_USERID}")
	private String pc_userId;
	@Value("${TS_USERID}")
	private String ts_userId;
	@Value("${KA_USERID}")
	private String ka_userId;
	@Value("${TN_USERID}")
	private String tn_userId;
	@Value("${GJ_USERID}")
	private String gj_userId;
	@Value("${AP_PWD}")
	private String ap_pwd;
	@Value("${TN_PWD}")
	private String tn_pwd;
	@Value("${TS_PWD}")
	private String ts_pwd;
	@Value("${GJ_PWD}")
	private String gj_pwd;
	@Value("${KA_PWD}")
	private String ka_pwd;
	@Value("${PC_PWD}")
	private String pc_pwd;

	public String twCibilReportsApi(CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		String status = null;
		logger.info(" 2W CIBIL API calling Started...." + cibilDataObj.getAppname());
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(cibilDataObj);
		logger.info("--------------------CIBIL Enquiry ::------------------------");
		logger.info("Customer Info:: " + jsonStr);
		logger.info("--------------------CIBIL Enquiry ::------------------------");
		cibilDataObj.setStateuserId(ap_userId);
		cibilDataObj.setStatepassword(ap_pwd);
		try {
			Map<String, String> response = getToken(jsonString(cibilDataObj), cibilDataObj);
			response.put("cid", cibilDataObj.getCibilid());
			updateIntoAppSheet(response, cibilDataObj.getCibilid(), cibilDataObj.getAppname());
			status = "SUCCESS";
		} catch (Exception error) {
			logger.info("error---->:::  " + error);
			status = "FAILED";
		}

		logger.info(" 2W CIBIL API calling ENDED...." + cibilDataObj.getAppname());
		return status;
	}

	public String cvCibilReportsApi(CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		String status = null;
		logger.info(" CV CIBIL API calling Started...." + cibilDataObj.getAppname());
		ObjectMapper Obj = new ObjectMapper();
		String jsonStr = Obj.writeValueAsString(cibilDataObj);
		logger.info("--------------------CIBIL Enquiry ::------------------------");
		logger.info("Customer Info:: " + jsonStr);
		logger.info("--------------------CIBIL Enquiry ::------------------------");
		cibilDataObj.setStateuserId(ap_userId);
		cibilDataObj.setStatepassword(ap_pwd);
		try {
			Map<String, String> response = getToken(jsonString(cibilDataObj), cibilDataObj);
			response.put("cid", cibilDataObj.getCibilid());
			updateIntoAppSheet(response, cibilDataObj.getCibilid(), cibilDataObj.getAppname());
			status = "SUCCESS";
		} catch (Exception error) {
			logger.info("error---->:::  " + error);
			status = "FAILED";
		}

		logger.info(" CV CIBIL API calling ENDED...." + cibilDataObj.getAppname());
		return status;
	}

	@PostMapping({ "/getToken" })
	private Map<String, String> getToken(String jsonData, CibilObj cibilObj) throws ParseException, KglExceptions {
		String requestBody = tokenCred;
		String cirFileUrl = "NA";
		String idvFileUrl = "NA";
		String url = tokenApi;
		HttpEntity<String> entity = null;
		ResponseEntity<String> response = null;
		HttpHeaders headers = new HttpHeaders();
		String token = null;
		HashMap<String, String> fileUrls = new HashMap<String, String>();

		try {
			headers.set("Content-Type", "application/json");
			entity = new HttpEntity<String>(requestBody, headers);
			response = (new RestTemplate()).exchange(url, HttpMethod.POST, entity, String.class, new Object[0]);
			JSONObject jsonObj = jsonParsing((String) response.getBody());
			token = (String) jsonObj.get("access_token");
			String responseBody = null;
			if (token.length() > 1) {
				String URL = firstCibilApi;
				headers.set("Content-Type", "application/json");
				headers.set("Authorization", "Bearer " + token);
				entity = new HttpEntity<String>(jsonData, headers);
				response = (new RestTemplate()).exchange(URL, HttpMethod.POST, entity, String.class, new Object[0]);
				if (response != null) {
					responseBody = (String) response.getBody();
					Object obj = jsonParsing(responseBody);
					JSONObject obj1 = (JSONObject) obj;
					JSONObject ar1 = (JSONObject) obj1.get("Fields");
					JSONObject obj3 = (JSONObject) ar1.get("Applicants");
					JSONArray ar2 = (JSONArray) obj3.get("Applicant");
					JSONObject obj4 = (JSONObject) ar2.get(0);
					JSONObject obj5 = (JSONObject) obj4.get("Services");
					JSONArray ar3 = (JSONArray) obj5.get("Service");
					JSONObject obj6 = (JSONObject) ar3.get(0);
					JSONObject obj7 = (JSONObject) obj6.get("Operations");
					JSONArray ar4 = (JSONArray) obj7.get("Operation");
					JSONObject cirData = (JSONObject) ar4.get(0);
					JSONObject obj8 = (JSONObject) cirData.get("Data");
					JSONObject obj9 = (JSONObject) obj8.get("Response");
					JSONObject obj10 = (JSONObject) obj9.get("RawResponse");
					JSONObject obj11 = (JSONObject) obj10.get("Document");
					Long cirDocId = (Long) obj11.get("Id");
					if (cirDocId == null) {
						fileUrls.put("cirFileUrl", "NA");
						fileUrls.put("idvFileUrl", "NA");
						fileUrls.put("CibilScore", "NA");
					} else {
						cirFileUrl = getCirHtmlFileUrl(cirDocId, token, cibilObj.getCibilid());
						String CibilScore = "NA";
						idvFileUrl = "http://kanakadurgafinance.com";
						logger.info("cirFileUrl " + cirFileUrl);
						logger.info("CibilScore " + CibilScore);
						logger.info("idvFileUrl " + idvFileUrl);
						fileUrls.put("cirFileUrl", cirFileUrl);
						fileUrls.put("idvFileUrl", idvFileUrl);
						fileUrls.put("CibilScore", CibilScore);
					}
				} else {
					fileUrls.put("cirFileUrl", "Cibil Beauro side issue");
					fileUrls.put("idvFileUrl", "Cibil Beauro side issue");
					fileUrls.put("CibilScore", "NA");
					logger.info("Cibil Beauro side issue server down... ");
				}
			}
		} catch (Exception error) {
			fileUrls.put("cirFileUrl", "Not Available");
			fileUrls.put("idvFileUrl", "Not Available");
			logger.info("error " + error);
		}

		return fileUrls;
	}

	private ResponseEntity<String> updateIntoAppSheet(Map<String, String> fileUrlresponse, String rowId,
			String appname) {
		String URL_2W = appsheet_update_cibil_info_url_2W;
		String URL_CV = appsheet_update_cibil_info_url_CV;
		String Url = null;
		if (appname.equalsIgnoreCase("CV")) {
			Url = URL_CV;
		} else if (appname.equalsIgnoreCase("2W")) {
			Url = URL_2W;
		}

		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		map.add("cid", rowId);
		map.add("cibil", (String) fileUrlresponse.get("cirFileUrl"));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		ResponseEntity<String> restTemplate = null;
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);
		try {
			restTemplate = (new RestTemplate()).exchange(Url, HttpMethod.POST, entity, String.class, new Object[0]);
		} catch (Exception var13) {
			logger.info("error  " + var13);
		}

		return restTemplate;
	}

	private String getCirHtmlFileUrl(Long cirId, String token, String cibilid) throws DocumentException, IOException {
		String url = null;
		try {
			String response = htmlFileApi(cirId, token);
			url = getURL(response, "CIBIL", cibilid);
		} catch (Exception var6) {
			logger.info("errorInfo::  " + var6);
		}

		return url;
	}

	

	private String htmlFileApi(Long docId, String token) {
		String URL = secondApi + docId;
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer " + token);
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<String> response = (new RestTemplate()).exchange(URL, HttpMethod.GET, entity, String.class,
				new Object[0]);
		String responseObj = (String) response.getBody();
		return responseObj;
	}

	private String jsonString(CibilObj objData) {
		String first_name = "\"" + objData.getFirst_name() + "\"";
		String last_name = "\"" + objData.getLast_name() + "\"";
		String dob = "\"" + objData.getDob() + "\"";
		String gender = "\"" + objData.getGender() + "\"";
		String idnumber = "\"" + objData.getKyctypeId() + "\"";
		String docNumber = "\"" + objData.getKycdocno() + "\"";
		String idnumber2 = "\"" + objData.getKyctypeId2() + "\"";
		String docNumber2 = "\"" + objData.getKycdocno2() + "\"";
		String telephone = "\"" + objData.getTelephone() + "\"";
		String add1 = "\"" + objData.getAddress1() + "\"";
		String add2 = "\"" + objData.getAddress2() + "\"";
		String city = "\"" + objData.getCity() + "\"";
		String pin = "\"" + objData.getPincode() + "\"";
		String reqAmount = "\"" + objData.getRequiredamount() + "\"";
		String stateCode = "\"" + objData.getStatecode() + "\"";
		String stateUserid = "\"" + objData.getStateuserId() + "\"";
		String statePassword = "\"" + objData.getStatepassword() + "\"";
		String jsonString = "{\r\n  \"RequestInfo\": {\r\n    \"SolutionSetName\": \"GO_KANAKADURGA_AGSS\",\r\n    \"ExecuteLatestVersion\": \"true\"\r\n  },\r\n  \"Fields\": {\r\n    \"Applicants\": {\r\n      \"Applicant\": {\r\n        \"ApplicantFirstName\": "
				+ first_name + ",\r\n        \"ApplicantMiddleName\": \"\",\r\n        \"ApplicantLastName\": "
				+ last_name + ",\r\n        \"DateOfBirth\": " + dob + ",\r\n        \"Gender\": " + gender
				+ ",\r\n        \"EmailAddress\": \"\",\r\n        \"Identifiers\": {\r\n          \"Identifier\": [\r\n            {\r\n              \"IdNumber\": "
				+ docNumber + ",\r\n              \"IdType\": " + idnumber
				+ "\r\n            },\r\n            {\r\n              \"IdNumber\": " + docNumber2
				+ ",\r\n              \"IdType\": " + idnumber2
				+ "\r\n            }\r\n          ]\r\n        },\r\n        \"Telephones\": {\r\n          \"Telephone\": {\r\n            \"TelephoneNumber\": "
				+ telephone
				+ ",\r\n            \"TelephoneType\": \"01\",\r\n            \"TelephoneCountryCode\": \"91\"\r\n          }\r\n        },\r\n        \"Addresses\": {\r\n          \"Address\": {\r\n            \"AddressType\": \"01\",\r\n            \"AddressLine1\": "
				+ add1 + ",\r\n            \"AddressLine2\": " + add2
				+ ",\r\n            \"AddressLine3\": \"\",\r\n            \"AddressLine4\": \"\",\r\n            \"AddressLine5\": \"\",\r\n            \"City\": "
				+ city + ",\r\n            \"PinCode\": " + pin
				+ ",\r\n            \"ResidenceType\": \"01\",\r\n            \"StateCode\": " + stateCode
				+ "\r\n          }\r\n        },\r\n        \"Services\": {\r\n          \"Service\": {\r\n            \"Id\": \"CORE\",\r\n            \"Operations\": {\r\n              \"Operation\": [\r\n                {\r\n                  \"Name\": \"ConsumerCIR\",\r\n                  \"Params\": {\r\n                    \"Param\": [\r\n                      {\r\n                        \"Name\": \"CibilBureauFlag\",\r\n                        \"Value\": \"false\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"Amount\",\r\n                        \"Value\": "
				+ reqAmount
				+ "\r\n                      },\r\n                      {\r\n                        \"Name\": \"Purpose\",\r\n                        \"Value\": \"10\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"ScoreType\",\r\n                        \"Value\": \"16\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"MemberCode\",\r\n                        \"Value\": "
				+ stateUserid
				+ "\r\n                      },\r\n                      {\r\n                        \"Name\": \"Password\",\r\n                        \"Value\": "
				+ statePassword
				+ "\r\n                      },\r\n                      {\r\n                        \"Name\": \"FormattedReport\",\r\n                        \"Value\": \"true\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"GSTStateCode\",\r\n                        \"Value\": \"28\"\r\n                      }\r\n                    ]\r\n                  }\r\n                },\r\n                {\r\n                  \"Name\": \"IDV\",\r\n                  \"Params\": {\r\n                    \"Param\": [\r\n                      {\r\n                        \"Name\": \"IDVerificationFlag\",\r\n                        \"Value\": \"true\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"ConsumerConsentForUIDAIAuthentication\",\r\n                        \"Value\": \"N\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"GSTStateCode\",\r\n                        \"Value\": \"28\"\r\n                      }\r\n                    ]\r\n                  }\r\n                },\r\n                {\r\n                  \"Name\": \"NTC\",\r\n                  \"Params\": {\r\n                    \"Param\": [\r\n                      {\r\n                        \"Name\": \"DSTuNtcFlag\",\r\n                        \"Value\": \"true\"\r\n                      },\r\n                      {\r\n                        \"Name\": \"NTCProductType\",\r\n                        \"Value\": \"CC\"\r\n                      }\r\n                    ]\r\n                  }\r\n                }\r\n              ]\r\n            }\r\n          }\r\n        }\r\n      }\r\n    },\r\n    \"ApplicationData\": {\r\n      \"GSTStateCode\": \"28\",\r\n      \"Services\": {\r\n        \"Service\": {\r\n          \"Id\": \"CORE\",\r\n          \"Skip\": \"N\",\r\n          \"Consent\": \"true\",\r\n          \"EnableSimulation\": \"False\"\r\n        }\r\n      }\r\n    }\r\n  }\r\n}";
		return jsonString;
	}

	private JSONObject jsonParsing(String stringToParse) throws ParseException {
		JSONParser jp = new JSONParser();
		JSONObject jobj = (JSONObject) jp.parse(stringToParse);
		return jobj;
	}

	private String getURL(String htmlCodeString, String type, String cibilid) throws DocumentException, IOException {
		String fileURL = sfilePath;
		String path = null;
		String Url = null;
		String Ul = serverUrl;
		if (type.equalsIgnoreCase("CIBIL")) {
			path = fileURL.concat(cibilid.concat("_Cibil.html"));
			Url = Ul.concat(cibilid.concat("_Cibil.html"));
		} else {
			path = fileURL.concat(cibilid.concat("_Idv.html"));
			Url = Ul.concat(cibilid.concat("_Idv.html"));
		}

		try {
			Files.write(Paths.get(path), htmlCodeString.getBytes(),
					new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.APPEND });
		} catch (IOException var9) {
			var9.printStackTrace();
		}

		return Url;
	}
}
