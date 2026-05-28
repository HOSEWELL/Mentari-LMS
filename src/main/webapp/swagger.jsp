<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <title>Mentari LMS API Documentation</title>

    <link rel="stylesheet"
          href="https://unpkg.com/swagger-ui-dist/swagger-ui.css">

    <style>
        body {
            margin: 0;
            background: #f5f7fb;
            font-family: Arial, sans-serif;
        }

        .topbar {
            background: #071633;
            color: white;
            padding: 16px 24px;
            font-size: 22px;
            font-weight: bold;
        }
    </style>
</head>

<body>

<div class="topbar">
    Mentari LMS - API Documentation
</div>

<div id="swagger-ui"></div>

<script src="https://unpkg.com/swagger-ui-dist/swagger-ui-bundle.js"></script>

<script>
window.onload = function () {

    SwaggerUIBundle({
        url: "${pageContext.request.contextPath}/api/openapi",
        dom_id: "#swagger-ui",
        deepLinking: true,
        presets: [
            SwaggerUIBundle.presets.apis
        ],
        layout: "BaseLayout"
    });

};
</script>

</body>
</html>