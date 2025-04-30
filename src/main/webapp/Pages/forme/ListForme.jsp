<%@ page import="usuel.Forme_usuel" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Forme_usuel[] formes = (Forme_usuel[]) request.getAttribute("formes");%>
<html>
<head>
    <title>Liste des Formes</title>
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
<body>
<h1>ETU002477</h1>
<a class="back-link" href="<%=request.getContextPath()%>/index.jsp">Retour</a>
<section class="body">
    <form action="<%=request.getContextPath()%>/insertForme.FormeController" method="post">
        <h2>Insertion d'une forme usuelle </h2>
        <div>
            <label>nom :</label>
            <input type="text" name="nom" required>
        </div>

        <div>
            <label>longueur * largeur * hauteur :</label>
            <input type="text" name="longueur" required>
            <input type="text" name="largeur" required>
            <input type="text" name="hauteur" required>
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
            <th>nom</th>
            <th>longueur</th>
            <th>largeur</th>
            <th>hauteur</th>
            <th>volume</th>
            <th>Date d'ajout</th>
            <th>action</th>
        </tr>
        </thead>
        <tbody>
        <% for (Forme_usuel forme: formes) { %>
        <tr>
            <td><%= forme.getId() %></td>
            <td><a href="<%=request.getContextPath()%>/Info.FormeController?id=<%= forme.getId() %>"><%= forme.getNom() %></a></td>
            <td><%= forme.getLongueur() %></td>
            <td><%= forme.getLargeur() %></td>
            <td><%= forme.getHauteur() %></td>
            <td><%= forme.getVolume() %></td>
            <td><%= forme.getDateAjout() %></td>
            <td></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</section>
</body>
</html>
