import com.loopj.android.http.*;

class TwitterRestClientUsage {
    public void getPublicTimeline() {
        TwitterRestClient.get("statuses/public_timeline.json", null, new JsonHttpResponseHandler<TwitterPost[]>(TwitterPost[].class) {
            @Override
            public void onSuccess(TwitterPost[] timeline) {
                TwitterPost firstEvent = timeline[0];

                // Do something with the response
                System.out.println(firstEvent.tweetText);
            }
        });
    }
}