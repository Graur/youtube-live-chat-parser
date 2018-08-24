# Парсер онлайн-чата стрима на канале Youtube

**Что делает:** 
1) Парсит live-chat на Youtube;
2) Ищет совпадения по имени с группой во Вконтакте;
3) Выводит output в консоль и на простую html-страницу.

**Чтобы запустить проект, необходимо добавить следующие данные в проперти файлы:**
1) [application.properties:](https://github.com/Graur/youtube-live-chat-parser/blob/master/src/main/resources/application.properties)
_spring.datasource.username_ - логин БД
_spring.datasource.password_ - пароль от БД
2) [vk.properties:](https://github.com/Graur/youtube-live-chat-parser/blob/master/src/main/resources/vk.properties)
_vk.token_ - токен в вк (получить можно перейдя по ссылке: `https://oauth.vk.com/authorize?client_id=ИД_ВАШЕГО_ПРИЛОЖЕНИЯ&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=offline,messages&response_type=token&v=5.78` 
и скопировав значение параметра `token` в это поле)  
_youtube.target.vkgroup.id_ - id группы, для которой будет производится сравнение по имени из Youtube чата (только числовые значения!)
3) [youtube.properties:](https://github.com/Graur/youtube-live-chat-parser/blob/master/src/main/resources/youtube.properties)
_youtube.apikey_ - API ключ, полученный в `https://console.developers.google.com/apis/`    
_youtube.channel.id_ - id канала на Youtube
