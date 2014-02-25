<%-- 
    Document   : ispisAktivnihZipKodova
    Created on : Jun 5, 2013, 9:13:40 PM
    Author     : jelvalcic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.servletContext.contextPath}/css/displaytag.css" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <sql:setDataSource
            var="NWTiS"
            driver="${applicationScope.BP_Konfig.driver_database}"
            url="${applicationScope.BP_Konfig.server_database}${applicationScope.BP_Konfig.user_database}"
            user="${applicationScope.BP_Konfig.user_username}"
            password="${applicationScope.BP_Konfig.user_password}"
            />

        <sql:transaction dataSource="${NWTiS}">

            <sql:query var="ispis">
                SELECT jelvalcic_activezipcodes.ZIP, CITY FROM jelvalcic_activezipcodes INNER JOIN zip_codes ON zip_codes.ZIP = jelvalcic_activezipcodes.ZIP;
            </sql:query>

            <display:table name="${ispis.rows}" pagesize="7">
                <display:column property="ZIP"/>
                <display:column property="CITY"/>
            </display:table>

        </sql:transaction>
    </body>
</html>
