# Rapido - Multipurpose Framework for Rapid Application Server Prototyping with Java 8

Deployment with Docker and Kubernetes on Google Cloud

## CORS setup validation

```
curl -H "Origin: http://example.com" \
  -H "Access-Control-Request-Method: POST" \
  -H "Access-Control-Request-Headers: X-Requested-With" \
  -X OPTIONS --verbose \
  http://localhost:8080/api/ping?echo=corstest
```
