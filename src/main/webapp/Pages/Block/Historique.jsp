<%@ page import="usuel.Forme_usuel" %>
<%@ page import="block.Block" %>
<%@ page import="java.util.List" %>
<%@ page import="stock.MouvementStock" %>
<%@ page import="java.util.HashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<MouvementStock> history  = (List<MouvementStock>) request.getAttribute("history");
    HashMap<MouvementStock,Double> price = (HashMap<MouvementStock,Double>) request.getAttribute("price");
    Block block = (Block) request.getAttribute("block");
    List<Forme_usuel> formes  = (List<Forme_usuel>) request.getAttribute("formes");
    double total = (double) request.getAttribute("total");
    Block child  = block.getChild();
    Block parent = block.getParent();

%>

<html>
<head>
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
<a class="back-link" href="<%=request.getContextPath()%>/killSession.StockController">Retour</a>
<section class="body">
    <h1>ETU002477</h1>
    <h2>Historique :</h2>
    <h4>volume : <%=block.getVolume()%></h4>
    <h4>Etat : Useless</h4>
    Parent :
    <%    if (parent != null) {
%>
    <a href="<%=request.getContextPath()%>/historique.StockController?idBlock=<%= parent.getId() %>"><button> block <%= parent.getId() %></button></a>
    <%
        }
        else {
    %>

    <p>Originel</p>

    <%
        } %>
    <h4>Child :</h4>
    <% if (child.getStatus().equals("useless")){
    %>
    <a href="<%=request.getContextPath()%>/historique.StockController?idBlock=<%= child.getId() %>"><button> block <%= child.getId() %></button></a>
    <%
    }else {
    %>
    <a href="<%=request.getContextPath()%>/AffList.StockController?idBlock=<%= child.getId() %>"><button>block <%= child.getId() %></button></a>

    <%
        }%>

    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>IdForme</th>
            <th>Quantite</th>
            <th>Date d'ajout</th>
            <th>total Vente</th>
            <th>Prix Revient Unitaire</th>
        </tr>
        </thead>
        <tbody>
        <% for (MouvementStock mouvementStock: history) { %>
        <tr>
            <td><%= mouvementStock.getId() %></td>
            <td><%= mouvementStock.getIdForme() %></td>
            <td><%= mouvementStock.getQtt() %></td>
            <td><%= mouvementStock.getDateAjout() %></td>
            <td><%= price.get(mouvementStock) %></td>
            <td><%= mouvementStock.getPrUnitaire() %></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td></td>
            <td><%= total %></td>
        </tr>
        </tbody>
    </table>
</section>
<section class="body">

    <h2>Simulation Volume :</h2>
    <h4>Reste <%=block.getSimulationReste().getRemaining()%></h4>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Forme</th>
            <th>Quantite</th>
            <th>Montant</th>
        </tr>
        </thead>
        <tbody>
        <% for (Forme_usuel forme_usuel: formes) { %>
        <tr>
            <td><%= forme_usuel.getId() %></td>
            <td><%= forme_usuel.getNom() %></td>
            <td><%= block.getSimulationReste().getSurfaceCount().get(forme_usuel.getId()) %></td>
            <td><%= block.getSimulationReste().getRevenuesPerSurface().get(forme_usuel.getId()) %></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><%=block.getSimulationReste().getMontantTotal()%></td>
            <td><%= block.getSimulationReste().getTotalUsed() %></td>
        </tr>
        </tbody>
    </table>
</section><section class="body">

    <h2>Simulation Prix :</h2>
    <h4>Reste <%=block.getSimulationPrix().getRemaining()%></h4>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Forme</th>
            <th>Quantite</th>
            <th>Montant</th>
        </tr>
        </thead>
        <tbody>
        <% for (Forme_usuel forme_usuel: formes) { %>
        <tr>
            <td><%= forme_usuel.getId() %></td>
            <td><%= forme_usuel.getNom() %></td>
            <td><%= block.getSimulationPrix().getSurfaceCount().get(forme_usuel.getId()) %></td>
            <td><%= block.getSimulationPrix().getRevenuesPerSurface().get(forme_usuel.getId()) %></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td></td>
            <td></td>
            <td></td>
            <td><%=block.getSimulationPrix().getMontantTotal()%></td>
            <td><%= block.getSimulationPrix().getTotalUsed() %></td>
        </tr>
        </tbody>
    </table>
</section>
</body>

</html>
