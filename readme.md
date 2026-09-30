> docker run -p 27017:27017 --name=mongodb_container -d mongo
> docker exec -it b55e bash
> mongosh
	> use test
	> db.employees.find()