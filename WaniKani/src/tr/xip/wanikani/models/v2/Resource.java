package tr.xip.wanikani.models.v2;

import org.apache.commons.lang3.NotImplementedException;
import org.joda.time.DateTime;

import java.io.Serializable;

import tr.xip.wanikani.models.Storable;

public class Resource<T extends Storable> extends BaseResponse<T> implements Serializable
{
    public int id;

    public Resource(int id, String object, String url, DateTime data_updated_at, T data)
    {
        super(object, url, data_updated_at, data);
        this.id = id;
    }

    @Override
    public void save() {
        data.save();
    }
}
