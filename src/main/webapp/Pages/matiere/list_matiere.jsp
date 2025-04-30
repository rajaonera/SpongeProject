<%@ page import="matiere.Matiere" %>
<%@ page import="matiere.Unite" %><%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 18/11/2024
  Time: 17:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  Matiere[] matieres =(Matiere[]) request.getAttribute("matieres");
  Unite[] unites =(Unite[]) request.getAttribute("unites");
%>
<html>
<head>
  <title>Formulaire d'Insertion de Matière Première</title>
  <style>
    body {
      background-color: #2e2e2e; /* Fond sombre */
      color: #f5f5f5; /* Texte clair */
      font-family: Arial, sans-serif;
      padding: 20px;
      display: flex;
      justify-content: space-between;
      gap: 40px;
    }

    h2 {
      color: #b97fe0; /* Titre violet */
      text-align: center;
      width: 100%;
    }

    .form-container {
      background-color: #4a2c66; /* Fond violet clair */
      padding: 20px;
      border-radius: 8px;
      width: 45%;
    }

    .list-container {
      background-color: #4a2c66; /* Fond violet clair */
      padding: 20px;
      border-radius: 8px;
      width: 45%;
      max-height: 300px;
      overflow-y: auto;
    }

    .form-group {
      margin-bottom: 15px;
    }

    .form-group label {
      display: block;
      font-weight: bold;
      margin-bottom: 5px;
    }

    .form-group input, .form-group select {
      width: 100%;
      padding: 8px;
      border: 1px solid #5e3c5c; /* Bordure violet foncé */
      border-radius: 5px;
      background-color: #3d263d; /* Fond sombre pour les champs */
      color: #fff;
    }

    .form-group input:focus, .form-group select:focus {
      border-color: #b97fe0; /* Bordure violette au focus */
      outline: none;
    }

    .form-group button {
      background-color: #b97fe0; /* Violet */
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      width: 100%;
    }

    .form-group button:hover {
      background-color: #a06ac2; /* Fond violet foncé au survol */
    }

    .list-group {
      list-style-type: none;
      padding: 0;
    }

    .list-group li {
      background-color: #3d263d;
      margin: 10px 0;
      padding: 10px;
      border-radius: 5px;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .list-group li span {
      font-size: 14px;
    }

  </style>
</head>
<body>

<div class="form-container">
  <h2>Formulaire d'Insertion de Matière Première</h2>
  <form id="matiereForm" action="insert.MatiereController">
    <!-- Nom matière -->
    <div class="form-group">
      <label for="nomMatiere">Nom de la Matière :</label>
      <input type="text" id="nomMatiere" name="nomMatiere" required placeholder="Entrez le nom de la matière">
    </div>

    <!-- Unité de mesure -->
    <div class="form-group">
      <label for="uniteMesure">Unité de Mesure :</label>
      <select id="uniteMesure" name="uniteMesure" required>
        <option value="">Sélectionnez une unité</option>
        <% for (int i = 0; i <unites.length ; i++) {
      %>
        <option value="<%=unites[i].getId()%>"><%=unites[i].getNom()%></option>
        <%
          }
        %>

      </select>
    </div>

    <!-- Bouton de soumission -->
    <div class="form-group">
      <button type="submit">Soumettre</button>
    </div>
  </form>
</div>

<div class="list-container">
  <h2>Liste des Matières Premières</h2>
  <ul class="list-group" id="matiereList">
    <% for (int i = 0; i <matieres.length ; i++) {
    %>
    <span>
      ID : <%=matieres[i].getId()%> -
      Nom: <%=matieres[i].getNom()%> -
      Unite: <%=matieres[i].getUnite().getNom()%> -
      <div class="form-group">
      <a href="stock.MatiereController?idMatiere=<%=matieres[i].getId()%>"> <button type="submit">Etat de stock </button> </a>
    </div>
    </span>
    <br>
    <%
      }
    %>

  </ul>
</div>

</body>
</html>
