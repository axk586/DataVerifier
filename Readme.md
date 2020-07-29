## This is a utility project to work around on the different file parsing

``` 
How to convert avro to csv using a dataflow
```
Step1 : 
1. Download the repo
2. move inside the root directory
3. execute the below command

`mvn clean compile exec:java -Dexec.mainClass=com.equifax.ews.dataflow.AvroCsvDataflow -Dexec.cleanupDaemonThreads=false -Dexec.args="--project=ews-ss-de-npe-1c88 --stagingLocation=gs://aruncloudspace/streamingdf/staging --tempLocation=gs://aruncloudspace/streamingdf/temp --subnetwork=https://www.googleapis.com/compute/v1/projects/efx-gcp-ews-svpc-npe-6787/regions/us-east1/subnetworks/ews-ss-de-npe-initial-npe-2 --usePublicIps=false --serviceAccount=ews-ss-de-df-qa@ews-ss-de-npe-1c88.iam.gserviceaccount.com --runner=DataflowRunner --region=us-east1 --zone=us-east1-b --output=gs://aruncloudspace/BIGFILE/Decrypted-TWN_DE_DM_IQY_20200728155440.txt --jobName=DataTest-AvroToCsv --avroSchema=gs://ews-de-twn-cnfg-qa/schema/newschema/INQUIRY_TWNBOC.avsc --inputFile=gs://ews-de-twnboc-staging-extracttmp-qa/Decrypted-TWN_DE_DM_IQY_20200728155440.avro --csvDelimiter=','"`