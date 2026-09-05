<%@ page import="java.util.List" %>
<%@ page import="com.sunrisedental.model.Appointment" %>
<%@ page import="com.sunrisedental.model.Bill" %>
<%@ page import="com.sunrisedental.model.DentistReport" %>
<%@ page import="com.sunrisedental.model.TreatmentReport" %>
<%@ page import="com.sunrisedental.model.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
User user = (User) session.getAttribute("loggedUser");
List<Appointment> appointments = (List<Appointment>) request.getAttribute("appointments");
List<Bill> recentBills = (List<Bill>) request.getAttribute("recentBills");
List<TreatmentReport> treatmentReports = (List<TreatmentReport>) request.getAttribute("treatmentReports");
List<DentistReport> dentistReports = (List<DentistReport>) request.getAttribute("dentistReports");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-layout">
    <aside class="sidebar">
        <div class="sidebar-brand">
            <div class="brand-icon">✚</div>
            <div>
                <h2>Sunrise Dental</h2>
                <span>Admin Panel</span>
            </div>
        </div>
        <nav class="sidebar-menu">
            <a class="active" href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/staff/appointment">Appointments</a>
            <a href="${pageContext.request.contextPath}/admin/staff">Staff Management</a>
            <a href="${pageContext.request.contextPath}/admin/dentists">Dentists</a>
            <a href="${pageContext.request.contextPath}/admin/treatments">Treatments & Charges</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div>
                <h1>Admin Dashboard</h1>
                <p>Welcome, <%= user.getFullName() %>. Clinic overview, billing and decision-support reports.</p>
            </div>
            <div class="topbar-user">Admin</div>
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
                <span>Cancelled</span>
                <strong>${cancelledAppointments}</strong>
            </div>
        </section>

        <section class="stats-grid">
            <div class="stat-card">
                <span>Total Revenue</span>
                <strong>Rs. <%= String.format("%.2f", (Double) request.getAttribute("totalRevenue")) %></strong>
            </div>
            <div class="stat-card">
                <span>Today's Revenue</span>
                <strong>Rs. <%= String.format("%.2f", (Double) request.getAttribute("todayRevenue")) %></strong>
            </div>
            <div class="stat-card">
                <span>Today's Appointments</span>
                <strong>${todayAppointments}</strong>
            </div>
            <div class="stat-card">
                <span>Active Dentists</span>
                <strong>${activeDentists}</strong>
            </div>
        </section>

        <section class="card">
            <div class="section-heading">
                <div>
                    <h2>Decision-Making Reports</h2>
                    <p class="muted">Use appointment, treatment and revenue information to support clinic decisions.</p>
                </div>
            </div>

            <div class="report-grid">
                <div class="card report-card">
                    <h3>Treatment Usage & Revenue</h3>
                    <div class="table-container">
                        <table>
                            <thead>
                            <tr>
                                <th>Treatment</th>
                                <th>Used</th>
                                <th>Revenue</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (TreatmentReport report : treatmentReports) { %>
                            <tr>
                                <td><%= report.getTreatmentName() %></td>
                                <td><%= report.getUsageCount() %></td>
                                <td>Rs. <%= String.format("%.2f", report.getRevenue()) %></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="card report-card">
                    <h3>Dentist Workload</h3>
                    <div class="table-container">
                        <table>
                            <thead>
                            <tr>
                                <th>Dentist</th>
                                <th>Appointments</th>
                                <th>Completed</th>
                            </tr>
                            </thead>
                            <tbody>
                            <% for (DentistReport report : dentistReports) { %>
                            <tr>
                                <td><%= report.getDentistName() %></td>
                                <td><%= report.getAppointmentCount() %></td>
                                <td><%= report.getCompletedCount() %></td>
                            </tr>
                            <% } %>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </section>

        <section class="card">
            <div class="section-heading">
                <div>
                    <h2>Recent Bills</h2>
                    <p class="muted">Bills generated after actual treatment confirmation.</p>
                </div>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Bill</th>
                        <th>Appointment</th>
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
                        <td><%= bill.getAppointmentNumber() %></td>
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

        <section class="card">
            <div class="section-heading">
                <div>
                    <h2>Appointment Details</h2>
                    <p class="muted">Patient, dentist, requested visit reason and current status.</p>
                </div>
                <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/appointment">View All</a>
            </div>

            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Appointment</th>
                        <th>Patient</th>
                        <th>Contact</th>
                        <th>Dentist</th>
                        <th>Requested Treatment</th>
                        <th>Date / Time</th>
                        <th>Status</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Appointment appointment : appointments) { %>
                    <tr>
                        <td><strong><%= appointment.getAppointmentNumber() %></strong></td>
                        <td>
                            <%= appointment.getPatientName() %>
                            <small><%= appointment.getPatientEmail() %></small>
                        </td>
                        <td><%= appointment.getContactNumber() %></td>
                        <td><%= appointment.getDentistName() %></td>
                        <td><%= appointment.getRequestedTreatmentName() == null ? "-" : appointment.getRequestedTreatmentName() %></td>
                        <td><%= appointment.getAppointmentDate() %><small><%= appointment.getAppointmentTime() %></small></td>
                        <td>
                            <span class="status status-<%= appointment.getStatus().toLowerCase() %>">
                                <%= appointment.getStatus() %>
                            </span>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
        </section>

        <section class="card">
            <h2>Administration</h2>
            <div class="action-grid">
                <a class="action-card" href="${pageContext.request.contextPath}/admin/staff">
                    <strong>Manage Staff</strong>
                    <span>Add staff or deactivate staff accounts.</span>
                </a>
                <a class="action-card" href="${pageContext.request.contextPath}/admin/treatments">
                    <strong>Treatments & Charges</strong>
                    <span>Add treatments, edit charges or deactivate treatments.</span>
                </a>
                <a class="action-card" href="${pageContext.request.contextPath}/admin/dentists">
                    <strong>Dentists</strong>
                    <span>Add dentists or deactivate dentists.</span>
                </a>
            </div>
        </section>
    </main>
</div>
</body>
</html>
