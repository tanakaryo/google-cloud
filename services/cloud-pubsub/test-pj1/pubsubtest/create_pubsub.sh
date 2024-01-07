#!/bin/bash

# create pubsub topic.
gcloud pubsub topics create test-topic

# create pubsub subscription.
gcloud pubsub subscriptions create test-sub --topic test-topic