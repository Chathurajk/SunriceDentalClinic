<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
List<Map<String, Object>> staffList = (List<Map<String, Object>>) request.getAttribute("staffList");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Staff Management</title>
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
            <a class="active" href="${pageContext.request.contextPath}/admin/staff">Staff Management</a>
            <a href="${pageContext.request.contextPath}/admin/dentists">Dentists</a>
            <a href="${pageContext.request.contextPath}/admin/treatments">Treatments & Charges</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="topbar">
            <div>
                <h1>Staff Management</h1>
                <p>Add new staff accounts and deactivate existing staff.</p>
            </div>
        </header>

        <% if (request.getAttribute("error") != null) { %>
        <div class="alert alert-error"><%= request.getAttribute("error") %></div>
        <% } %>

        <section class="card">
            <h2>Add Staff</h2>
            <form action="${pageContext.request.contextPath}/admin/staff" method="post" class="form-grid">
                <input type="hidden" name="action" value="add">

                <div class="form-group">
                    <label>Full Name</label>
                    <input name="fullName" required>
                </div>

                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" required>
                </div>

                <div class="form-group">
                    <label>Contact Number</label>
                    <input name="contactNumber" required>
                </div>

                <div class="form-group">
                    <label>Password</label>
                    <input type="password" name="password" minlength="6" required>
                </div>

                <div class="form-actions full">
                    <button class="btn btn-primary" type="submit">Add Staff</button>
                </div>
            </form>
        </section>

        <section class="card">
            <h2>Staff Accounts</h2>
            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Contact</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <% for (Map<String, Object> staff : staffList) { %>
                    <tr>
                        <td><%= staff.get("name") %></td>
                        <td><%= staff.get("email") %></td>
                        <td><%= staff.get("contact") %></td>
                        <td><%= Boolean.TRUE.equals(staff.get("active")) ? "ACTIVE" : "INACTIVE" %></td>
                        <td>
                            <% if (Boolean.TRUE.equals(staff.get("active"))) { %>
                            <form class="inline-form"
                                  action="${pageContext.request.contextPath}/admin/staff"
                                  method="post">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="<%= staff.get("id") %>">
                                <button class="btn btn-small btn-danger" type="submit">Deactivate</button>
                            </form>
                            <% } else { %>
                            <span class="muted">Deactivated</span>
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
