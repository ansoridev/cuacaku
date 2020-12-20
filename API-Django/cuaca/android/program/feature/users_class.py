import json, base64

class users_construct:
    email = ""
    password = ""
    name = ""
    
    def __init__(self, body, headers = {}):
        self.body = json.loads(body) if body else {}
        if "email" in self.body and "password" in self.body:
            self.email = self.body['email']
            self.password = self.body['password']
        if "name" in self.body:
            self.name = self.body['name']
            
        if 'Authorization' in headers:
            if 'Basic' in headers['Authorization']:
                import base64
                auth = base64.b64decode(headers['Authorization'].replace('Basic ', '')).decode("utf-8").split(':')
                self.email = auth[0]
                self.password = auth[1]
            
        import re
        regex = lambda email : re.search('^[a-z0-9]+[\._]?[a-z0-9]+[@]\w+[.]\w{2,3}$',email)
        self.isEmailValid = regex(self.email)
            
    def getName(self):
        from ...models import User
        userQuery = User.objects.filter(email = self.email)
        self.name = userQuery[0].name if userQuery else ""
        return self.name
    
class users_validate(users_construct):
    status = False
    
    def __init__(self, body, headers = {}):
        self.users = users_construct(body, headers)
        
        from ...models import User
        usersQuery = User.objects.filter(email = self.users.email)
        self.isRegistered = True if usersQuery else False
        if usersQuery:
            from django.contrib.auth.hashers import check_password
            if check_password(self.users.password, usersQuery[0].password):
                self.status = True
    
    def getResult(self):
        return {
            "status": self.status,
            "data": {
                "email": self.users.email,
                "name": self.users.getName()
            }
        }
        
class users_register(users_validate):
    status = False
    def __init__(self, body, headers = {}):
        self.users = users_validate(body, headers)
        if not self.users.isRegistered and self.users.users.isEmailValid:
            from ...models import User
            from django.contrib.auth.hashers import make_password
            
            user = User(
                name = self.users.users.name,
                email = self.users.users.email,
                password = make_password(self.users.users.password, hasher="pbkdf2_sha256")
            )
            user.save()
            self.user = user
            self.status = True
            
    def getResult(self):
        return {
            "status": self.status,
            "data": {
                "id": self.user.id,
                "email": self.users.users.email,
                "name": self.users.users.name,
                "authorization": base64.b64encode(str(self.users.users.email + ":" + self.users.users.password).encode("utf-8")).decode("utf-8")
            }
        }