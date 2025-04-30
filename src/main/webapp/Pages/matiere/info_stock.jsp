<%@ page import="matiere.Matiere" %>
<%@ page import="matiere.Stock" %><%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 18/11/2024
  Time: 22:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Matiere matiere =  (Matiere) request.getAttribute("matiere");
    Stock[] stock  = matiere.getStock();

%>
<html>
<head>
    <title>Formulaire d'Insertion de Stock de <%=matiere.getNom()%></title>
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

        .form-container, .stock-state-container {
            background-color: #4a2c66; /* Fond violet clair */
            padding: 20px;
            border-radius: 8px;
            width: 45%;
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

        /* Table style for stock state */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table, th, td {
            border: 1px solid #5e3c5c;
        }

        th, td {
            padding: 10px;
            text-align: center;
        }

        th {
            background-color: #4a2c66;
            color: #fff;
        }

        td {
            background-color: #3d263d;
            color: #fff;
        }

    </style>
</head>
<body>

<div class="form-container">
    <h2>Formulaire d'Insertion de <%=matiere.getNom()%></h2>
    <form id="stockForm" action="insertStock.MatiereController" method="post">
        <!-- Prix unitaire -->
        <input type="hidden"  name="idMatiere" required value="<%=matiere.getId()%>" >
        <div class="form-group">
            <label for="prixUnitaire">Prix Unitaire (Ar) :</label>
            <input type="number" id="prixUnitaire" name="prixUnitaire" required placeholder="Entrez le prix unitaire" min="0" step="0.01">
        </div>

        <!-- Quantité d'entrée -->
        <div class="form-group">
            <label for="quantiteEntree">Quantité d'Entrée :</label>
            <input type="number" id="quantiteEntree" name="quantite" required placeholder="Entrez la quantité en <%=matiere.getUnite().getNom()%>" min="0" step="0.01">
        </div>

        <!-- Date d'ajout -->
        <div class="form-group">
            <label for="dateAjout">Date d'Ajout :</label>
            <input type="datetime-local" id="dateAjout" name="date_ajout" step="1" required>
        </div>

        <!-- Bouton de soumission -->
        <div class="form-group">
            <button type="submit">Soumettre</button>
        </div>
    </form>
</div>

<div class="stock-state-container">
    <h2>État du Stock</h2>
    <table id="stockStateTable">
        <thead>
        <tr>
            <th>ID</th>
            <th>quantite (<%=matiere.getUnite().getNom()%>)</th>
            <th>Type </th>
            <th>prix d'achat </th>
            <th>Date d' ajout</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (int i = 0; i < stock.length; i++) {
        %>
        <tr>
        <td><%=stock[i].getId()%></td>
        <td><%=stock[i].getQuantity()%> <%=matiere.getUnite().getNom()%></td>
        <td><%=stock[i].getTypeMouv()%></td>
        <td><%=stock[i].getPrice()%></td>
        <td><%=stock[i].getDateAjout()%></td>
        </tr>

        <%
            }
        %>
        </tbody>
    </table>
</div>

</body>
</html>
