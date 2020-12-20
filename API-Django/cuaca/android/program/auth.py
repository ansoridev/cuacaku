from django.views import View
from django.http import JsonResponse
from .feature.users_class import users_validate, users_register

class Signin(View, users_validate):
    result = {
        "status": False,
        "message": "Your email or password is incorrect, Please check your credentials"
    }
    
    def post(self, request, *args, **kwargs):
        users = users_validate(request.body, request.headers)
        if users.status:
            self.result = users.getResult()
            return JsonResponse(self.result)
        return JsonResponse(self.result)

class Signup(View, users_register):
    result = {
        "status": False,
        "message": "Your email is invalid or already registered on our system."
    }
    
    def post(self, request, *args, **kwargs):
        users = users_register(request.body)
        return JsonResponse(users.getResult()) if users.status else JsonResponse(self.result)