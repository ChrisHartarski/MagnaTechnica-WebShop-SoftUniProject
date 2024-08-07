# MagnaTechnica-WebShop-SoftUniProject
Magna Technica Webshop App project for SoftUni Web Module Exam 2024. Main goal of the project is developing abilities in building MVC applications, database management using JPA, Spring security, connecting to services via REST.

## The webshop has the following features:
- **User management** - user registration and authorization with separate roles (User and Admin). Users (both user and admin) have the functionality to edit their profiles (including email(used as username) and password).
- **Machines section** - section with available machines. Users have the possibility to make an enquiry, while admin users can modify and delete machines. The machines service is on another repository (link below), and the communication between the two is via REST.
- **Spare parts section** - section with available spare parts quantities and prices. Users can add parts to cart, while admin users can modify and delete parts.
- **User cart** - orders can be placed via the cart with the items in the cart. Cart is saved on every user so even if you logout and login again the cart will keep the items in it.
- **Contacts** - section with contacts and location on Google Maps using Google Maps API.
- **Admin Panel section** - gives the admin users some additional functionalities like adding new brands, parts, machines, viewing orders and managing them, adding new users (new admin users included), and initializing Mock database (with brands, parts and machines).
- **Internationalization** - the website is fully available in English and Bulgarian.

## Additional information:
- The repository for the Machines Service can be accessed here: https://github.com/ChrisHartarski/MagnaTechnica-WebShop-Machines-SoftUniProject.
- For front end using templates from https://startbootstrap.com/
- Using MySQL database
- When starting project for the first time DB is initialized with 2 users: 1 with ADMIN role and 1 with USER role.
- For testing purposes you can create a Mock DB with brands, parts and machines. To do so login as ADMIN -> Admin Panel (in navbar) -> Initialize Mock DB (button).