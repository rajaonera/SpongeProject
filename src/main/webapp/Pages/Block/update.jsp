<%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 11/11/2024
  Time: 09:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="block.Block" %>
<%
    Block block  =(Block) request.getAttribute("block");
%>
<html>
<head>
    <title>Update</title>
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
        input[type="text"],input[type="number"], input[type="email"], input[type="date"] {
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

        .popup{
            display: flex;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color:#121212;
            justify-content: center;
            align-items: center;
        }
        .popup-content{
            background-color: #1e1e1e;
            padding: 20px;
            border-radius: 5px;
            width: 300px;
            position: relative;
        }

    </style>

</head>
<body>
<div id="popup" class="popup">

    <div class="popup-content">
        <form action="updatePRevient.BlockController" method="post">
            <div>
                <label>BlockID :</label>
                <input value="<%=block.getId()%>" type="number" name="idBlock" step="1" required>
            </div>
            <div>
                <label>PrixRevient /cm3 :</label>
                <input type="number" name="prixRevient" step="0.01" required>
            </div>
            <button type="submit">Update </button>
        </form>
    </div>
</div>

</body>
</html>
