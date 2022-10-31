# Data-Parser-and-Api
Приложение парсит открытые данные о цене на сырую нефть марки “Юралс” и предоставляет к ним доступ по API.
Запуск плиложения:
1) через docker:               
  docker build ./ - собрать образ через Dockerfile                     
  docker run -p 8080:8080 [образ]                         
2) java -jar DataParserApi-1.0.0.jar в target                    
Приложению нужен dataset.csv.(P.S. Хотел сделать так, чтобы качало файл с сайт автомат в коде. Но сервер присылал 403 ошибку. Хотя так качает. Наверное думает что запросы автоматические)                 
Работа приложения:
a) Главная страница - http://localhost:8080/home. На ней есть все нужные запросы.
b) Либо запрос на нужные url. Цена в формате yyyy-mm-dd.         
Цена на заданную дату - http://localhost:8080/prices/price?date=2022-08-15                 
Средняя цена за промежуток времени - http://localhost:8080/prices/price/average?first=2013-03-15&second=2013-06-14                        
Максимальная и минимальная цены за промежуток времени - http://localhost:8080/prices/price/minmax?first=2013-03-15&second=2013-06-14           
Статистика по загруженным данным - http://localhost:8080/prices/all
