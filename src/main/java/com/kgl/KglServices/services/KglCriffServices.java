package com.kgl.KglServices.services;
import com.itextpdf.text.DocumentException;
import com.kgl.KglServices.model.CibilObj;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

@Service
public class KglCriffServices {
	private static final Logger logger = LoggerFactory.getLogger(KglCriffServices.class);
	   @Value("${serverfilePath}")
	   private String sfilePath;
	   @Value("${serverUrl}")
	   private String serverUrl;
	   @Value("${CV_CRIFF_API}")
	   private String appsheet_update_criff_info_url_CV;
	   @Value("${2W_CRIFF_API}")
	   private String appsheet_update_criff_info_url_2W;
	   @Value("${CRIFF_URL}")
	   private String CRIFFURL;
	   @Value("${CRIFF_USERID}")
	   private String CRIFFUSERID;
	   @Value("${CRIFF_PASSWORD}")
	   private String CRIFFPASSWORD;
	   @Value("${CRIFF_MBRID}")
	   private String MBRID;
	   @Value("${CRIFF_PRODUCT_TYPE}")
	   private String PRODUCTTYPE;
	   @Value("${CRIFF_PRODUCT_VERSION}")
	   private String CRIFFPRODUCTVERSION;
	   @Value("${CRIFF_REQ_VOL_TYPE}")
	   private String REQVOLTYPE;

