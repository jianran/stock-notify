# Stock Notify

A Spring Boot application that monitors stock prices and sends Discord notifications when stocks drop by 3% within 1 hour.

## Features

- Monitors stock prices every 10 minutes (configurable)
- Detects price drops of 3% or more within 1 hour (configurable)
- Sends direct messages to Discord when thresholds are met
- Alpha Vantage API integration for US stock prices
- GitHub Actions CI/CD pipeline
- Docker support for easy deployment

## Prerequisites

- Java 17+
- Maven 3.8+
- Alpha Vantage API key (free at https://www.alphavantage.co/support/#api-key)
- Discord Bot token

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/your-username/stock-notify.git
cd stock-notify
```

### 2. Configure environment variables

Copy `.env.example` to `.env` and fill in your credentials:

```bash
cp .env.example .env
```

Edit `.env`:
```
ALPHA_VANTAGE_API_KEY=your_alpha_vantage_api_key_here
DISCORD_BOT_TOKEN=your_discord_bot_token_here
DISCORD_USER_ID=your_discord_user_id_here
```

### 3. Update stock symbols

Edit `src/main/resources/application.yml` to configure your stock symbols:

```yaml
stock:
  symbols:
    - AAPL
    - GOOGL
    - MSFT
    - TSLA
    - NVDA
```

### 4. Build and run

```bash
# Local development
mvn spring-boot:run

# Or build JAR and run
mvn clean package
java -jar target/stock-notify-1.0.0.jar
```

### 5. Run with Docker

```bash
docker-compose up --build
```

## Discord Bot Setup

1. Go to [Discord Developer Portal](https://discord.com/developers/applications)
2. Create a new application
3. Create a bot under the "Bot" tab
4. Copy the bot token
5. Get your User ID from Discord (enable Developer Mode, right-click your username)

## GitHub Actions CI/CD

The project includes a GitHub Actions workflow that:
- Runs on every push to main/develop branches
- Runs on every pull request
- Builds the project with Maven
- Runs tests
- Deploys to server when pushed to main branch

### Required GitHub Secrets

In your repository settings, add these secrets:
- `DEPLOY_HOST`: Server IP address
- `DEPLOY_USER`: SSH username
- `DEPLOY_SSH_KEY`: Private SSH key

## Configuration

### Stock Configuration

```yaml
stock:
  symbols: [AAPL, GOOGL, MSFT, TSLA]  # Stock symbols to monitor
  alpha-vantage-api-key: YOUR_KEY       # Alpha Vantage API key
  drop-threshold: 3.0                   # Price drop percentage threshold
  check-interval-minutes: 10            # How often to check prices
  history-window-hours: 1               # Time window for price comparison
```

### Discord Configuration

```yaml
discord:
  bot-token: YOUR_BOT_TOKEN     # Discord bot token
  user-id: YOUR_USER_ID         # Your Discord user ID
```

## Project Structure

```
stock-notify/
├── src/main/java/com/stocknotify/
│   ├── StockNotifyApplication.java      # Main application entry point
│   ├── config/
│   │   ├── StockConfig.java             # Stock configuration
│   │   └── DiscordConfig.java           # Discord configuration
│   ├── model/
│   │   ├── AlphaVantageResponse.java    # API response model
│   │   └── StockPriceData.java          # Stock price data model
│   └── service/
│       ├── StockPriceMonitorService.java # Main monitoring service
│       └── discord/
│           └── DiscordClient.java       # Discord API client
├── src/main/resources/
│   ├── application.yml                  # Main configuration
│   ├── application-dev.yml              # Development config
│   └── application-prod.yml             # Production config
├── .github/workflows/ci-cd.yml          # CI/CD pipeline
├── Dockerfile                           # Docker build config
├── docker-compose.yml                   # Docker Compose config
└── pom.xml                              # Maven dependencies
```

## API Endpoints

The application runs as a background service with no public API endpoints. To add monitoring endpoints:

```bash
# Health check (can be added)
GET /actuator/health
```

## License

MIT License
