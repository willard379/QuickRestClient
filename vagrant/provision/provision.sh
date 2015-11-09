#!/bin/sh

# Set environment
USER_HOME=/home/vagrant
WORKSPACE=$USER_HOME/workspace

# System locale
sed -i -e "s/^LANG=.*/LANG="ja_JP.utf8"/g" /etc/sysconfig/i18n

# System clock
rm -rf /etc/localtime
ln -sf /usr/share/zoneinfo/Asia/Tokyo /etc/localtime

# SELinux
sed -i -e "s/^SELINUX=enabled/SELINUX=disabled/g" /etc/selinux/config
setenforce 0

# EPEL
yum localinstall -y http://dl.fedoraproject.org/pub/epel/6/x86_64/epel-release-6-8.noarch.rpm

# man
yum install -y man

# vim
yum install -y vim

# OpenJDK
yum install -y java-1.8.0-openjdk-devel
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk.x86_64
export PATH=$PATH:$JAVA_HOME/bin

# Tomcat
yum install -y tomcat
service tomcat start

# Maven
wget http://ftp.kddilabs.jp/infosystems/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.tar.gz
tar zxvf apache-maven-3.3.3-bin.tar.gz
export M2_HOME=$USER_HOME/apache-maven-3.3.3
export M2=$M2_HOME/bin
export PATH=$PATH:$M2

# Git
yum install -y git
mkdir $WORKSPACE

# Deploy test-server
cd $WORKSPACE
git clone https://github.com/willard379/QuickRestClient-TestServer.git
cd $WORKSPACE/QuickRestClient-TestServer
mvn package
cp target/test-server-0.0.1.war /var/lib/tomcat/webapps/test-server.war
service tomcat reload

# Packaging
cd $WORKSPACE
git clone https://github.com/willard379/QuickRestClient.git
cd $WORKSPACE/QuickRestClient
mvn compile test -DargLine="-Dserver.host=localhost"

exit 0