	   public String twCriffReportsApi(CibilObj cibilObj) {
	      String timestamp = randomNo();
	      logger.info("--------------------TW Criff Enquiry ::------------------------" + cibilObj.toString());
	      logger.info("CRIFF API DATA:::" + cibilObj.toString());
	      String URL = CRIFFURL;
	      String xmlString = "<REQUEST-REQUEST-FILE><HEADER-SEGMENT><SUB-MBR-ID>KANAKADURGA FINANCE LIMITED</SUB-MBR-ID><INQ-DT-TM>29-07-2021 20:00:51</INQ-DT-TM><REQ-VOL-TYP>C01</REQ-VOL-TYP><REQ-ACTN-TYP>SUBMIT</REQ-ACTN-TYP><TEST-FLG>HMTEST</TEST-FLG><AUTH-FLG>Y</AUTH-FLG><AUTH-TITLE>USER</AUTH-TITLE><RES-FRMT>XML/HTML</RES-FRMT><MEMBER-PRE-OVERRIDE>N</MEMBER-PRE-OVERRIDE><RES-FRMT-EMBD>Y</RES-FRMT-EMBD><LOS-NAME>abc</LOS-NAME><LOS-VENDER>cde</LOS-VENDER><LOS-VERSION>1.0</LOS-VERSION><MFI><INDV>true</INDV><SCORE>true</SCORE><GROUP>true</GROUP></MFI><CONSUMER><INDV>true</INDV><SCORE>true</SCORE></CONSUMER><IOI>true</IOI></HEADER-SEGMENT><INQUIRY><APPLICANT-SEGMENT><APPLICANT-NAME><NAME1>" + cibilObj.getFirst_name().concat(" " + cibilObj.getLast_name()) + "</NAME1><NAME2></NAME2><NAME3></NAME3></APPLICANT-NAME><DOB><DOB-DATE>" + cibilObj.getDob() + "</DOB-DATE><AGE></AGE><AGE-AS-ON></AGE-AS-ON></DOB><IDS><ID><TYPE>" + cibilObj.getKyctypeId() + "</TYPE><VALUE>" + cibilObj.getKycdocno() + "</VALUE></ID><ID><TYPE>" + cibilObj.getKyctypeId2() + "</TYPE><VALUE>" + cibilObj.getKycdocno2() + "</VALUE></ID></IDS><RELATIONS><RELATION><TYPE></TYPE><NAME></NAME></RELATION></RELATIONS><KEY-PERSON><TYPE></TYPE><NAME></NAME></KEY-PERSON><NOMINEE><TYPE></TYPE><NAME></NAME></NOMINEE><PHONES><PHONE><TELE-NO-TYPE>P01</TELE-NO-TYPE><TELE-NO>" + cibilObj.getTelephone() + "</TELE-NO></PHONE></PHONES></APPLICANT-SEGMENT><ADDRESS-SEGMENT><ADDRESS><TYPE>D01</TYPE><ADDRESS-1>" + cibilObj.getAddress1() + "</ADDRESS-1><CITY>" + cibilObj.getCity() + "</CITY><STATE>" + cibilObj.getStatecode() + "</STATE><PIN>" + cibilObj.getPincode() + "</PIN></ADDRESS></ADDRESS-SEGMENT><EMAIL></EMAIL><APPLICATION-SEGMENT><INQUIRY-UNIQUE-REF-NO>" + timestamp + "</INQUIRY-UNIQUE-REF-NO><CREDT-RPT-ID>3450</CREDT-RPT-ID><CREDT-REQ-TYP>INDV</CREDT-REQ-TYP><CREDT-RPT-TRN-ID>2021</CREDT-RPT-TRN-ID><CREDT-INQ-PURPS-TYP>ACCT-ORIG</CREDT-INQ-PURPS-TYP><CREDT-INQ-PURPS-TYP-DESC>JLG INDIVIDUAL</CREDT-INQ-PURPS-TYP-DESC><CREDIT-INQUIRY-STAGE>PRE-DISB</CREDIT-INQUIRY-STAGE><CREDT-RPT-TRN-DT-TM>29-07-2021 20:00:51</CREDT-RPT-TRN-DT-TM><MBR-ID>MFI00</MBR-ID><KENDRA-ID>1234</KENDRA-ID><BRANCH-ID>3008</BRANCH-ID><LOS-APP-ID>" + timestamp + "</LOS-APP-ID><LOAN-AMOUNT>" + cibilObj.getRequiredamount() + "</LOAN-AMOUNT></APPLICATION-SEGMENT></INQUIRY></REQUEST-REQUEST-FILE>";
	      HttpHeaders headers = new HttpHeaders();
	      headers.set("requestXML", xmlString);
	      headers.set("userId", CRIFFUSERID);
	      headers.set("password", CRIFFPASSWORD);
	      headers.set("mbrid", MBRID);
	      headers.set("productType", PRODUCTTYPE);
	      headers.set("productVersion", CRIFFPRODUCTVERSION);
	      headers.set("reqVolType", REQVOLTYPE);
	      HttpEntity<String> entity = new HttpEntity<String>(headers);
	      RestTemplate restTemplate = new RestTemplate();
	      ResponseEntity<String> response = null;
	      String HtmlCode = null;
	      String criffLink = null;
	      String resp = null;
	      try {
	         response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class, new Object[0]);
	         if (response != null) {
	            resp = (String)response.getBody();
	            HtmlCode = getLongStrings(resp);
	            criffLink = getURL(HtmlCode, cibilObj.getFirst_name(), cibilObj.getKycdocno(), "criff", cibilObj.getCibilid());
	            updateIntoAppSheet(criffLink, cibilObj.getAppname(), cibilObj.getCibilid());
	         } else {
	            criffLink = "http://error.html";
	            updateIntoAppSheet(criffLink, cibilObj.getAppname(), cibilObj.getCibilid());
	         }
	      } catch (Exception error) {
	         logger.info("Error:: " + error.getMessage());
	      }

