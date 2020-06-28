package tr.xip.wanikani.models.v2;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;

public class Collection<T> extends BaseResponse<ArrayList<T>> implements Serializable
{
    private Pages pages;
    private int total_count;

    public Collection(String object, String url, Pages pages, int total_count, DateTime data_updated_at, ArrayList<T> data)
    {
        super(object, url, data_updated_at, data);
        this.pages = pages;
        this.total_count = total_count;
    }
}
