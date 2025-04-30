<%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 09/11/2024
  Time: 16:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>verification</title>
</head>
<body>
<%@ page import="block.Block" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% HttpSession session1 = request.getSession(false);
  Block block = (Block) session1.getAttribute("block");

%>

<html>
<head>
  <%
    if (request.getAttribute("message") != null) {
      String message = (String) request.getAttribute("message");
  %>
    <script>alert("<%=message%>")</script>
  <%
  }
  %>
  <title>Transformation </title>
  <%--    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/listeEmp.css">--%>
  <style>
    body {
      background-color: #121212; /* Couleur de fond sombre */
      color: #ffffff; /* Couleur du texte */
      font-family: Arial, sans-serif;
    }
    a {
      color: #bb86fc; /* Couleur des liens violets */
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }
    .body {
      max-width: 800px;
      margin: auto;
      padding: 20px;
      background-color: #1e1e1e; /* Couleur de fond de la section */
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);
    }
    form {
      margin-bottom: 20px;
    }
    label {
      display: block;
      margin-bottom: 5px;
    }
    input[type="text"], input[type="email"], input[type="date"] {
      width: 100%;
      padding: 10px;
      border: 1px solid #333;
      border-radius: 4px;
      background-color: #2a2a2a; /* Couleur de fond des champs */
      color: #ffffff;
    }
    select {
      width: 100%; /* Largeur complète */
      padding: 10px; /* Espacement interne */
      margin-bottom: 15px; /* Espacement entre les champs */
      border: 1px solid #ccc; /* Bordure */
      border-radius: 5px; /* Coins arrondis */
    }
    button {
      background-color: #6200ea; /* Couleur du bouton violet */
      color: white;
      padding: 10px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    button:hover {
      background-color: #3700b3; /* Couleur du bouton violet au survol */
    }
    table {
      width: 100%;,
    border-collapse: collapse;
      margin-top: 20px;
    }
    th, td {
      padding: 10px;
      border: 1px solid #333;
      text-align: left;
    }
    th {
      background-color: #2a2a2a; /* Couleur de fond des en-têtes */
    }
    tr:nth-child(even) {
      background-color: #222; /* Couleur de fond des lignes paires */
    }
  </style>
</head>
<body>
<a class="back-link" href="<%=request.getContextPath()%>/AffList.StockController?idBlock=<%= block.getId() %>">Retour</a>
<section class="body">
  <form action="<%=request.getContextPath()%>/verification.StockController" method="post">
    <h2>Verification : block <%=block.getId()%> -- <%=block.getVolume() %> cm3 </h2>
    <div>
      <label>longueur * largeur * hauteur :</label>
      <input type="text" name="longueur" required>
      <input type="text" name="largeur" required>
      <input type="text" name="hauteur" required>
    </div>
    <div>
      <label>marge :</label>
      <input type="number" name="marge" step="0.01" required>
    </div>
    <div>
      <label>Date d'ajout :</label>
      <input type="date" name="date_ajout" required>
    </div>

    <button type="submit">Valider</button>
  </form>
</section>

</body>

</html>

</body>
</html>
