```bash
# build jar file
mvn install

# build a docker image with jar
docker build -t test .
# run the image
docker run -p 8080:8080 test

# build an image with maven
# instruction: https://codelabs.developers.google.com/codelabs/cloud-run-deploy#3
# src ref: https://github.com/GoogleCloudPlatform/getting-started-java/blob/main/bookshelf/1-cloud-run/pom.xml
mvn package jib:build -Dimage=gcr.io/test-addon-on-alt-runtime/test:1.0.1
docker pull gcr.io/test-addon-on-alt-runtime/test:1.0.1
docker run -p 8080:8080 gcr.io/test-addon-on-alt-runtime/test:1.0.1
# deploy the service
gcloud run deploy --image=gcr.io/test-addon-on-alt-runtime/test:1.0.1 --platform managed
gcloud run deploy test --image=gcr.io/test-addon-on-alt-runtime/test:1.0.1 --platform managed --region=europe-west2
```


Google GCP docker repository: https://console.cloud.google.com/artifacts?project=test-addon-on-alt-runtime

GCL cloud run: https://console.cloud.google.com/run?hl=en&project=test-addon-on-alt-runtime

GWAO on alt runtime: https://developers.google.com/workspace/add-ons/guides/alternate-runtimes#java

GWM SDK: https://console.cloud.google.com/apis/api/appsmarket-component.googleapis.com/credentials?project=test-addon-on-alt-runtime&hl=en

Card builder: https://addons.gsuite.google.com/uikit/builder

Sample github project: https://github.com/googleworkspace/add-ons-samples/blob/ece111df69770694e83a6e893982d24e7a3fd6cd/java/3p-resources/src/main/java/Create3pResources.java 

Log viewer: https://console.cloud.google.com/logs/query;cursorTimestamp=2024-09-17T19:56:15.927113Z;duration=PT1H?referrer=search&hl=en&project=test-addon-on-alt-runtime
