<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Ініціалізація змінних для збереження стану форми та результату
    String lat1Str = request.getParameter("lat1");
    String lon1Str = request.getParameter("lon1");
    String lat2Str = request.getParameter("lat2");
    String lon2Str = request.getParameter("lon2");

    String result = "";
    String errorMsg = "";

    // Обробка форми після натискання кнопки "Solve"
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        try {
            // Перевірка на порожні значення
            if (lat1Str != null && !lat1Str.trim().isEmpty() &&
                lon1Str != null && !lon1Str.trim().isEmpty() &&
                lat2Str != null && !lat2Str.trim().isEmpty() &&
                lon2Str != null && !lon2Str.trim().isEmpty()) {

                // Зчитування координат
                double lat1 = Double.parseDouble(lat1Str.replace(",", "."));
                double lon1 = Double.parseDouble(lon1Str.replace(",", "."));
                double lat2 = Double.parseDouble(lat2Str.replace(",", "."));
                double lon2 = Double.parseDouble(lon2Str.replace(",", "."));

                // Радіус Землі в метрах
                double R = 6371000;

                // Конвертація в радіани
                double phi1 = Math.toRadians(lat1);
                double phi2 = Math.toRadians(lat2);

                // Різниця координат в радіанах
                double deltaPhi = Math.toRadians(lat2 - lat1);
                double deltaLambda = Math.toRadians(lon2 - lon1);

                // Формула гаверсинуса
                double a = Math.pow(Math.sin(deltaPhi / 2), 2) +
                           Math.cos(phi1) * Math.cos(phi2) *
                           Math.pow(Math.sin(deltaLambda / 2), 2);

                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                double distance = R * c;

                // Форматування результату
                result = String.format(java.util.Locale.US, "%.2f m", distance);
            } else {
                errorMsg = "Всі поля повинні бути заповнені.";
            }
        } catch (NumberFormatException e) {
            errorMsg = "Помилка вводу: будь ласка, введіть коректні числові значення.";
        }
    }
%>
<!DOCTYPE html>
<html lang="uk">
<head>
    <meta charset="UTF-8">
    <title>Distance Calculator</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 25px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            width: 350px;
        }
        h2 {
            text-align: center;
            color: #333;
            margin-top: 0;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
            color: #555;
            font-weight: bold;
            font-size: 14px;
        }
        input[type="text"] {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .btn-group {
            display: flex;
            justify-content: space-between;
            margin-top: 20px;
        }
        button {
            width: 48%;
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-weight: bold;
        }
        .btn-solve {
            background-color: #4CAF50;
            color: white;
        }
        .btn-solve:hover {
            background-color: #45a049;
        }
        .btn-clear {
            background-color: #f44336;
            color: white;
        }
        .btn-clear:hover {
            background-color: #da190b;
        }
        .result-box {
            margin-top: 20px;
            padding: 10px;
            background-color: #e7f3fe;
            border-left: 6px solid #2196F3;
            font-size: 16px;
        }
        .error-box {
            margin-top: 20px;
            padding: 10px;
            background-color: #ffebee;
            border-left: 6px solid #f44336;
            color: #c62828;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>Distance Calculator</h2>
    <form method="POST" action="index.jsp">
        <div class="form-group">
            <label for="lat1">Latitude 1 (degrees):</label>
            <input type="text" id="lat1" name="lat1" value="<%= lat1Str != null ? lat1Str : "" %>">
        </div>

        <div class="form-group">
            <label for="lon1">Longitude 1 (degrees):</label>
            <input type="text" id="lon1" name="lon1" value="<%= lon1Str != null ? lon1Str : "" %>">
        </div>

        <div class="form-group">
            <label for="lat2">Latitude 2 (degrees):</label>
            <input type="text" id="lat2" name="lat2" value="<%= lat2Str != null ? lat2Str : "" %>">
        </div>

        <div class="form-group">
            <label for="lon2">Longitude 2 (degrees):</label>
            <input type="text" id="lon2" name="lon2" value="<%= lon2Str != null ? lon2Str : "" %>">
        </div>

        <div class="btn-group">
            <button type="submit" class="btn-solve">Solve</button>
            <button type="button" class="btn-clear" onclick="window.location.href='index.jsp'">Clear</button>
        </div>
    </form>

    <%-- Блок виведення результату --%>
    <% if (!result.isEmpty()) { %>
        <div class="result-box">
            <strong>Distance:</strong> <%= result %>
        </div>
    <% } %>

    <%-- Блок виведення помилок --%>
    <% if (!errorMsg.isEmpty()) { %>
        <div class="error-box">
            <%= errorMsg %>
        </div>
    <% } %>
</div>

</body>
</html>