from django.contrib import admin
from . import models as db

# Register your models here.
@admin.register(db.User)
class UserClass(admin.ModelAdmin):
    search_fields = ['name', 'email']
    list_display = ['name', 'email']
    list_per_page = 10