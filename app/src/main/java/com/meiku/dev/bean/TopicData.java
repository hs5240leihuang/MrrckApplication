package com.meiku.dev.bean;


/*"boardId": 1,
"createDate": "2015-10-28 16:44:53.0",
"delStatus": 0,
"id": 1,
"menuId": 0,
"name": "dada",
"remark": "dsada",
"topicList": [],
"updateDate": "2015-10-28 16:44:55.0",
"userId": 1
*/
public class TopicData {
	private int boardId;
	private int id;
	private int menuId;
	private String name;
	private String remark;
	
	public TopicData(String name, int id) {
		this.name = name;
		this.id = id;
	}
	public int getBoardId() {
		return boardId;
	}
	public void setBoardId(int boardId) {
		this.boardId = boardId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
