
<html>
<head>
    <title>Accueil</title>
    <%--  <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/home.css">--%>
    <style>
        body {
            background-color: #121212; /* Couleur de fond sombre */
            color: #e0e0e0; /* Couleur du texte clair */
            font-family: Arial, sans-serif; /* Choix de la police */
        }

        .header {
            background-color: #1e1e1e; /* Couleur de fond du header */
            padding: 20px; /* Espacement */
            text-align: center; /* Centrer le texte */
        }

        .navbar a {
            color: #bb86fc; /* Couleur des liens */
            text-decoration: none; /* Pas de soulignement */
        }

        .menu {
            display: flex;
            justify-content: center; /* Centrer les boutons */
            flex-direction: column;
            align-items: center; /* Centrer les éléments */
        }

        .menu button {
            background-color: #6200ea; /* Couleur du bouton */
            color: white; /* Couleur du texte du bouton */
            padding: 10px 20px; /* Espacement interne */
            border: none; /* Pas de bordure */
            border-radius: 5px; /* Coins arrondis */
            cursor: pointer; /* Curseur de pointeur */
            margin: 10px 0; /* Espacement vertical entre les boutons */
            transition: background-color 0.3s; /* Transition pour l'effet au survol */
        }

        .menu button:hover {
            background-color: #3700b3; /* Couleur au survol */
        }
    </style>

</head>
<body>
<section class="menu">
    <nav>
        <p><a href="<%=request.getContextPath()%>/AffList.BlockController">Blocks</a></p>
        <p><a href="<%=request.getContextPath()%>/AffList.FormeController">formes Usuelles </a></p>
        <p><a href="<%=request.getContextPath()%>/AffList.StockController">Modelisation et transformation </a></p>
        <p><a href="<%=request.getContextPath()%>/affList.ResultatController">Dashboard </a></p>
    </nav>
</section>
</body>
</html>
