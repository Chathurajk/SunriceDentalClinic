<%@ page import="com.sunrisedental.model.Appointment" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
Appointment a=(Appointment)request.getAttribute("appointment");%>
<!DOCTYPE html>
<html>
    <head>
        <title>Search Appointment</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <div class="app-layout">
            <aside class="sidebar">
                <div class="sidebar-brand">
                    <div class="brand-icon">✚</div>
                    <div>
                        <h2>Sunrise Dental</h2>
                        <span>Staff Panel</span>
                    </div>
                </div>
                <nav class="sidebar-menu">
                    <a href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a>
                    <a href="${pageContext.request.contextPath}/staff/appointment">Appointments</a>
                    <a class="active" href="${pageContext.request.contextPath}/staff/appointment?action=search">Search</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </aside>
            <main class="main-content">
                <header class="topbar">
                    <div>
                        <h1>Search Appointment</h1>
                        <p>Find by appointment number</p>
                    </div>
                </header>
                <section class="card">
                    <form action="${pageContext.request.contextPath}/staff/appointment" method="get" class="search-form">
                        <input type="hidden" name="action" value="search">
                        <input name="number" placeholder="APP-..." required>
                        <button class="btn btn-primary">Search</button>
                    </form>
                </section>
                <%
                if(a!=null) {%>
                <section class="card">
                    <div class="detail-grid">
                        <div>
                            <span>Appointment</span>
                            <strong>
                            <%=a.getAppointmentNumber()%>
                        </strong>
                    </div>
                    <div>
                        <span>Patient</span>
                        <strong>
                        <%=a.getPatientName()%>
                    </strong>
                </div>
                <div>
                    <span>Email</span>
                    <strong>
                    <%=a.getPatientEmail()%>
                </strong>
            </div>
            <div>
                <span>Dentist</span>
                <strong>
                <%=a.getDentistName()%>
            </strong>
        </div>
        <div>
            <span>Date</span>
            <strong>
            <%=a.getAppointmentDate()%>
        </strong>
    </div>
    <div>
        <span>Time</span>
        <strong>
        <%=a.getAppointmentTime()%>
    </strong>
</div>
<div>
    <span>Status</span>
    <strong>
    <%=a.getStatus()%>
</strong>
</div>
</div>
</section>
<%
}%>
</main>
</div>
</body>
</html>
