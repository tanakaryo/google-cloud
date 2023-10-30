# Execution

## how to execute in local
`  mvn compile exec:java \
      -Dexec.mainClass=com.example.App \
      -Dexec.args="--inputText=Wow"`

## Deploy to Google Cloud and Execute Dataflow Job
`mvn -Pdataflow-runner compile exec:java -Dexec.mainClass=com.example.App -Dexec.args="--project=nifty-xxxx --gcpTempLocation=gs://xxx/temp/ --runner=DataflowRunner --region=asia-northeast1"`
