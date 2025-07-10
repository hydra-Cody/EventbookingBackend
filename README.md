## 🔗 Project Links

- 🌐 **Live Site**: [https://food-zjio.vercel.app/](https://food-zjio.vercel.app/)
- 🛠️ **Backend Repo**: [Event Booking Backend](https://github.com/hydra-Cody/EventbookingBackend)
- 🎨 **Frontend Repo**: [Event Booking Frontend](https://github.com/anuragdw710/EventBooking)

---

# 🎟️ Event Booking Application

A Spring Boot-based RESTful web application that enables users to **create**, **view**, and **manage events** with ease. Perfect for organizers and attendees alike!

---

## 🚀 Tech Stack

| Layer | Technology |

| ------------- | -------------------------- |

| 🧠 Language | Java 17 |

| 🧰 Framework | Spring Boot 3+ |

| 💾 Database | PostgreSQL / MySQL / H2 |

| 📦 ORM/JPA | Hibernate, Spring Data JPA |

| ✅ Validation | Jakarta Bean Validation |

| ⚙️ Build Tool | Maven |

---

## 🗃️ Database Schema

### 👤 Users Table

| Field | Type | Description |

| ----------- | -------- | ------------------------- |

| `id` | Long | Primary key |

| `name` | String | User's full name |

| `email` | String | Unique email address |

| `password` | String | Encrypted password |

| `role` | Enum | `USER` or `ORGANIZER` |

| `createdAt` | DateTime | Timestamp of registration |

---

### 📅 Events Table

| Field | Type | Description |

| ------------- | ------- | --------------------------- |

| `id` | Long | Primary key |

| `title` | String | Name of the event |

| `description` | String | Optional details |

| `location` | String | Physical or virtual address |

| `eventDate` | Date | Scheduled date |

| `imageUrl` | String | Cover image URL |

| `capacity` | Integer | Max attendees (positive) |

| `latitude` | Double | Geo-location |

| `longitude` | Double | Geo-location |

| `organizerId` | Long | Reference to `users(id)` |

---

## 🌐 API Endpoints

> Base URL: `/api`

### 👤 Users

#### 📌 Register a User

**POST** `/api/users/register`

```json
{
  "name": "John Doe",

  "email": "john@example.com",

  "password": "secret123",

  "role": "ORGANIZER"
}
```

🔍 Get User by ID

**GET** /api/users/{id}

📅 Events

✍️ Create an Event

**POST** /api/events?organizerId={organizerId}

```json
{
  "title": "Spring Boot Conference",

  "description": "Deep dive into Spring Boot 3",

  "location": "Online",

  "eventDate": "2025-08-01",

  "imageUrl": "https://example.com/spring.jpg",

  "capacity": 200,

  "latitude": 28.6139,

  "longitude": 77.209
}
```

✅ 201 Created: Event created successfully

❌ 404 Not Found: Invalid organizerId

❗ 400 Bad Request: Missing or invalid fields

📃 Get All Events

**GET** /api/events

🔍 Get Event by ID

**GET** /api/events/{id}

▶️ Run the Project Locally

Clone the Repository

```json

bash

Copy

Edit

git clone https://github.com/your-username/event-booking-app.git

cd event-booking-app

Configure application.properties
```

<details>  
<summary>🛠️ PostgreSQL Example</summary>

```json
properties

Copy

Edit

spring.datasource.url=jdbc:postgresql://localhost:5432/eventdb

spring.datasource.username=postgres

spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
```

</details>  
<details>  
<summary>🧪 In-Memory H2 (Dev Only)</summary>

```json

properties

Copy

Edit

spring.datasource.url=jdbc:h2:mem:testdb

spring.datasource.driver-class-name=org.h2.Driver

spring.datasource.username=sa

spring.datasource.password=

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

spring.h2.console.enabled=true

spring.jpa.hibernate.ddl-auto=update
```

</details>

Run the App

```json

bash

Copy

Edit

./mvnw spring-boot:run

Access Swagger UI at: http://localhost:8080/swagger-ui.html (if enabled)

```

📌 Notes

Make sure to provide a valid organizerId when creating events.

You can handle bookings and authentication as next features.

📬 Contact

For feedback, bugs, or suggestions, feel free to reach out via issues or email.

🧡 Happy Coding!
