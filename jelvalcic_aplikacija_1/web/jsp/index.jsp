<%-- 
    Document   : index
    Created on : Jun 3, 2013, 10:26:21 PM
    Author     : jelvalcic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Početna stranica</title>
    </head>
    <body>
        <h1>Početna stranica</h1>
        
        <a href="${pageContext.servletContext.contextPath}/Kontroler">Kontroler</a><br>
        <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">Prijava korisnika</a><br>
        <a href="${pageContext.servletContext.contextPath}/OdjavaKorisnika">Odjava korisnika</a><br>
        <a href="${pageContext.servletContext.contextPath}/IspisAktivnihZipKodova">IspisAktivnihZipKodova</a><br>
        <a href="${pageContext.servletContext.contextPath}/IspisMeteoPodataka">IspisMeteoPodataka</a><br>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnika">PregledDnevnikaServerZahtjeva</a><br>
        <a href="${pageContext.servletContext.contextPath}/PregledDnevnikaKorisnickihZahtjeva">PregledDnevnikaKorisnickihZahtjeva</a><br>
        <a href="${pageContext.servletContext.contextPath}/jsp/dokumentacija.html">Projektna dokumentacija</a>
    </body>
</html>
