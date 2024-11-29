package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private int id;			// ID
    private int userId;		// 利用者ID   →誰がつぶやいたか
    private String text;		// つぶやき   →メッセージ本文
    private Date createdDate;	// 登録日時   →いつつぶやいたか
    private Date updatedDate;	// 更新日時

    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

    // getter/setterは省略されているので、自分で記述しましょう。
}
