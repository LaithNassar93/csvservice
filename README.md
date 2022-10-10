# Mini-proj1

Implement a Restful service allowing an admin to trigger a batch job.

The batch job , reads a CSV file and writes its contents into Mysql DB after performing some conversion.

## Installation

Run command:  

```bash
docker-compose up -d
```

## Usage

Use Postman 



# 1. Call Http Post and it will return Token
```docker
curl --location --request POST 'http://localhost:8085/api/v1/authenticate' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "laith",
    "password": "123123"
}'
```

# 2. Call Http Get Job, you have to use the token from step one 
```docker
curl --location --request GET 'http://localhost:8085/api/v1/job' \
--header 'Authorization: Bearer TOKEN_FROM_STEP_ONE'
```