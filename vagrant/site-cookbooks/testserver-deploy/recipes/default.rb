#
# Cookbook Name:: testserver-deploy
# Recipe:: default
#
# Copyright 2015, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

workspace_path = '/opt/workspace'
repo_name = 'QuickRestClient-TestServer'

directory 'workspace' do
  mode '0777' 
  path "#{workspace_path}"
  action :create
end

git 'checkout' do
  destination "#{workspace_path}/#{repo_name}"
  repository 'https://github.com/willard379/QuickRestClient-TestServer.git'
  action :sync
end

script 'build' do
  interpreter "bash"
  code <<-EOH
    cd #{workspace_path}/#{repo_name}
    mvn clean package
    cp -f target/test-server-*.war /var/lib/tomcat/webapps/test-server.war
  EOH
  notifies :restart, 'service[tomcat]'
end

service 'tomcat' do
  service_name 'tomcat'
  supports :restart => true
end