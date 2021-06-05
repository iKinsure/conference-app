# Conference app

### List of endpoints

- GET /api/lectures

response
```
[
    {
        "id": "a3a5e0ed-729b-4c09-8fb9-b382d255fb86",
        "name": "orange",
        "startTime": "10:00:00",
        "endTime": "11:45:00",
        "category": "A",
        "size": 0,
        "maxSize": 5
    },
    {
        "id": "6c561066-4519-4ff1-9d3e-85eb5de0d781",
        "name": "apple",
        "startTime": "12:00:00",
        "endTime": "13:45:00",
        "category": "A",
        "size": 0,
        "maxSize": 5
    }
]
```
- GET /api/users

response
```
[
    {
        "id": "f0b4772d-dd89-4758-9561-869575342eee",
        "email": "test@gmail.com",
        "login": "teste123",
        "lectures": [
            {
                "id": "a03cff6b-3e5e-4129-ac11-768db9e04005",
                "name": "orange",
                "startTime": "10:00:00",
                "endTime": "11:45:00",
                "category": "A",
                "size": 5,
                "maxSize": 5
            }
        ]
    }
]
```

- GET /api/users/teste123/lectures

response
```
[
    {
        "id": "a03cff6b-3e5e-4129-ac11-768db9e04005",
        "name": "orange",
        "startTime": "10:00:00",
        "endTime": "11:45:00",
        "category": "A",
        "size": 5,
        "maxSize": 5
    }
]
```

- PUT /api/users?lang=pl

request
```
{
    "email": "test234@gmail.com",
    "login": "teset234"
}
```
response
```
{
    "message": "Ten użytkownik nie istnieje"
}
```

- POST /api/lectures/3f56870f-d500-42cf-ba09-3c3288402b90

request
```
{
    "email": "helloworld@gmail.com",
    "login": "helloworld"
}
```
response
```
{
    "message": "Zarezerwowano prelekcję pomyślnie"
}
```

- DELETE /api/lectures/3f56870f-d500-42cf-ba09-3c3288402b90?lang=en

request
```
{
    "email": "helloworld@gmail.com",
    "login": "helloworld"
}
```
response
```
{
    "message": "This lecture does not exist"
}
```

### How to run
- you need java 11 and npm
- pull this repository
- go to conference\src\frontend and build frontend project with npm
- move build folder content to conference\src\main\java\resources\public
- now you can build app with java and run 
