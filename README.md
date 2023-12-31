# Spring Boot PGP Encryption Demo

This Spring Boot application demonstrates user authentication and PGP encryption using Bouncy Castle.

## Features

- User authentication with a default user.
- PGP encryption of a JSON payload.
- Redirects to an external URL with the encrypted token.

## Prerequisites

- Java 8 or higher
- Maven

## Setup & Installation

1. Clone the repository:
    ```bash
    git clone [YOUR_REPOSITORY_LINK]
    cd [YOUR_REPOSITORY_DIRECTORY]
    ```

2. Update the `PgpEncryptionService.java` file with your PGP public key in Base64 format.

3. Build the project:
    ```bash
    mvn clean install
    ```

4. Run the application:
    ```bash
    mvn spring-boot:run
    ```

5. Access the application on:
    ```
    http://localhost:8080
    ```

## Usage

1. Login using the default credentials:
    ```
    Username: admin
    Password: tomcat
    ```

2. Once authenticated, the application will encrypt the provided JSON payload and redirect to the specified external URL with the encrypted token.

## Dependencies

- Spring Boot Web for RESTful APIs.
- Bouncy Castle for PGP encryption (`bcprov-jdk15on` and `bcpg-jdk15on`).

## Issues & Debugging

- Ensure that the public key used for encryption matches the private key used for decryption.
- If facing decryption issues like `premature end of stream in PartialInputStream`, ensure the encrypted data's integrity.

## License

[Your preferred license, e.g., MIT, GPL, etc.]
