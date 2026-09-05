<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.YearMonth" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.List" %>
<%@ page import="com.sunrisedental.model.Appointment" %>
<%@ page import="com.sunrisedental.model.Bill" %>
<%@ page import="com.sunrisedental.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
User user = (User) session.getAttribute("loggedUser");
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
List<Bill> recentBills = (List<Bill>) request.getAttribute("recentBills");
YearMonth month = YearMonth.now();
LocalDate firstDay = month.atDay(1);
int startOffset = firstDay.getDayOfWeek().getValue() - 1;
int daysInMonth = month.lengthOfMonth();
DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Dashboard</title>
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
            <a class="active" href="${pageContext.request.contextPath}/staff/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/staff/appointment?action=add">+ New Appointment</a>
            <a href="${pageContext.request.contextPath}/staff/appointment">All Appointments</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div>
                <h1>Staff Dashboard</h1>
                <p>Welcome, <%= user.getFullName() %>. Manage today's clinic work.</p>
            </div>
            <div class="topbar-user">Staff</div>
        </header>

        <section class="stats-grid">
            <div class="stat-card">
                <span>Total Appointments</span>
                <strong>${totalAppointments}</strong>
            </div>
            <div class="stat-card">
                <span>Booked</span>
                <strong>${bookedAppointments}</strong>
            </div>
            <div class="stat-card">
                <span>Completed</span>
                <strong>${completedAppointments}</strong>
            </div>
            <div class="stat-card">
                <span>Available Dentists</span>
                <strong>${activeDentists}</strong>
            </div>
            <div class="stat-card">
                <span>Today's Appointments</span>
                <strong>${todayAppointments}</strong>
            </div>
        </section>

        <section class="card">
            <h2>Quick Actions</h2>
            <div class="action-grid">
                <a class="action-card" href="${pageContext.request.contextPath}/staff/appointment?action=add">
                    <strong>New Appointment</strong>
                    <span>Register a patient appointment.</span>
                </a>
                <a class="action-card" href="${pageContext.request.contextPath}/staff/appointment">
                    <strong>Manage Appointments</strong>
                    <span>Edit, cancel, complete treatment and view bills.</span>
                </a>
            </div>
        </section>

        <section class="card">
            <div class="section-heading">
                <div>
                    <h2>Appointment Calendar</h2>
                    <p class="muted"><%= month.getMonth() %> <%= month.getYear() %></p>
                </div>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/appointment?action=add">+ New Appointment</a>
            </div>

            <div class="calendar">
                <div class="calendar-header">Mon</div>
                <div class="calendar-header">Tue</div>
                <div class="calendar-header">Wed</div>
                <div class="calendar-header">Thu</div>
                <div class="calendar-header">Fri</div>
                <div class="calendar-header">Sat</div>
                <div class="calendar-header">Sun</div>

                <% for (int i = 0; i < startOffset; i++) { %>
                <div class="calendar-day empty"></div>
                <% } %>

                <% for (int day = 1; day <= daysInMonth; day++) {
                    LocalDate date = month.atDay(day);
                    String dateValue = date.format(dateFormatter);
                %>
                <div class="calendar-day <%= date.equals(LocalDate.now()) ? "today" : "" %>">
                    <div class="calendar-date"><%= day %></div>
                    <% for (Appointment appointment : appointments) {
                        if (dateValue.equals(appointment.getAppointmentDate())) { %>
                        <a class="calendar-event" href="${pageContext.request.contextPath}/staff/appointment?action=edit&id=<%= appointment.getId() %>">
                            <strong><%= appointment.getAppointmentTime() %></strong>
                            <span><%= appointment.getPatientName() %></span>
                        </a>
                    <% }} %>
                </div>
                <% } %>
            </div>
        </section>

        <section class="card">
            <div class="section-heading">
                <div>
                    <h2>Recent Bills</h2>
                    <p class="muted">Generated bills are available here after treatment confirmation.</p>
                </div>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Bill</th>
                        <th>Patient</th>
                        <th>Treatment</th>
                        <th>Consultation</th>
                        <th>Total</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Bill bill : recentBills) { %>
                    <tr>
                        <td><strong><%= bill.getBillNumber() %></strong></td>
                        <td><%= bill.getPatientName() %></td>
                        <td><%= bill.getTreatmentName() == null ? "-" : bill.getTreatmentName() %></td>
                        <td>Rs. <%= String.format("%.2f", bill.getConsultationFee()) %></td>
                        <td><strong>Rs. <%= String.format("%.2f", bill.getTotalAmount()) %></strong></td>
                        <td>
                            <a class="btn btn-small btn-primary" href="${pageContext.request.contextPath}/staff/bill?appointmentId=<%= bill.getAppointmentId() %>">
                                View / Print
                            </a>
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
