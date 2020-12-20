import requests, json

class weather:
    lang = "id"
    lat = -5.11
    lon = 105.31
    
    def __init__(self, body, api = "29a4816697128ff54501bb28940a063a"):
        self.body = json.loads(body) if body else {}
        if "lat" in self.body and "long" in self.body:
            self.lat = self.body["lat"]
            self.lon = self.body["long"]
            
        self.api = api
    
    def __getRequest(self, *tipe):
        searchBy = ["current","minutely","hourly","daily","alert"]
        for search in tipe:
            searchBy = str(searchBy)[1:-1].replace("'", "").replace(", ", ",").replace(search, "")
        url = f"https://api.openweathermap.org/data/2.5/onecall?lat={self.lat}&lon={self.lon}&appid={self.api}&exclude={searchBy}&lang={self.lang}&units=metric"
        r = requests.get(url)
        return json.loads(json.dumps(r.json()))
    
    def current(self):
        return {
            "status": True,
            "data": self.__getRequest("current")
        }
        
    def hourly(self):
        return {
            "status": True,
            "data": self.__getRequest("hourly")
        }
        
    def daily(self):
        return {
            "status": True,
            "data": self.__getRequest("daily")
        }
        
    def custom(self, *tipe):
        return {
            "status": True,
            "data": self.__getRequest(*tipe)
        }