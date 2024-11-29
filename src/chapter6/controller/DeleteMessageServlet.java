package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

// [追加仕様]つぶやきの削除

@WebServlet(urlPatterns = { "/deleteMessage" })
public class DeleteMessageServlet  extends HttpServlet {
    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");
    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public DeleteMessageServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());


      String messageId = request.getParameter("delMessageId");

      Integer msgId = Integer.valueOf(messageId);

      new MessageService().delete(msgId);

      response.sendRedirect("./");

    }
}
