# ID-Processor Service

Микросервис на **Java 23** и **Spring Boot 4.0.4**, предназначенный для высокопроизводительной обработки идентификаторов и интеграции с **Apache Kafka**. Сервис оптимизирован для работы под высокой нагрузкой и полностью интегрирован в экосистему мониторинга.

## Стек технологий

* **Core:** Java 23, Spring Boot 4.0.4
* **Messaging:** Apache Kafka (Confluent Platform)
* **Build Tool:** Maven
* **Environment:** Docker / Docker Compose
* **Observability:** Prometheus, Grafana, Kafka Exporter

---

## Архитектура и функционал

Сервис реализует RESTful API для приема данных, их предварительной обработки и последующей отправки в топик `id-processor-topic`.

### Основные фичи:
1.  **Batch Processing:** Оптимизированная отправка сообщений в Кафку для снижения нагрузки на сеть.
3.  **Observability-first:** Полное покрытие метриками Micrometer и кастомными тегами.

---

## 📊 Мониторинг и Observability

Сервис поставляется с готовым набором инструментов для анализа производительности:

### Ключевые метрики:
* **Kafka Consumer Lag:** Очередь необработанных сообщений в топике.
* **P95 Sent Time:** Время отправки 95% батчей (Latency).
* **JVM Resources:** Мониторинг `CodeCache` (segmented), Load Average и использования CPU.

---

## 📦 Запуск окружения

Для запуска всего стека (Kafka, Zookeeper, Prometheus, Grafana, Exporter) используйте Docker Compose:

```bash
docker-compose up -d
```

### Эндпоинты:
* **Service API:** `http://localhost:8080`
* **Metrics (Prometheus format):** `http://localhost:8080/actuator/prometheus`
* **Prometheus UI:** `http://localhost:9090`
* **Grafana:** `http://localhost:3000`

---


## 📈 Нагрузочное тестирование (JMeter)

Сервис успешно проходит стресс-тесты. Типичные показатели при нагрузке:
* **Load Average:** ~19.4 (на 12-ядерной системе).
* **p95 Latency:** ~0.133s (при росте интенсивности на 50%+).

---

## Конфигурация
Настройку размера батча можно найти в application.yaml `batch-size: 100`
