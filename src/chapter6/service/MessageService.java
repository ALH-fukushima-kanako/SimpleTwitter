package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;
import chapter6.logging.InitApplication;

public class MessageService {


    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public MessageService() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    public void insert(Message message) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());
      Connection connection = null;
      try {
          connection = getConnection();
          new MessageDao().insert(connection, message);
          commit(connection);
      } catch (RuntimeException e) {
          rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
          throw e;
      } catch (Error e) {
          rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
          throw e;
      } finally {
          close(connection);
      }
  }


	public List<UserMessage> select(String userId) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        final int LIMIT_NUM = 1000;

        Connection connection = null;


        /* 実践課題その➁
         * idをnullで初期化
         * ServletからuserIdの値が渡ってきていたら
         * 整数型に型変換し、idに代入
         */
         Integer id = null;
         if(!StringUtils.isEmpty(userId)) {
             id = Integer.parseInt(userId);
         }

        try {
            connection = getConnection();

            /* 実践課題その➁
             * messageDao.selectに引数としてInteger型のidを追加
             * idがnullだったら全件取得する
             * idがnull以外だったら、その値に対応するユーザーIDの投稿を取得する
             */
            List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM);
            commit(connection);

            return messages;
        } catch (RuntimeException e) {
            rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } catch (Error e) {
            rollback(connection);
		log.log(Level.SEVERE, new Object(){}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
            throw e;
        } finally {
            close(connection);
        }
    }

}

