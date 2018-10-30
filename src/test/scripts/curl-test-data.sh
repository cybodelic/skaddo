#!/usr/bin/env bash
# Create players
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"Frodo@mittelerde.de\", \"nickName\" : \"Froddo\", \"firstName\" : \"Frodo\", \"lastName\" : \"Beutlin\" }" http://localhost:8080/users;
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"gandalf@mittelerde.de\", \"nickName\" : \"Gandi3000\", \"firstName\" : \"Gandalf\" }" http://localhost:8080/users;
curl -X POST -H "Content-Type:application/json" -d "{ \"userID\" : \"Aragorn@mittelerde.de\", \"nickName\" : \"Argon\" }" http://localhost:8080/users;

# Create a player group
PGURI=`curl -i -X POST -H "Content-Type:application/json" -d "{ \"name\" : \"Die Gefährten\" }" http://localhost:8080/playerGroups |grep Location |awk '{print $2}'`;
PGURI=`tr -dc '[[:print:]]' <<< "${PGURI}"`;
groupId=`awk -F/ '{print $NF}' <<< ${PGURI}`;
echo "Created player group "${PGURI};

# create associations for players and player group
curl -X PUT -d $'http://localhost:8080/users/gandalf@mittelerde.de\nhttp://localhost:8080/users/Frodo@mittelerde.de\nhttp://localhost:8080/users/Aragorn@mittelerde.de' -H "Content-Type:text/uri-list" $PGURI/players;
curl -X PUT -d "http://localhost:8080/users/Frodo@mittelerde.de" -H "Content-Type:text/uri-list" $PGURI/createdBy;

# create a match
MURI=`curl -i -X POST -H "Content-Type:application/json" -d "{}" http://localhost:8080/matches |grep Location |awk '{print $2}'`;
MURI=`tr -dc '[[:print:]]' <<< "${MURI}"`;
echo "Created "${MURI};

# create a round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":18}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PUT -d "http://localhost:8080/users/gandalf@mittelerde.de" -H "Content-Type:text/uri-list" ${RURI}/declarer;
curl -X PUT -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;
# create another round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":72}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PUT -d "http://localhost:8080/users/gandalf@mittelerde.de" -H "Content-Type:text/uri-list" ${RURI}/declarer;
curl -X PATCH -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;
# create another round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":-24}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PUT -d "http://localhost:8080/users/gandalf@mittelerde.de" -H "Content-Type:text/uri-list" ${RURI}/declarer;
curl -X PATCH -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;
# create another round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":-72}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PUT -d "http://localhost:8080/users/Frodo@mittelerde.de" -H "Content-Type:text/uri-list" ${RURI}/declarer;
curl -X PATCH -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;
# create another round and add to match
RURI=`curl -i -X POST -H "Content-Type:application/json" -d "{\"score\":120}" http://localhost:8080/rounds |grep Location |awk '{print $2}'`;
RURI=`tr -dc '[[:print:]]' <<< "${RURI}"`;
echo "Created "${RURI};
curl -X PUT -d "http://localhost:8080/users/Aragorn@mittelerde.de" -H "Content-Type:text/uri-list" ${RURI}/declarer;
curl -X PATCH -d "${RURI}" -H "Content-Type:text/uri-list" $MURI/rounds;

# add match to player group
curl -X PUT -d "${MURI}" -H "Content-Type:text/uri-list" $PGURI/matches;

# create another match and add to player group
MURI=`curl -i -X POST -H "Content-Type:application/json" -d "{}" http://localhost:8080/matches |grep Location |awk '{print $2}'`;
MURI=`tr -dc '[[:print:]]' <<< "${MURI}"`;
echo "Created "${MURI};
curl -X PATCH -d "${MURI}" -H "Content-Type:text/uri-list" $PGURI/matches;

curl -i ${PGURI}"/scores";