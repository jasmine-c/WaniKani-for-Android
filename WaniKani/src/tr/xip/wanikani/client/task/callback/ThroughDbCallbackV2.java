package tr.xip.wanikani.client.task.callback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.wanikani.client.error.RetrofitErrorHandler;
import tr.xip.wanikani.models.Storable;

public abstract class ThroughDbCallbackV2<T extends Storable> implements Callback<T> {
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        final T result = response.body();

        if (result == null) return;

        if (response.isSuccessful()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    result.save();
                }
            }).start();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        RetrofitErrorHandler.handleError(t);
    }
}
