## Create an Application 

rhc app create -a jobfinder -t jbossews-1.0 -l <openshift_login_email> -d

## Adding MongoDB and RockMongo Client Cartridge

```
rhc cartridge add -a jobfinder -c mongodb-2.0 -l <openshift_login_email>
rhc cartridge add -a jobfinder -c rockmongo-1.1 -l <openshift_login_email>
rhc cartridge add -a jobfinder -c postgresql-8.4 -l <openshift_login_email>

```

## Pulling code from github and pushing to OpenShift

After you have created the application using rhc create app command and added MongoDB,PostgreSQL,and RockMongoDB client cartridge using rhc cartridge add command you have to checkout the code from my github. To do that follow the steps mentioned below.

```
git remote add jobfinder -m master git@github.com:shekhargulati/jobfinder.git
 
git pull -s recursive -X theirs jobfinder master

```
## Importing Jobs Data to MongoDB

```
rhc app show -a jobfinder -l <openshift_login_email>

scp jobs-data.json <instance_ssh_access>:app-root/data

ssh <instance_ssh_access>

mongoimport -d jobfinder -c jobs --file jobs-data.json -u $OPENSHIFT_MONGODB_DB_USERNAME -p $OPENSHIFT_MONGODB_DB_PASSWORD -h $OPENSHIFT_MONGODB_DB_HOST -port $OPENSHIFT_MONGODB_DB_PORT

login to database using mongo client and create a 2d index
> db.jobs.ensureIndex({"location":"2d"})

```

## Run MongoDB queries

While you are in mongo shell lets execute some commands

```
Count of all the Java jobs near to my location

db.jobs.find({"location":{$near : [48.1530699,11.5992338]},"skills":"java"}).limit(2)


Give me Address of all the Java jobs near to my location

db.jobs.find({"location":{$near : [48.1530699,11.5992338]},"skills":"java"},{"formattedAddress":1}).limit(2)
```

## Deploy the application to cloud

```
git push
```

## Application in Cloud

The application will be up and running on OpenShift and you can see results on following urls
https://jobfinder-<domain-name>.rhcloud.com/