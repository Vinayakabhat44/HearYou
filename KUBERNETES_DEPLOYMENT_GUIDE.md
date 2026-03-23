# Kubernetes Deployment Guide for MitraAI

This document provides a concise, step‑by‑step guide to deploy the MitraAI application on a Kubernetes cluster.

## Prerequisites
- Kubernetes cluster (Kind, Minikube, or a cloud provider).
- `kubectl` configured to talk to the cluster.
- Container registry access (Docker Hub, GitHub Packages, ECR, etc.).
- Helm 3 installed (optional but recommended).

## 1. Build & Push Docker Images
```bash
# From the project root
docker build -t <registry>/mitra-gateway-service:latest ./mitra-gateway-service
docker build -t <registry>/mitra-auth-service:latest ./mitra-auth-service
docker build -t <registry>/mitra-core-service:latest ./mitra-core-service
docker build -t <registry>/mitra-discovery-service:latest ./mitra-discovery-service

# Push images
docker push <registry>/mitra-gateway-service:latest
docker push <registry>/mitra-auth-service:latest
docker push <registry>/mitra-core-service:latest
docker push <registry>/mitra-discovery-service:latest
```

## 2. Create Helm Chart (or use raw manifests)
```bash
helm create mitraai
# Remove the example templates and add the following files:
#   templates/deployment.yaml (gateway, auth, core, discovery)
#   templates/service.yaml
#   templates/ingress.yaml
#   templates/configmap.yaml
#   templates/secret.yaml (DB passwords, JWT keys)
#   templates/pvc.yaml (MySQL StatefulSet)
#   templates/redis.yaml (optional – use official chart)
```

## 3. Populate `values.yaml`
```yaml
image:
  repository: <registry>/mitra-gateway-service
  tag: latest
replicaCount: 2
service:
  type: ClusterIP
  port: 8080
ingress:
  enabled: true
  hosts:
    - host: mitra.example.com
      paths:
        - path: /
          pathType: Prefix
mysql:
  auth:
    rootPassword: "${MYSQL_ROOT_PASSWORD}"
    database: mitra_core
  persistence:
    enabled: true
    size: 5Gi
redis:
  enabled: true
```

## 4. Deploy to the Cluster
```bash
helm upgrade --install mitraai ./mitraai -f values.yaml
```

## 5. Verify Deployment
```bash
kubectl get pods -n default
kubectl get svc -n default
kubectl describe ingress -n default
```

Visit `http://<ingress-host>/api/feed/hierarchical` with a valid JWT token.

## 6. Optional Production Hardening
- Use **SealedSecrets** or **Vault** for sensitive data.
- Enable TLS via **cert‑manager** and set `ingress.tls` in `values.yaml`.
- Add **HorizontalPodAutoscaler** for `mitra-core-service`.
- Configure **Prometheus**/Grafana for metrics and **ELK** for logs.

---
*Generated on 2026-03-08.*
