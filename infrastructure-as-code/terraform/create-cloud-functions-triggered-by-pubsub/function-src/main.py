from cloudevents.http import CloudEvent

import functions_framework


@functions_framework.cloud_event
def hello_pubsub(cloud_event: CloudEvent) -> tuple:
    
    print("hello, funcsitons!")