package com.company.data;

public class DummyPostData {
	String mail;
	String name;

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DummyPostData [mail=" + mail + ", name=" + name + "]";
	}

}