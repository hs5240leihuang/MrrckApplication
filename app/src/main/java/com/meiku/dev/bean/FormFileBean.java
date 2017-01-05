package com.meiku.dev.bean;

import java.io.File;

//上传的文件BEAN
public class FormFileBean {
	private File file;
	private String fileName;
	
	public File getFile() {
		return file;
	}
	public void setFile(File file) {
		this.file = file;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public FormFileBean(File file, String fileName) {
		super();
		this.file = file;
		this.fileName = fileName;
	}
	public FormFileBean() {
		// TODO Auto-generated constructor stub
	}

}
