<h2>Зависимости</h2>
<p>Для работы на данный момент требуется только поднятая база данных, docker-compose.yml для нее приложен в папке ./misc этого проекта.
<b>Перед сборкой контейнера docker требуется изменить стандартные данные подключения</b></p>
<i>Позже понадобится и рабочая инстанция keycloak.</i>

Проект запускается из класса PhoneNumberApiApplication, и при запуске треубет установки переменных среды

application.properties:
```yaml
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
spring.security.oauth2.client.registration.keycloak.client-id=${KEYCLOAK_CLIENT_ID}
spring.security.oauth2.client.registration.keycloak.client-secret=${KEYCLOAK_CLIENT_SECRET}
spring.security.oauth2.client.provider.keycloak.issuer-uri=${KEYCLOAK_URI}
```

Эти переменные среды следует указать при запуске приложения, сделав их такими же, как и в контейнере postgres и в keycloak.

<h2>Инструкция для запуска</h2>




1. Изменить данные авторизации базы данных в файле docker-compose.yml
2. Ввести команду  ```docker compose up``` в консоль, находясь в папке с файлом docker-compose.yml. <b>Важно, чтобы на этом этапе был запущен docker.</b>
3. Прописать данные авторизации Postgres в переменные среды, это можно сделать в окне редактирования конфигурации IntelliJ Idea.
4. Запустить приложение

По умолчанию сервер запускается на порте 8081


