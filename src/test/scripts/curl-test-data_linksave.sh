#!/usr/bin/env bash
# Create players
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"Frodo@mittelerde.de\", \"nickName\" : \"Froddo\", \"firstName\" : \"Frodo\", \"lastName\" : \"Beutlin\" }" http://localhost:8080/users;
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"gandalf@mittelerde.de\", \"nickName\" : \"Gandi3000\", \"firstName\" : \"Gandalf\" }" http://localhost:8080/users;
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"Aragorn@mittelerde.de\", \"nickName\" : \"Argon\" }" http://localhost:8080/users;

# create a match
MURI=`curl -i -X POST -H "Content-Type:application/json" -d "{}" http://localhost:8080/matches |grep Location |awk '{print $2}'`;
MURI=`tr -dc '[[:print:]]' <<< "${MURI}"`;
echo "Created "${MURI};

# create a round not added to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":1}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};

# create a round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":18}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X POST -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;

# create a round not added to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":1}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};

# create a round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":72}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X POST -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;

# create a round not added to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":1}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};

# create a round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":36}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PATCH -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;