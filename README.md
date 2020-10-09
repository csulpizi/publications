# publications
A lightweight Java class for broadcasting messages and triggering events.

## Rationale 

When developing a Java app, I realized that there weren't many lightweight custom listeners available, so I build the class `Publication`. A `Publication` is a colleciton of lambdas. Whenever a new message is posted to the `Publication`, each lambda is called to consume the message. This allows you to 

This class can be used to broadcast messages that you want multiple objects to be aware of. For example, you might have a collection of objects of various classes that you want to perform actions whenever an event is triggered. The event would sent them each a message to be consumed, which might have useful meta information or context around why the event was triggered, or maybe the message is a lambda itself that can be called by each object.

## Usage  

The [src/examples](src/examples) folder contains a few simple examples that use the `Publication` class.

The [Docs](Docs.md) markdown file contains a quick explanation of all of the methods available, otherwise the [source](src/publications/Publication.java) should provide more information.

The basic methods are as follows:
- A `Publication` is a class that wraps a ConcurrentHashmap. The reference types `<K,V>` represent the class for the keys in the hashmap (aka subscriber ID's) and the class of the message type respectively. `java.util.UUID` is a good class for `<K>`, and `<V>` could be something simple like a `String` or something more complicated like a custom message class.
- The method `subscribe` lets you add a new subscription to a Publication. The arguments for `subscribe` are the ID for this subscriber (needs to be unique. This ID will be needed if you want to `unsubscribe` this subscription in the future) and a function of type `java.util.function.Consumer<V>`. Whenever a new message is published to the publication, the function for each subscriber will be called to consume the message.
- The method `unsubscribe` lets you remove a previously added subscription. Simply give it the ID of the subscriber you wish to remove
- The method `publish` lets you trigger all of the subscriptions' functions. The argument for `publish` is the message to be consumed.

## Additional Features
- A Publication can be given expiration conditions using the methods `addExpiryCount` and `addExpiryMs`. If a subscriber has consumed the maximum number of messages or has been subscribed for longer than the maximum time specified, that subscriber will be unsubscribed when a new message is published, before the subscriber consumes the message
- Each subscriber can also be given expiration conditions using the methods `addSubscriberExpiryCount` and `addSubscriberExpiryMs`. 
- A Publication can keep track of past messages. Use the `addRecordSize` method to specify a maximum number of records kept, and the `getLastMessage` and `getAllRecordedMessages` methods to access the history. The default record size is 1.
