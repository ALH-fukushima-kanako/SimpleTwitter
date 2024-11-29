package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// [仕様追加]ログインフィルター
// FilterはServletより前に実行される
// ユーザー編集画面・つぶやき編集画面に対してログインフィルターを適用
@WebFilter(urlPatterns = {"/setting", "/edit" })
public class LoginFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// キャスト
		HttpServletRequest httprequest = (HttpServletRequest) request;
		HttpServletResponse httpresponse = (HttpServletResponse)response;

		// セッション情報取得
		HttpSession session = httprequest.getSession();

		// ログイン情報が取得できない場合はログイン画面へ遷移
		if( null == session.getAttribute("loginUser")) {
			// エラーメッセージ設定
			List<String> errorMessages = new ArrayList<String>();
			errorMessages.add("ログインをしてください");
			session.setAttribute("errorMessages", errorMessages);
			httpresponse.sendRedirect("./login.jsp");
			return;
		}
		// Servletを実行
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// 今回は処理無し

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 今回は処理無し
	}

}
