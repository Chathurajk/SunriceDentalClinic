<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Sunrise Dental Clinic</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body class="login-body">
        <div class="login-container">
            <div class="login-header">
                <div class="brand-icon">✚</div>
                <h1>Sunrise Dental Clinic</h1>
                <p>Dental Management System</p>
            </div>
            <%
            if(request.getAttribute("error")!=null) {%>
            <div class="alert alert-error">
                <%=request.getAttribute("error")%>
            </div>
            <%
            }%>
            <form action="${pageContext.request.contextPath}/login" method="post" class="login-form">
                <label>Email Address</label>
                <input type="email" name="email" required>
                <label>Password</label>
                <input type="password" name="password" required>
                <button class="btn btn-primary full-width">Sign In</button>
            </form>
            <div class="login-footer">Sunrise Dental Clinic</div>
        </div>
    </body>
</html>
