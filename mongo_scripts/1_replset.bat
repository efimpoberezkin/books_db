call conf.bat

echo "deleting files for a clean start"

call rmdirs.bat

SLEEP 1

call mkdirs.bat

start mongod --port 27017 --dbpath "%MONGO_DATA_DIR%\%PRIMARY%" --replSet set
start mongod --port 27018 --dbpath "%MONGO_DATA_DIR%\%SECONDARY%" --replSet set
start mongod --port 30000 --dbpath "%MONGO_DATA_DIR%\%ARBITER%" --replSet set

SLEEP 5

start mongo --port 27017 --eval "config = { _id: 'set', members: [{ _id: 0, host: 'localhost:27017' },{ _id: 1, host: 'localhost:27018', hidden: true, priority: 0, slaveDelay: 30 },{ _id: 2, host: 'localhost:30000', arbiterOnly: true }]}; rs.initiate(config);"