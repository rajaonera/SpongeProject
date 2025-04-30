<%@ page import="machine.Resultat" %>
<%@ page import="annexe.Utils" %><%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 18/11/2024
  Time: 17:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
  Resultat[] resultats = (Resultat[]) request.getAttribute("resultats");
%>
<html>
<title>Dashboard Machine</title>
<style>
  body {
    background-color: #2e2e2e; /* Fond sombre */
    color: #f5f5f5; /* Texte clair */
    font-family: Arial, sans-serif;
    margin: 0;
    padding: 0;
  }

  /* Navbar style */
  nav {
    background-color: #4a2c66; /* Violet clair */
    padding: 10px;
    text-align: center;
  }

  nav a {
    color: #fff;
    text-decoration: none;
    padding: 10px 20px;
    margin: 0 10px;
    border-radius: 5px;
  }

  nav a:hover {
    background-color: #3d263d; /* Sombre sur survol */
  }

  nav select {
    padding: 5px;
    background-color: #3d263d;
    color: white;
    border-radius: 5px;
    border: none;
    font-size: 16px;
  }

  .dashboard {
    background-color: #4a2c66; /* Fond violet clair */
    color: #fff; /* Texte blanc */
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    font-size: 18px;
  }

  .dashboard .item {
    margin-right: 20px;
  }

  table {
    width: 100%;
    border-collapse: collapse;
    border-radius: 8px;
  }

  th, td {
    padding: 12px;
    text-align: center;
    border: 1px solid #5e3c5c; /* Bordure violet foncé */
  }

  th {
    background-color: #4a2c66; /* Fond violet clair pour les en-têtes */
    color: #ffffff; /* Texte blanc pour l'en-tête */
  }

  tr:nth-child(even) {
    background-color: #3d263d; /* Fond sombre pour les lignes paires */
  }

  tr:nth-child(odd) {
    background-color: #4a2c66; /* Fond violet clair pour les lignes impaires */
  }

  td {
    background-color: #3d263d; /* Fond sombre pour les cellules */
  }

  td, th {
    border-radius: 6px;
  }

  h2 {
    color: #b97fe0; /* Titre violet */
    text-align: center;
  }

</style>
</head>
<body>
<!-- Navbar -->
<nav>
  <a href="<%=request.getContextPath()%>/index.jsp">Accueil</a>
  <a href="<%=request.getContextPath()%>/affList.MatiereController">Matieres Premieres</a>
  <a href="<%=request.getContextPath()%>/affList.UniteController">Unites</a>
  <a href="<%=request.getContextPath()%>/affList.FormuleController">Formules </a>
  <a href="<%=request.getContextPath()%>/affList.MachineController">Machines </a>
  <a href="<%=request.getContextPath()%>/affList.MachineController">Generate </a>

  <form action="<%=request.getContextPath()%>/affListWithAnnee.ResultatController" method="post">
    <select id="machineSelect" name="annee" >

      <option value="">Choisir une annee ...</option>
      <option value="0">Tous</option>
    <option value="2022">2022</option>
    <option value="2023">2023</option>
    <option value="2024">2024</option>
  </select>
    <input type="submit" value="rechercher">
  </form>

</nav>

<h2>Tableau des Montants </h2>
<H2>ETU002477_Hugues</H2>

<table>
  <thead>
  <tr>
    <th>Machine</th>
    <th>nombre de blocks</th>
    <th>Volume total</th>
    <th>Montant Théorique</th>
    <th>Montant Pratique</th>
    <th>Ecart</th>
  </tr>
  </thead>
  <tbody id="tableData">
  <%
    for (Resultat resultat: resultats){
  %>

  <tr>
    <td>Machine <%=resultat.getId()%></td>
    <td><%=resultat.getCount() %> blocs</td>
    <td><%=Utils.write(resultat.getTotalvolume()) %></td>
    <td><%=Utils.write(resultat.getTotalTheorique()) %></td>
    <td><%=Utils.write(resultat.getTotalMachiniste()) %></td>
    <td><%=Utils.write(resultat.getEacart())%></td>
  </tr>
  <%
    }
  %>

  </tbody>
</table>

</body>
</html>
