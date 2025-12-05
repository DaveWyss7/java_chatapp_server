# Chat Server Backend

Reactive Spring Boot Chat Server mit PostgreSQL, JWT Authentication und WebSocket Support.

## Features
- ✅ JWT Authentication
- ✅ Reactive R2DBC (PostgreSQL)
- ✅ REST API für User/ChatRoom/Messages
- ✅ WebSocket für Echtzeit-Chat
- ✅ Docker Compose Setup

## Quick Start

### Mit Docker:
```bash
docker-compose up --build
```

### Lokal (PostgreSQL muss laufen):
```bash
./mvnw spring-boot:run
```

## API Endpoints

### Authentication
- `POST /api/auth/register` - User Registration
- `POST /api/auth/login` - User Login

### Chat Rooms
- `GET /api/chatrooms` - Get all chat rooms
- `POST /api/chatrooms` - Create chat room (requires auth)
- `GET /api/chatrooms/{id}` - Get chat room by ID

### Messages
- `GET /api/chat/rooms/{roomId}/messages?limit=50` - Get messages
- `POST /api/chat/rooms/{roomId}/messages` - Send message (requires auth)

### WebSocket
- `ws://localhost:8080/ws/chat?token=<JWT_TOKEN>` - Real-time chat

