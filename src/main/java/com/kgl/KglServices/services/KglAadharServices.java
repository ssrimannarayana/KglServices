package com.kgl.KglServices.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kgl.KglServices.model.AadharPojo;

@Service
public class KglAadharServices {

	@Value("${AADHAR_DATA_REST_API}")
	private String aADHAR_dATA_rEST_API;
	
	@Value("${AADHAR_DATA_API_UPDATE}")
	private String aADHAR_dATA_API_UPDATE;
	
	@Value("${DbURL}")
	private String dbURL;
	
	@Value("${UserNames}")
	private String userName;
	
	@Value("${PassWord}")
	private String passWord;

	private static final Logger logger = LoggerFactory.getLogger(KglAadharServices.class);

	public boolean aadharDataService() throws JsonMappingException, JsonProcessingException, SQLException {
		boolean status = false;
		Connection conn = null;
		try {
			conn = getMssqlConnection(dbURL, userName, passWord);
			if (conn != null) {
				List<String> list = getGoogleSeetData(aADHAR_dATA_rEST_API);
				if (list.size() > 0) {
					List<AadharPojo> aadharList = getResultedData(list, conn);
					if (aadharList.size() > 0)
						status = UpdateIntoGoogleSheet(aadharList,aADHAR_dATA_API_UPDATE);
				}
			}
			conn.close();
		}catch(Exception e) {
			logger.info("connection failed to remote system");
		}
		return status;
	}

	private Connection getMssqlConnection(String dbURL, String userName, String passWord) {
		// TODO Auto-generated method stub
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(dbURL, userName, passWord);
			if (conn != null) {
				logger.info("Connected");
			}
		} catch (SQLException ex) {
			logger.info("connection failed");
		}
		return conn;
	}

	private static List<AadharPojo> getResultedData(List<String> list, Connection conn) throws SQLException {
		String query2 = "Select APLN.Application_Number as [Application Number]\r\n"
				+ ", CM.Customer_Code as [Customer Code]\r\n" + ", CM.Customer_Name as [Customer Name]\r\n"
				+ ", CUSCON.Identity_Values as [Aadhar Number]\r\n"
				+ "from KLF_S3G_LIVE..S3G_ORG_ApplicationProcess(Nolock) APLN\r\n"
				+ "Inner Join KLF_S3G_LIVE..S3G_ORG_CustomerMaster(Nolock) CM\r\n"
				+ "On CM.Customer_ID = APLN.Customer_ID\r\n"
				+ "Inner Join KLF_S3G_LIVE..S3G_ORG_CustomerConstitutionDocs(Nolock) CUSCON\r\n"
				+ "On CUSCON.Customer_ID = CM.Customer_ID\r\n" + "Where CUSCON.ConstitutionDocumentCategory_ID = 1\r\n"
				+ "And APLN.Application_Number in(" + list + ")";
		String query = query2.replace("['", "'").replace("']", "'");
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		ResultSet resultSet = preparedStatement.executeQuery();
		List<AadharPojo> aadharList = new ArrayList<AadharPojo>();
		while (resultSet.next()) {
			AadharPojo aadharPojo = new AadharPojo();
			aadharPojo.setApplication_No(resultSet.getString("Application Number"));
			aadharPojo.setCustomer_Code(resultSet.getString("Customer Code"));
			aadharPojo.setCustomer_Name(resultSet.getString("Customer Name"));
			;
			aadharPojo.setAadhar_Number(resultSet.getString("Aadhar Number"));
			aadharList.add(aadharPojo);
		}
		resultSet.close();
		preparedStatement.close();
		conn.close();
		return aadharList;
	}

	private static List<String> getGoogleSeetData(String aADHAR_dATA_rEST_API) throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub
		// AADHAR_DATA_REST_API//
		String aadharData_URL = aADHAR_dATA_rEST_API;
		List<AadharPojo> list = getCampData(aadharData_URL);
		int recordsSize = list.size();
		List<String> list2 = new ArrayList<>();
		for (int i = 0; i < recordsSize; i++) {
			list2.add("'" + list.get(i).getApplication_No() + "'");
		}
		return list2;
	}

	private static List<AadharPojo> getCampData(String apiUrl) throws JsonMappingException, JsonProcessingException {
		List<AadharPojo> responseDataObj = null;
		try {
			ResponseEntity<String> response = new RestTemplate().getForEntity(apiUrl, String.class);
			ObjectMapper objectMapper;
			if (response != null) {
				String stringArry = response.getBody().toString();
				objectMapper = new ObjectMapper();
				responseDataObj = objectMapper.readValue(stringArry, new TypeReference<List<AadharPojo>>() {
				});
			}
		} catch (Exception e) {
			logger.info("google sheet api response No records found:::" + e);
		}
		return responseDataObj;
	}

	private static boolean UpdateIntoGoogleSheet(List<AadharPojo> aadharList,String aADHAR_dATA_API_UPDATE) {
		// AADHAR_DATA_API_UPDATE//
		String url = aADHAR_dATA_API_UPDATE;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		boolean dataUpdateIntoGoogleSheetStatus = false;
		MultiValueMap<String, String> map = null;
		try {
			for (int i = 0; i < aadharList.size(); i++) {
				map = new LinkedMultiValueMap<String, String>();
				if (i == aadharList.size() - 1) {
					map.add("sendMail", "TRUE");
				} else {

					map.add("sendMail", "FALSE");
				}
				map.add("Application_No", aadharList.get(i).getApplication_No());
				map.add("Customer_Code", aadharList.get(i).getCustomer_Code());
				map.add("Customer_Name", aadharList.get(i).getCustomer_Name());
				map.add("Aadhar_Number", aadharList.get(i).getAadhar_Number());
				HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map,
						headers);
				ResponseEntity<String> restTemplate = new RestTemplate().exchange(url, HttpMethod.POST, entity,
						String.class);
				if (restTemplate.getStatusCode().toString().equalsIgnoreCase("302 FOUND")) {
					dataUpdateIntoGoogleSheetStatus = true;
				}
			}
		} catch (Exception e) {
			logger.info("Update into googlesheet status id::  " + e.getMessage());
		}
		return dataUpdateIntoGoogleSheetStatus;
	}

}
