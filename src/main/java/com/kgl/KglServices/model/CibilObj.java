package com.kgl.KglServices.model;

import java.io.Serializable;

public class CibilObj implements Serializable {
	private static final long serialVersionUID = 1L;
	private String cibilid;
	private String first_name;
	private String last_name;
	private String gender;
	private String dob;
	private String telephone;
	private String kyctypeId;
	private String kycdocno;
	private String kyctypeId2;
	private String kycdocno2;
	private String address1;
	private String address2;
	private String city;
	private String statecode;
	private String pincode;
	private String requiredamount;
	private String stateuserId;
	private String statepassword;
	private String appname;

	public String getCibilid() {
		return this.cibilid;
	}

	public String getFirst_name() {
		return this.first_name;
	}

	public String getLast_name() {
		return this.last_name;
	}

	public String getGender() {
		return this.gender;
	}

	public String getDob() {
		return this.dob;
	}

	public String getTelephone() {
		return this.telephone;
	}

	public String getKyctypeId() {
		return this.kyctypeId;
	}

	public String getKycdocno() {
		return this.kycdocno;
	}

	public String getKyctypeId2() {
		return this.kyctypeId2;
	}

	public String getKycdocno2() {
		return this.kycdocno2;
	}

	public String getAddress1() {
		return this.address1;
	}

	public String getAddress2() {
		return this.address2;
	}

	public String getCity() {
		return this.city;
	}

	public String getStatecode() {
		return this.statecode;
	}

	public String getPincode() {
		return this.pincode;
	}

	public String getRequiredamount() {
		return this.requiredamount;
	}

	public String getStateuserId() {
		return this.stateuserId;
	}

	public String getStatepassword() {
		return this.statepassword;
	}

	public String getAppname() {
		return this.appname;
	}

	public void setCibilid(String cibilid) {
		this.cibilid = cibilid;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setKyctypeId(String kyctypeId) {
		this.kyctypeId = kyctypeId;
	}

	public void setKycdocno(String kycdocno) {
		this.kycdocno = kycdocno;
	}

	public void setKyctypeId2(String kyctypeId2) {
		this.kyctypeId2 = kyctypeId2;
	}

	public void setKycdocno2(String kycdocno2) {
		this.kycdocno2 = kycdocno2;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setStatecode(String statecode) {
		this.statecode = statecode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public void setRequiredamount(String requiredamount) {
		this.requiredamount = requiredamount;
	}

	public void setStateuserId(String stateuserId) {
		this.stateuserId = stateuserId;
	}

	public void setStatepassword(String statepassword) {
		this.statepassword = statepassword;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public CibilObj(String cibilid, String first_name, String last_name, String gender, String dob, String telephone,
			String kyctypeId, String kycdocno, String kyctypeId2, String kycdocno2, String address1, String address2,
			String city, String statecode, String pincode, String requiredamount, String stateuserId,
			String statepassword, String appname) {
		this.cibilid = cibilid;
		this.first_name = first_name;
		this.last_name = last_name;
		this.gender = gender;
		this.dob = dob;
		this.telephone = telephone;
		this.kyctypeId = kyctypeId;
		this.kycdocno = kycdocno;
		this.kyctypeId2 = kyctypeId2;
		this.kycdocno2 = kycdocno2;
		this.address1 = address1;
		this.address2 = address2;
		this.city = city;
		this.statecode = statecode;
		this.pincode = pincode;
		this.requiredamount = requiredamount;
		this.stateuserId = stateuserId;
		this.statepassword = statepassword;
		this.appname = appname;
	}

	public String toString() {
		return "CibilObj [cibilid=" + this.cibilid + ", first_name=" + this.first_name + ", last_name=" + this.last_name
				+ ", gender=" + this.gender + ", dob=" + this.dob + ", telephone=" + this.telephone + ", kyctypeId="
				+ this.kyctypeId + ", kycdocno=" + this.kycdocno + ", kyctypeId2=" + this.kyctypeId2 + ", kycdocno2="
				+ this.kycdocno2 + ", address1=" + this.address1 + ", address2=" + this.address2 + ", city=" + this.city
				+ ", statecode=" + this.statecode + ", pincode=" + this.pincode + ", requiredamount="
				+ this.requiredamount + ", stateuserId=" + this.stateuserId + ", statepassword=" + this.statepassword
				+ ", appname=" + this.appname + "]";
	}

}
