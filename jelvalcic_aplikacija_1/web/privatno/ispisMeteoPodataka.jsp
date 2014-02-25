<%-- 
    Document   : ispisMeteoPodataka
    Created on : Jun 5, 2013, 11:23:48 PM
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
        <title>Pregled meteo podataka</title>
        <link href="${pageContext.servletContext.contextPath}/css/displaytag.css" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <form method="POST" action="${pageContext.servletContext.contextPath}/privatno/ispisMeteoPodataka.jsp">
            Zip: <input type="text" name="zip" value="${param.zip}"/><br/>
            Od:  <input type="text" name="interval_od" value="${param.interval_od}"/><br/>
            Do:  <input type="text" name="interval_do" value="${param.interval_do}"/><br/>
            Broj zapisa po stranici: 
            <input type="radio" checked name="brojZapisa" value="5"/>5
            <input type="radio" name="brojZapisa" value="10"/>10
            <input type="radio" name="brojZapisa" value="20"/>20
            <input type="radio" name="brojZapisa" value="50"/>50
            <input type="radio" name="brojZapisa" value=""/>Svi<br/>
            <input type="submit" value=" Filtriraj "/><br/>
        </form>

        <sql:setDataSource
            var="bp"
            driver="${applicationScope.BP_Konfig.driver_database}"
            url="${applicationScope.BP_Konfig.server_database}${applicationScope.BP_Konfig.user_database}"
            user="${applicationScope.BP_Konfig.user_username}"
            password="${applicationScope.BP_Konfig.user_password}"
            />

        <sql:transaction dataSource="${bp}">

            <sql:query var="ispis">
                SELECT meteo_podaci.*, CITY FROM meteo_podaci INNER JOIN zip_codes ON meteo_podaci.ZIP_zahtjevani = zip_codes.ZIP


                <c:choose>
                    <c:when test="${!(empty param.zip)}">
                        WHERE zip_zahtjevani='${param.zip}'
                        <c:set var="where" value="true"/>
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${!(empty param.interval_od) && (empty where)}">
                        WHERE datum_i_vrijeme>=STR_TO_DATE('${param.interval_od}','%d.%m.%Y %k.%i.%s')
                        <c:set var="where" value="true"/>
                    </c:when>
                    <c:when test="${!(empty param.interval_od)}">
                        AND datum_i_vrijeme>=STR_TO_DATE('${param.interval_od}','d.%m.%Y %k.%i.%s')
                    </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${!(empty param.interval_do) && (empty where)}">
                        WHERE datum_i_vrijeme<=STR_TO_DATE('${param.interval_do}','d.%m.%Y %k.%i.%s')
                        <c:set var="where" value="true"/>
                    </c:when>
                    <c:when test="${!(empty param.interval_do)}">
                        AND datum_i_vrijeme<=STR_TO_DATE('${param.interval_do}','d.%m.%Y %k.%i.%s')
                    </c:when>
                </c:choose>
            </sql:query>
        </sql:transaction>

        <display:table name="${ispis.rows}" pagesize="${param.brojZapisa}">
            <display:column sortable="true" headerClass="sortable" property="ZIP_zahtjevani"/>
            <display:column sortable="true" headerClass="sortable" property="CITY"/>
            <display:column sortable="true" headerClass="sortable" property="ZIP_vraceni"/>
            <display:column sortable="true" headerClass="sortable" property="Tlak"/>
            <display:column sortable="true" headerClass="sortable" property="Vlaga"/>
            <display:column sortable="true" headerClass="sortable" property="Temperatura"/>
            <display:column sortable="true" headerClass="sortable" property="Vjetar"/>
            <display:column sortable="true" headerClass="sortable" property="Smjer_vjetra"/>
            <display:column sortable="true" headerClass="sortable" property="Datum_i_vrijeme"/>
        </display:table>

    </body>
</html>

