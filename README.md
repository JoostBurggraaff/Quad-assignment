## Getting Started

### Prerequisites

- Java 17 installed
- Maven installed
- Node.js 21.x

### Backend Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/JoostBurggraaff/Quad-assignment
   cd <repo-folder>/backend

2. **Build the backend**

To build the Spring Boot application using Maven, run:

    
    mvn clean install

3. **Run the backend**

To start the Spring Boot backend server, run:

    mvn spring-boot:run
The server should start at http://localhost:8080.


### Frontend Setup

1. **Navigate to the frontend folder**


    cd <repo-folder>/frontend

2. **Install dependencies**

Install all required packages using npm:

    npm install

3. **Build the Angular application**

To build the Angular project for production:

    ng build --prod

4. **Serve the frontend**

To serve the Angular frontend in development mode:

    ng serve
The UI will be available at http://localhost:4200.

### Integration
By default, the frontend is designed to interact with the backend on http://localhost:8080. Ensure both the frontend and backend servers are running.