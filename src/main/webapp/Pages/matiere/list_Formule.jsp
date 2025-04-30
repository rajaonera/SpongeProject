<%@ page import="formule.Formule" %>
<%@ page import="matiere.Matiere" %><%--
  Created by IntelliJ IDEA.
  User: HUGUES
  Date: 18/11/2024
  Time: 23:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Formule[] formules = (Formule[]) request.getAttribute("formules");
    Matiere[] matieres = (Matiere[]) request.getAttribute("matieres");

%>
<html>
<head>
    <title>Formulaire d'Insertion de Formule</title>
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

        .form-container, .form-list-container {
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
    <h2>Formulaire d'Insertion de Formule</h2>
    <form method="post" action="insert.FormuleController">
        <div class="form-group">
            <label for="nom">Nom formule :</label>
            <input type="text" id="nom" name="nom" required>
        </div>

        <!-- Bouton de soumission -->
        <div class="form-group">
            <button type="submit">Soumettre</button>
        </div>
    </form>
</div>

<div class="form-container">
    <h2>Formulaire d'Insertion de Formule</h2>
    <form id="formuleForm" method="post" action="insertDetails.FormuleController">
        <div class="form-group">
            <label for="formuleSelect" >Choisir une formule :
                <select name="idFormule">
                    <option id="formuleSelect" value="">Coisir une formule</option>
                <%
                    if (formules != null) {
                        for (int i = 0; i < formules.length; i++) {
                %>
                    <option id="formuleSelect" value="<%=formules[i].getId()%>"><%=formules[i].getNom()%></option>
                    <%
                            }
                        }
                    %>
                </select>
            </label>
        </div>

        <div class="form-group">
                <%
            for (int i = 0; i < matieres.length; i++) {
                %>
            <label for="matiereSelect<%=matieres[i].getId()%>"><%=matieres[i].getNom()%> :</label>
            <input type="hidden" name="idMatiere<%=matieres[i].getId()%>" value="<%=matieres[i].getId()%>">
            <input type="number" min="0" step="0.01" id="matiereSelect<%=matieres[i].getId()%>" name="matiere<%=matieres[i].getId()%>" required placeholder="Entrez la quantité en <%=matieres[i].getUnite().getNom()%>">
                <%
                    }
                %>
        </div>
        <!-- Bouton de soumission -->
        <div class="form-group">
            <button type="submit">Soumettre</button>
        </div>
    </form>
</div>

<div class="form-list-container">
    <h2>Liste des Formules Ajoutées</h2>
    <table id="formuleTable">
        <thead>
        <tr>
            <th>Formule</th>
            <%
                for (int i = 0; i < matieres.length; i++) {
            %>
            <th><%=matieres[i].getNom()%> (<%=matieres[i].getUnite().getNom()%>)</th>
            <%
                }
            %>

        </tr>
        </thead>
        <tbody>

        <%
            for (int j = 0; j < formules.length ; j++) {
        %>
        <tr>
        <td><%=formules[j].getNom()%> </td>
        <%


            for (int i = 0; i < matieres.length; i++) {
        %>
        <td><%=formules[j].getDetails().getOrDefault(matieres[i].getId(),0.0)%> </td>
        <%
                }
            %>
        </tr>

        <%
            }
        %>

        </tbody>
    </table>
</div>

</body>
</html>
