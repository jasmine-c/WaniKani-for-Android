package tr.xip.wanikani.models.v2;

import org.joda.time.DateTime;

import java.io.Serializable;

public class Resource<T> extends BaseResponse<T> implements Serializable
{
    private int id;

    public Resource(int id, String object, String url, DateTime data_updated_at, T data)
    {
        super(object, url, data_updated_at, data);
        this.id = id;
    }
}
