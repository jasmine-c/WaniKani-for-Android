package tr.xip.wanikani.models.v2;

import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public abstract class BaseResponse<T> implements Serializable, Storable
{
    public String object;
    public String url;
    public DateTime data_updated_at;
    public T data;

    protected BaseResponse(String object, String url, DateTime data_updated_at, T data)
    {
        this.object = object;
        this.url = url;
        this.data_updated_at = data_updated_at;
        this.data = data;
    }
}
