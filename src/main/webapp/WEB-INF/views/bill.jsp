<%@ page import="com.sunrisedental.model.Bill" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
Bill bill = (Bill) request.getAttribute("bill");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Generated Bill</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="app-layout">
    <aside class="sidebar no-print">
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
        <header class="topbar no-print">
            <div>
                <h1>Bill Generated</h1>
                <p>Treatment confirmed and bill calculated successfully.</p>
            </div>
            <button class="btn btn-primary" onclick="window.print()">Print Bill</button>
        </header>

        <section class="card bill-card" id="printableBill">
            <div class="bill-header">
                <div>
                    <h2>Sunrise Dental Clinic</h2>
                    <p>Patient Treatment Bill</p>
                </div>
                <div class="bill-number">
                    <span>Bill Number</span>
                    <strong><%= bill.getBillNumber() %></strong>
                </div>
            </div>

            <div class="detail-grid">
                <div>
                    <span>Patient</span>
                    <strong><%= bill.getPatientName() %></strong>
                </div>
                <div>
                    <span>Appointment</span>
                    <strong><%= bill.getAppointmentNumber() %></strong>
                </div>
                <div>
                    <span>Status</span>
                    <strong><%= bill.getStatus() %></strong>
                </div>
            </div>

            <table class="bill-table">
                <thead>
                <tr>
                    <th>Description</th>
                    <th>Amount</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td><%= bill.getTreatmentName() == null ? "Actual Treatment" : bill.getTreatmentName() %></td>
                    <td>Rs. <%= String.format("%.2f", bill.getTreatmentCharge()) %></td>
                </tr>
                <tr>
                    <td>Consultation Fee (Doctor)</td>
                    <td>Rs. <%= String.format("%.2f", bill.getConsultationFee()) %></td>
                </tr>
                <tr class="bill-total">
                    <td>Total Bill</td>
                    <td>Rs. <%= String.format("%.2f", bill.getTotalAmount()) %></td>
                </tr>
                </tbody>
            </table>

            <p class="bill-note">
                Total = Treatment Charge + Consultation Fee
            </p>
        </section>

        <div class="form-actions no-print">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/staff/dashboard">Go to Dashboard</a>
            <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/appointment">Back to Appointments</a>
        </div>
    </main>
</div>
</body>
</html>
