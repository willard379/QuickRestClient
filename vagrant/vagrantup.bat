@echo off
call bundle --version > nul
if not %ERRORLEVEL% == 0 (
  call gem install bundler
)

call berks --version > nul
if not %ERRORLEVEL% == 0 (
  call bundle install
)

if not exist "cookbooks" (
  call bundle exec berks vendor cookbooks
)
@echo on

@vagrant up