package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class UserMessage implements Serializable {

	// 利用者マスタ + つぶやきテーブル

    private int id;			// ID
    private String account;		// アカウント
    private String name;		// 名前
    private int userId;		// 利用者ID
    private String text;		// 	つぶやき
    private Date createdDate;	// つぶやき登録日時

    public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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


}