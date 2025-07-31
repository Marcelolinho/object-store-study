# Docker Para MinIO #

````bash
docker run -p 9000:9000 -p 9001:9001 \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  --name minio \
  quay.io/minio/minio server /data --console-address ":9001"
````