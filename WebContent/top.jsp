<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>簡易Twitter</title>
		<link href="./css/style.css" rel="stylesheet" type="text/css">
    </head>
    <body>
        <div class="main-contents">
		<div class="header">
		    <c:if test="${ empty loginUser }">
		        <a href="login.jsp">ログイン</a>
		        <a href="signup">登録する</a>
		    </c:if>
		    <c:if test="${ not empty loginUser }">
		        <a href="./">ホーム</a>
		        <a href="setting">設定</a>
		        <a href="logout">ログアウト</a>
		    </c:if>
		</div>

		<div class="narrow-down">
		    <form action="./" method="get">
		        日付：<input type="date" name="startday" value="${startday}"></input>&nbsp;～&nbsp;<input type="date" name="endday"  value="${endday}"></input>
		        <input type="submit" value="絞り込み">
		    </form>
		</div>

		<c:if test="${ not empty loginUser }">
		    <div class="profile">
		        <div class="name"><h2><c:out value="${loginUser.name}" /></h2></div>
		        <div class="account">@<c:out value="${loginUser.account}" /></div>
		        <div class="description"><c:out value="${loginUser.description}" /></div>
		    </div>
		</c:if>

		<c:if test="${ not empty errorMessages }">
		    <div class="errorMessages">
		        <ul>
		            <c:forEach items="${errorMessages}" var="errorMessage">
		                <li><c:out value="${errorMessage}" />
		            </c:forEach>
		        </ul>
		    </div>
		    <c:remove var="errorMessages" scope="session" />
		</c:if>

		<div class="form-area">
		    <c:if test="${ isShowMessageForm }">
		        <form action="message" method="post">
		            いま、どうしてる？<br />
		            <textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
		            <br />
		            <input type="submit" value="つぶやく">（140文字まで）
		        </form>
		    </c:if>
		</div>

		<div class="messages">
		    <c:forEach items="${messages}" var="message">
		        <div class="message">
		            <div class="account-name">
		                <span class="account">
		                <!-- <c:out value="${message.account}" /> -->
						    <a href="./?user_id=<c:out value="${message.userId}"/> ">
						        <c:out value="${message.account}" />
						    </a>
		                </span>
		                <span class="name"><c:out value="${message.name}" /></span>
		            </div>
		            <div class="text"><pre><c:out value="${message.text}" /></pre></div>
		            <div class="date"><fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /></div>
		        </div>
		        <!-- [仕様追加]つぶやき編集・削除(ログイン時のみ可) -->
				<c:if test="${ not empty loginUser && loginUser.account == message.account}">
			        <div class="box editbtn">
			        	<form action="edit" method="get">
			        		<input type="hidden" name="editMessageId" value="${message.id}">
							<button type="submit">編集</button>
						</form>
			        </div>
			        <div class="box delbtn">
			        	<form action="deleteMessage" method="post">
			        		<input type="hidden" name="delMessageId" value="${message.id}">
							<button type="submit">削除</button>
						</form>
			        </div>
				</c:if>
				<br />
			    <!-- [仕様追加]返信の表示(未ログインでも閲覧可) -->
			    <div class="commentArea">
			     	<c:forEach items="${comments}" var="comments">
			      		<c:if test="${message.id == comments.messageId}">
							<span class="name">${comments.account}&nbsp;&nbsp;${comments.name}</span><br />
							<div class="text"><pre>${comments.text}</pre></div>
							<div class="date"><fmt:formatDate value="${comments.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" /></div><br />
			        	</c:if>
					</c:forEach>
			    </div>
				<br />
				<c:if test="${ not empty loginUser}">
			        <!-- [仕様追加]つぶやきの返信(ログイン時のみ可) -->
			        <form action="comment" method="post">
			            返信<br />
			            <textarea name="ctext" cols="100" rows="5" class="tweet-box"></textarea>
			            <br />
			            <input type="hidden" name="cmessageId" value="${message.id}">
			            <input type="submit" value="返信">
			        </form>
					<br />
				</c:if>
		    </c:forEach>
		</div>

            <div class="copyright"> Copyright(c)Fukushima.K</div>
        </div>
    </body>
</html>