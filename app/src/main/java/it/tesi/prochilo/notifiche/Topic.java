package it.tesi.prochilo.notifiche;

public class Topic {

    public final String id;
    public final String userId;
    public final String topic;
    public final String timestamp;

    private Topic(final String id, final String userId,
                  final String topic, final String timestamp) {
        this.id = id;
        this.userId = userId;
        this.topic = topic;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Id: ").append(id).append(", ");
        sb.append("UserId: ").append(userId).append(", ");
        sb.append("Topic: ").append(topic).append(", ");
        sb.append("Timestamp: ").append(timestamp);
        return sb.toString();
    }

    public static class Builder {
        private String mId;
        private String mUserId;
        private String mTopic;
        private String mTimestamp;

        private Builder(final String id, final String userId) {
            this.mId = id;
            this.mUserId = userId;
        }

        public static Builder create(final String id, final String mUserId) {
            return new Builder(id, mUserId);
        }

        public Builder addTopic(final String topic) {
            this.mTopic = topic;
            return this;
        }

        public Builder addTimestamp(final String timestamp) {
            this.mTimestamp = timestamp;
            return this;
        }

        public Topic build() {
            return new Topic(mId, mUserId, mTopic, mTimestamp);
        }
    }
}