	      return criffLink;
	   }

	   public String cvCriffReportsApi(CibilObj cibilObj) {
	      String timestamp = randomNo();
	      logger.info("--------------------CV Criff Enquiry ::------------------------" + cibilObj.toString());
	      logger.info("CRIFF API DATA:::" + cibilObj.toString());
	      String URL = CRIFFURL;
	      String xmlString = "<REQUEST-REQUEST-FILE><HEADER-SEGMENT><SUB-MBR-ID>KANAKADURGA FINANCE LIMITED</SUB-MBR-ID><INQ-DT-TM>29-07-2021 20:00:51</INQ-DT-TM><REQ-VOL-TYP>C01</REQ-VOL-TYP><REQ-ACTN-TYP>SUBMIT</REQ-ACTN-TYP><TEST-FLG>HMTEST</TEST-FLG><AUTH-FLG>Y</AUTH-FLG><AUTH-TITLE>USER</AUTH-TITLE><RES-FRMT>XML/HTML</RES-FRMT><MEMBER-PRE-OVERRIDE>N</MEMBER-PRE-OVERRIDE><RES-FRMT-EMBD>Y</RES-FRMT-EMBD><LOS-NAME>abc</LOS-NAME><LOS-VENDER>cde</LOS-VENDER><LOS-VERSION>1.0</LOS-VERSION><MFI><INDV>true</INDV><SCORE>true</SCORE><GROUP>true</GROUP></MFI><CONSUMER><INDV>true</INDV><SCORE>true</SCORE></CONSUMER><IOI>true</IOI></HEADER-SEGMENT><INQUIRY><APPLICANT-SEGMENT><APPLICANT-NAME><NAME1>" + cibilObj.getFirst_name().concat(" " + cibilObj.getLast_name()) + "</NAME1><NAME2></NAME2><NAME3></NAME3></APPLICANT-NAME><DOB><DOB-DATE>" + cibilObj.getDob() + "</DOB-DATE><AGE></AGE><AGE-AS-ON></AGE-AS-ON></DOB><IDS><ID><TYPE>" + cibilObj.getKyctypeId() + "</TYPE><VALUE>" + cibilObj.getKycdocno() + "</VALUE></ID><ID><TYPE>" + cibilObj.getKyctypeId2() + "</TYPE><VALUE>" + cibilObj.getKycdocno2() + "</VALUE></ID></IDS><RELATIONS><RELATION><TYPE></TYPE><NAME></NAME></RELATION></RELATIONS><KEY-PERSON><TYPE></TYPE><NAME></NAME></KEY-PERSON><NOMINEE><TYPE></TYPE><NAME></NAME></NOMINEE><PHONES><PHONE><TELE-NO-TYPE>P01</TELE-NO-TYPE><TELE-NO>" + cibilObj.getTelephone() + "</TELE-NO></PHONE></PHONES></APPLICANT-SEGMENT><ADDRESS-SEGMENT><ADDRESS><TYPE>D01</TYPE><ADDRESS-1>" + cibilObj.getAddress1() + "</ADDRESS-1><CITY>" + cibilObj.getCity() + "</CITY><STATE>" + cibilObj.getStatecode() + "</STATE><PIN>" + cibilObj.getPincode() + "</PIN></ADDRESS></ADDRESS-SEGMENT><EMAIL></EMAIL><APPLICATION-SEGMENT><INQUIRY-UNIQUE-REF-NO>" + timestamp + "</INQUIRY-UNIQUE-REF-NO><CREDT-RPT-ID>3450</CREDT-RPT-ID><CREDT-REQ-TYP>INDV</CREDT-REQ-TYP><CREDT-RPT-TRN-ID>2021</CREDT-RPT-TRN-ID><CREDT-INQ-PURPS-TYP>ACCT-ORIG</CREDT-INQ-PURPS-TYP><CREDT-INQ-PURPS-TYP-DESC>JLG INDIVIDUAL</CREDT-INQ-PURPS-TYP-DESC><CREDIT-INQUIRY-STAGE>PRE-DISB</CREDIT-INQUIRY-STAGE><CREDT-RPT-TRN-DT-TM>29-07-2021 20:00:51</CREDT-RPT-TRN-DT-TM><MBR-ID>MFI00</MBR-ID><KENDRA-ID>1234</KENDRA-ID><BRANCH-ID>3008</BRANCH-ID><LOS-APP-ID>" + timestamp + "</LOS-APP-ID><LOAN-AMOUNT>" + cibilObj.getRequiredamount() + "</LOAN-AMOUNT></APPLICATION-SEGMENT></INQUIRY></REQUEST-REQUEST-FILE>";
	      HttpHeaders headers = new HttpHeaders();
	      headers.set("requestXML", xmlString);
	      headers.set("userId", CRIFFUSERID);
	      headers.set("password", CRIFFPASSWORD);
	      headers.set("mbrid", MBRID);
	      headers.set("productType", PRODUCTTYPE);
	      headers.set("productVersion", CRIFFPRODUCTVERSION);
	      headers.set("reqVolType", REQVOLTYPE);
	      HttpEntity<String> entity = new HttpEntity<String>(headers);
	      RestTemplate restTemplate = new RestTemplate();
	      ResponseEntity<String> response = null;
	      String HtmlCode = null;
	      String criffLink = null;
	      String resp = null;

	      try {
	         response = restTemplate.exchange(URL, HttpMethod.POST, entity, String.class, new Object[0]);
	         if (response != null) {
	            resp = (String)response.getBody();
	            HtmlCode = getLongStrings(resp);
	            criffLink = getURL(HtmlCode, cibilObj.getFirst_name(), cibilObj.getKycdocno(), "criff", cibilObj.getCibilid());
	            updateIntoAppSheet(criffLink, cibilObj.getAppname(), cibilObj.getCibilid());
	         } else {
	            criffLink = "http://error.html";
	            updateIntoAppSheet(criffLink, cibilObj.getAppname(), cibilObj.getCibilid());
	         }
	      } catch (Exception var15) {
	         logger.info("Error:: " + var15.getMessage());
	      }

	      return criffLink;
	   }
	   

	   public String randomNo() {
	      String Mbrid = "NBF0001252";
	      Random r = new Random(System.currentTimeMillis());
	      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
	      String fianlId = Mbrid.concat(String.valueOf((1 + r.nextInt(2)) * 100000 + r.nextInt(100000))).concat(String.valueOf(timestamp.getTime()));
	      return fianlId;
	   }

	   private String getLongStrings(String resp) {
	      Pattern p = Pattern.compile("<CONTENT>(.+?)</CONTENT>", 32);
	      Matcher m = p.matcher(resp);
	      m.find();
	      String Data = null;

	      try {
	         Data = m.group(1);
	      } catch (Exception var6) {
	         logger.info("There is no content in this file..." + var6);
	      }

	      return Data;
	   }

	   private String getURL(String htmlCodeString, String name, String kycNo, String type, String crifId) throws DocumentException, IOException {
	      String fileURLPath = sfilePath;
	      String serverURL = serverUrl;
	      String Url = null;
	      String path = null;
	      if (type.equalsIgnoreCase("criff")) {
	         path = fileURLPath.concat(crifId.concat("_criff.html"));
	         Url = serverURL.concat(crifId.concat("_criff.html"));
	      }

	      try {
	         Files.write(Paths.get(path), htmlCodeString.getBytes(), new OpenOption[]{StandardOpenOption.CREATE, StandardOpenOption.APPEND});
	      } catch (IOException var11) {
	         var11.printStackTrace();
	      }

	      return Url;
	   }

	   private ResponseEntity<String> updateIntoAppSheet(String criffLink, String appname, String criffid) {
	      String URL_CV = appsheet_update_criff_info_url_CV;
	      String URL_2W = appsheet_update_criff_info_url_2W;
	      String Url = null;
	      if (appname.equalsIgnoreCase("CV")) {
	         Url = URL_CV;
	      } else if (appname.equalsIgnoreCase("2W")) {
	         Url = URL_2W;
	      }

	      MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
	      map.add("cid", criffid);
	      map.add("CRIFF_REPORT", criffLink);
	      HttpHeaders headers = new HttpHeaders();
	      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
	      ResponseEntity<String> restTemplate = null;
	      HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(map, headers);

	      try {
	         restTemplate = (new RestTemplate()).exchange(Url, HttpMethod.POST, entity, String.class, new Object[0]);
	      } catch (Exception error) {
	         logger.info("error  " + error);
	      }
	      return restTemplate;
	   }
}
