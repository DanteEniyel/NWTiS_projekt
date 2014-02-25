<%-- 
    Document   : pregledDnevnika
    Created on : Jun 7, 2013, 6:31:52 PM
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
        <title>Pregled dnevnika server zahtjeva</title>
        <link href="${pageContext.servletContext.contextPath}/css/displaytag.css" type="text/css" rel="stylesheet"/>
    </head>
    <body>
        <form method="POST" action="${pageContext.servletContext.contextPath}/admin/pregledDnevnika.jsp">
            Korisnicko ime: <input type="text" name="korIme" value="${param.korIme}"/><br/>
            Odgovor servera:  <input type="text" name="odgServera" value="${param.odgServera}"/><br/>
            Od datuma:  <input type="text" name="interval_od" value="${param.interval_od}"/><br/>
            Do datuma:  <input type="text" name="interval_do" value="${param.interval_do}"/><br/>
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
                SELECT dnevnik.* FROM dnevnik 


                <c:choose>
                    <c:when test="${!(empty param.korIme)}">
                        WHERE Korisnik ='${param.korIme}'
                        <c:set var="where" value="true"/>
                    </c:when>
                </c:choose>
                        <c:choose>
                    <c:when test="${!(empty param.odgServera) && (empty where)}">
                         WHERE SUBSTRING( Odgovor_servera, 4, 2 ) =  '${param.odgServera}'
                        <c:set var="where" value="true"/>
                    </c:when>
                    <c:when test="${!(empty param.odgServera)}">
                        AND SUBSTRING( Odgovor_servera, 4, 2 ) =  '${param.odgServera}'
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
                <display:column sortable="true" headerClass="sortable" property="Datum_i_vrijeme"/>
                <display:column sortable="true" headerClass="sortable" property="Korisnik"/>
                <display:column sortable="true" headerClass="sortable" property="Komanda"/>
                <display:column sortable="true" headerClass="sortable" property="Odgovor_servera"/>
                <display:column sortable="true" headerClass="sortable" property="Korisnicka_komanda"/>
                <display:column sortable="true" headerClass="sortable" property="Ispravna_komanda"/>
            </display:table>

    </body>
</html>