# net-tools

Network debugging tools

docker build -t sfgroups/nettools .

docker push sfgroups/nettools

docker tag sfgroups/nettools sfgroups/nettools:1.0

docker push sfgroups/nettools:1.0

docker run --rm -it -p 2222:22 -p 8080:80 --name nettools sfgroups/nettools

docker exec -ti nettools bash

ab -n 10  http://localhost:8080/

