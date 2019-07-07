<%@ page import="rweb.Rserve" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>R을 이용하여 wordcloud 이미지 출력하기</title>
    <script>
        function wordcloud() {
            wordcloudimg.src = "img/<%= Rserve.wordcloud("seoul_new.txt", request)%>";
        }
    </script>
</head>
<body>
<input type="button" value="서울시 응답소 분석" onclick="wordcloud()"><br>
<img src="" id="wordcloudimg" alt="">
</body>
</html>
