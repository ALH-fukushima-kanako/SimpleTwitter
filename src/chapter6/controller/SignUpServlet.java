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

import org.apache.commons.lang.StringUtils;

import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.UserService;

@WebServlet(urlPatterns = { "/signup" })
public class SignUpServlet extends HttpServlet {
	    /**
	    * ロガーインスタンスの生成
	    */
	    Logger log = Logger.getLogger("twitter");

	    /**
	    * デフォルトコンストラクタ
	    * アプリケーションの初期化を実施する。
	    */
	    public SignUpServlet() {
	        InitApplication application = InitApplication.getInstance();
	        application.init();

	    }

	    @Override
	    protected void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {

		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        request.getRequestDispatcher("signup.jsp").forward(request, response);
	    }

	    // ①doPostメソッド：「登録」ボタンが押下されると動く＝リクエストに対する受け口の役割
	    @Override
	    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        List<String> errorMessages = new ArrayList<String>();

	        // ②getUserメソッド：リクエストパラメータを取得
	        User user = getUser(request);

	        // ③doPostメソッドに戻ってくる

	        // ④isValidメソッド：リクエストパラメータに対するバリデーションを行う
	        if (!isValid(user, errorMessages)) {
	            request.setAttribute("errorMessages", errorMessages);
	            request.getRequestDispatcher("signup.jsp").forward(request, response);
	            return;
	        }

	        // ⑤doPostメソッドに戻ってくる

	        // ユーザー情報登録Service
	        // ⑥UserService.javaのinsertメソッドを呼び出す
	        new UserService().insert(user);

	        // 【外部のメソッドを呼び出す基本構文：戻り値なしパターン】
	        // 【 new クラス名().メソッド名()
	        // 【外部のメソッドを呼び出す基本構文：戻り値ありパターン】
	        // 戻り値の型 変数名 = new クラス名().メソッド名()

	        // リダイレクト：TopServletのdoGet呼び出し
	        response.sendRedirect("./");

	    // 【補足】”./”の表記について
        //  sendRedirectに指定されている”./”は、コンテキストルートというものを指します。
	    //  コンテキストルートは、アプリケーションのデフォルトURLと考えてください。
	    //  同様にしてtomcatは”/index.jsp”というものをデフォルトURLとして設定しているため、
	    //  リダイレクトに”./”を指定すると、”/index.jsp”が呼び出される＝TopServletが呼び出されるという挙動になっています

        // 「Servlet自身に対応する画面を呼びたい場合はフォワード、別画面を呼びたい場合はリダイレクト」


	    }

	    private User getUser(HttpServletRequest request) throws IOException, ServletException {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        User user = new User();
	        user.setName(request.getParameter("name"));
	        user.setAccount(request.getParameter("account"));
	        user.setPassword(request.getParameter("password"));
	        user.setEmail(request.getParameter("email"));
	        user.setDescription(request.getParameter("description"));

	        return user;
	    }

	    private boolean isValid(User user, List<String> errorMessages) {


		  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
	        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

	        String name = user.getName();
	        String account = user.getAccount();
	        String password = user.getPassword();
	        String email = user.getEmail();

	        if (!StringUtils.isEmpty(name) && (20 < name.length())) {
	            errorMessages.add("名前は20文字以下で入力してください");
	        }

	        if (StringUtils.isEmpty(account)) {
	            errorMessages.add("アカウント名を入力してください");
	        } else if (20 < account.length()) {
	            errorMessages.add("アカウント名は20文字以下で入力してください");
	        }
	        // 実践課題その③
	        User checkuser = new UserService().select(user.getAccount());
	        // 重複したアカウントありの場合
	        if(checkuser != null) {
	    	    errorMessages.add("アカウント名が重複しています");
	        }

	        if (StringUtils.isEmpty(password)) {
	            errorMessages.add("パスワードを入力してください");
	        }

	        if (!StringUtils.isEmpty(email) && (50 < email.length())) {
	            errorMessages.add("メールアドレスは50文字以下で入力してください");
	        }

	        if (errorMessages.size() != 0) {
	            return false;
	        }
	        return true;
	    }
	}
