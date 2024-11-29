package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

//[追加仕様]つぶやきの編集

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet  extends HttpServlet {
 /**
 * ロガーインスタンスの生成
 */
 Logger log = Logger.getLogger("twitter");
 /**
 * デフォルトコンストラクタ
 * アプリケーションの初期化を実施する。
 */
 public EditServlet() {
     InitApplication application = InitApplication.getInstance();
     application.init();

 }

 // 編集ボタン押下時
 protected void doGet(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		        " : " + new Object(){}.getClass().getEnclosingMethod().getName());


     String messageId = request.getParameter("editMessageId");

     Message message = null;

     if(!(StringUtils.isBlank(messageId)) && (messageId.matches("^[0-9]*$"))) {
    	Integer msgId = Integer.valueOf(messageId);
    	message = new MessageService().select(msgId);
     }

     // [仕様追加]打鍵テスト1回目 不具合修正
     if(message == null) {
    	HttpSession session = request.getSession();
       	List<String> errorMessages = new ArrayList<String>();
       	errorMessages.add("不正なパラメータが入力されました");
       	session.setAttribute("errorMessages", errorMessages);
       	response.sendRedirect("./");
       	return;
     }

   	 	// 編集対象のつぶやき情報を保持
   	 	request.setAttribute("editMsg", message);
   	 	request.getRequestDispatcher("edit.jsp").forward(request, response);
 }

 // 編集後の更新ボタン押下時
 protected void doPost(HttpServletRequest request, HttpServletResponse response)
         throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
		        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

      HttpSession session = request.getSession();
      List<String> errorMessages = new ArrayList<String>();

      String text = request.getParameter("text");
      String messageId = request.getParameter("msgId");

      Integer msgId = Integer.valueOf(messageId);

      // 入力値のバリデーション
      if (!isValid(text, errorMessages)) {
          session.setAttribute("errorMessages", errorMessages);
          // [仕様追加]打鍵テスト1回目 不具合修正
          // 入力内容が保持されていること
          Message message = new Message();
          message.setId(msgId);
          message.setText(text);
          request.setAttribute("editMsg", message);
          // 入力値が不正な場合には編集画面を表示
          request.getRequestDispatcher("edit.jsp").forward(request, response);
          return;
      }

      Message message = new Message();
      message.setText(text);
      message.setId(msgId);

      new MessageService().update(message);
      response.sendRedirect("./");
 }

 private boolean isValid(String text, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
     " : " + new Object(){}.getClass().getEnclosingMethod().getName());

     //if (StringUtils.isEmpty(text)) {
	    if (StringUtils.isBlank(text)) {
         errorMessages.add("入力してください");
     } else if (140 < text.length()) {
         errorMessages.add("140文字以下で入力してください");
     }

     if (errorMessages.size() != 0) {
         return false;
     }
     return true;
 }

}
