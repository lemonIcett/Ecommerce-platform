\# DevOps-Driven Microservices E-Commerce Platform



!\[CI/CD Pipeline](https://github.com/lemonIcett/Ecommerce-platform/actions/workflows/ci.yml/badge.svg)

!\[License: MIT](https://img.shields.io/badge/License-MIT-green.svg)



A production-grade distributed e-commerce system demonstrating end-to-end DevOps practices — containerization, automated CI/CD, and live observability.



\---



\## Architecture

\[ User Service :8081 ] ──┐

\[ Product Service :8082 ] ──┼──► \[ Docker Compose Network ]

\[ Order Service :8083 ] ──┘         │

\[ Prometheus :9090 ]

\[ Grafana :3000 ]



\---



\## Tech Stack



| Layer | Technology |

|---|---|

| Backend | Java 17, Spring Boot 3 |

| Build | Maven, JUnit, Mockito, JaCoCo |

| Containers | Docker, Docker Compose |

| CI/CD | GitHub Actions |

| Monitoring | Prometheus, Grafana |

| Auth | JWT |



\---



\## Services



| Service | Port | Responsibility |

|---|---|---|

| user-service | 8081 | Registration, login, JWT auth |

| product-service | 8082 | Product CRUD, inventory |

| order-service | 8083 | Place orders, calls product-service |



\---



\## How to Run Locally



\### Prerequisites

\- Docker Desktop installed and running

\- Java 17+

\- Maven 3.9+



\### Start everything



```bash

git clone https://github.com/lemonIcett/Ecommerce-platform.git

cd Ecommerce-platform

docker-compose up --build

```



\### Access the services

\- User Service: http://localhost:8081

\- Product Service: http://localhost:8082

\- Order Service: http://localhost:8083

\- Grafana Dashboard: http://localhost:3000 (admin / admin)

\- Prometheus: http://localhost:9090



\---



\## CI/CD Pipeline



Every push to `dev` triggers the pipeline:



1\. Maven build + JUnit tests

2\. JaCoCo coverage report generated

3\. Docker images built and pushed to Docker Hub

4\. Pipeline fails and blocks deploy if any test fails



\---



\## Monitoring



Live Grafana dashboard tracks:

\- HTTP request rate per service

\- Error rate (5xx responses)

\- JVM heap memory usage

\- p95 response time



\---



\## Project Structure

devops-ecommerce-platform/

├── user-service/

├── product-service/

├── order-service/

├── monitoring/

│   ├── prometheus/

│   └── grafana/

├── .github/

│   └── workflows/

└── docker-compose.yml



\---



\## License



MIT

