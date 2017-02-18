wget https://bxms-qe-jenkins.rhev-ci-vms.eng.rdu2.redhat.com/job/BxMS/job/binaries/job/binaries-all/job/jboss-eap-6.4/lastSuccessfulBuild/artifact/server.zip --no-check-certificate 
unzip server.zip
rm server.zip
wget https://bxms-qe-jenkins.rhev-ci-vms.eng.rdu2.redhat.com/job/BxMS/job/binaries/job/binaries-all/job/bxms-6.4/lastSuccessfulBuild/artifact/binaries/brms.deployable.eap6.latest.zip --no-check-certificate 
unzip -o brms.deployable.eap6.latest.zip
rm brms.deployable.eap6.latest.zip
./jboss-eap-6.4/bin/add-user.sh -a --user kieserver --password kieserver1! --role kie-server,rest-all
./jboss-eap-6.4/bin/standalone.sh &
echo ~~~~~~~~~~~~~~~~~~~~~~~
echo Sleep until EAP starts ~ 1m
echo ~~~~~~~~~~~~~~~~~~~~~~~
sleep 1m
echo ~~~~~~~~~~~~~~~~~~~~~~~
echo Maven build now
echo ~~~~~~~~~~~~~~~~~~~~~~~
mvn clean install
echo ~~~~~~~~~~~~~~~~~~~~~~~
echo Shutting down
echo ~~~~~~~~~~~~~~~~~~~~~~~
./jboss-eap-6.4/bin/jboss-cli.sh -c command=shutdown
