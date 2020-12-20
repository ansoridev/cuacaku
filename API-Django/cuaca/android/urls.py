"""cuaca URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.urls import path
from .program.auth import Signin, Signup
from .program.weather import Current, Forecast

urlpatterns = [
    path('auth', Signin.as_view()),
    path('users', Signup.as_view()),
    path('weathers/current', Current.as_view()),
    path('weathers/forecast', Forecast.as_view())
]
