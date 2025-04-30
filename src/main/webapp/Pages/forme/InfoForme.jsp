<%@ page import="usuel.Forme_usuel" %>
<%@ page import="usuel.PrixUsuel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Forme_usuel forme = (Forme_usuel) request.getAttribute("forme");
    PrixUsuel[] prixUsuels = (PrixUsuel[])  request.getAttribute("listePrix");
%>

<html>
<head>
    <title>Forme : details et prix </title>
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
            width: 100%;
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
<h1>ETU002477</h1>
<body>
<a class="back-link" href="<%=request.getContextPath()%>/AffList.FormeController">Retour</a>
<section class="body">
    <form action="<%=request.getContextPath()%>/insertPrix.FormeController" method="post">
        <h2>Insertion prix : <%= forme.getNom()%>  </h2>
        <input type="hidden" name="id" value="<%= forme.getId()%>" required>
        <input type="hidden" name="typePrice" value="vente" required>
        <div>
            <label>Prix :</label>
            <input type="number" name="prix" step="0.01" required>
        </div>

        <div>
            <label>Date d'ajout :</label>
            <input type="date" name="date_ajout" required>
        </div>

        <button type="submit">Valider</button>
    </form>
    <h2>Liste des Formes Usuels :</h2>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Prix de vente</th>
            <th>Date d'ajout</th>
            <th>action</th>
        </tr>
        </thead>
        <tbody>
        <% for (PrixUsuel prix: prixUsuels) { %>
        <tr>
            <td><%= prix.getId() %></td>
            <td><%= prix.getUnitPrice() %></td>
            <td><%= prix.getDateModif() %></td>
            <td></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</section>
</body>
</html>
