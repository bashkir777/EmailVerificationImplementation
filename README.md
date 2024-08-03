
#  Email Verification Implementation

## Description: Two-Factor Authentication Implementation with Email verification and JWT

This project focuses on implementing a robust two-factor authentication (2FA) system using email verification and JSON Web Tokens (JWT). 

## Key Technologies and Tools:

__Backend__
- Spring (Boot, Data...)
- RabbitMQ
- JUnit 5

__Database__
- Postgres 16

__Frontend__ 
- React JS
- Bootstrap

__Deployment__
- Nginx
- Docker
- Docker Compose

## Deployment

### Clone repository
```bash
git clone .../EmailVerificationImplementation 
```

### Create gmail, enable 2FA and create app password
__Here is how to do it__: https://www.youtube.com/watch?v=ugIUObNHZdo&t=171s

### Go to root folder
```bash
cd EmailVerificationImplementation
```
__Edit docker-compose.yml by inserting your application password and gmail mail into it.__ 

### Start network using docker compose
```bash
docker-compose up --build
```
__Application will be available at http://localhost:80__

## Screenshots

__Login page__
![Registration page](https://drive.google.com/uc?id=1vJwee0m2gpCTEBf2c_-kkNlRGlBee050)

__Registration page__
![Registration page](https://drive.google.com/uc?id=1xJGTqbEGAZCJxKhlVqv3TCzAHxPYR1rF)

__One time password page__
![One time password page](https://drive.google.com/uc?id=11mF0o4jnfKby8tEL5MfdkRhzqBs0PeiV)

__Change password page__
![Forgot password](https://drive.google.com/uc?id=1rWSsWol8mQMzBd8TFyCcIv9qpOUAo5DV)

__Email confirmation message__
![Email confirmation message](https://drive.google.com/uc?id=1Yk3wxLezHw1t9gehT5NZJGpHxo2_0ZIo)
