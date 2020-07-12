package tr.xip.wanikani.models.v2;

import java.io.Serializable;

public class Pages implements Serializable
{
    public String next_url;
    public String previous_url;
    public int per_page;

    public Pages(String next_url, String previous_url, int per_page)
    {
        this.next_url = next_url;
        this.previous_url = previous_url;
        this.per_page = per_page;
    }
}
