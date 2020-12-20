from django.http import JsonResponse
from django.views import View
from .feature.users_class import users_validate
from .feature.weather_class import weather

result = {
    "status": False,
    "message": "Your email or password is incorrect, Please check your credentials"
}

class Current(View, users_validate, weather):
    def get(self, request, *args, **kwargs):
        users = users_validate(request.body, request.headers)
        if not users.status:
            return JsonResponse(result)
        
        forecast = weather(request.body)
        forecast.lon = request.GET.get('lon', '')
        forecast.lat = request.GET.get('lat', '')
        forecast.lang = request.GET.get('lang', 'en')
        
        return JsonResponse(forecast.current(), safe=False)
        
class Forecast(View, users_validate, weather): 
    def get(self, request, *args, **kwargs):
        users = users_validate(request.body, request.headers)
        if not users.status:
            return JsonResponse(result)
        
        forecast = weather(request.body)
        forecast.lon = request.GET.get('lon', '')
        forecast.lat = request.GET.get('lat', '')
        forecast.lang = request.GET.get('lang', 'id')
        
        return JsonResponse(forecast.custom("current", "hourly"), safe=False)
        