# 🎬 Movie Ticket Management System

A **JavaFX-based desktop application** that allows users to browse movies, book tickets, and manage bookings efficiently. The system also provides a powerful **admin panel** for managing movies, showtimes, and booking reports.

---

## 📘 Table of Contents

* [About the Project](#-about-the-project)
* [Features](#-features)
* [Tech Stack](#-tech-stack)
* [Project Structure](#-project-structure)
* [Database Design](#-database-design)
* [Installation](#-installation)
* [Usage](#-usage)
* [Future Improvements](#-future-improvements)
* [Author](#-author)

---

## 🧠 About the Project

The **Movie Ticket Management System** is designed to automate and simplify the movie ticket booking process for both customers and cinema administrators.

It eliminates manual ticket handling by providing:

* A user-friendly interface for customers
* An efficient management system for admins
* Secure booking records with export and verification support

This project is ideal for demonstrating **Java OOP concepts**, **JavaFX UI design**, **database integration**, and **real-world software architecture**.

---

## 🚀 Features

### 🎟 User Features

* View available movies and showtimes
* Book tickets for selected movies
* View and cancel bookings
* Booking confirmation with **QR code**
* Email notifications *(optional)*

### 🧑‍💼 Admin Features

* Add, edit, and delete movies
* Manage showtimes and ticket pricing
* View booking reports
* Export booking/ticket data as **PDF**
* Validate tickets using **Booking ID or QR Code**

---

## ⚙️ Tech Stack

| Category      | Technology                         |
| ------------- | ---------------------------------- |
| Language      | Java (JDK 17+)                     |
| UI Framework  | JavaFX                             |
| Database      | MySQL                              |
| Build Tool    | Maven                              |
| IDE           | IntelliJ IDEA / NetBeans / Eclipse |
| Email Service | JavaMail API                       |
| QR Code       | ZXing Library                      |
| PDF Export    | iText / PDFBox                     |

---

## 🗂️ Project Structure

```text
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
│   │
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
```

---

## 🧩 Database Design

**Database Name:** `movie_ticket_db`

### Tables

#### `users`

* id
* name
* email
* password
* role

#### `movies`

* id
* title
* genre
* duration
* trailer_url
* poster
* description

#### `showtimes`

* id
* movie_id
* date
* time
* price

#### `bookings`

* id
* user_id
* showtime_id
* seats
* booking_date

#### `payments`

* id
* booking_id
* amount
* payment_method
* transaction_id
* payment_date

---

## 🖥️ Installation

### Prerequisites

* Java JDK 17 or higher
* MySQL Server & MySQL Workbench
* IntelliJ IDEA / NetBeans / Eclipse
* JavaFX SDK *(if not bundled with IDE)*

### Steps

1. **Clone the repository**

   ```bash
   git clone https://github.com/yourusername/MovieTicketManagementSystem.git
   ```

2. **Open the project** in your preferred IDE.

3. **Configure the database**

   * Create a database named `movie_ticket_db`
   * Import the SQL script:

     ```sql
     database/movie_ticket_db.sql
     ```

4. **Update database credentials** in `DBConnection.java`

   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/movie_ticket_db";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password";
   ```

5. **Run the application** from `HelloApplication.java`.

---

## 🧭 Usage

* Launch the application
* Log in as **User** or **Admin**
* Users can browse movies and book tickets
* Admins can manage movies, showtimes, and reports
* Tickets can be verified using **QR Code or Booking ID**

---

## 🔮 Future Improvements

* Integrate online payment gateway *(SSLCommerz / Stripe)*
* Seat map visualization during booking
* Movie recommendation system
* Web-based version using **React & Spring Boot**
* User profile and booking history system

---

## 👨‍💻 Author

**Abid Hasan**
🎓 3rd Year CSE Student, Southeast University
💡 Passionate about Java, DSA, and full-stack development

---

⭐ *If you like this project, consider giving it a star!*
