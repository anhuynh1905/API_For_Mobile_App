# API for Mobile App

This project is a Spring Boot application that serves as the backend API for a mobile application. It provides endpoints for user management, media management (uploading, fetching, and deleting music and video files), and profile updates, including profile picture uploads.

## Features

- **User Management**: Handles authentication and profile updates.
- **Media Management**: Supports uploading, fetching, and deleting media files (both music and video).
- **Profile Picture Uploads**: Allows users to update their profile pictures.
- **Azure Integration**:
  - **Azure SQL Database** for data persistence.
  - **Azure Blob Storage** for storing media files and profile pictures.
- **Security**: Uses JSON Web Tokens (JWT) for secure API access.
- **Email Service**: Configured for sending OTP emails during authentication.

## Configuration

The application is configured via the `application.properties` file located in `Mobile_API/src/main/resources/application.properties`. Key configuration properties include:

- **Database Connection**:
  - `spring.datasource.url`
  - `spring.datasource.username`
  - `spring.datasource.password`

- **JPA/Hibernate Settings**:
  - `spring.jpa.hibernate.ddl-auto`
  - `spring.jpa.show-sql`
  - `spring.jpa.properties.hibernate.dialect`

- **JWT**:
  - `jwt.secret`
  - `jwt.expiration`

- **Email Settings**:
  - `spring.mail.host`
  - `spring.mail.port`
  - `spring.mail.username`
  - `spring.mail.password`

- **Azure Blob Storage**:
  - `azure.storage.connection-string`
  - `azure.storage.container.music` (container for music files)
  - `azure.storage.container.video` (container for video files)
  - `azure.storage.container.profiles` (container for profile pictures)

**Note:** Environment variables such as `AZURE_SQL_DATABASE`, `AZURE_SQL_USERNAME`, `AZURE_SQL_PASSWORD`, `JWT_SECRET_KEY`, `EMAIL_HOST`, `EMAIL_PORT`, `EMAIL_USERNAME`, `EMAIL_PASSWORD`, and `AZURE_STORAGE_CONNECTION_STRING` must be set accordingly.

## Getting Started

### Prerequisites

- Java 17 or later
- Maven 3.6+
- An Azure account with SQL Database and Blob Storage configured

### Setup & Running Locally

1. **Clone the Repository**
   ```bash
   git clone https://github.com/anhuynh1905/API_For_Mobile_App.git
   ```

2. **Set Environment Variables**
   Ensure all required environment variables are properly configured.

3. **Build the Application**
   ```bash
   cd API_For_Mobile_App/Mobile_API
   mvn clean install
   ```

4. **Run the Application**
   ```bash
   mvn spring-boot:run
   ```
   The application runs by default on port `8080`.

## API Endpoints

### Media Management

- **Upload Media File**
  - **Endpoint:** `POST /api/media/upload`
  - **Description:** Upload a media file (music or video) with additional metadata (title, description, type).

- **Get User Media Files**
  - **Endpoint:** `GET /api/media`
  - **Description:** Retrieve all media files for the authenticated user.

- **Get Specific Media Details**
  - **Endpoint:** `GET /api/media/{id}`
  - **Description:** Fetch details of a specific media file by its ID.

- **Delete Media File**
  - **Endpoint:** `DELETE /api/media/{id}`
  - **Description:** Delete a specific media file by its ID.

### Profile Management

- **Update Profile**
  - **Endpoint:** `PUT /api/profile`
  - **Description:** Update profile information such as full name and email.

- **Upload Profile Picture**
  - **Endpoint:** `POST /api/profile/picture`
  - **Description:** Upload a new profile picture image.

## Contributing

Huỳnh Văn Đức An - HCMUTE

Note: Not testing yet !!

