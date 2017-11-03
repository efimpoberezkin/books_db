call conf.bat

rmdir /s /q "%MONGO_DATA_DIR%\%PRIMARY%"
rmdir /s /q "%MONGO_DATA_DIR%\%SECONDARY%"
rmdir /s /q "%MONGO_DATA_DIR%\%ARBITER%"