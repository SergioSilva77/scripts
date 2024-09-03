package com.ocr;

import org.json.JSONObject;

public class Result {
	public int getQtdResult() {
		return qtdResult;
	}

	public void setQtdResult(int qtdResult) {
		this.qtdResult = qtdResult;
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	private int qtdResult;
	private JSONObject json;
	private String documentType;
}
