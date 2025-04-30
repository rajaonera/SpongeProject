<%@ page import="block.Block" %>
<%@ page import="formule.Formule" %>
<%@ page import="machine.Machine" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Block[] blocks = (Block[]) request.getAttribute("blocks");
    Machine[] machines  = (Machine[])   request.getAttribute("machines");
%>
<html>
<head>
    <title>Liste des BLock</title>
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


    </style>
</head>
<body>
<h1>ETU002477</h1>
<a class="back-link" href="<%=request.getContextPath()%>/index.jsp">Retour</a>
    <section class="body">
        <form action="<%=request.getContextPath()%>/insertBlock.BlockController" method="post">
            <h2>Insertion d'un block</h2>

            <div>
                <label>longueur * largeur * hauteur :</label>
                <input type="text" name="longueur" required>
                <input type="text" name="largeur" required>
                <input type="text" name="hauteur" required>
            </div>
            <div class="form-group">
                <label for="machineSelect" >Choisir une machine :
                    <select name="idMachine">
                        <option id="machineSelect" value="">Coisir une machine</option>
                        <%
                            if (machines != null) {
                                for (int i = 0; i < machines.length; i++) {
                        %>
                        <option id="machineSelect" value="<%=machines[i].getId()%>"><%=machines[i].getNom()%></option>
                        <%
                                }
                            }
                        %>
                    </select>
                </label>
            </div>

            <div>
                <label>Date d'ajout :</label>
                <input type="datetime-local" name="date_ajout" step="1" required>
            </div>
            <div>
                <label>prix d'achat :</label>
                <input type="number" name="unit_price" required>
            </div>
            <button type="submit">Valider</button>
        </form>
        <h2>Liste des Blocks :</h2>
        <table>
            <thead>
            <tr>
                <th>#</th>
                <th>source</th>
                <th>longueur</th>
                <th>largeur</th>
                <th>hauteur</th>
                <th>volume</th>
                <th>prix unitaire</th>
                <th>Date d'ajout</th>
                <th>Etat</th>
                <th>Id Machine</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <% for (Block block: blocks) { %>
            <tr>
                <% if (block.getStatus().equals("useless")){
                %>
                <td><a href="<%=request.getContextPath()%>/historique.StockController?idBlock=<%= block.getId() %>">block <%= block.getId() %></a></td>
                <%
                }else {
                %>
                <td><a href="<%=request.getContextPath()%>/AffList.StockController?idBlock=<%= block.getId() %>">block <%= block.getId() %></a></td>

                <%
                    }%>

                <% if (block.getIdSource() == 0){
                %>

                <td>Originel</td>
                <%
                }else {
                %>
                <td><a href="<%=request.getContextPath()%>/historique.StockController?idBlock=<%= block.getIdSource() %>">block <%= block.getIdSource() %></a></td>
                <%
                    }%>
                <td><%= block.getLongueur() %></td>
                <td><%= block.getLargeur() %></td>
                <td><%= block.getHauteur() %></td>
                <td><%= block.getVolume() %></td>
                <td><%= block.getUnitPrice() %></td>
                <td><%= block.getDateAjout() %></td>
                <td><%= block.getStatus() %></td>
                <td><%= block.getMachine().getNom() %></td>
                <td><a href= "<%=request.getContextPath()%>/update.BlockController?idBlock=<%=block.getId()%>" ><button onclick="openPopup()">Update</button></a></td>
            </tr>
            <% } %>
            </tbody>
        </table>
    </section>
</body>
</html>
