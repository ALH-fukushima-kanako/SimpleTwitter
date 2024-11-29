package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {


/**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public MessageDao() {
        InitApplication application = InitApplication.getInstance();
        application.init();
    }

    public void insert(Connection connection, Message message) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO messages ( ");
            sql.append("    user_id, ");
            sql.append("    text, ");
            sql.append("    created_date, ");
            sql.append("    updated_date ");
            sql.append(") VALUES ( ");
            sql.append("    ?, ");                  // user_id
            sql.append("    ?, ");                  // text
            sql.append("    CURRENT_TIMESTAMP, ");  // created_date
            sql.append("    CURRENT_TIMESTAMP ");   // updated_date
            sql.append(")");

            ps = connection.prepareStatement(sql.toString());

            ps.setInt(1, message.getUserId());
            ps.setString(2, message.getText());

            ps.executeUpdate();
        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }

    // [仕様追加]つぶやき削除
    public void delete(Connection connection, int messageId) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM messages ");
            sql.append("WHERE ");
            sql.append("id = ? ");

            ps = connection.prepareStatement(sql.toString());

            ps.setInt(1, messageId);

            ps.executeUpdate();

        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }

    // [仕様追加]つぶやき編集 (編集前データ取得)
    public Message select(Connection connection, int messageId) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	  Message message = new Message();

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("SELECT * FROM messages ");
            sql.append("WHERE ");
            sql.append("id = ? ");

            ps = connection.prepareStatement(sql.toString());

            ps.setInt(1, messageId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                message.setId(rs.getInt("id"));
                message.setUserId(rs.getInt("user_id"));
                message.setText(rs.getString("text"));
                message.setCreatedDate(rs.getTimestamp("created_date"));
                message.setUpdatedDate(rs.getTimestamp("updated_date"));
            }else {
            	// [仕様追加]打鍵テスト1回目 不具合修正 存在しないIDの場合はnullで返す
            	message = null;
            }

            return message;

        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }

    // [仕様追加]つぶやき編集 (編集データ更新)
    public void update(Connection connection, Message message) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        PreparedStatement ps = null;
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE messages SET ");
            sql.append("text = ? ");
            sql.append("created_date = CURRENT_TIMESTAMP "); // 更新日を反映
            sql.append("WHERE id = ? ");

            ps = connection.prepareStatement(sql.toString());

            ps.setString(1, message.getText());
            ps.setInt(2, message.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw new SQLRuntimeException(e);
        } finally {
            close(ps);
        }
    }
}