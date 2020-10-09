# Publication Documentation

## Type Parameters
`K` - the type of keys maintained by this map
`V` - the type of messages that will be consumed on `publish`

## Main Methods
`void subscribe(K id, Consumer<V> f)`

Add a new subscriber to the `Publication`. 
- `id`: A unique ID of type `K` to represent this subscriber
- `f`: A lambda that will consume type `<V>` when the method `publish` is called

---

`void unsubscribe(K id)`

Remove a subscriber from the `Publication`. 
- `id`: The ID of type `K` of the subscriber you wish to remove

---

`void unsubscribeAll()`

Remove all subscribers from the `Publication`

---
`void publish(V message)`

For each subscriber, call the saved lambda on the given message.
- `message`: An object of type `V` that each lambda will consume

## Expiry
**Note: By default, subscribers do not expire.**

`void setExpiryMs(int expiryMs)`

Set the amount of milliseconds after which each subscriber should be unsubscribed, calculated from the time that subscriber was added. Subscribers that expire will no longer consume messages and are removed from the `Publication`.
- `expiryMs`: The number of milliseconds a subscriber will stay subscribed

---

`void setExpiryCount(int expiryCount)`

Set the amount of messages after which each subscriber should be unsubscribed, calculated from the time this subscriber was first added. Subscribers that expire will no longer consume messages and are removed from the `Publication`.
- `expiryMs`: The number of messages a subscriber will consume before unsubscribing

---

`void setSubscriberExpiryMs(K id, int expiryMs)`

Set the amount of milliseconds after which this specific subscriber should be unsubscribed, calculated from the time this subscriber was first added. Subscribers that expire will no longer consume messages and are removed from the `Publication`.
- `id`: The ID of type `K` of the subscriber you want to add expiry to
- `expiryMs`: The number of milliseconds this subscriber will stay subscribed

---

`void setSubscriberExpiryCount(K id, int expiryCount)`

Set the amount of messages after which this specific subscriber should be unsubscribed, calculated from the time this subscriber was first added. Subscribers that expire will no longer consume messages and are removed from the `Publication`.
- `id`: The ID of type `K` of the subscriber you want to add expiry to
- `expiryCount`: The number of messages this subscriber will consume before unsubscribing

---

`void touch(K id)`

Reset the timestamp and message count of the specified subscriber, delaying expiry.
- `id`: The ID of type `K` of the subscriber you want to reset

## History Functions

`void setRecordSize(int recordSize)`

Set the maximum number of messages to store for the `Publication`. Default = 1.
-`recordSize`: The maximum number of records to store.

---

`V getLastMessage()`

Return the last message published to the `Publication`.

---

`LinkedList<V> getAllRecordedMessages()`

Return a linked list of all messages stored for the `Publication`. The first item in the list is the oldest message. If the size of the linked list exceeds the maximum size, the oldest entries are overwritten. The max size is the record size specified for the `Publication` (Default = 1).
 
## Utility Functions

`java.util.Date getInitTime()`

Return the `Date` object that this entity was created at.

---

`long uptime()`

Return the amount of milliseconds this entity has been created for.

---

`long countSubscribers()`

Return the number of subscribers currently stored in the `Publication`

---

`int countMessages()`

Return the total number of messages published to the `Publication`

---

`int countExpiredSubscribers()`

Return the total number of subscribers who have been automatically unsubcribed due to 
