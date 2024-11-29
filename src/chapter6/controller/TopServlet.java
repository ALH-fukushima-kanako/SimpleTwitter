package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
    * ロガーインスタンスの生成
    */
    Logger log = Logger.getLogger("twitter");

    /**
    * デフォルトコンストラクタ
    * アプリケーションの初期化を実施する。
    */
    public TopServlet() {
        InitApplication application = InitApplication.getInstance();
        application.init();

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {


	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        boolean isShowMessageForm = false;
        User user = (User) request.getSession().getAttribute("loginUser");

        // 画面表示する・しないの制御のためセッションからユーザー情報取得できるか確認
        if (user != null) {
            isShowMessageForm = true;
        }
        /* 実践課題その➁
         * String型のuser_idの値をrequest.getParameter("user_id")で
         * JSPから受け取るように設定
         * MessageServiceのselectに引数としてString型のuser_idを追加
         */
        String userId = request.getParameter("user_id");

        // [仕様追加]絞り込み
        String startdate = request.getParameter("startday");
        String enddate = request.getParameter("endday");

        // メッセージを取得
        List<UserMessage> messages = new MessageService().select(userId, startdate, enddate);

        // [追加仕様]返信の表示
        // 全てのコメントを取得
        List<UserComment> comments = new CommentService().select();

        // リクエストにメッセージをセット
        request.setAttribute("messages", messages);
        request.setAttribute("comments", comments);

        // [仕様追加]打鍵テスト2回目 不具合修正
        request.setAttribute("startday", startdate);
        request.setAttribute("endday", enddate);

        // 画面表示する・しないの制御
        request.setAttribute("isShowMessageForm", isShowMessageForm);
        request.getRequestDispatcher("/top.jsp").forward(request, response);
    }
}
