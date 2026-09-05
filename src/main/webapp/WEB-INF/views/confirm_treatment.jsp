<%@ page import="java.util.List" %>
<%@ page import="com.sunrisedental.model.Appointment" %>
<%@ page import="com.sunrisedental.model.Treatment" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
Appointment appointment = (Appointment) request.getAttribute("appointment");
List<Treatment> treatments = (List<Treatment>) request.getAttribute("treatments");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirm Treatment</title>
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
                <h1>Confirm Actual Treatment</h1>
                <p>Select the treatment performed by the dentist and enter the consultation fee specified by the doctor.</p>
            </div>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error">
            <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <section class="card">
            <div class="detail-grid">
                <div>
                    <span>Appointment</span>
                    <strong><%= appointment.getAppointmentNumber() %></strong>
                </div>
                <div>
                    <span>Patient</span>
                    <strong><%= appointment.getPatientName() %></strong>
                </div>
                <div>
                    <span>Requested Visit Reason</span>
                    <strong><%= appointment.getRequestedTreatmentName() == null ? "Not specified" : appointment.getRequestedTreatmentName() %></strong>
                </div>
            </div>
        </section>

        <section class="card">
            <h2>Actual Treatment & Billing</h2>
            <p class="muted">
                The treatment charge comes from the selected treatment. The consultation fee is entered according to the doctor's instruction.
            </p>

            <form action="${pageContext.request.contextPath}/staff/confirm-treatment" method="post">
                <input type="hidden" name="appointmentId" value="<%= appointment.getId() %>">

                <div class="form-grid">
                    <div class="form-group">
                        <label for="actualTreatmentId">Actual Treatment</label>
                        <select id="actualTreatmentId" name="actualTreatmentId" required>
                            <option value="">Select actual treatment</option>
                            <% for (Treatment treatment : treatments) { %>
                            <option value="<%= treatment.getId() %>" data-charge="<%= treatment.getCharge() %>">
                                <%= treatment.getName() %> - Rs. <%= String.format("%.2f", treatment.getCharge()) %>
                            </option>
                            <% } %>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="consultationFee">Consultation Fee (Doctor's Fee)</label>
                        <input
                                id="consultationFee"
                                name="consultationFee"
                                type="number"
                                min="0"
                                step="0.01"
                                placeholder="e.g. 2000.00"
                                required
                        >
                    </div>
                </div>

                <div class="calculation-box">
                    <div>
                        <span>Treatment Charge</span>
                        <strong id="treatmentCharge">Rs. 0.00</strong>
                    </div>
                    <div>
                        <span>Consultation Fee</span>
                        <strong id="displayConsultationFee">Rs. 0.00</strong>
                    </div>
                    <div class="calculation-total">
                        <span>Total Bill</span>
                        <strong id="totalAmount">Rs. 0.00</strong>
                    </div>
                </div>

                <div class="form-actions">
                    <button class="btn btn-primary" type="submit">Confirm Treatment & Generate Bill</button>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/staff/appointment">Cancel</a>
                </div>
            </form>
        </section>
    </main>
</div>

<script>
    const treatmentSelect = document.getElementById('actualTreatmentId');
    const consultationInput = document.getElementById('consultationFee');
    const treatmentCharge = document.getElementById('treatmentCharge');
    const displayConsultationFee = document.getElementById('displayConsultationFee');
    const totalAmount = document.getElementById('totalAmount');

    function updateTotal() {
        const selected = treatmentSelect.options[treatmentSelect.selectedIndex];
        const charge = selected && selected.value
            ? parseFloat(selected.dataset.charge)
            : 0;
        const consultation = parseFloat(consultationInput.value) || 0;
        const total = charge + consultation;

        treatmentCharge.textContent = 'Rs. ' + charge.toFixed(2);
        displayConsultationFee.textContent = 'Rs. ' + consultation.toFixed(2);
        totalAmount.textContent = 'Rs. ' + total.toFixed(2);
    }

    treatmentSelect.addEventListener('change', updateTotal);
    consultationInput.addEventListener('input', updateTotal);
</script>
</body>
</html>
