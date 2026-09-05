<%@ page import="java.util.*" %>
<%@ page import="com.sunrisedental.model.*" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
Appointment a=(Appointment)request.getAttribute("appointment");
List<Dentist> ds=(List<Dentist>)request.getAttribute("dentists");
List<Treatment> ts=(List<Treatment>)request.getAttribute("treatments");
boolean edit=a!=null;%>
<!DOCTYPE html>
<html>
    <head>
        <title>Appointment</title>
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
                    <a class="active" href="${pageContext.request.contextPath}/staff/appointment?action=add">+ New Appointment</a>
                    <a href="${pageContext.request.contextPath}/staff/appointment">All Appointments</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </nav>
            </aside>
            <main class="main-content">
                <header class="topbar">
                    <div>
                        <h1>
                        <%=edit?"Edit":"New"%> Appointment</h1>
                        <p>Patient and appointment details</p>
                    </div>
                </header>
                <%
                if(request.getAttribute("error")!=null) {%>
                <div class="alert alert-error">
                    <%=request.getAttribute("error")%>
                </div>
                <%
                }%>
                <section class="card">
                    <form action="${pageContext.request.contextPath}/staff/appointment" method="post" class="form-grid">
                        <%
                        if(edit) {%>
                        <input type="hidden" name="id" value="<%=a.getId()%>">
                        <%
                        }%>
                        <div class="form-group">
                            <label>Appointment Number</label>
                            <input name="appointmentNumber" value="<%=edit?a.getAppointmentNumber():"Auto generated"%>" readonly>
                        </div>
                        <div class="form-group">
                            <label>Patient Name</label>
                            <input name="patientName" value="<%=edit?a.getPatientName():""%>" required>
                        </div>
                        <div class="form-group">
                            <label>Patient Email</label>
                            <input type="email" name="patientEmail" value="<%=edit?a.getPatientEmail():""%>" required>
                        </div>
                        <div class="form-group">
                            <label>Contact Number</label>
                            <input name="contactNumber" value="<%=edit?a.getContactNumber():""%>" required>
                        </div>
                        <div class="form-group full">
                            <label>Address</label>
                            <input name="address" value="<%=edit?a.getAddress():""%>" required>
                        </div>
                        <div class="form-group">
                            <label>Dentist</label>
                            <select name="dentistId" required>
                            <option value="">Select Dentist</option>
                            <%
                            for(Dentist d:ds) {%>
                            <option value="<%=d.getId()%>" <%=edit&&a.getDentistId()==d.getId()?"selected":""%>>
                            <%=d.getName()%> - <%=d.getSpecialization()%>
                        </option>
                        <%
                        }%>
                    </select>
                </div>
                <div class="form-group">
                    <label>Requested Treatment / Visit Reason</label>
                    <select name="requestedTreatmentId" required>
                    <option value="">Select</option>
                    <%
                    for(Treatment t:ts) {%>
                    <option value="<%=t.getId()%>" <%=edit&&a.getRequestedTreatmentId()==t.getId()?"selected":""%>>
                    <%=t.getName()%>
                </option>
                <%
                }%>
            </select>
        </div>
        <div class="form-group">
            <label>Date</label>
            <input type="date" name="appointmentDate" min="<%=java.time.LocalDate.now()%>" value="<%=edit?a.getAppointmentDate():""%>" required>
        </div>
        <div class="form-group">
            <label>Time</label>
            <input type="time" name="appointmentTime" value="<%=edit?a.getAppointmentTime():""%>" required>
        </div>
        <div class="form-group full">
            <label>Notes</label>
            <textarea name="notes" rows="4">
            <%=edit?a.getNotes():""%>
        </textarea>
    </div>
    <div class="form-actions full">
        <button class="btn btn-primary">
        <%=edit?"Update":"Save"%> Appointment</button>
        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/appointment">Cancel</a>
    </div>
</form>
</section>
</main>
</div>
</body>
</html>
