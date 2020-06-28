package tr.xip.wanikani.models.v2;

import org.joda.time.DateTime;

import java.io.Serializable;

public abstract class BaseResponse<T> implements Serializable
{
    protected String object;
    protected String url;
    protected DateTime data_updated_at;
    protected T data;

    protected BaseResponse(String object, String url, DateTime data_updated_at, T data)
    {
        this.object = object;
        this.url = url;
        this.data_updated_at = data_updated_at;
        this.data = data;
    }
}
