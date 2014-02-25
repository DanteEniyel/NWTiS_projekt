<%-- 
    Document   : login
    Created on : Jun 6, 2013, 8:41:38 PM
    Author     : jelvalcic
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>NWTiS prijava</title>
    </head>
    <body>
        <form method="POST" action="${pageContext.servletContext.contextPath}/ProvjeraKorisnika">
            Korisničko ime: <input name="kor_ime" maxlength="20"><br>
            Lozinka: <input name="lozinka" type="password" maxlength="15"><br>
            <input type="submit" value=" Prijavi se ">
                
        </form>
    </body>
</html>
