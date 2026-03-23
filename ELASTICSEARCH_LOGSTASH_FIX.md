# Resolving Logstash → Elasticsearch Index Errors

The logs you posted show two main problems:

1. **Invalid index name** – Logstash tries to write to an index called `MitraAI-logs-2026.03.08`. Elasticsearch requires **lower‑case** index names.
2. **APM‑Server template missing** – The APM server cannot find the `traces-apm` index template, which results in a 404.

Below is a concise, step‑by‑step guide to fix both issues.

---

## 1️⃣ Fix the Logstash index name (lower‑case)

### a) Update `logstash/pipeline/logstash.conf`

```conf
input {
  # your existing input definitions (e.g. beats, tcp, etc.)
}

filter {
  # any filters you already have
}

output {
  elasticsearch {
    hosts => ["http://elasticsearch:9200"]
    # Use a lower‑case index name. The %{+YYYY.MM.dd} pattern adds the date.
    index => "mitraai-logs-%{+YYYY.MM.dd}"
    user => "elastic"            # <-- adjust if you use a different user
    password => "${ELASTIC_PASSWORD}"   # set via .env or docker‑compose env
    ssl => false
  }
  # optional: stdout { codec => rubydebug }
}
```

**Why:** Elasticsearch rejects any index name containing uppercase letters. Changing the pattern to `mitraai-logs-%{+YYYY.MM.dd}` satisfies the requirement.

### b) Re‑deploy Logstash
```bash
# From the project root
docker-compose stop logstash
docker-compose up -d logstash
```
Check the logs again:
```bash
docker logs -f mitra-ai-logstash-1
```
You should no longer see the `invalid_index_name_exception` error.

---

## 2️⃣ Resolve the APM‑Server template error

The APM server expects the `traces-apm` index template to exist. There are two common ways to address this:

### Option 1 – Let the APM server install the template automatically
1. Ensure the environment variable `setup.kibana.host` points to your Kibana service (e.g. `http://kibana:5601`).
2. Add the following to the **APM‑server** service definition in `docker-compose.yml`:
```yaml
  apm-server:
    image: docker.elastic.co/apm/apm-server:8.12.0
    environment:
      - output.elasticsearch.hosts=http://elasticsearch:9200
      - setup.kibana.host=http://kibana:5601
      - setup.template.enabled=true   # <-- make sure this is true
      - apm-server.secret_token=${APM_SECRET_TOKEN}
    ports:
      - "8200:8200"
```
3. Restart the APM server:
```bash
docker-compose up -d apm-server
```
The server will now create the `traces-apm` template on first start.

### Option 2 – Manually load the template (if you prefer a one‑time operation)
```bash
# Download the official template (adjust version if needed)
curl -L -o apm-template.json https://raw.githubusercontent.com/elastic/apm-server/main/_dev/apm-server/template.json

# Load it into Elasticsearch
curl -XPUT "http://localhost:9200/_template/traces-apm" -H 'Content-Type: application/json' -d @apm-template.json
```
After loading, restart the APM server.

---

## 3️⃣ Kibana authentication error (kibana_system)
The message:
```
Authentication of [kibana_system] was terminated by realm [reserved] - failed to authenticate user [kibana_system]
```
means the default `kibana_system` password does not match the one stored in Elasticsearch.

### Fix
1. Set the password in your `.env` (or directly in `docker-compose.yml`):
```dotenv
ELASTIC_PASSWORD=yourStrongElasticPassword
KIBANA_SYSTEM_PASSWORD=yourStrongKibanaPassword
```
2. Pass the password to the Kibana container:
```yaml
  kibana:
    image: docker.elastic.co/kibana/kibana:8.12.0
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch:9200
      - ELASTICSEARCH_USERNAME=elastic
      - ELASTICSEARCH_PASSWORD=${ELASTIC_PASSWORD}
      - SERVERNAME=kibana
      - XPACK_SECURITY_ENABLED=true
      - XPACK_ENCRYPTEDSAVEDOBJECTS_ENCRYPTIONKEY=${KIBANA_ENCRYPTION_KEY}
```
3. Re‑create the Kibana container:
```bash
docker-compose up -d kibana
```
Now Kibana should start without the authentication error.

---

## 4️⃣ Quick verification checklist
1. `docker logs -f mitra-ai-logstash-1` → No `invalid_index_name_exception`.
2. `curl -XGET http://localhost:9200/_cat/indices?v` → Index `mitraai-logs-2026.03.08` appears (lower‑case).
3. `docker logs -f mitra-ai-apm-server-1` → No `resource_not_found_exception` for `traces-apm`.
4. `docker logs -f mitra-ai-kibana-1` → No authentication failure for `kibana_system`.

---

## 5️⃣ Optional: Persist the changes in version control
Add the updated `logstash.conf` and the modified `docker-compose.yml` sections to your repo and commit:
```bash
git add logstash/pipeline/logstash.conf docker-compose.yml
git commit -m "Fix Logstash index name (lowercase) and APM template setup"
```

---

*Generated on 2026‑03‑08.*
