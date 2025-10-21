🎬 Movie Ticket Management System

A JavaFX-based desktop application that allows users to browse movies, book tickets, and manage bookings efficiently.
The system also supports admin features like adding movies, managing showtimes, and exporting booking data.

📘 Table of Contents

About the Project

Features

Tech Stack

Project Structure

Database Design

Installation

Usage

Future Improvements

Author

🧠 About the Project

The Movie Ticket Management System is designed to simplify the process of booking and managing movie tickets.
It provides an easy-to-use interface for customers and an administrative interface for managing movies, schedules, and bookings.

The goal is to automate the ticket booking process and reduce manual work for cinema operators.

🚀 Features
🎟 User Features

View available movies and showtimes

Book tickets for selected movies

View and cancel bookings

Get booking confirmation with QR code

Email notifications (optional)

🧑‍💼 Admin Features

Add, edit, or delete movies

Manage showtimes and ticket pricing

View booking reports

Export ticket or booking data as PDF

Validate tickets using booking ID or QR code

⚙️ Tech Stack
Category	Technology
Language	Java (JDK 17+)
Framework	JavaFX
Database	MySQL
Build Tool	Maven / IntelliJ IDE
Email Service	JavaMail API
Barcode/QR	ZXing Library
PDF Export	iText / PDFBox (if used)
🗂️ Project Structure
MovieTicketManagementSystem/
│
├── src/
│   ├── bd/edu/seu/ticket_booking/
│   │   ├── Controller/
│   │   │   ├── CustomerController.java
│   │   │   ├── AdminController.java
│   │   │   └── LoginController.java
│   │   ├── Model/
│   │   │   ├── Movie.java
│   │   │   ├── Booking.java
│   │   │   └── User.java
│   │   ├── Utility/
│   │   │   ├── DBConnection.java
│   │   │   └── CurrentBooking.java
│   │   ├── HelloApplication.java
│   │   └── Main.java
│   └── resources/
│       ├── fxml/
│       │   ├── login.fxml
│       │   ├── admin.fxml
│       │   └── customer.fxml
│       ├── images/
│       └── styles.css
│
├── pom.xml
└── README.md

🧩 Database Design

Database Name: movie_ticket_db

Tables:

users

id, name, email, password, role

movies

id, title, genre, duration, trailer_url, poster, description

showtimes

id, movie_id, date, time, price

bookings

id, user_id, showtime_id, seats, booking_date

payments

id, booking_id, amount, payment_method, transaction_id, payment_date

🖥️ Installation
Prerequisites

Install Java JDK 17+

Install MySQL Server & Workbench

Install IntelliJ IDEA / NetBeans / Eclipse

Install JavaFX SDK (if not included in your IDE)

Steps

Clone this repository:

git clone https://github.com/yourusername/MovieTicketManagementSystem.git


Open the project in your IDE.

Configure the MySQL database:

Import the SQL script provided in database/movie_ticket_db.sql.

Update your DB connection settings in DBConnection.java:

private static final String URL = "jdbc:mysql://localhost:3306/movie_ticket_db";
private static final String USER = "root";
private static final String PASSWORD = "your_password";


Run the application from HelloApplication.java.

🧭 Usage

Launch the app.

Log in as a user or admin.

Users can browse movies and book tickets.

Admin can manage movie listings and view reports.

Bookings can be exported or verified using QR code.


🔮 Future Improvements

Add online payment gateway (e.g., SSLCommerz / Stripe)

Include seat map visualization

Add movie recommendation system

Create web-based version using React & Spring Boot

Implement user profile syste

👨‍💻 Author

Abid Hasan
🎓 2nd Year CSE Student, Southeast University
💡 Passionate about Java, DSA, and full-stack development
🔗 LinkedIn
 | GitHub
