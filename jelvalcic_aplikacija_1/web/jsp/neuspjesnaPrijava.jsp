<%-- 
    Document   : neuspjesnaPrijava
    Created on : Jun 6, 2013, 9:39:08 PM
    Author     : jelvalcic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Neuspješna prijava</title>
    </head>
    <body>
        <h1>Došlo je do greške prilikom prijave, molim pokušati ponovo!</h1>
        <p>${requestScope['javax.servlet.error.message']}</p></br>
        
         <a href="${pageContext.servletContext.contextPath}/PrijavaKorisnika">Prijava korisnika</a><br>
    </body>
</html>
