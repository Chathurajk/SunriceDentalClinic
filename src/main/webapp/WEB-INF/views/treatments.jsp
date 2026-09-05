<%@ page import="java.util.List" %>
<%@ page import="com.sunrisedental.model.Treatment" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
List<Treatment> treatments = (List<Treatment>) request.getAttribute("treatments");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Treatments & Charges</title>
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
            <a href="${pageContext.request.contextPath}/admin/dashboard">Dashboard</a>
            <a href="${pageContext.request.contextPath}/staff/appointment">Appointments</a>
            <a href="${pageContext.request.contextPath}/admin/staff">Staff Management</a>
            <a href="${pageContext.request.contextPath}/admin/dentists">Dentists</a>
            <a class="active" href="${pageContext.request.contextPath}/admin/treatments">Treatments & Charges</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div>
                <h1>Treatments & Charges</h1>
                <p>Add treatments and update their charges.</p>
            </div>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <section class="card">
            <h2>Add Treatment</h2>
            <form action="${pageContext.request.contextPath}/admin/treatments" method="post" class="form-grid">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label>Name</label>
                    <input name="name" required>
                </div>

                <div class="form-group">
                    <label>Charge</label>
                    <input type="number" name="charge" min="0" step="0.01" required>
                </div>

                <div class="form-group full">
                    <label>Description</label>
                    <textarea name="description" rows="3"></textarea>
                </div>

                <div class="form-actions full">
                    <button class="btn btn-primary" type="submit">Add Treatment</button>
                </div>
            </form>
        </section>

        <section class="card">
            <h2>Existing Treatments</h2>
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Description</th>
                        <th>Charge</th>
                        <th>Status</th>
                        <th>Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Treatment treatment : treatments) { %>
                    <tr>
                        <td><strong><%= treatment.getName() %></strong></td>
                        <td><%= treatment.getDescription() == null ? "" : treatment.getDescription() %></td>
                        <td>Rs. <%= String.format("%.2f", treatment.getCharge()) %></td>
                        <td><%= treatment.isActive() ? "ACTIVE" : "INACTIVE" %></td>
                        <td>
                            <% if (treatment.isActive()) { %>
                            <form class="edit-treatment-form"
                                  action="${pageContext.request.contextPath}/admin/treatments"
                                  method="post">
                                <input type="hidden" name="action" value="edit">
                                <input type="hidden" name="id" value="<%= treatment.getId() %>">
                                <input name="name" value="<%= treatment.getName() %>" required>
                                <input name="description" value="<%= treatment.getDescription() == null ? "" : treatment.getDescription() %>">
                                <input type="number" name="charge" value="<%= treatment.getCharge() %>" min="0" step="0.01" required>
                                <button class="btn btn-small btn-primary" type="submit">Save</button>
                            </form>

                            <form class="inline-form" action="${pageContext.request.contextPath}/admin/treatments" method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= treatment.getId() %>">
                                <button class="btn btn-small btn-danger" type="submit">Deactivate</button>
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
