<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:pageTemplate pageTitle="Users">
    <h1>Users</h1>

    <%-- Task: Hide the invoicing button if the user is not in the INVOICING group --%>
    <c:if test="${pageContext.request.isUserInRole('INVOICING')}">
        <div class="mb-4">
            <form method="POST" action="${pageContext.request.contextPath}/Users">
                <button type="submit" class="btn btn-primary">Generate Invoices</button>
            </form>
        </div>
    </c:if>

    <div class="container text-center">
        <c:forEach var="user" items="${users}">
            <div class="row border-bottom py-2">
                <div class="col">
                        ${user.username}
                </div>
                <div class="col">
                        ${user.email}
                </div>

                    <%-- Task: Only display invoicing data if the user has the group --%>
                <c:if test="${pageContext.request.isUserInRole('INVOICING')}">
                    <div class="col">
                        <c:forEach var="invName" items="${invoicingNames}">
                            <c:if test="${invName == user.username}">
                                <span class="badge bg-info text-dark">Invoicing Active</span>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>
</t:pageTemplate>