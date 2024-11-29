package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.UserComment;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class UserCommentDao {

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public UserCommentDao() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }


    // [追加仕様]つぶやきの返信を表示
    public List<UserComment> select(Connection connection) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  	List<UserComment> commentList = new ArrayList<UserComment>();

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT ");
            sql.append("  comments.id as id, ");					// 返信ID
            sql.append("  comments.message_id as msgid, ");			// つぶやきID
            sql.append("  comments.user_id as userid, ");			// 利用者ID
            sql.append("  users.account as account, ");				// アカウント
            sql.append("  users.name as name, ");					// 名前
            sql.append("  comments.text as text, ");				// 返信
            sql.append("  comments.created_date as created_date ");	// 返信登録日時
            sql.append("FROM comments ");
            sql.append("INNER JOIN users ");
            sql.append("ON comments.user_id = users.id ");
            sql.append("ORDER BY comments.message_id ASC, comments.created_date ASC ");

            ps = connection.prepareStatement(sql.toString());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
            	UserComment message = new UserComment();
            	message.setId(rs.getInt("id"));
                message.setMessageId(rs.getInt("msgid"));
                message.setUserId(rs.getInt("userid"));
            	message.setAccount(rs.getString("account"));
            	message.setName(rs.getString("name"));
            	message.setText(rs.getString("text"));
                message.setCreatedDate(rs.getTimestamp("created_date"));
                commentList.add(message);
            }

            return commentList;

        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }
}
