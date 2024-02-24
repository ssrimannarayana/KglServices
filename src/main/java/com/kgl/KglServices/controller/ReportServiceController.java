package com.kgl.KglServices.controller;

import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.kgl.KglServices.exceptions.KglExceptions;
import com.kgl.KglServices.model.CibilObj;
import com.kgl.KglServices.services.KglAadharServices;
import com.kgl.KglServices.services.KglCibilServices;
import com.kgl.KglServices.services.KglCriffServices;

@RestController
@RequestMapping({ "/reports/" })
public class ReportServiceController {
	@Autowired
	KglCibilServices kglCibilServices;

	@Autowired
	KglCriffServices kglCriffServices;

	@Autowired
	KglAadharServices kglAadharServices;

	@GetMapping({ "/test" })
	public String test() {
		return "hi";
	}

	@GetMapping({ "/aadharData" })
	public boolean aadharData() throws JsonProcessingException, SQLException {
		return kglAadharServices.aadharDataService();
	}

	@PostMapping({ "/twCibilEnquiry" })
	public String twCibilEnquiry(@RequestBody CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		return kglCibilServices.twCibilReportsApi(cibilDataObj);
	}

	@PostMapping({ "/twCriffEnquiry" })
	public String twCriffEnquiry(@RequestBody CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		return kglCriffServices.twCriffReportsApi(cibilDataObj);
	}

	@PostMapping({ "/cvCibilEnquiry" })
	public String cvCibilEnquiry(@RequestBody CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		return kglCibilServices.cvCibilReportsApi(cibilDataObj);
	}

	@PostMapping({ "/cvCriffEnquiry" })
	public String cvCriffEnquiry(@RequestBody CibilObj cibilDataObj) throws JsonProcessingException, KglExceptions {
		return kglCriffServices.cvCriffReportsApi(cibilDataObj);
	}

}
