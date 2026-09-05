<%@ page import="java.util.List" %>
<%@ page import="com.sunrisedental.model.Appointment" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Appointments</title>
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
            <a href="${pageContext.request.contextPath}/staff/appointment?action=add">+ New Appointment</a>
            <a class="active" href="${pageContext.request.contextPath}/staff/appointment">All Appointments</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div>
                <h1>Appointments</h1>
                <p>Manage clinic appointments and completed treatment bills.</p>
            </div>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/appointment?action=add">+ New Appointment</a>
        </header>



        <section class="card">
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Number</th>
                        <th>Patient</th>
                        <th>Dentist</th>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Appointment appointment : appointments) { %>
                    <tr>
                        <td><%= appointment.getAppointmentNumber() %></td>
                        <td>
                            <%= appointment.getPatientName() %>
                            <small><%= appointment.getPatientEmail() %></small>
                        </td>
                        <td><%= appointment.getDentistName() %></td>
                        <td><%= appointment.getAppointmentDate() %></td>
                        <td><%= appointment.getAppointmentTime() %></td>
                        <td>
                            <span class="status status-<%= appointment.getStatus().toLowerCase() %>">
                                <%= appointment.getStatus() %>
                            </span>
                        </td>
                        <td>
                            <% if (!"CANCELLED".equals(appointment.getStatus())) { %>
                            <a class="btn btn-small btn-secondary"
                               href="${pageContext.request.contextPath}/staff/appointment?action=edit&id=<%= appointment.getId() %>">
                                Edit
                            </a>

                            <% if (!"COMPLETED".equals(appointment.getStatus())) { %>
                            <a class="btn btn-small btn-secondary"
                               href="${pageContext.request.contextPath}/staff/confirm-treatment?id=<%= appointment.getId() %>">
                                Treatment
                            </a>
                            <% } else { %>
                            <a class="btn btn-small btn-primary"
                               href="${pageContext.request.contextPath}/staff/bill?appointmentId=<%= appointment.getId() %>">
                                View Bill
                            </a>
                            <% } %>

                            <form class="inline-form"
                                  action="${pageContext.request.contextPath}/staff/appointment"
                                  method="post">
                                <input type="hidden" name="action" value="cancel">
                                <input type="hidden" name="id" value="<%= appointment.getId() %>">
                                <button class="btn btn-small btn-danger" type="submit">Cancel</button>
                            </form>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>
    </main>
</div>
</body>
</html>
