# Atur-Aja-Mobile

## Install database
* Clone project https://github.com/Atur-Aja/AturAja-Backend.git, branch percobaan
* install project dependancies
```
composer install
```
* Replace the existing .env with the given .env
* Generate JWT Secret Key
```
php artisan jwt:secret
```
* Run MySQL database using XAMPP control panel
* Migrate database
```
php artisan migrate
```
* run database
```
php artisan serve --host 0.0.0.0
```
## run android
* clone this project
* run with emulator pixel 4 XL API 30
