# TZ_news

<img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210827-165125_%20.jpg" width="240" height="500"/>   <img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210816-182558_%20.jpg" width="240" height="500"/>   <img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210816-182603_%20.jpg" width="240" height="500"/>   <img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210816-182608_%20.jpg" width="240" height="500"/>   <img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210816-182615_%20.jpg" width="240" height="500"/>   <img src="https://github.com/vmitforjob/TZ_news/blob/master/Screenshot_20210827-165134_%20.jpg" width="240" height="500"/>   

# Необходимо реализовать одностраничное приложение, работающее по следующему принципу:

## Добавлено:

### Загрузка данных при первом открытии приложения 

### Возможность отображать(вернуть) скрытые новости 
####       Перейти с помощью меню -> Скрытые новости -> Отображать

### Адаптер создается один раз после обнавляются данные 

## Оно должно получать список новостей с веб-сервиса.
### Реализовано с помощью -> Retrofit 2
  
## Пришедший ответ должен быть записан в локальную базу данных.
### Реализовано с помощью -> Room, Sqlite
  
## Новости должны отображаться пользователю в виде списка.
### Реализовано с помощью -> Recyclerview 
  
## Каждая новость может быть открыта отдельно.
### Реализовано с помощью -> Fragment, WebView 

## (дополнительно) Реализовать поиск новостей. 
### Реализовано с помощью -> SearchView, добавлено в Recyclerview Adapter -> Filterable
  
## (дополнительно) Реализовать скрытие новостей.
### Реализовано с помощью -> Room, Update параметра isHide на true
  
