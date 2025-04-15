docker build -t meal-planner .
docker stop meal-planner || true
docker rm meal-planner || true
docker run -d -p 8080:8080 --name meal-planner meal-planner
