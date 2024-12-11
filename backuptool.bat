@echo off
:loop
mysqldump -u root -puw1kIYuqRcRS6@SgX@ hso > backup.sql
timeout /t 1800 /nobreak > NUL
goto loop
