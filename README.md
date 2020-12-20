# Cuacaku

[![N|Solid](https://ahmadansori.com/assets/img/ansori.png)](https://ahmadansori.com/)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://github.com/ansoridev/cashierless-pay)

Perkenalkan Aplikasi Cuacaku yang memiliki fitur untuk meramalkan cuaca dengan menggunakan Android Java sebagai bahasa pemrograman android dan menggunakan Django sebagai Restful API
Sebagai pembelajaran penggunaan Restful API, List View Base Adapter, Get Image by URL, dan OOP Python / Django

- Web Teknologi: 
      Django 3 (Python 3)
- Android Teknologi:
      Java
	  OkHttp
	  Google Mobile Service

Pada project ini menggunakan API Cuaca dari Open Weather Map, dan menggunakan Django Admin untuk mengontrol user, Terdapat fitur Log In dan Register pada aplikasi ini

### Installation Django

Anda dapat mengaktifkan project Django dengan membuat environment terpisah untuk project ini dengan cara

Untuk Windows
```
> python3 -m venv venv
> "venv/Scripts/activate"
> pip install -r requirement.txt
```

Untuk Linux
```
$ python3 -m venv venv
$ source "venv/bin/activate"
$ pip install -r requirement.txt
```

Setelah itu anda dapat menyesuaikan database sesuai dengan konfigurasi keinginan anda
dan lakukan migrasi database dengan cara
```
> cd Cashierless
> py m migrate
> py m makemigrations android
> py m migrate android
```

Lalu lakukan pengaktifan server Django
```
> python3 m runserver 0.0.0.0:80 --insecure
```

dan untuk membuat akun untuk administrator yang berada di path /admin
```
> python3 m createsuperuser
```

### Installation Android

Anda dapat membuka project pada folder Android-Java, lalu anda dapat melakukan Build Gradle dan Voila, anda dapat membuild APK nya 

### Terima Kasih
