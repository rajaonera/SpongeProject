<%@ page import="usuel.Forme_usuel" %>
<%@ page import="block.Block" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% Forme_usuel[] formes = (Forme_usuel[]) request.getAttribute("formes");%>
<% HttpSession session1 = request.getSession(false);
    Block block = (Block) session1.getAttribute("block");
    List<Forme_usuel> choices  = (List<Forme_usuel>) session1.getAttribute("choices");
    List<Integer> qtt_choices  = (List<Integer>) session1.getAttribute("qtt_choices");
    int count = 0;
    double reste =block.getVolume();
    Block parent = block.getParent();
    if (session1.getAttribute("volume_reste") != null) {
        reste  = (double) session1.getAttribute("volume_reste") ;
    }
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
        input[type="text"],input[type="number"] input[type="email"], input[type="date"] {
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
<a href="<%=request.getContextPath()%>/restartSession.StockController?idBlock=<%=block.getId()%>"><button> reinitialiser formes </button></a>
<a href="<%=request.getContextPath()%>/Pages/model/verification.jsp"><button> verification </button></a>
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
    <form action="<%=request.getContextPath()%>/insertPanier.StockController" method="post">
        <h2>Transformation : block <%=block.getId()%> -- <%=block.getVolume() %> m3 </h2>

        <label>Forme ID :</label>
        <div>
            <select name="idForme" id="" required>
                <% for (Forme_usuel b: formes){
                %>
                <option value="<%=b.getId()%>"><%=b.getNom()%>--<%=b.getVolume()%> m3</option>
                <%
                    }
                %>
            </select>
        </div>
        <div>
            <label>Quantite :</label>
            <input type="number" name="qtt" required>
            <input type="hidden" name="idBlock" value="<%=block.getId()%>" required>
        </div>
        <div>
            <label>Date d'ajout :</label>
            <input type="date" name="date_ajout" required>
        </div>

        <button type="submit">Valider</button>
    </form>
    <h2>Formes usuelles choisis :</h2>
    <h4>volume restant : <%=reste%></h4>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>nom</th>
            <th>Quantite</th>
            <th>Date d'ajout</th>
        </tr>
        </thead>
        <tbody>
        <% for (Forme_usuel forme: choices) { %>
        <tr>
            <td><%= forme.getId() %></td>
            <td><%= forme.getNom() %></td>
            <td><%= qtt_choices.get(count) %></td>
            <td><%= forme.getDateAjout() %></td>
            <td></td>
        </tr>
        <%
        count++;
        } %>
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
