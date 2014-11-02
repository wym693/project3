package demo;

import java.io.Serializable;

//С˵����
public class Novel implements Serializable{
	
	private int id;
	private String name;
	private String author;
	private String detail; //���
	//С˵����
	private String content;
	
	private String type;//С˵���� ���� ����
	
	
	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Novel() {
	}
	
	
	public Novel( String name, String author, String detail,
			String content,String type) {
		this.name = name;
		this.author = author;
		this.detail = detail;
		this.content = content;
		this.type=type;
	}
	
	public Novel(int id, String name, String author, String detail,
			String content,String type) {
		this.id=id;
		this.name = name;
		this.author = author;
		this.detail = detail;
		this.content = content;
		this.type=type;
	}




	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
	
	
	
	

}
