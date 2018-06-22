DEPLOYMENT_REPO_ID="oss.sonatype.org"


H2_HOME=$HOME/.deploym2

M2REPOSITORY=$H2_HOME/repository
mkdir -p ${M2REPOSITORY}
cp build/oss.sonatype.settings.xml ${H2_HOME}/settings.xml

sed -i s,LOCAL_M2,$H2_HOME,g ${H2_HOME}/settings.xml
sed -i s/OSS_SONATYPE_USER/$OSS_SONATYPE_USER/g ${H2_HOME}/settings.xml
sed -i s/OSS_SONATYPE_PASS/$OSS_SONATYPE_PASS/g ${H2_HOME}/settings.xml
sed -i s/DEPLOYMENT_REPO_ID/$DEPLOYMENT_REPO_ID/g ${H2_HOME}/settings.xml

#GPG_PRIVATE_KEY=`cat build/gpg_private`
#echo GPG_PRIVATE_KEY: $GPG_PRIVATE_KEY
#gpg -v --batch --import <(echo "$GPG_PRIVATE_KEY")

mvn -s ${H2_HOME}/settings.xml -Dmaven.repo.local=$M2REPOSITORY deploy