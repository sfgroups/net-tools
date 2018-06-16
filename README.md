
docker build -t sfgroups/nettools .

docker push sfgroups/nettools

docker tag sfgroups/nettools sfgroups/nettools:1.0
docker push sfgroups/nettools:1.0

docker run --rm -it -p 8080:80 --name nettools sfgroups/nettools

docker build -t sfgroups/alphine_nettools .

docker exec -ti nettools bash


ab -n 10  http://localhost:8080/

# net-tools
